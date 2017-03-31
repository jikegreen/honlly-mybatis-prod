package com.honlly.mybatis.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * @date 2015年8月17日 上午11:19:24 
 * @author wangk
 * @Description:
 * @project nfsq_sms
 */
public class JsonUtil {

	private static Logger logger = Logger.getLogger(JsonUtil.class);  
    private static final ObjectMapper objectMapper;  
    static {  
        objectMapper = new ObjectMapper();  
        //去掉默认的时间戳格式  
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);  
        //设置为中国上海时区  
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));  
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);  
        //空值不序列化  
        objectMapper.setSerializationInclusion(Include.NON_NULL);  
        //反序列化时，属性不存在的兼容处理  
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
        //序列化时，日期的统一格式  
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));  
  
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);  
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
        //单引号处理  
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);  
    }  
  
    public static <T> T toObject(String json, Class<T> clazz) {  
        try {  
            return objectMapper.readValue(json, clazz);  
        } catch (JsonParseException e) {  
            logger.error(e.getMessage(), e);  
        } catch (JsonMappingException e) {  
            logger.error(e.getMessage(), e);  
        } catch (IOException e) {  
            logger.error(e.getMessage(), e);  
        }  
        return null;  
    }  
  
    public static <T> String toJson(T entity) {  
        try {  
            return objectMapper.writeValueAsString(entity);  
        } catch (JsonGenerationException e) {  
            logger.error(e.getMessage(), e);  
        } catch (JsonMappingException e) {  
            logger.error(e.getMessage(), e);  
        } catch (IOException e) {  
            logger.error(e.getMessage(), e);  
        }  
        return null;  
    }  
  
    public static <T> T toCollection(String json, TypeReference<T> typeReference) {  
        try {  
            return objectMapper.readValue(json, typeReference);  
        } catch (JsonParseException e) {  
            logger.error(e.getMessage(), e);  
        } catch (JsonMappingException e) {  
            logger.error(e.getMessage(), e);  
        } catch (IOException e) {  
            logger.error(e.getMessage(), e);  
        }  
        return null;  
    }  
  
    /** 
     * json string convert to map 
     */  
    @SuppressWarnings("unchecked")
	public static Map<String, Object> json2map(String jsonStr)  
            throws Exception {  
        return objectMapper.readValue(jsonStr, Map.class);  
    }  
  
    /** 
     * json string convert to map with javaBean 
     */  
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)  
            throws Exception {  
        Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,  
                new TypeReference<Map<String, T>>() {  
                });  
        Map<String, T> result = new HashMap<String, T>();  
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {  
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));  
        }  
        return result;  
    }  
  
    /** 
     * json array string convert to list with javaBean 
     */  
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)  
            throws Exception {  
        List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr,  
                new TypeReference<List<T>>() {  
                });  
        List<T> result = new ArrayList<T>();  
        for (Map<String, Object> map : list) {  
            result.add(map2pojo(map, clazz));  
        }  
        return result;  
    }  
  
    /** 
     * map convert to javaBean 
     */  
    public static <T> T map2pojo(Map<String,Object> map, Class<T> clazz) {  
        return objectMapper.convertValue(map, clazz);  
    } 
}
