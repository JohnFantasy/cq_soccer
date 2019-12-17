package com.laofaner.cq_soccer.utils;

import java.util.HashMap;

/**
 * @program: cq_soccer
 * @description: 测试京东方签名代码
 * @author: fyz
 * @createAt: 2019-12-16 14:15
 **/
public class SignUtilTest {
    public static void main(String[] args) {
        //get请求，有参
//        HashMap<String, String> map = new HashMap<>();
//        map.put("latitude","2957184");
//        map.put("longitude","10657101");
//        map.put("mid","289001");
//        String nonceStr = "KYGVP";
//        String paramsString = SignUtil.mapToOrderedString(map);
//        String sign1 = SignUtil.getSign(paramsString,nonceStr);
//        System.err.println("noncestr "+nonceStr);
//        System.err.println("sign1 "+sign1);

        // post请求json参
        String json="{\n" +
                "    \"filters\": {\n" +
                "        \"couponName\": \"\",\n" +
                "        \"couponType\": 2,\n" +
                "        \"eMSort\": \"\",\n" +
                "        \"gId\": \"\",\n" +
                "        \"hasCoupon\": \"上井\",\n" +
                "        \"latitude\": \"2957054\",\n" +
                "        \"longitude\": \"2957054\",\n" +
                "        \"status\": \"1\"\n" +
                "    },\n" +
                "    \"isPC\": true,\n" +
                "    \"noSession\": true,\n" +
                "    \"pageNum\": 1,\n" +
                "    \"pageSize\": 20\n" +
                "}";
        String nonceStr = "KYGVP";
        System.err.println(nonceStr);
//        String sign = SignUtil.getSignForJsonString(json);
        String sign = SignUtil.getSign(json,nonceStr);
//        String nonceStr = SignUtil.getNonceStr();
        System.err.println(sign);
    }

}
