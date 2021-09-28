package com.vv.live.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 作者：By 15968
 * 日期：On 2021/7/5
 * 时间：At 10:10
 */

public class ScreenUtil {
    public static int getScreenWidth(Context activity) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
} 