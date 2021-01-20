package com.community.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class ResultVo {

    // 返回统一数据格式
    public static String getJsonString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (map != null) {
            // 遍历 map 有三种方法，这里使用遍历 key 的方法
            for (String key : map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    public static String getJsonString(int code, String msg) {
        return getJsonString(code, msg, null);
    }

    public static String getJsonString(int code) {
        return getJsonString(code, null, null);
    }

}
