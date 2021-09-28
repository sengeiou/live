package com.vv.live.util;

import java.lang.reflect.Field;

/**
 * 作者：By 15968
 * 日期：On 2021/6/13
 * 时间：At 20:57
 */

public class InvokeUtils {
    public static void updateFinalModifiers(Field field) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
//        Field modifiersField = Field.class.getDeclaredField("modifiers");
//        modifiersField.setAccessible(true);
//        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }
} 