package com.milisong.wechat.miniapp.dto;

import com.google.common.collect.Maps;
import com.milisong.wechat.miniapp.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成二维码请求
 *
 * @author sailor wang
 * @date 2018/10/24 下午4:29
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniQrcodeRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = -2487202236739617223L;

    private String scene; //最大32个可见字符，只支持数字，大小写英文以及部分特殊字符

    private String page; //必须是已经发布的小程序存在的页面

    @Required
    private int width; //二维码的宽度，默认为 430px，最小 280px，最大 1280px

    private boolean autoColor; //自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false

    @Required
    private WxMaCodeLineColor lineColor; //auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示，默认全 0

    @Required
    private boolean isHyaline; //是否需要透明底色，为 true 时，生成透明底色的小程序码，默认 false


    public Map<String,Object> toMap(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("scene",this.getScene());
        map.put("page",this.getPage());
        map.put("width",this.getWidth());
        map.put("auto_color",this.isAutoColor());
        map.put("is_hyaline",this.isHyaline());
        map.put("line_color",this.getLineColor());
        return map;
    }
}