package com.laofaner.cq_soccer.utils;


import com.laofaner.cq_soccer.domain.enums.ExceptionEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @program: cq_soccer
 * @description: API接口签名验签工具类：HTTP请求，在请求header中加入命名为authorization的key，该键对应的value为appid=xxx&noncestr=xxx&signature=xxx
 *        请求方对参数签名有两种情形：
 *        一、http post方式且请求体为json格式：
 *              将序列化后的json整体字符串前后分别拼接分配的appsecret，
 *              再在后面拼接随机字符串（5位）和appid，拼接后的字符串进行一次MD5操作，得到的字符串即为签名
 *        二、http  get方式请求或者post表单方式请求：
 *              将所有请求参数键值对按照参数键的字典序升序排序，然后以（key1value1key2value2...）的形式转换为字符串，
 *              将字符串前后分别拼接分配的appsecret，
 *              再在后面拼接随机字符串(5位)和appid，拼接后的字符串进行一次MD5操作，得到的字符串即为签名
 *        **注意：
 *              appsecret参与签名计算但不参与参数封装！
 * @author: fyz
 * @create: 2019-11-21 09:56
 **/
public class SignUtil {

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("alpha","hahah");
        map.put("cat","hahah");
        map.put("beta","hahah");
        map.put("123","hahah");
        map.put("b23","hahah");

        System.out.println(SignUtil.mapToOrderedString(map));
    }

    private static final String APPID = "APPID";
    private static final String APPSECRET = "XXXXXX";
    private static final String NONCESTR = getNonceStr();

    //针对json请求参数获取签名
    private static String getSignForJsonString(String jsonParams) {
        if (StringUtils.isBlank(jsonParams)) {
            throw new RuntimeException(ExceptionEnum.PARAM_IS_NULL_EXCEPTION.getMessage());
        }
        return getSign(jsonParams, NONCESTR);
    }

    //针对json格式请求参数验签
    private static boolean checkSignForJson(String json, String nonceStr, String signature) {
        return getSign(json, nonceStr).equals(signature);
    }

    //针对map格式请求参数获取签名
    private static String getSignForMapParams(Map<String, String> mapParams) {
        if (CollectionUtils.isEmpty(mapParams)) {
            throw new RuntimeException(ExceptionEnum.PARAM_IS_NULL_EXCEPTION.getMessage());
        }
        return getSign(mapToOrderedString(mapParams), NONCESTR);
    }

    //针对map格式请求参数验签
    private static boolean checkSignForMap(Map<String, String> mapParams, String nonceStr, String signature) {
        return getSign(mapToOrderedString(mapParams), nonceStr).equals(signature);
    }

    //签名规则：appsecret+参数字符串+appsecret+随机字符串（5位）+appid
    private static String getSign(String params, String nonceStr) {
        return DigestUtils.md5Hex(APPSECRET + params + APPSECRET + nonceStr + APPID);
    }

    //map转为按key字典升序排序的字符串
    private static String mapToOrderedString(Map<String, String> map) {
        TreeMap<String, String> orderedMap = new TreeMap<>(map);
        Set<Map.Entry<String, String>> entries = orderedMap.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : entries) {
            sb.append(entry.getKey()).append(entry.getValue());
        }
        return sb.toString();
    }

    //获取5位随机字符串
    private static String getNonceStr() {

        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        //长度为几就循环几次
        for (int i = 0; i < 5; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString().toUpperCase();
    }
}
