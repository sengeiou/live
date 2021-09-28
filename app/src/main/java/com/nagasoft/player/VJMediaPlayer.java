package com.nagasoft.player;

import android.content.Context;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.IOException;

public class VJMediaPlayer {
    public static final String[] LIBS = new String[]{"ffplay", "p2pcore", "ffmpeg"};
    private static boolean sLoaded = false;
    private Context mContext;
    private boolean mPlaying;
    private Thread mRenderThread;

    private native void Close();

    private native void EnableDecodeAudio(boolean z);

    private native void EnableErrorCallback();

    private native double GetProgress();

    private native void InitCodecs();

    private native void Open(String str) throws IOException;

    private native void Play() throws IOException;

    private native void SetSurface(Surface surface) throws IOException;

    private native void Stop();

    public native double GetCurrentTime();

    public native double GetDuration();

    public native boolean IsDecodingAudio();

    public native boolean IsLiveStream();

    public native boolean IsPlaying();

    public native boolean Pause(boolean z);

    public native void Seek(double d);

    private static boolean loadLibs() {
        if (sLoaded) {
            return true;
        }
        boolean err = false;
        for (int i = 0; i < LIBS.length; i++) {
            try {
                System.loadLibrary(LIBS[i]);
                Log.d("FFMpeg", "load lib: " + LIBS[i]);
            } catch (UnsatisfiedLinkError e) {
                Log.d("FFMpeg", "Couldn't load lib: " + LIBS[i] + " - " + e.getMessage());
                err = true;
            }
        }
        if (!err) {
            sLoaded = true;
        }
        return sLoaded;
    }

    public VJMediaPlayer() {
        initVideoView();
    }

    private void initVideoView() {
        loadLibs();
        if (sLoaded) {
            EnableErrorCallback();
            InitCodecs();
            Log.d("FFMpeg", "initVideoView");
        }
    }

    public void setDisplay(SurfaceHolder sh) {
        try {
            SetSurface(sh.getSurface());
        } catch (IOException e) {
        }
    }

    public void start() {
        this.mPlaying = true;
        Log.d("FFMpeg", "startVideo");
        if (this.mRenderThread == null) {
            this.mRenderThread = new Thread() {
                public void run() {
                    try {
                        VJMediaPlayer.this.Play();
                    } catch (IOException e) {
                        VJMediaPlayer.this.mPlaying = false;
                    }
                }
            };
            this.mRenderThread.start();
        }
    }

    public void setVideoPath(String filePath) throws IOException {
        Log.d("FFMpeg", "setVideoPath:" + filePath);
        Open(filePath);
    }

    public boolean pause() {
        Log.d("FFMpeg", "Pause");
        this.mPlaying = false;
        return Pause(true);
    }

    public boolean resume() {
        Log.d("FFMpeg", "resume");
        this.mPlaying = true;
        return Pause(false);
    }

    public void decodeAudio(boolean decode) {
        Log.d("FFMpeg", "decodeAudio");
        EnableDecodeAudio(decode);
    }

    public void stop() throws InterruptedException {
        if (this.mPlaying) {
            Log.d("FFMpeg", "stop");
            this.mPlaying = false;
            Stop();
            if (this.mRenderThread != null) {
                this.mRenderThread.join();
            }
            this.mRenderThread = null;
        }
    }

    public void release() {
        Log.d("FFMpeg", "release");
        try {
            stop();
        } catch (InterruptedException e) {
        }
        Close();
    }

    protected void finalize() throws Throwable {
        Log.d("FFMpeg", "finalize");
        sLoaded = false;
    }
}
