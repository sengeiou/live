package com.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.vv.live.ChannelDatas;
import com.vv.live.FavorList;

public class MySettings {
    private Context context;

    public MySettings(Context context) {
        this.context = context;
    }

    public void saveVersion(int ver) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("ver", ver);
        editor.commit();
    }

    public void saveSetVer(int setver) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("setver", setver);
        editor.commit();
    }

    public void savemac(String mac) {
        if (mac.contains("00:00:00")) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("mac", mac);
        editor.commit();
    }

    public void clearSetting() {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear().commit();
        ChannelDatas.clearDiy();
        FavorList.clearFavor(context);
    }


    public int getLocalVersion() {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getInt("ver", 0);
    }

    public int getLocalSetVer() {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getInt("setver", 0);
    }

    public String getLastNet() {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getString("nettype", "电信");
    }

    public String getregion() {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getString("region", "");
    }

    public String getmac() {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getString("mac", "");
    }

    public String getNetType() {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getString("nettype", "");
    }

    public void saveNetType(String nettype) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("nettype", nettype);
        editor.commit();
    }

    public void saveregion(String region) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("region", region);
        editor.commit();
    }

    public int getCurrentSource(String channelName) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getInt(channelName, 0);
    }

    public boolean getSettingboolean(String key) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public long getSetting(String key) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    public int getSettingInt(String key) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public String getSettingStr(String key, String defaultkey) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getString(key, defaultkey);
    }

    public String getSettingStr(String key) {
        SharedPreferences sp = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public void saveSetting(String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putLong(key, value);
        editor.commit();// 提交修改
    }

    public void saveSetting(String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putInt(key, value);
        editor.commit();// 提交修改
    }

    public void saveSetting(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putString(key, value);
        editor.commit();// 提交修改
    }

    public void saveSetting(String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("live", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putBoolean(key, value);
        editor.commit();// 提交修改
    }

    static {
        System.loadLibrary("getName");
    }

    public native String getName();

    public native String getSign();

}
