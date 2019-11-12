package com.laofaner.cq_soccer.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

    public static String readFile(String path) {
        Resource resource = new ClassPathResource(path);
        if (resource.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
                byte[] buf = new byte[1024];
                // 定义一个StringBuffer用来存放字符串
                StringBuffer sb = new StringBuffer();
                // 开始读取数据
                int len = 0;// 每次读取到的数据的长度
                while ((len = inputStream.read(buf)) != -1) {// len值为-1时，表示没有数据了
                    // append方法往sb对象里面添加数据
                    sb.append(new String(buf, 0, len, "utf-8"));
                }
                // 输出字符串
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "文件不存在";
    }

    public static String readWantedText(String url, String wanted) {
        List list = new ArrayList();
        Resource resource = new ClassPathResource(url);
        if (resource.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String temp = "";// 用于临时保存每次读取的内容
                while (temp != null) {
                    temp = br.readLine();
                    if (temp != null && temp.contains(wanted)) {
                        temp = temp.replaceAll(wanted, "");
                        return temp;
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "文件不存在";
    }

}
