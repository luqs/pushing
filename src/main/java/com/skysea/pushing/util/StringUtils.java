package com.skysea.pushing.util;

/**
 * Created by zhangzhi on 2014/9/1.
 */
public final class StringUtils {
    private StringUtils() {}

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
