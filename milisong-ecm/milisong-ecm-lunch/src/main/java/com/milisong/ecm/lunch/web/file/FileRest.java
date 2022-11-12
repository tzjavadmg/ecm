package com.milisong.ecm.lunch.web.file;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.enums.FileRestEnum;
import com.milisong.ecm.common.properties.OssFileProperties;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
@Slf4j
@RestController
@RequestMapping("/v1/file/")
public class FileRest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OssFileProperties fileProperties;

    private static final String DEFUALT_MODULE = "common";
    private static final Byte BIZ_TYPE = 5;//米立送对应的oss业务线编号
    /**
     * 上传商品图片（保存本地和OSS）
     *
     * @param module
     * @param file
     * @return
     */
    @PostMapping("upload")
    public ResponseResult<String> upload(@RequestParam(value = "module", required = false) String module,
                                                  @RequestPart("file") MultipartFile file) {
        try {
            // 生成uuid名
            log.info("【上传商品图片】={}", module);
            ResponseResult<String> responseResult = uploadOSS(module, file);
            if (responseResult == null || !responseResult.isSuccess()) {
                return ResponseResult.buildFailResponse(FileRestEnum.FILE_OSS_ERROR.getCode(),FileRestEnum.FILE_OSS_ERROR.getDesc());
            }
            String relativePath = responseResult.getData();
            String targetPath =concatAbsolutePath(relativePath);
            File targetFile = new File(targetPath);
            if(!targetFile.exists()){
                File parentFile = targetFile.getParentFile();
                if(!parentFile.exists()){
                    parentFile.mkdirs();
                }
                targetFile.createNewFile();
            }
            file.transferTo(targetFile);
            // 相对路径
            return ResponseResult.buildSuccessResponse(relativePath);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResponseResult.buildFailResponse(FileRestEnum.FILE_OSS_ERROR.getCode(),FileRestEnum.FILE_OSS_ERROR.getDesc());
        }
    }


    @PostMapping("uploadOSS")
    ResponseResult<String> uploadOSS(@RequestParam(value = "module", required = false) String module, @RequestPart("file") MultipartFile file){
        try {
            log.info("【oss上传】开始");
            // check file pattern
            String fileName = file.getOriginalFilename();
            String pattern = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            //如果配置了文件格式限制，就进行校验
            if(fileProperties.getFormat() != null && fileProperties.getFormat().length != 0){
                Stream<String> stream = Arrays.stream(fileProperties.getFormat());
                boolean matchFormat = stream.anyMatch(s -> s.equalsIgnoreCase(pattern));
                if(!matchFormat) {
                    return ResponseResult.buildFailResponse(FileRestEnum.FILE_FORMART_ERROR.getCode(),FileRestEnum.FILE_FORMART_ERROR.getDesc());
                }
            }
            // 相对路径
            String relativePath = getRelativePath(fileName, module);
            // send post
            byte[] bytes = file.getBytes();
            //如果配置了文件最大限制，就进行校验
            if(StringUtils.isNotBlank(fileProperties.getMaxSingleFileSize())) {
                if (bytes.length > parseSize(fileProperties.getMaxSingleFileSize())) {
                    return ResponseResult.buildFailResponse(FileRestEnum.FILE_SIZE_ERROR.getCode(),FileRestEnum.FILE_SIZE_ERROR.getDesc());
                }
            }
            String content = new String(Base64.encodeBase64(bytes), "UTF-8");
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("bizType", BIZ_TYPE);//milisong对应oss枚举
            map.add("fileName", relativePath);
            map.add("content", content);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            JSONObject jsonObject = restTemplate.postForObject(fileProperties.getUploadUrl(), entity, JSONObject.class);
            Boolean success = jsonObject.getBoolean("success");
            if (jsonObject != null && success) {
                ResponseResult<String> response = ResponseResult.buildSuccessResponse();
                response.setData(relativePath);
                return response;
            }
            return ResponseResult.buildFailResponse(FileRestEnum.FILE_OSS_ERROR.getCode(),FileRestEnum.FILE_OSS_ERROR.getDesc(),jsonObject.getString("message"));
        } catch (IOException e) {
            return ResponseResult.buildFailResponse(FileRestEnum.FILE_OSS_ERROR.getCode(),FileRestEnum.FILE_OSS_ERROR.getDesc(), e.getMessage());
        }
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
     * @param relativePath
     * @return
     */
    private String concatAbsolutePath(String relativePath){
        if(relativePath.startsWith("/")) {
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
}
