package com.laofaner.cq_soccer.interceptors;

import com.laofaner.cq_soccer.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * @program: cq_soccer
 * @description: 验签拦截器
 * @author: fyz
 * @create: 2019-11-25 15:15
 **/
@Slf4j
public class CheckSignatureInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader("authorization");
        if (StringUtils.isNotBlank(authorization)) {
            try {
                StringTokenizer stringTokenizer = new StringTokenizer(authorization, "&");
                String timestamp = stringTokenizer.nextToken().split("=")[1];
                String nonce_str = stringTokenizer.nextToken().split("=")[1];
                String signature = stringTokenizer.nextToken().split("=")[1];
                String method = request.getMethod();
                //get
                String queryString = request.getQueryString();
                //json
                String json = IOUtils.toString(request.getInputStream());
                //表单
                Map<String, String[]> parameterMap = request.getParameterMap();
                log.info("queryString{}", queryString);
                log.info("json{}", json);
                log.info("parameterMap{}", parameterMap);
                if (StringUtils.isBlank(queryString) && StringUtils.isBlank(json) && (null == parameterMap || parameterMap.size() == 0)) {
                    //没有参数,放行
                    return true;
                }
                boolean pass = false;
                if ("GET".equals(method)) {
                    pass = SignUtil.getSign(queryString, nonce_str).equals(signature);
                } else if ("POST".equals(method)) {
                    if (StringUtils.isNotBlank(json)) {
                        pass = SignUtil.getSign(json, nonce_str).equals(signature);
                    } else {
                        TreeMap<String, String[]> stringTreeMap = new TreeMap<>(parameterMap);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringTreeMap.forEach((key, value) -> {
                            stringBuilder.append("&").append(key).append("=").append(value[0]);
                        });
                        String substring = stringBuilder.substring(1);
                        pass = SignUtil.getSign(substring, nonce_str).equals(signature);
                    }
                }
                return pass;
            } catch (Exception e) {
                log.error("check sign throws an exception ,which is " + e.getMessage() + e);
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
