package org.websocketchat.websocketchat.utils;

/**
 * @author ALL
 * @date 2024/5/14
 * @Description
 */
public class NumberUtils {
    public static long parseToLongOrDefault(String str) {
        if (str == null || !str.matches("\\d+")) {
            return -1;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            // 如果数字太大，无法转换为long类型，也返回-1
            return -1;
        }
    }
}
