package com.forcetech.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.forcetech.android.ForceTV;

public class P9PService extends Service {

    private ForceTV forceTV;

    @Override
    public IBinder onBind(Intent arg0) {
		try {
			forceTV = new ForceTV();
			forceTV.start("p9p", 9913);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
		try {
			forceTV.stop();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return super.onUnbind(intent);
    }

}
