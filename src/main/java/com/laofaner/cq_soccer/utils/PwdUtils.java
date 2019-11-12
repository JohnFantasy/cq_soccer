package com.laofaner.cq_soccer.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: fyz
 * @CreateDate: 2019/5/9 16:26
 * @Version: 1.0
 **/
public class PwdUtils {
    public static boolean isWeakPassword(String pwd) {
        if (StringUtils.isEmpty(pwd)) {
            return true;
        }
        if (pwd.matches(".*\\d+.*") && pwd.matches(".*[a-zA-Z]+.*") && pwd.matches(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*") && pwd.length() > 5 && pwd.length() < 17) {
            return false;
        }
        return true;
    }
}
