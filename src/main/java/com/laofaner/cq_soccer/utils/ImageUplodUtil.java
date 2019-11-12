package com.laofaner.cq_soccer.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ImageUplodUtil {
    public static void main(String[] args) {
        Map<String, ContentBody> reqParam = new HashMap<String, ContentBody>();
        reqParam.put("image", new FileBody(new File("C:/Users/laofa/Desktop/4.png")));
        reqParam.put("realName", new StringBody("测试添加", ContentType.MULTIPART_FORM_DATA));
        reqParam.put("mobile", new StringBody("13111111111", ContentType.MULTIPART_FORM_DATA));
        reqParam.put("identityCard", new StringBody("50022611111141411", ContentType.MULTIPART_FORM_DATA));
        reqParam.put("sex", new StringBody("1", ContentType.MULTIPART_FORM_DATA));
        String result = "";
        try {
            result = postFileMultiPart("http://ar2u3b.natappfree.cc/call/method", reqParam);
            System.out.println(result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject resJson = JSONObject.parseObject(result);
        resJson.toJSONString();
        System.out.println(result);
    }

    public static String postFileMultiPart(String url, Map<String, ContentBody> reqParam) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.      
            HttpPost httppost = new HttpPost(url);
            //setConnectTimeout：设置连接超时时间，单位毫秒。setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。  
            RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(15000).build();
            httppost.setConfig(defaultRequestConfig);

            System.out.println("executing request " + httppost.getURI());

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (Entry<String, ContentBody> param : reqParam.entrySet()) {
                multipartEntityBuilder.addPart(param.getKey(), param.getValue());
            }
            HttpEntity reqEntity = multipartEntityBuilder.build();
            httppost.setEntity(reqEntity);
            // 执行post请求.      
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("got response");
            try {
                // 获取响应实体      
                HttpEntity entity = response.getEntity();
                //System.out.println("--------------------------------------");    
                // 打印响应状态      
                //System.out.println(response.getStatusLine());    
                if (entity != null) {
                    return EntityUtils.toString(entity, Charset.forName("UTF-8"));
                }
                //System.out.println("------------------------------------");    
            } finally {
                response.close();
            }
        } finally {
            // 关闭连接,释放资源      
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void inputstreamtofile(InputStream ins,File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}