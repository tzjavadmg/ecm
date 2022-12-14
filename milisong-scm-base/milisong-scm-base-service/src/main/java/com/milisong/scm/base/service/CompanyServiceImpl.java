package com.milisong.scm.base.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.api.ShopStockService;
import com.milisong.oms.api.StockService;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.common.properties.SystemProperties;
import com.milisong.scm.base.constant.CompanyConstant;
import com.milisong.scm.base.constant.ErrorEnum;
import com.milisong.scm.base.domain.*;
import com.milisong.scm.base.dto.*;
import com.milisong.scm.base.dto.http.ShopGoodsCount;
import com.milisong.scm.base.dto.mq.CompanyMqDto;
import com.milisong.scm.base.dto.param.CompanySearchDto;
import com.milisong.scm.base.dto.param.CompanyUpdateListParam;
import com.milisong.scm.base.mapper.*;
import com.milisong.scm.base.mq.MqProducerUtil;
import com.milisong.scm.base.util.DateUtil;
import com.milisong.scm.base.utils.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RefreshScope
@Service("companySerivce")
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private ShopService shopService;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CompanyExtMapper companyExtMapper;
    @Autowired
    private CompanyMealAddressMapper companyMealAddressMapper;
    @Autowired
    private CompanyMealTimeExtMapper companyMealTimeExtMapper;
    @Autowired
    private CompanyMealTimeMapper companyMealTimeMapper;
    @Resource
    private ShopStockService shopStockService;
    @Value("${oss.get-shop-goods-count-url}")
    private String getShopGoodsCountUrl;

    @Resource
    private SystemProperties systemProperties;

    @Override
    public ResponseResult<Pagination<CompanyDto>> pageSearch(@RequestBody CompanySearchDto param) {
        return search(param, false);
    }

    @Override
    public ResponseResult<Pagination<CompanyDto>> pageSearchWithWeigth(@RequestBody CompanySearchDto param) {
        return search(param, true);
    }

    @Override
    public ResponseResult<CompanyPolymerizationDto> queryById(Long id) {
        if (null == id) {
            return ResponseResult.buildFailResponse("400", "??????id????????????");
        }
        CompanyPolymerizationDto dto = queryCompanyPolymerizationDto(id, false);
        if (null != dto) {
            return ResponseResult.buildSuccessResponse(dto);
        }

        return ResponseResult.buildFailResponse("404", "????????????id????????????????????????");
    }

    @Override
    public ResponseResult<CompanyPolymerizationDto> queryAllById(Long id) {
        if (null == id) {
            return ResponseResult.buildFailResponse("400", "??????id????????????");
        }
        CompanyPolymerizationDto dto = queryCompanyPolymerizationDto(id, true);
        if (null != dto) {
            return ResponseResult.buildSuccessResponse(dto);
        }

        return ResponseResult.buildFailResponse("404", "????????????id????????????????????????");
    }

    @Override
    public ResponseResult<CompanyDto> querySimpleById(Long id) {
        if (null == id) {
            return ResponseResult.buildFailResponse("400", "??????id????????????");
        }
        Company company = companyMapper.selectByPrimaryKey(id);
        if (null != company) {
            return ResponseResult.buildSuccessResponse(BeanMapper.map(company, CompanyDto.class));
        }

        return ResponseResult.buildFailResponse("404", "????????????id????????????????????????");
    }

    @Override
    @Transactional
    public ResponseResult<String> saveOrUpdate(@RequestBody CompanyPolymerizationDto dto) {
        ResponseResult<String> result = ResponseResult.buildSuccessResponse();
        Boolean transformFlag = false;
        String oldShopCode = null;
        ResponseResult<ShopDto> shopResp = shopService.queryById(dto.getShopId());
        if (null == shopResp || !shopResp.isSuccess() || null == shopResp.getData()) {
            throw new RuntimeException("??????shopId????????????????????????");
        }

        // ?????????????????????code
        ShopDto shop = shopResp.getData();
        if (null == shop) {
            throw new RuntimeException("??????shopId????????????????????????");
        }
        dto.setShopCode(shop.getCode());
        dto.setShopName(shop.getName());

        // ?????????????????????
        if (CollectionUtils.isEmpty(dto.getMealAddressList())) {
            return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(), "????????????????????????");
        }

        // ??????????????????????????????
        Map<String, Long> map = dto.getMealAddressList().stream().collect(Collectors.groupingBy(p -> p.getMealAddress(), Collectors.counting()));
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(), "????????????" + entry.getKey() + "?????????????????????1");
            }
        }

        // ?????????????????????
        for (CompanyMealAddressDto item : dto.getMealAddressList()) {
            if (StringUtils.isBlank(item.getPicture())) {
                return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(), "????????????" + item.getMealAddress() + "???????????????????????????");
            }
        }

        // ???????????????
        if (!setLonAndLatInfo(dto, result)) {
            return result;
        }

        // ?????????????????????
        dto.setMealAddressCount(dto.getMealAddressList().size());

        // ??????????????????
        Company record = BeanMapper.map(dto, Company.class);
        if (null == dto.getId()) {
            record.setId(IdGenerator.nextId());

            companyMapper.insertSelective(record);

        } else {
            Company company = companyMapper.selectByPrimaryKey(dto.getId());
            // ????????????????????????
            if (dto.getShopId() != null && !company.getShopId().equals(dto.getShopId())) {
                if(!checkTime()){
                    return ResponseResult.buildFailResponse(ErrorEnum.COMPANY_UPDATE_ERROR.getCode(),ErrorEnum.COMPANY_UPDATE_ERROR.getDesc());
                }
                Map<String, Object> params = new HashMap<>();
                params.put("companyId",company.getId());
                params.put("shopCode",company.getShopCode());
                // ??????????????????????????????
                String resultStr = RestUtils.get(getShopGoodsCountUrl, params);
                ResponseResult ossResult = JSONObject.parseObject(resultStr, ResponseResult.class);
                log.info("?????????????????????????????????:{}",JSONObject.toJSONString(ossResult.getData()));
                if (ossResult.isSuccess()) {
                    List<ShopGoodsCount> shopGoodsCounts = JSONArray.parseArray(JSONObject.toJSONString(ossResult.getData()), ShopGoodsCount.class);
                    // ????????????????????????
                    if(!checkGoodsCounts(shopGoodsCounts,company.getShopCode(),dto.getShopCode())){
                        return ResponseResult.buildFailResponse(ErrorEnum.COMPANY_UPDATE_FAIL.getCode(),ErrorEnum.COMPANY_UPDATE_FAIL.getDesc());
                    }
                }
                transformFlag = true;
                oldShopCode = company.getShopCode();
            }
            companyMapper.updateByPrimaryKeySelective(record);

            // ?????????????????????????????????
            CompanyMealAddressExample example = new CompanyMealAddressExample();
            example.createCriteria().andCompanyIdEqualTo(dto.getId());
            CompanyMealAddress address = new CompanyMealAddress();
            address.setIsDeleted(Boolean.TRUE);
            address.setUpdateBy(dto.getUpdateBy());
            companyMealAddressMapper.updateByExampleSelective(address, example);
        }

        // ??????????????????
        if (!CollectionUtils.isEmpty(dto.getMealAddressList())) {
            dto.getMealAddressList().forEach(item -> {
                CompanyMealAddress mealAddress = BeanMapper.map(item, CompanyMealAddress.class);
                mealAddress.setCompanyId(record.getId());
                mealAddress.setIsDeleted(Boolean.FALSE);
                if (null != item.getId()) {
                    mealAddress.setUpdateBy(dto.getCreateBy());
                    companyMealAddressMapper.updateByPrimaryKeySelective(mealAddress);
                } else {
                    mealAddress.setCreateBy(dto.getCreateBy());
                    mealAddress.setId(IdGenerator.nextId());
                    companyMealAddressMapper.insertSelective(mealAddress);
                }
            });
        }

        // ??????MQ
        sendCompanyMq(record.getId(),transformFlag,oldShopCode);

        return ResponseResult.buildSuccessResponse();
    }

    /**
     * ?????????????????????????????????????????? (12?????????23???59???59???)
     * @return
     */
    private Boolean checkTime() {
        LocalTime now = LocalTime.now();
        LocalTime end = LocalTime.of(23, 59, 59);
        LocalTime start = LocalTime.of(12, 0, 0);
        return now.isAfter(start) && now.isBefore(end);
    }

    private Boolean checkGoodsCounts(List<ShopGoodsCount> shopGoodsCounts,String beforeShopCode,String afterShopCode) {
            // ????????????
            Map<Date, List<ShopGoodsCount>> dataMap = shopGoodsCounts.stream().collect(Collectors.groupingBy(ShopGoodsCount::getDeliveryDate));
            List<StockService.ShopDailyStock> shopDailyStocks = new ArrayList<>();
            for(Map.Entry<Date,List<ShopGoodsCount>> entry : dataMap.entrySet()){
                StockService.ShopDailyStock stock = new StockService.ShopDailyStock();
                stock.setSaleDate(entry.getKey());
                stock.setShopCode(beforeShopCode);
                stock.setGoodsStocks(BeanMapper.mapList(entry.getValue(), StockService.GoodsStock.class));
                shopDailyStocks.add(stock);
            }
            // ????????????
            Map<Date, List<ShopGoodsCount>> map = shopGoodsCounts.stream()
                    .map(shopGoodsCount -> {
                        shopGoodsCount.setGoodsCount(-shopGoodsCount.getGoodsCount());
                        return shopGoodsCount;
                    }).collect(Collectors.groupingBy(ShopGoodsCount::getDeliveryDate));
            for(Map.Entry<Date,List<ShopGoodsCount>> entry : map.entrySet()){
                StockService.ShopDailyStock stock = new StockService.ShopDailyStock();
                stock.setSaleDate(entry.getKey());
                stock.setShopCode(afterShopCode);
                stock.setGoodsStocks(BeanMapper.mapList(entry.getValue(), StockService.GoodsStock.class));
                shopDailyStocks.add(stock);
            }
            log.info("????????????:{}",JSONObject.toJSONString(shopDailyStocks));
            ResponseResult<?> result = shopStockService.repairOnSaleStock(shopDailyStocks);
            log.info("??????????????????:{}",JSONObject.toJSONString(result));
            return result.isSuccess();

    }

    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public ResponseResult<String> updateWeight(@RequestBody CompanyUpdateListParam param) {
        param.getList().forEach(item -> {
            Company record = companyMapper.selectByPrimaryKey(item.getId());

            record.setUpdateBy(param.getUpdateBy());
            record.setId(item.getId());
            record.setWeight(item.getWeight());
            companyMapper.updateByPrimaryKeySelective(record);
        });

        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<List<CompanyMealAddressDto>> queryMealAddressList(Long id) {
        CompanyMealAddressExample example = new CompanyMealAddressExample();
        example.createCriteria().andCompanyIdEqualTo(id);
        List<CompanyMealAddress> list = companyMealAddressMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseResult.buildSuccessResponse();
        }
        return ResponseResult.buildSuccessResponse(BeanMapper.mapList(list, CompanyMealAddressDto.class));
    }

    @Override
    public ResponseResult syncAllCompany() {
        List<Long> ids = companyExtMapper.selectAllCompanyId();
        ids.stream().forEach(id -> sendCompanyMq(id,false,null));
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<String> saveOrUpdateMealTime(@RequestBody CompanyDto dto) {
        // ??????????????????????????????
        if (saveMealTime(dto, dto.getMealTimeList(), CompanyConstant.BusinessLine.BREAKFAST.getValue())) {
            return ResponseResult.buildFailResponse("???????????????????????????");
        }
        // ????????????????????????
        if (saveMealTime(dto, dto.getLunchMealTimeList(), CompanyConstant.BusinessLine.LUNCH.getValue())) {
            return ResponseResult.buildFailResponse("???????????????????????????");
        }
        // ??????MQ
        sendCompanyMq(dto.getId(),false,null);
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<List<CompanyPolymerizationDto>> queryByShopId(Long shopId) {
        CompanyDto dto = new CompanyDto();
        dto.setShopId(shopId);
        // ????????????id???????????????id
        List<Long> companyIds = companyExtMapper.selectCompanyIdByParam(dto);
        // ????????????????????????????????????
        List<CompanyPolymerizationDto> companyPolymerizationDtos = companyIds.stream().map(companyId -> {
            CompanyPolymerizationDto companyPolymerizationDto = queryCompanyPolymerizationDto(companyId, true);
            return companyPolymerizationDto;
        }).collect(Collectors.toList());
        return ResponseResult.buildSuccessResponse(companyPolymerizationDtos);
    }

    @Override
    public List<CompanyMealAddressDto> queryMealAddress() {
        CompanyMealAddressExample example = new CompanyMealAddressExample();
        example.createCriteria().andCompanyIdIsNotNull();
        List<CompanyMealAddress> companyMealAddresses = companyMealAddressMapper.selectByExample(example);
        companyMealAddresses.stream().forEach(company -> {
            String pic = systemProperties.getFileOss().getViewUrl().concat("/6/").concat(company.getPicture());
            company.setPicture(pic);
        });
        List<CompanyMealAddressDto> list = BeanMapper.mapList(companyMealAddresses, CompanyMealAddressDto.class);
        return list;
    }

    @Override
    public void updateCompanyMealAddressPic(@RequestBody CompanyMealAddressDto dto) {
        CompanyMealAddress companyMealAddress = BeanMapper.map(dto,CompanyMealAddress.class);
        companyMealAddressMapper.updateByPrimaryKeySelective(companyMealAddress);
    }

    // ??????????????????
    private boolean saveMealTime(CompanyDto dto, List<CompanyMealTimeDto> mealTimeList, Byte businessLine) {
        if (!CollectionUtils.isEmpty(mealTimeList)) {
            // ????????????
            List<Date> beginTimeCollect = mealTimeList.stream().map(CompanyMealTimeDto::getDeliveryTimeBegin).collect(Collectors.toList());
            List<Date> endTimeCollect = mealTimeList.stream().map(CompanyMealTimeDto::getDeliveryTimeEnd).collect(Collectors.toList());
            for (CompanyMealTimeDto companyMealTimeDto : mealTimeList) {
                Date deliveryTimeBegin = companyMealTimeDto.getDeliveryTimeBegin();
                Date deliveryTimeEnd = companyMealTimeDto.getDeliveryTimeEnd();
                long count = beginTimeCollect.stream().filter(date -> date.equals(deliveryTimeBegin)).count();
                if (count > 1) {
                    return true;
                }
                count = endTimeCollect.stream().filter(date -> date.equals(deliveryTimeEnd)).count();
                if (count > 1) {
                    return true;
                }
            }
            // ??????????????????????????????
            companyMealTimeExtMapper.updateByCompanyId(dto.getId(), businessLine, Boolean.TRUE);
            mealTimeList.stream().forEach(mealTime -> {
                CompanyMealTime companyMealTime = BeanMapper.map(mealTime, CompanyMealTime.class);
                companyMealTime.setIsDeleted(Boolean.FALSE);
                companyMealTime.setCompanyId(dto.getId());

                if (null != companyMealTime.getId()) {
                    companyMealTime.setUpdateBy(dto.getUpdateBy());
                    companyMealTimeMapper.updateByPrimaryKeySelective(companyMealTime);
                } else {
                    companyMealTime.setCreateBy(dto.getCreateBy());
                    companyMealTime.setId(IdGenerator.nextId());
                    companyMealTime.setBusinessLine(businessLine);
                    companyMealTimeMapper.insertSelective(companyMealTime);
                }
            });
        }
        return false;
    }

    /**
     * ?????????????????????
     *
     * @param dto
     */
    private boolean setLonAndLatInfo(CompanyDto dto, ResponseResult<String> result) {
        try {
            String url = systemProperties.getLbsUrl().getPointServiceUrl().concat("?address=").concat(dto.getRegionName()).concat(dto.getDetailAddress());
            String rt = RestUtils.get(url);
            log.info("??????lbs???????????????????????????{}", rt);
            boolean flag = processLbsResult(rt, dto, result, false);
            if (flag) {
                url = systemProperties.getLbsUrl().getConvertServiceUrl().concat("?lon=").concat(String.valueOf(dto.getLonBaidu())).concat("&lat=").concat(String.valueOf(dto.getLatBaidu()));
                rt = RestUtils.get(url);
                return processLbsResult(rt, dto, result, true);
            }
        } catch (Exception e) {
            log.error("????????????LBS????????????", e);
            result.setSuccess(false);
            result.setCode("500");
            result.setMessage("??????LBS????????????");
            return false;
        }
        return true;
    }

    /**
     * ??????LBS????????????
     *
     * @param lbsResult
     * @param dto
     * @param result
     * @param isGaode
     * @return
     */
    private boolean processLbsResult(String lbsResult, CompanyDto dto, ResponseResult<String> result, boolean isGaode) {
        if (StringUtils.isBlank(lbsResult)) {
            result.setSuccess(false);
            result.setCode("500");
            result.setMessage("??????LBS?????????????????????");
        } else {
            JSONObject json = JSONObject.parseObject(lbsResult);
            if (json.getBooleanValue("success")) {
                if (isGaode) {
                    dto.setLonGaode(new BigDecimal(json.getJSONObject("data").getDoubleValue("lon")));
                    dto.setLatGaode(new BigDecimal(json.getJSONObject("data").getDoubleValue("lat")));
                } else {
                    dto.setLonBaidu(new BigDecimal(json.getJSONObject("data").getDoubleValue("lon")));
                    dto.setLatBaidu(new BigDecimal(json.getJSONObject("data").getDoubleValue("lat")));
                }
            } else {
                result.setSuccess(false);
                result.setCode(json.getString("code"));
                result.setMessage(json.getString("message"));
                return false;
            }
        }
        return true;
    }

    /**
     * ????????????????????????
     *
     * @param id
     * @return
     */
    private CompanyPolymerizationDto queryCompanyPolymerizationDto(Long id, boolean all) {
        Company company = companyMapper.selectByPrimaryKey(id);
        if (null != company) {
            CompanyPolymerizationDto dto = BeanMapper.map(company, CompanyPolymerizationDto.class);
            CompanyMealAddressExample example = new CompanyMealAddressExample();
            CompanyMealAddressExample.Criteria criteria = example.createCriteria().andCompanyIdEqualTo(id);
            if (!all) {
                criteria.andIsDeletedEqualTo(Boolean.FALSE);
            }
            List<CompanyMealAddress> mealAddressList = companyMealAddressMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(mealAddressList)) {
                dto.setMealAddressList(Collections.emptyList());
            } else {
                List<CompanyMealAddressDto> list = BeanMapper.mapList(mealAddressList, CompanyMealAddressDto.class);
                list.forEach(item -> {
                    item.setCompletePicture(systemProperties.getFileOss().getViewUrl().concat("/5/").concat(item.getPicture()));
                });
                dto.setMealAddressList(list);
            }
            // ??????????????????
            List<CompanyMealTimeDto> companyMealTimeDtos = companyMealTimeExtMapper.selectByCompanyId(id, CompanyConstant.BusinessLine.BREAKFAST.getValue(), false);
            if (CollectionUtils.isEmpty(companyMealTimeDtos)) {
                dto.setMealTimeList(Collections.emptyList());
            } else {
                dto.setMealTimeList(companyMealTimeDtos);
            }
            // ??????????????????
            List<CompanyMealTimeDto> lunchMealTimeDtos = companyMealTimeExtMapper.selectByCompanyId(id, CompanyConstant.BusinessLine.LUNCH.getValue(), false);
            if (CollectionUtils.isEmpty(lunchMealTimeDtos)) {
                dto.setLunchMealTimeList(Collections.emptyList());
            } else {
                dto.setLunchMealTimeList(lunchMealTimeDtos);
            }
            return dto;
        }
        return null;
    }

    /**
     * ??????MQ??????
     *
     * @param id
     */
    private void sendCompanyMq(Long id,Boolean transformFlag,String oldShopCode) {
        CompanyPolymerizationDto dto = queryCompanyPolymerizationDto(id, false);
        if (null != dto) {
            CompanyMqDto mqDto = new CompanyMqDto();
            BeanUtils.copyProperties(dto, mqDto, "deliveryTimeBegin", "deliveryTimeEnd", "workingHours");
            if (null != dto.getDeliveryTimeBegin() && null != dto.getDeliveryTimeEnd()) {
                mqDto.setDeliveryTimeBegin(DateUtil.formatDate(dto.getDeliveryTimeBegin(), "HH:mm"));
                mqDto.setDeliveryTimeEnd(DateUtil.formatDate(dto.getDeliveryTimeEnd(), "HH:mm"));
            }
            if (null != dto.getWorkingHours()) {
                mqDto.setWorkingHours(DateUtil.formatDate(dto.getWorkingHours(), "HH:mm"));
            }
            mqDto.setMealTimeList(dto.getMealTimeList());
            mqDto.setLunchMealTimeList(dto.getLunchMealTimeList());
            mqDto.setTransformShop(transformFlag);
            mqDto.setOldShopCode(oldShopCode);
            // ??????MQ
            MqProducerUtil.sendCompanyInfoMsg(mqDto);
        } else {
            throw new RuntimeException("????????????id???????????????????????????" + String.valueOf(id));
        }

    }

    /**
     * ??????????????????
     *
     * @param param
     * @param isWeight
     * @return
     */
    private ResponseResult<Pagination<CompanyDto>> search(CompanySearchDto param, boolean isWeight) {
        Pagination<CompanyDto> pagination = new Pagination<CompanyDto>();
        pagination.setPageNo(param.getPageNo());
        pagination.setPageSize(param.getPageSize());

        CompanyExample example = new CompanyExample();
        CompanyExample.Criteria criteria = example.createCriteria();

        if (isWeight) {
            criteria.andShopIdEqualTo(param.getShopId());
        } else {
            if (null != param.getShopId()) {
                criteria.andShopIdEqualTo(param.getShopId());
            }
        }

        if (StringUtils.isNotBlank(param.getBuildingName())) {
            criteria.andBuildingNameLike("%".concat(param.getBuildingName()).concat("%"));
        }
        if (StringUtils.isNotBlank(param.getName())) {
            criteria.andNameLike("%".concat(param.getName()).concat("%"));
        }
        if (param.getOpenStatus() != null) {
            criteria.andOpenStatusEqualTo(param.getOpenStatus());
        }
        if (null != param.getId()) {
            criteria.andIdEqualTo(param.getId());
        }

        long count = companyMapper.countByExample(example);
        pagination.setTotalCount(count);
        if (count > 0) {
            example.setPageSize(param.getPageSize());
            example.setStartRow(param.getStartRow());

            example.setOrderByClause(" -weight desc ");

            List<Company> data = companyMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(data)) {
                List<CompanyDto> companyDtos = BeanMapper.mapList(data, CompanyDto.class);
                companyDtos.stream().forEach(company -> {
                    List<CompanyMealTimeDto> companyMealTimeDtos = companyMealTimeExtMapper.selectByCompanyId(company.getId(), CompanyConstant.BusinessLine.BREAKFAST.getValue(), false);
                    List<CompanyMealTimeDto> lunchMealTimeDtos = companyMealTimeExtMapper.selectByCompanyId(company.getId(), CompanyConstant.BusinessLine.LUNCH.getValue(), false);
                    company.setMealTimeList(companyMealTimeDtos);
                    company.setLunchMealTimeList(lunchMealTimeDtos);
                });
                pagination.setDataList(companyDtos);
            }
        }
        if (null == pagination.getDataList()) {
            pagination.setDataList(Collections.emptyList());
        }

        return ResponseResult.buildSuccessResponse(pagination);
    }
}
