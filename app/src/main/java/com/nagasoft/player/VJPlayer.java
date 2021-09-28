package com.nagasoft.player;

import android.util.Log;
import java.lang.ref.WeakReference;

public class VJPlayer {
    public static final int PLAYER_NOTIFY_URL = 4113;
    protected static final String TAG = "NGPlayer";
    private static boolean gbload;
    private final int mNativeListener = 0;
    private final int mNativePlayer = 0;
    private String mStrVideoURL;
    private UrlChanged mUCCallback = null;
    private OnVJMSErrorListener mVELCallback = null;

    public static final native void native_init();

    public native void _release();

    public native String getMac();

    public native long getPlayBackDuration();

    public native boolean isLiveStream();

    public native boolean isPlayBackStream();

    public native boolean isVodFile();

    public final native void native_finalize();

    public final native void native_setup(Object obj);

    public native boolean seekPlayBack(long j);

    public native void setURL(String str);

    public native void setVJMSBufferTimeout(int i);

    public native boolean start();

    public native void stop();

    static {
        gbload = false;
        try {
            System.loadLibrary("p2pcore");
            System.loadLibrary("vjplayer_jni");
            gbload = true;
            native_init();
        } catch (Throwable e) {
            gbload = false;
            Log.e(TAG, "loadLibrary libvjplayer_jni error: " + e.toString(), e);
            e.printStackTrace();
        }
    }

    public VJPlayer(UrlChanged uc) {
        if (gbload) {
            native_setup(new WeakReference<>(this));
        }
        this.mUCCallback = uc;
    }

    public void setOnVJMSErrorListener(OnVJMSErrorListener vel) {
        this.mVELCallback = vel;
    }

    public void notifyPlayURL(String strURL) {
        Log.d(TAG, "from native string is " + strURL);
        this.mStrVideoURL = strURL;
        notifyUI(strURL);
    }

    public void notifyError(int nErrorCode) {
        Log.d(TAG, "from native error is " + nErrorCode);
        notifyUIError(nErrorCode);
    }

    protected void notifyUI(String strURL) {
        if (this.mUCCallback != null) {
            this.mUCCallback.onUrlChanged(strURL);
        }
    }

    protected void notifyUIError(int nCode) {
        if (this.mVELCallback != null) {
            this.mVELCallback.onVJMSError(nCode);
        }
    }

    public String getVideoURL() {
        return this.mStrVideoURL;
    }
}
