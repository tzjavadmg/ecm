package com.milisong.wechat.miniapp.util;

import com.google.common.collect.Lists;
import com.milisong.wechat.miniapp.annotation.Required;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 校验为空字段
 * @author sailor wang
 * @date 2018/10/9 下午2:57
 * @description
 */
@Slf4j
public class BeanFieldsUtils {

    /**
     * 检查bean里标记为@Required的field是否为空，为空则抛异常或者对象
     *
     * @param bean 要检查的bean对象
     */
    public static String checkRequiredFields(Object bean) {
        if (bean == null){
            return "参数对象不能为空";
        }
        List<String> requiredFields = Lists.newArrayList();

        List<Field> fields = new ArrayList<>(Arrays.asList(bean.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(bean.getClass().getSuperclass().getDeclaredFields()));
        for (Field field : fields) {
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                if (field.isAnnotationPresent(Required.class)) {
                    // 两种情况，一种是值为null，
                    // 另外一种情况是类型为字符串，但是字符串内容为空的，都认为是没有提供值
                    boolean isRequiredMissing = field.get(bean) == null
                            || (field.get(bean) instanceof String
                            && StringUtils.isBlank(field.get(bean).toString())
                    );
                    if (isRequiredMissing) {
                        requiredFields.add(field.getName());
                    }
                }
                field.setAccessible(isAccessible);
            } catch (SecurityException | IllegalArgumentException
                    | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }

        if (!requiredFields.isEmpty()) {
            String msg = "必填字段 " + requiredFields + " 不能为空";
            log.warn(msg);
            return msg;
        }
        return null;
    }

}
