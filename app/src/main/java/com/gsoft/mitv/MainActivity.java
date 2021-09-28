package com.gsoft.mitv;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.vv.live.ChannelManager;

import java.lang.ref.WeakReference;

public class MainActivity extends Service {

    static {
        System.loadLibrary("mitv");
    }

    private static WeakReference<MainActivity> wr;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ChannelManager.copyAssetFile(this, "libmitv.so", getCacheDir() + "/libmitv.so");
        wr = new WeakReference<>(this);
    }

    public static void loadlib() {
        MainActivity m = wr.get();
        m.loadLibrary(1);
    }

    private native void loadLibrary(int lib);

}
