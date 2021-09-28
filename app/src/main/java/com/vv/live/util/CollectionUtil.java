package com.vv.live.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 作者：By hdy
 * 日期：On 2019/4/3
 * 时间：At 12:17
 */
public class CollectionUtil {
    public static <T> List<T> asList(T... a){
        List<T> data = new ArrayList<>();
        Collections.addAll(data, a);
        return data;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static String listToString(List<String> list, String cha) {
        StringBuilder builder = new StringBuilder();
        if (list == null || list.size() <= 0) {
            return "";
        } else if (list.size() <= 1) {
            return list.get(0);
        } else {
            builder.append(list.get(0));
        }
        for (int i = 1; i < list.size(); i++) {
            builder.append(cha).append(list.get(i));
        }
        return builder.toString();
    }

    public static String listToString(List<String> list) {
        return listToString(list, "&&");
    }
}
