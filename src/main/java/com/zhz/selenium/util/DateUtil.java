package com.zhz.selenium.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {

    public static final String DATE_PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String DATE_PATTERN_yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_yyyyMMdd = "yyyyMMdd";
    public static final String DATE_PATTERN_yyyy_MM_dd = "yyyy-MM-dd";

    public static Date parseDate(String inputDateStr, String pattern){
        SimpleDateFormat formatUtil = new SimpleDateFormat(pattern);
        Date resDate = null;
        try {
            resDate = formatUtil.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resDate;
    }

    public static String parseDate(Date inputDateStr, String pattern){
        SimpleDateFormat formatUtil = new SimpleDateFormat(pattern);
        String resDate = null;
        try {
            resDate = formatUtil.format(inputDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resDate;
    }

    public static Date parseDate(long DateLong){
        return new Date(DateLong);
    }
}