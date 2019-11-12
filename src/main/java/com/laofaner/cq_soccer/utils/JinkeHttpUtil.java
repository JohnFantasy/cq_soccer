package com.laofaner.cq_soccer.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 *
 */
public class JinkeHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(JinkeHttpUtil.class);

    private static String APPID = "78967886";
    private static String SIGNKEY = "Llpq1ts51D28i5SCTRCGis";

    /**
     * 向指定 URL 发送POST方法的请求(应金科接口变化，请求参数原来为json格式，先采用post参数形式，故增加此方法)
     *
     * @param url 发送请求的 URL
     * @param paramMap 请求参数，请求参数为map的形式。
     * @return 所代表远程资源的响应结果
     * @date 2018-06-05
     */
    public static String sendPost(String url, Map<String, String> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        paramMap.put("appid",APPID);
        String nonce_str = System.currentTimeMillis()/1000+JinkeSecurityUtil.getRandomString();
        String sign = createSign(SIGNKEY,nonce_str);
        paramMap.put("nonce_str",nonce_str);
        paramMap.put("sign",sign);

        //将参数拼接成key=value的形式
        String param = "";
        for (Map.Entry<String, String> pair : paramMap.entrySet()) {
            param += pair.getKey() + "=" + pair.getValue()+"&";
        }
        param = param.substring(0, param.length() - 1);
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
            out.print(param);
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

    public static String post(String url, String body) throws Exception {
        String result = null;
        BufferedReader bufferedReader = null;
        Gson gson = new Gson();
        try {
            URL request = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) request.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            //有数据提交时
            if (body != null) {
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(body.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            StringBuilder builder = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
            result = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException execption) {
                execption.printStackTrace();
            }
        }
        return result;
    }

    public static String postMap(String url, Map<String,String> map) throws Exception {
        String result = null;
        BufferedReader bufferedReader = null;
        map.put("appid",APPID);
        String nonce_str = System.currentTimeMillis()/1000+JinkeSecurityUtil.getRandomString();
        String sign = createSign(SIGNKEY,nonce_str);
        map.put("nonce_str",nonce_str);
        map.put("sign",sign);
        Gson gson = new Gson();
        String body = gson.toJson(map, HashMap.class);

        try {
            URL request = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) request.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            //有数据提交时
            if (body != null) {
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(body.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            StringBuilder builder = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
            result = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException execption) {
                execption.printStackTrace();
            }
        }
        return result;
    }

    public static String createSign(String singnkey, String nonce_str) {
        SortedMap<String, String> sort=new TreeMap<>(Comparator.naturalOrder());

        String s = singnkey+nonce_str;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(s.getBytes(StandardCharsets.UTF_8));
            String sign = bytesToHexString(digest);
            return sign;
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
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
}
