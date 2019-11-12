package com.laofaner.cq_soccer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author fyz
 * @Params
 * @Description: 处理金科请求签名工具类
 * @Date 2018/04/27 3:04 PM
 */
public class JinkeSecurityUtil {

    private static final String SIGN = "sign";

    private static Logger logger = LoggerFactory.getLogger(JinkeSecurityUtil.class);

    private static String APPID;

    private static String MCHID;

    private static String APPSECRET;

    private static final String KEY_NAME = "appsecret";

    private static Pattern CH_CHARACTER = Pattern.compile("[\\u4e00-\\u9fa5]+");

    public static void init(String appid, String mchId, String appSecret) {
        APPID = appid;
        MCHID = mchId;
        APPSECRET = appSecret;
    }

    /**
     * @Author fyz
     * @Params 传入参数：请求对象
     * 传出参数：增加验签之后的请求字符串
     * @Description:
     * @Date 2018/04/27 9:52 AM
     */
    public static String encodeString(Object obj) {

        //将请求对象转为HashMap集合
        Map<String, Object> map = new HashMap<>();
        map.putAll(ConvertObjToMapString(obj));

        return encodeString(map);
    }

    /**
     * @Author fyz
     * @Params 传入参数：请求参数
     * 传出参数：增加验签之后的请求字符串
     * @Description:
     * @Date 2018/04/27 9:52 AM
     */
    public static String encodeString(String param) {

        // 将param分解成map
        Map<String, String> paramMap = new HashMap<>();
        if (!StringUtils.isEmpty(param)) {
            String[] paramArr = param.split("&", 0);
            for (String str : paramArr) {
                paramMap.put(str.split("=", -1)[0], str.split("=", -1)[1]);
            }
        }
        return encodeString(paramMap);
    }

    /**
     * 将中文进行URL编码
     * @param url
     * @return
     */
    public static String chEncode(String url)
    {
        try {
            if (url == null) {
                return "";
            }
            Matcher matcher = CH_CHARACTER.matcher(url);
            while (matcher.find()) {
                String tmp = matcher.group();
                url = url.replaceAll(tmp, URLEncoder.encode(tmp,"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }

        return url;
    }

    /**
     * @Author fyz
     * @Params 传入参数：请求MAP
     * 传出参数：增加验签之后的请求字符串
     * @Description:
     * @Date 2018/04/27 9:52 AM
     */
    public static String encodeString(Map<String, String> pramMap) {
        if(!pramMap.containsKey("SecretKey")){
            //将集合所有元素放入treemap中，使其按照key自然顺序排序
            TreeMap<String, String> orderedRequestKV = new TreeMap<>();

            orderedRequestKV.putAll(pramMap);

            //根据金科协议，首先删除原有报文中的sign
            if (orderedRequestKV.containsKey(SIGN)) {
                orderedRequestKV.remove(SIGN);
            }

            // 加入APPID、商户号、Unix时间戳、随机字符串
            orderedRequestKV.put("appid", APPID);
            orderedRequestKV.put("mchid", MCHID);
            //orderedRequestKV.put("appsecret", APPSECRET);
            orderedRequestKV.put("timestamp", System.currentTimeMillis()/1000+"");
            orderedRequestKV.put("noise", getRandomString());
            String sign = createSign(orderedRequestKV, APPSECRET);

            StringBuilder sb = new StringBuilder();
            String result = null;
            for (String key : orderedRequestKV.keySet()) {
                if (!"appsecret".equals(key)) {
                    sb.append(key + "=" + orderedRequestKV.get(key) + "&");
                }

            }
            if (sb.length() > 1) {
                result = sb.substring(0, sb.length() - 1);
            }

            result += ("&" + SIGN + "=" + sign);
            return result;
        }
        String notifyStr = "";
        for (Map.Entry<String, String> pair : pramMap.entrySet()) {
            notifyStr += pair.getKey() + "=" + pair.getValue()+"&";
        }
        notifyStr = notifyStr.substring(0, notifyStr.length() - 1);
        return notifyStr;

    }

    public static String createOssSign(String signkey,String nonce_str) {
        String s = signkey+nonce_str;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] digest = m.digest(s.getBytes(StandardCharsets.UTF_8));
            String sign = bytesToHexString(digest);
            return sign;
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private static String createSign(Map<String, String> paramMap, String key) {
        SortedMap<String, String> sort=new TreeMap<>(Comparator.naturalOrder());

        paramMap.put(KEY_NAME, key);
        sort.putAll(paramMap);
        Set<Map.Entry<String, String>> es = sort.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : es) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (null != v && !"".equals(v) && !"null".equals(v) && !"sign".equals(k)) {
                sb.append(k).append("=").append(URLEncoder.encode(v)).append("&");
            }
        }
        String s = sb.substring(0, sb.length()-1);
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] digest = m.digest(s.getBytes(StandardCharsets.UTF_8));
            String sign = bytesToHexString(digest).toUpperCase();
            return sign;
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    // 对象转集合Map<String, String>
    private static Map<String, String> ConvertObjToMapString(Object obj) {
        Map<String, String> reMap = new HashMap<String, String>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    if (o != null) {
                        reMap.put(fields[i].getName(), o.toString());
                    }
                } catch (NoSuchFieldException e) {
                    logger.error(e.getMessage(), e);
                } catch (IllegalArgumentException e) {
                    logger.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
        }
        return reMap;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * byte[] 转十六进制 String
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aDigest : bytes) {
            if ((0xff & aDigest) < 0x10) {
                sb.append("0").append(Integer.toHexString((0xFF & aDigest)));
            } else {
                sb.append(Integer.toHexString(0xFF & aDigest));
            }
        }

        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    //生成5位随机字符串
    public static String getRandomString() {
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
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
