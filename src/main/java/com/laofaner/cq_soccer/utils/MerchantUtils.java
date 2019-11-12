package com.laofaner.cq_soccer.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MerchantUtils {
    public static String postMap(String url, Map<String,String> map) throws Exception {
        String result = null;
        BufferedReader bufferedReader = null;
        Gson gson = new Gson();
        String body = gson.toJson(map, HashMap.class);

        try {
            URL request = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) request.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("POST");
            //有数据提交时
            if (body != null) {
                conn.setRequestProperty("Content-Type", "test/plain; charset=GBK");
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
}
