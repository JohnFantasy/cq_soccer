package com.laofaner.cq_soccer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @program: cq_soccer
 * @description: 用户服务请求接口
 * @author: fyz
 * @create: 2019-11-12 16:15
 **/
@RestController
@RequestMapping("user")
public class UserController {

    @PostMapping("test")
    public String test(@RequestParam Map<String, String> map) {

        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ":" + value);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }
}
