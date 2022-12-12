package com.zhz.selenium.util;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class StringUtil {
    public static boolean isEmpty(Object obj) {
        if (obj== null) {
            return true;
        }
        if ((obj instanceof String)) {
            return ("").equals(obj);
        } else if (obj instanceof Map) {
            return ((Map)obj).isEmpty();
        } else if (obj instanceof Object[]) {
            Object[] object = (Object[])obj;
            if (object.length == 0) {
                return true;
            }
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        }
        return false;
    }


    public static String toString(Object obj){
        return (obj == null) ? "" : obj.toString();
    }

    public static String saveInt(String str){
        return str.substring(0,str.lastIndexOf("."));
    }
}
