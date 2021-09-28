package com.anymediacloud.iptv.standard;

import com.gsoft.mitv.MainActivity;

public class ForceTV {
    private static final int MEMORY = 20971520;

    public native int start(int port, int memory);

    public native int stop();

    static {
        MainActivity.loadlib();
    }

    public void start(int port) {
        try {
            start(port, 20971520);
        } catch (Exception e) {
        }
    }
}
