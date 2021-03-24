package com.mxk.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 */
public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");

    public static String formatToYYYYMMDDHHmmss(LocalDateTime dateTime){
        return dateTime.format(FORMATTER);
    }


}
