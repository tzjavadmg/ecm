package com.milisong.scm.base.rest;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.dto.CompanyMealAddressDto;
import com.milisong.scm.base.propreties.OssFileProperties;
import com.milisong.scm.base.propreties.SysConstant;
import com.milisong.upms.utils.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/4   14:44
 *    desc    : 文件上传下载
 *    version : v1.0
 * </pre>
 */
@Api(tags = "文件上传")
@Slf4j
@RestController
@RequestMapping("/file/")
public class FileRest {


    @Autowired
    private OssFileProperties fileProperties;
    @Autowired
    private CompanyService companyService;

    private static final String DEFUALT_MODULE = "common";
//    private static final Byte BIZ_TYPE = 6;//米立送早餐对应的oss业务线编号

    /**
     * 上传商品图片（保存本地和OSS）
     *
     * @param module
     * @param file
     * @return
     */
    @ApiOperation("上传文件到OSS同时备份到本地")
    @PostMapping("upload")
    public ResponseResult<String> upload(@RequestParam(value = "module", required = false) String module,
                                         @RequestPart("file") MultipartFile file) {
        try {
            // 生成uuid名
            log.info("【上传商品图片】={}", module);
            ResponseResult<String> responseResult = uploadOSS(module, file);
            if (responseResult == null || !responseResult.isSuccess()) {
                return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getCode(), SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getDesc());
            }
            String relativePath = responseResult.getData();
            String targetPath = concatAbsolutePath(relativePath);
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                File parentFile = targetFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                targetFile.createNewFile();
            }
            file.transferTo(targetFile);
            // 相对路径
            return ResponseResult.buildSuccessResponse(relativePath);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getCode(), SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getDesc());
        }
    }


    @ApiOperation("上传文件到OSS")
    @PostMapping("uploadOSS")
    ResponseResult<String> uploadOSS(@RequestParam(value = "module", required = false) String module, @RequestPart("file") MultipartFile file) {
        try {
            log.info("【oss上传】开始");
            // check file pattern
            String fileName = file.getOriginalFilename();
            String pattern = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            //如果配置了文件格式限制，就进行校验
            if (fileProperties.getFormat() != null && fileProperties.getFormat().length != 0) {
                Stream<String> stream = Arrays.stream(fileProperties.getFormat());
                boolean matchFormat = stream.anyMatch(s -> s.equalsIgnoreCase(pattern));
                if (!matchFormat) {
                    return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.FILE_FORMART_ERROR.getCode(), SysConstant.SYSTEM_INFO.FILE_FORMART_ERROR.getDesc());
                }
            }
            // 相对路径
            String relativePath = getRelativePath(fileName, module);
            // send post
            byte[] bytes = file.getBytes();
            //如果配置了文件最大限制，就进行校验
            if (StringUtils.isNotBlank(fileProperties.getMaxSingleFileSize())) {
                if (bytes.length > parseSize(fileProperties.getMaxSingleFileSize())) {
                    return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.FILE_SIZE_ERROR.getCode(), SysConstant.SYSTEM_INFO.FILE_SIZE_ERROR.getDesc());
                }
            }
            String content = new String(Base64.encodeBase64(bytes), "UTF-8");
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("bizType", fileProperties.getBizType());//milisong对应oss枚举
            map.add("fileName", relativePath);
            map.add("content", content);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            JSONObject jsonObject = RestClient.getRestTemplate().postForObject(fileProperties.getUploadUrl(), entity, JSONObject.class);
            Boolean success = jsonObject.getBoolean("success");
            if (jsonObject != null && success) {
                ResponseResult<String> response = ResponseResult.buildSuccessResponse();
                response.setData(relativePath);
                return response;
            }
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getCode(), SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getDesc(), jsonObject.getString("message"));
        } catch (IOException e) {
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getCode(), SysConstant.SYSTEM_INFO.FILE_OSS_ERROR.getDesc(), e.getMessage());
        }
    }

    @GetMapping("change")
    public ResponseResult<String> changePic() {
        List<CompanyMealAddressDto> picList = companyService.queryMealAddress();
        String path = "/data/files/images/milisong/temp/";
//        String path = "D:\\img\\";
        for (CompanyMealAddressDto company : picList) {
            // 下载阿里云图片到本地 转成加密字符串
            try {
                String mealAddress = company.getPicture();
                String str = imageBase64(mealAddress);
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                int index = mealAddress.lastIndexOf("/");
                String substring = mealAddress.substring(index + 1);
                File tempFile = new File(path);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                boolean flag = generateImage(str, path + substring);
                if (flag) {
                    File file = new File(path + substring);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                    ResponseResult<String> picResult = this.upload("milisong", multipartFile);
                    if (picResult.isSuccess()) {
                        company.setPicture(picResult.getData());
                        companyService.updateCompanyMealAddressPic(company);
                    }

                }
            } catch (IOException e) {
                log.error("手动上传图片失败", e);
            }
        }
        return null;
    }

    /**
     * 获取文件的相对路径
     *
     * @param originName
     * @param module
     * @return
     */
    private static String getRelativePath(String originName, String module) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileType = originName.substring(originName.lastIndexOf("."));
        String fileName = uuid.concat(fileType);
        // 每月创建一个文件夹
        String datePath = new SimpleDateFormat("yyyyMM").format(new Date());
        String targetPath = null;
        if (StringUtils.isNotBlank(module)) {
            targetPath = module.concat("/").concat(datePath).concat("/").concat(fileName);
        } else {
            targetPath = DEFUALT_MODULE.concat("/").concat(datePath).concat("/").concat(fileName);
        }
        return targetPath;
    }

    /**
     * 存放和读取文件的绝对路径
     *
     * @param relativePath
     * @return
     */
    private String concatAbsolutePath(String relativePath) {
        if (relativePath.startsWith("/")) {
            return fileProperties.getLocalBackupPath().concat(relativePath);
        } else {
            return fileProperties.getLocalBackupPath().concat(relativePath);
        }
    }

    private static long parseSize(String size) {
        size = size.toUpperCase();
        if (size.endsWith("KB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)).longValue() * 1024L;
        } else {
            return size.endsWith("MB") ? Long.valueOf(size.substring(0, size.length() - 2)).longValue() * 1024L * 1024L : Long.valueOf(size).longValue();
        }
    }

    public static String imageBase64(String path) {
        InputStream in = null;
        ByteArrayOutputStream byteArrOps = null;
        int length = -1;
        byte[] buffer = new byte[1024 * 2];
        byte[] data = null;
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000 * 10);
            if (urlConnection.getResponseCode() == 200) {
                in = urlConnection.getInputStream();
                byteArrOps = new ByteArrayOutputStream();
                while ((length = in.read(buffer)) != -1) {
                    byteArrOps.write(buffer, 0, length);
                }
                //总大小
                int totalSize = urlConnection.getContentLength();
                byteArrOps.flush();
                data = byteArrOps.toByteArray();
                // 下载大小: data.length
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrOps != null) {
                    byteArrOps.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null == data) {
            return null;
        }
        return encoder.encode(data);
    }

    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public static void main(String[] args){
//        String path = "D:\\img\\";
//        String imgPath = "http://jshuii-test.oss-cn-hangzhou.aliyuncs.com/6/milisong/201812/b13276ac6d9e46b6bb61f3771df6a983.jpg";
//        String str = imageBase64(imgPath);
//        int index = imgPath.lastIndexOf("/");
//        String substring = imgPath.substring(index+1);
//        boolean b = generateImage(str, path+substring);
//
//        File file = new File(path+substring);
//        FileInputStream fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream(file);
//            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
//                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
//            this.upload("milisong",multipartFile);
//        } catch (IOException e) {
//            log.error("文件上传失败");
//        }
//        System.out.println(b);
//    }
}
