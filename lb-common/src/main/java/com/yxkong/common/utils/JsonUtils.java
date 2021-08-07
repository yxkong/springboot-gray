package com.yxkong.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * @Author: yxkong
 * @Date: 2021/5/19 9:40 上午
 * @version: 1.0
 */
public class JsonUtils {
    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
    }

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * 将json转换成字符串(带序列化Feature)
     * <p>输出空置字段 </p>
     * <p>list字段如果为null，输出为[]，而不是null  </p>
     * <p>数值字段如果为null，输出为0，而不是null </p>
     * <p>Boolean字段如果为null，输出为false，而不是null   </p>
     * <p>字符类型字段如果为null，输出为""，而不是null</p>
     * @author ducongcong
     * @createDate 2016年7月6日
     * @updateDate
     * @param object
     * @return
     */
    public static String toJsonWithFeatures(Object object) {
        return JSON.toJSONString(object, config, features);
    }
    /**
     * 经json转换成字符串，按实际情况
     * @author ducongcong
     * @createDate 2016年7月6日
     * @updateDate
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if(object == null) {
            return null;
        }
        return JSON.toJSONString(object, config);
    }
    /**
     * 将json转成object
     * @author ducongcong
     * @createDate 2016年7月6日
     * @updateDate
     * @param json
     * @return
     */
    public static Object fromJson(String json) {
        return JSON.parse(json);
    }
    /**
     * 将json转成对应的对象
     * @author ducongcong
     * @createDate 2016年7月6日
     * @updateDate
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 按类型转化
     * @param json
     * @param type 例：  new TypeReference<Map<String, Object>>() {}
     * @return
     * @author ducongcong
     * @createDate 2016年8月9日
     * @updateDate
     */
    public static <T> T fromJson(String json, TypeReference<T> type) {
        return JSON.parseObject(json, type.getType());
    }

    /**
     * 将json转成数组
     * @author ducongcong
     * @createDate 2016年7月6日
     * @updateDate
     * @param json
     * @return
     */
    public static <T> Object[] toArray(String json) {
        return toArray(json, null);
    }

    /**
     *  转换为数组
     * @author ducongcong
     * @createDate 2016年7月6日
     * @updateDate
     * @param json
     * @param clazz
     * @return
     */
    public static <T> Object[] toArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz).toArray();
    }

    /**
     *  转换为List
     * @author ducongcong
     * @createDate 2016年7月6日
     * @updateDate
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 将javabean转化为序列化的json字符串
     * @param keyvalue
     * @return
     */
    public static Object beanToJson(Object keyvalue) {
        String textJson = JSON.toJSONString(keyvalue);
        return JSON.parse(textJson);
    }

    /**
     * 将string转化为序列化的json字符串
     * @param json
     * @return
     */
    public static Object textToJson(String json) {
        return JSON.parse(json);
    }

    /**
     * json字符串转化为map
     * @param json
     * @return
     */
    public static Map<String,Object> stringToCollect(String json) {
        return JSONObject.parseObject(json);
    }
    public static JSONObject jsonObject(String text){
        return JSONObject.parseObject(text);
    }

}