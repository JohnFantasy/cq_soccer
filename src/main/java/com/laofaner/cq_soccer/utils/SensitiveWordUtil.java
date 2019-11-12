package com.laofaner.cq_soccer.utils;

import java.util.*;

/**
 * Created by
 */
public class SensitiveWordUtil {
    private static HashMap<String, Object> sensitiveMap;

    public static void loadSensitiveWord(Set<String> set){
        sensitiveMap = new HashMap<String, Object>(set.size());
        HashMap<String, String> newMap;
        Map nowMap;
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String word = iterator.next();
            nowMap = sensitiveMap;
            for (int i = 0; i< word.length(); i++){
                char c = word.charAt(i);
                Object wordMap = nowMap.get(c);
                if (wordMap != null){
                    nowMap = (Map)wordMap;
                } else {
                    newMap = new HashMap<String, String>();
                    newMap.put("isEnd","0");
                    nowMap.put(c, newMap);
                    nowMap = newMap;
                }
            }
            nowMap.put("isEnd","1");
        }
        //System.out.println(sensitiveMap);
    }

    public static String checkSensitiveWord(String txt) {
        if (txt == null) {
            return "";
        }
        Set<String> set = new HashSet<String>();
        Map nowMap = sensitiveMap;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < txt.length(); i++){
            char c = txt.charAt(i);
            nowMap = (Map)nowMap.get(c);
            if (nowMap != null){
                sb.append(c);
                if ("1".equals((nowMap).get("isEnd"))){
                    set.add(sb.toString());
                    sb.setLength(0);
                    nowMap = sensitiveMap;
                }
            } else {
                sb.setLength(0);
                nowMap = sensitiveMap;
            }
        }
        //System.out.println(set);
        //long start = System.currentTimeMillis();
        for (String word : set){
            txt = txt.replace(word, repeat("*", word.length()));
        }
        //System.out.println("use time: " + (System.currentTimeMillis()-start));
        //System.out.println(txt);
        return txt;
    }

    public static String repeat(String s, int count) {
        String result = "";
        while (count > 0) {
            if (count%2 == 1){
                result += s;
            }
            s += s;
            count = count >> 1;
        }
        return result;
    }
}
