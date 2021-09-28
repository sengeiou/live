package com.vv.live.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.multidex.MultiDex;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.model.HttpHeaders;
import com.vv.live.ChannelDatas;
import com.vv.live.CrashHandler;
import com.vv.live.util.ToastMgr;
import com.wanjian.cockroach.Cockroach;
import com.wanjian.cockroach.ExceptionHandler;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * 作者：By 15968
 * 日期：On 2021/5/4
 * 时间：At 19:29
 */

public class Application extends android.app.Application {
    private static WeakReference<Context> mContext;
    public static Application application = null;
    public static final long HTTP_TIMEOUT_MILLISECONDS = 10000;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mContext = new WeakReference<>(getApplicationContext());
        installCrashHandler();
        //OKGO配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(HTTP_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(HTTP_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(HTTP_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, HttpsUtils.UnSafeTrustManager)
                .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        HttpHeaders headers = new HttpHeaders();
        HttpHeaders.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        headers.put("charset", "UTF-8");
        OkGo.getInstance().init(this).setOkHttpClient(builder.build())
                .setRetryCount(1)
                .addCommonHeaders(headers);
    }

    public static Context getContext() {
        return mContext.get();
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    private void installCrashHandler() {
        CrashHandler.getInstance().initDefaultHandler(getContext());
        Cockroach.install(this, new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                Timber.e(throwable, "--->onUncaughtExceptionHappened:" + thread + "<---");
                String fileName = CrashHandler.getInstance().saveCatchInfo2File(throwable);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        String path = ChannelDatas.getRootDir() + File.separator + "崩溃日志" + File.separator;
                        ToastMgr.shortBottomCenter(getApplicationContext(), "检测到异常崩溃信息，地址：" + path);
                    }
                });
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                String fileName = CrashHandler.getInstance().saveCatchInfo2File(throwable);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        String path = ChannelDatas.getRootDir() + File.separator + "崩溃日志" + File.separator;
                        ToastMgr.shortBottomCenter(getApplicationContext(), "检测到异常崩溃信息，地址：" + path);
                    }
                });
            }

            @Override
            protected void onEnterSafeMode() {

            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                Thread thread = Looper.getMainLooper().getThread();
                CrashHandler.getInstance().crashMySelf(thread, e);
            }
        });
    }
}
