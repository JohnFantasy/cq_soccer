package com.laofaner.cq_soccer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 *
 */
public class JinkeHttpUtil4Prepay {

    private static Logger logger = LoggerFactory.getLogger(JinkeHttpUtil4Prepay.class);

    private static String APPID = "tq6d5h9oseig7rkauh";
    private static String APPSECRET = "8BlzjAItFGV7gbCO1MzQ1dXHYplRtZkn";
    private static String MCHID = "1865811760";

    /**
     * 向指定 URL 发送POST方法的请求(应金科接口变化，请求参数原来为json格式，先采用post参数形式，故增加此方法)
     *
     * @param url      发送请求的 URL
     * @param paramMap 请求参数，请求参数为map的形式。
     * @return 所代表远程资源的响应结果
     * @date 2018-06-05
     */
    public static String sendPost(String url, Map<String, String> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        paramMap.put("appid", APPID);
        paramMap.put("appsecret", APPSECRET);
        paramMap.put("mchid", MCHID);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        paramMap.put("timestamp", timestamp);

        String noise = JinkeSecurityUtil.getRandomString();
        paramMap.put("noise", noise);
        //将参数拼接成key=value的形式
        String paramToBeSent = "";
        String paramTobeMd5 = "";
        Collection<String> keyset = paramMap.keySet();
        List list = new ArrayList<String>(keyset);
        Collections.sort(list);//这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
        for (int i = 0; i < list.size(); i++) {
            paramTobeMd5 += list.get(i) + "=" + paramMap.get(list.get(i)) + "&";
            if(!list.get(i).equals("appsecret")){
                paramToBeSent += list.get(i) + "=" + paramMap.get(list.get(i)) + "&";
            }
        }
        paramTobeMd5 = paramTobeMd5.substring(0, paramTobeMd5.length() - 1);


        String sign = createSign(paramTobeMd5);
        paramToBeSent += "sign=" + sign;

        System.out.println(paramToBeSent);


        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(paramToBeSent);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static String createSign(String nonce_str) {
        SortedMap<String, String> sort = new TreeMap<>(Comparator.naturalOrder());

        String s = nonce_str;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(s.getBytes(StandardCharsets.UTF_8));
            String sign = bytesToHexString(digest).toUpperCase();
            return sign;
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * byte[] 转十六进制 String
     *
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
}
