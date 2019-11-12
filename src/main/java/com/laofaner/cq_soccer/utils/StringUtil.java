package com.laofaner.cq_soccer.utils;

import java.util.regex.Pattern;

/**
 * Created by
 */
public class StringUtil {
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    public static String padRight(String src, int len) {
        return padRight(src, len, ' ');
    }

    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    public static String padLeft(String src, int len) {
        return padLeft(src, len, ' ');
    }

    public static Boolean IsGuid(String strSrc) {
        return Pattern.matches("^[A-F0-9]{8}(-[A-F0-9]{4}){3}-[A-F0-9]{12}$", strSrc);
    }
}
