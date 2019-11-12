package com.laofaner.cq_soccer.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by
 */
public class LngAndLatUtil {
    public static Map<String, Double> getLngAndLatByAddress(String address, String key) throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=" + key;
        String json = HttpUtil.get(url);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        if (jsonObject.get("status").toString().equals("0")) {
            double lng = jsonObject.getAsJsonObject("result").getAsJsonObject("location").get("lng").getAsDouble();
            double lat = jsonObject.getAsJsonObject("result").getAsJsonObject("location").get("lat").getAsDouble();
            map.put("lng", lng);
            map.put("lat", lat);
        }
        return map;
    }

    public static Map<String, Double> changePosition(Double longitude, Double latitude, int type, String key) throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + longitude + "," + latitude
                + "&from=" + type + "&to=5&ak=" + key;
        String json = HttpUtil.get(url);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        if (jsonObject.get("status").toString().equals("0")) {
            JsonArray jsonArray = jsonObject.getAsJsonArray("result");
            double lng = jsonArray.get(0).getAsJsonObject().get("x").getAsDouble();
            double lat = jsonArray.get(0).getAsJsonObject().get("y").getAsDouble();
            map.put("lng", lng);
            map.put("lat", lat);
        }
        return map;
    }
}
