package com.milisong.breakfast.scm.rest;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/3/7   16:43
 *    desc    : 临时读写redis接口
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/redis/")
public class DataRest {

    private static final ResponseResult<Object> ILLEGAL_KEY_RES = ResponseResult.buildFailResponse("30042","缺少key信息");


    @PostMapping("get")
    public ResponseResult<String> getKV(@RequestBody RedisGetDataDto dataDto){
        Object o;
        if(StringUtils.isBlank(dataDto.getKey())){
            return ResponseResult.buildFailResponse(ILLEGAL_KEY_RES.getCode(),ILLEGAL_KEY_RES.getMessage());
        }
        if(dataDto.isHash()){
            if(StringUtils.isBlank(dataDto.getHkey())){
                o = RedisCache.hGetAll(dataDto.getKey());
            }else{
                o = RedisCache.hGet(dataDto.getKey(), dataDto.getHkey());
            }
        }else{
            o = RedisCache.get(dataDto.getKey());
        }
        if(o == null){
            return ResponseResult.buildSuccessResponse();
        }else {
            return ResponseResult.buildSuccessResponse(o.toString());
        }
    }

    @PostMapping("incr")
    public ResponseResult<String> incr(@RequestBody RedisIncrDataDto dataDto){
        String key = dataDto.getKey();
        if(StringUtils.isBlank(key)){
            return ResponseResult.buildFailResponse(ILLEGAL_KEY_RES.getCode(),ILLEGAL_KEY_RES.getMessage());
        }
        Long aLong = RedisCache.incrBy(key, dataDto.getIncrVal());
        if(dataDto.isDoExpire()){
            RedisCache.expire(key,dataDto.getExpireVal(), dataDto.getTimeUnit());
        }
        return ResponseResult.buildSuccessResponse(String.valueOf(aLong));
    }

    @PostMapping("set")
    public ResponseResult<String> set(@RequestBody RedisSetDataDto dataDto){
        String key = dataDto.getKey();
        if(StringUtils.isBlank(key)){
            return ResponseResult.buildFailResponse(ILLEGAL_KEY_RES.getCode(),ILLEGAL_KEY_RES.getMessage());
        }
        if(dataDto.isHash()){
            RedisCache.hPut(key,dataDto.getHkey(),dataDto.getValue());
        }else{
            RedisCache.set(key,dataDto.getValue());
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Getter
    @Setter
    private static class RedisGetDataDto{
        private boolean hash;
        private String key;
        private String hkey;

    }

    @Getter
    @Setter
    private static class RedisIncrDataDto{
        private String key;
        private Integer incrVal;
        private boolean doExpire;
        private Integer expireVal;
        private TimeUnit timeUnit;
    }

    @Getter
    @Setter
    private static class RedisSetDataDto{
        private String key;
        private boolean hash;
        private String hkey;
        private String value;
    }
}
