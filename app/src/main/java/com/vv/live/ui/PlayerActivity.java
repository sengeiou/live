package com.vv.live.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.forcetech.service.P2PService;
import com.forcetech.service.P2p_AService;
import com.forcetech.service.P3PService;
import com.forcetech.service.P4PService;
import com.forcetech.service.P5PService;
import com.forcetech.service.P6PService;
import com.forcetech.service.P8PService;
import com.forcetech.service.P9PService;
import com.media.Settings;
import com.nagasoft.player.UrlChanged;
import com.setting.MySettings;
import com.smarx.notchlib.NotchScreenManager;
import com.view.ChannelListView;
import com.view.IJKVideoView;
import com.view.ScrollTextView;
import com.view.XVideoView;
import com.vv.live.ChannelData;
import com.vv.live.ChannelDatas;
import com.vv.live.ChannelObject;
import com.vv.live.FavorList;
import com.vv.live.FavorObject;
import com.vv.live.HttpRequest;
import com.vv.live.ListViewAdapterLeft;
import com.vv.live.ListViewAdapterRight;
import com.vv.live.ListViewAdapterSettingLeft;
import com.vv.live.ListViewAdapterSettingRight;
import com.vv.live.Play;
import com.vv.live.R;
import com.vv.live.SettingOption;
import com.vv.live.data.SubManager;
import com.vv.live.parser.HttpParser;
import com.vv.live.util.ClipboardUtil;
import com.vv.live.util.IPUtils;
import com.vv.live.util.StringUtil;
import com.vv.live.util.ToastMgr;
import com.webserver.WebServer;

import net.media.mpc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;
import tv.danmaku.ijk.media.player.IMediaPlayer;

//import com.nagasoft.player.VJPlayer;

public class PlayerActivity extends Activity implements OnPreparedListener, OnErrorListener, OnCompletionListener, OnInfoListener, android.view.GestureDetector.OnGestureListener, tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener, tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener, tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener, tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener, UrlChanged {
    public static Context context;
    private ScrollTextView tip_chname;
    private IJKVideoView ijkVideoView;
    private XVideoView mVideoView;
    private TextView tv_srcinfo;
    public static int curType = 0;
    public static int codemodel = 1;
    public static int decoder = 1;
    private boolean autoStart;
    private int screenScale;
    private boolean reverseUD;
    private int bufferTimeOut = 15000;
    private ArrayList<String> channelSourceArrayList;
    private int curChannelIndex = 0;
    private int curSourceIndex = 0;
    public static ChannelDatas channelDatas;
    public static FavorList favorList;
    private PopupWindow mChannelSelectWindow;
    public static int mLeftPos;
    private View menuView;
    private ChannelListView mLeftList;
    private ChannelListView mRightList;
    private ListViewAdapterLeft mAdapterLeft;
    private ListViewAdapterRight mAdapterRight;
    private MySettings mySettings = new MySettings(this);
    private MediaController mMediaController;
    //    private VJPlayer vjPlayer;
    private Runnable rHideMenu;
    private int mCount;
    private int mNum;
    private Runnable rEnterChannel;
    private View settingView;
    private ChannelListView mSettingList;
    private ListViewAdapterSettingLeft mAdapterSetting;
    protected Runnable rHideSetting;
    private PopupWindow mSettingWindow;
    private String mUrl = "";
    private Runnable rChangeSource;
    private float x1;
    private LinearLayout ll_epg;
    protected HashMap<Integer, String> epgData;
    private ScrollTextView tip_epg1;
    private ScrollTextView tip_epg2;
    private LinearLayout ll_epginfo;
    private Runnable rHideEpg;
    public static String playUrl;
    public boolean isDownLoading;
    private mpc mp;
    private boolean mCanSeek = false;
    private String verSettingText;
    private int iTime = 0;
    private int defaultfontsize = 20;
    private long traffic_data = 0;
    private Runnable rShowProgress;
    private ServiceConnection conn;
    public static String myappver = "";
    public static String appname;
    private boolean isShowError = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            try {
                switch (msg.what) {
                    case Play.UPDATE_CHANNEL:
                        updateChannelDataNoTip();
                        break;
                    case Play.SETVIDEOURI:
                        try {
                            setVideo();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        break;
                    //重新加载
                    case Play.RELOAD:
                        //设置视频url
                        try {
                            setVideoUri();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    //开始播放
                    case Play.START_PLAY:
                        try {
                            playVideo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    //保存位置
                    case Play.SAVEPOS:
                        try {
                            if (mCanSeek) {
                                long curPos = 0;
                                if (ijkVideoView.getVisibility() == View.VISIBLE) {
                                    curPos = ijkVideoView.getCurrentPosition();
                                } else {
                                    curPos = mVideoView.getCurrentPosition();
                                }
                                mySettings.saveSetting(getcurrentCategory() + getCurrentChannelName(), curPos);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        break;
                    //隐藏数字
                    case Play.HIDE_NUM:
                        //电视节目.设置为可见性！
                        tv_shownum.setVisibility(View.INVISIBLE);
                        handler.removeMessages(Play.SHOW_NUM);
                        break;
                    //时间显示
                    case Play.SHOW_NUM:
                        Date setTimeOption = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss EEE");
                        tv_shownum.setText(sdf.format(setTimeOption));
                        if (setTimeOn) {
                            tv_shownum.setTextColor(Color.WHITE);
                            tv_shownum.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize + 8);
                            tv_shownum.setVisibility(View.VISIBLE);
                        } else {
                            tv_shownum.setTextColor(Color.WHITE);
                            tv_shownum.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize + 8);
                            tv_shownum.setVisibility(View.INVISIBLE);
                        }
                        Message m = Message.obtain();
                        m.what = Play.SHOW_NUM;
                        handler.sendMessageDelayed(m, 1000);
                        break;
                    //隐藏音量
                    case Play.HIDE_VOLUME:
                        tv_voluminfo.setVisibility(View.INVISIBLE);
                        break;
                    case Play.ENTERCHANNEL:
                        enterChannel(mNum);
                        break;
                    //隐藏加载动画
                    case Play.HIDE_EPG:
                        ll_loading.setVisibility(View.GONE);
                        tv_tiploading.setVisibility(View.GONE);
                        if (mChannelSelectWindow != null && !mChannelSelectWindow.isShowing()) {
                            tv_shownum.setVisibility(View.INVISIBLE);
                        }
                        handler.removeCallbacks(rShowProgress);
                        ll_epg.setVisibility(View.GONE);
                        break;
                    //加载显示
                    case Play.SHOW_EPG:
                        if (mChannelSelectWindow != null && !mChannelSelectWindow.isShowing()) {
                            //设置为白色
                            tv_shownum.setTextColor(Color.WHITE);
                            tv_shownum.getPaint().setFakeBoldText(true);
                            tv_shownum.setVisibility(View.VISIBLE);
                        }
                        ll_loading.setVisibility(View.VISIBLE);
                        tv_tiploading.setVisibility(View.VISIBLE);
                        showEPG();
                        break;

                    case Play.SHOW_NET_SPEED:
                        String speedtext;
                        if (traffic_data < 1024) {
                            //右下方网速显示
                            speedtext = "0KB/s";
                        } else if (traffic_data / 1024 > 1024) {
                            speedtext = traffic_data / 1024 / 1024 + "MB/s";
                        } else {
                            speedtext = traffic_data / 1024 + "KB/s";
                        }

                        if (showNetOn) {
                            tv_netspeedinfo.setText(speedtext);
                            tv_netspeedinfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                            tv_netspeedinfo.setVisibility(View.VISIBLE);
                            tv_right_top_tipnetspeed.setText(speedtext);
                            tv_right_top_tipnetspeed.setVisibility(View.VISIBLE);
                        } else {
                            tv_netspeedinfo.setText(speedtext);
                            tv_netspeedinfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                            tv_netspeedinfo.setVisibility(View.INVISIBLE);
                        }
                        break;
                    //TVbus源准备就绪
                    case Play.TVBUS_ONPREPARED:
//                        try {
//                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                            playUrl = jsonObject.optString("hls");
                        prepareSetVideoUri();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        break;
                    //播放VJMS源
                    case Play.PLAY_VJMS:
                        playUrl = msg.obj.toString();
                        prepareSetVideoUri();
                        break;
                    //隐藏公告
                    case Play.HIDEADTEXT:
                        break;
                    //显示公告
                    case Play.SHOWADTEXT:
                        break;
                    //节目列表更新
                    case Play.UPDATE_SUCESS:
                        Toast.makeText(getApplicationContext(), "节目列表更新成功！", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ChannelDatas.initDir();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //设置沉浸式状态栏，在MIUI系统中，状态栏背景透明。原生系统中，状态栏背景半透明。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        try {
            myappver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            appname = getPackageManager()
                    .getApplicationLabel(
                            (getPackageManager().getPackageInfo(
                                    getPackageName(), 0).applicationInfo))
                    .toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        defaultfontsize = getScreenWidth() / 42;
        initViews();
        getSettings();
        startAppTimer();
        context = this;
        loadChannelData();

        showNetSpeed();
        initForceTV();

        initWebServer();
        //腾讯在线日志
        //CrashReport.initCrashReport(getApplicationContext());

        try {
            if (channelDatas.size() <= curType) {
                curType = channelDatas.size() - 1;
            }
            if (channelDatas.size() > curType && channelDatas.get(curType).psw.isEmpty()) {
                playVideo();
            } else {
                curType = 0;
                curChannelIndex = 0;
                playVideo();
            }
        } catch (Exception e) {
        }

        SubManager.checkSub(this);
    }

    //加载频道数据
    public static void loadChannelData() {
        favorList = new FavorList(context);
        channelDatas = new ChannelDatas(context);
        channelDatas.loadData(context);
        channelDatas.loadFavor(favorList);
    }

    //初始化web服务器
    private void initWebServer() {
        new WebServer().start();
    }

    //多P播放支持
    private void initForceTV() {
        try {
            conn = new ServiceConnection() {
                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                }

                @Override
                public void onServiceConnected(ComponentName arg0, IBinder arg1) {
                }
            };
            bindService(new Intent(this, P2p_AService.class), conn, 1);
            bindService(new Intent(this, P2PService.class), conn, 1);
            bindService(new Intent(this, P3PService.class), conn, 1);
            bindService(new Intent(this, P4PService.class), conn, 1);
            bindService(new Intent(this, P5PService.class), conn, 1);
            bindService(new Intent(this, P6PService.class), conn, 1);
//            bindService(new Intent(this, P7PService.class), conn, 1);
            bindService(new Intent(this, P8PService.class), conn, 1);
            bindService(new Intent(this, P9PService.class), conn, 1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //显示网络速度
    private void showNetSpeed() {
        if (rNetSpeed == null) {
            rNetSpeed = new Runnable() {
                private long total_rdata = TrafficStats.getTotalRxBytes();

                @Override
                public void run() {
                    traffic_data = TrafficStats.getTotalRxBytes() - total_rdata;
                    total_rdata = TrafficStats.getTotalRxBytes();
                    sendMessage(Play.SHOW_NET_SPEED);
                    showNetSpeed();
                }
            };
        }
        handler.postDelayed(rNetSpeed, 1000);
    }

    //启用计时器
    private void startAppTimer() {
        if (timerCheck == null) {
            timerCheck = new Timer();
        } else {
            timerCheck.cancel();
        }
        timerCheck.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(Play.SAVEPOS);
            }
        }, 6000, 6000);
    }

    private int fontSize = 8;
    private Timer timerCheck;
    private TextView tv_setting_title;
    private TextView tv_isp;
    private LinearLayout ll_loading;
    private View tv_tiploading;

    private TextView tv_voluminfo;
    private TextView tv_curepg_left;
    private TextView tv_nextepg_left;
    private boolean startScroll;
    private GestureDetector gestureDetector;
    private AudioManager mAudioManager;
    private int currentVolume;
    private int maxVolume;
    public static int mRightPos;
    private TextView tv_shownum;
    private TextView tv_channelnum;
    public static int leftHeight = 5;
    private Runnable rNetSpeed;
    private Button btn_exit;
    private Button btn_setting;
    private PopupWindow dialog;
    public static int settingHeight;
    private boolean showNetOn = false;
    private boolean setTimeOn = true;
    private TextView tv_netspeedinfo;
    private MediaPlayer mPlayer;
    private TextView tv_webadmin;
    public static String o = "";
    private Thread playThread;
    protected int leftLines = 8;
    protected int rightLines = 8;
    private Runnable rVolume;
    private ListView mSettingValueList;
    private ArrayList<SettingOption> settingArrayList;
    private ListViewAdapterSettingRight mAdapterSettingValue;
    //    private ForceTV mitv;
    protected int mSettingPos = 0;
    private boolean needDecode = false;
    public static int rightHeight;
    private ImageView iv_circle_bg;
    private ObjectAnimator objectAnimator;
    private TextView tv_right_top_channel_name;
    private TextView tv_right_top_tipnetspeed;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (channelDatas.size() <= curType) {
                curType = channelDatas.size() - 1;
            }
            if (channelDatas.size() > curType && channelDatas.get(curType).psw.isEmpty()) {
                playVideo();
            } else {
                curType = 0;
                curChannelIndex = 0;
                playVideo();
            }
        } catch (Exception e) {
        }
    }

    //销毁
    @Override
    protected void onDestroy() {
        try {
            mp.MPCStop();
            ijkVideoView.stopPlayback();
            mVideoView.stopPlayback();
            timerCheck.cancel();
            unbindService(conn);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    //停止
    @Override
    protected void onStop() {
        try {
            ijkVideoView.stopPlayback();
            mVideoView.stopPlayback();
        } catch (Exception e) {
        }
        timerCheck.cancel();
        super.onStop();
    }

    //暂停
    @Override
    protected void onPause() {
        try {
            ijkVideoView.pause();
        } catch (Exception e) {
        }
        super.onPause();
    }

    //播放
    private void playVideo() {
        Timber.d("playVideo, setVideo");
        try {
            if (mMediaController == null) {
                mMediaController = new MediaController(this);
                mMediaController.hide();
            }
            curSourceIndex = mySettings.getCurrentSource(getCurrentChannelName());
            setVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //节目源播放
    private void setVideo() throws Exception {
        try {
            needDecode = false;
            try {
                if (channelDatas.size() <= curType) {
                    curType = channelDatas.size() - 1;
                }
                if (!channelDatas.get(curType).psw.isEmpty() && !channelDatas.get(curType).hasPassed) {
                    return;
                }
            } catch (Exception e) {
            }
            channelSourceArrayList = new ArrayList<String>();
            if (curType >= channelDatas.size()) {
                curType = channelDatas.size() - 1;
            }
            if (curType < 0) {
                return;
            }
            if (channelDatas.get(curType).data.size() == 0) {
                return;
            }
            if (curChannelIndex >= channelDatas.get(curType).data.size()) {
                curChannelIndex = 0;
            }
            if (channelDatas.get(curType).data.get(curChannelIndex).source.size() == 0) {
                return;
            }
            if (curSourceIndex >= channelDatas.get(curType).data.get(curChannelIndex).source.size()) {
                curSourceIndex = 0;
            }
            if (rHideEpg != null) handler.removeCallbacks(rHideEpg);

            channelSourceArrayList = channelDatas.get(curType).data.get(curChannelIndex).source;
            //sqlUtils.getSources(channelName,getcurrentCategory());

            if (channelSourceArrayList.size() == 0) return;
            if (curSourceIndex >= channelSourceArrayList.size()) {
                curSourceIndex = 0;
            }

            mUrl = channelSourceArrayList.get(curSourceIndex);

            mCanSeek = false;
            if (bufferTimeOut < 5000) bufferTimeOut = 5000;
            if (mUrl.toLowerCase().startsWith("p") || mUrl.startsWith("vjms") || mUrl.startsWith("tvbus")) {
                changeSourceOnBuffering(bufferTimeOut + 20000);
            } else {
                changeSourceOnBuffering(bufferTimeOut);
            }


//        if (mitv != null) {
//            mitv.stop();
//            mitv = null;
//        }
            if (mp == null) {
                mp = mpc.getInstance();
            }

//            if (vjPlayer != null) {
//                vjPlayer.stop();
//                vjPlayer._release();
//                vjPlayer = null;
//            }
            saveSetting();

            if (mUrl.startsWith("tvbus")) {
                ToastMgr.shortBottomCenter(this, "格式不支持：" + mUrl);
            } else if (mUrl.startsWith("msc")) {
                playUrl = mp.getUrl(mUrl);
                prepareSetVideoUri();
            } else if (mUrl.startsWith("mitv")) {
                ToastMgr.shortBottomCenter(this, "格式不支持：" + mUrl);
                //            mitv = new ForceTV();
                //            mitv.start(9002);
                //            playUrl = getUrl(mUrl, "9002");
                //            prepareSetVideoUri();
            } else if (mUrl.startsWith("P2p")) {
                playUrl = getUrl(mUrl, "9001");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("p2p")) {
                playUrl = getUrl(mUrl, "9906");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("p3p")) {
                playUrl = getUrl(mUrl, "9907");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("p4p")) {
                playUrl = getUrl(mUrl, "9908");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("p5p")) {
                playUrl = getUrl(mUrl, "9909");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("p6p")) {
                playUrl = getUrl(mUrl, "9910");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("p7p")) {
                ToastMgr.shortBottomCenter(this, "格式不支持：" + mUrl);
                //            playUrl = getUrl(mUrl, "9911");
                //            prepareSetVideoUri();
            } else if (mUrl.startsWith("p8p")) {
                playUrl = getUrl(mUrl, "9912");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("p9p")) {
                playUrl = getUrl(mUrl, "9913");
                prepareSetVideoUri();
            } else if (mUrl.startsWith("vjms")) {
                ToastMgr.shortBottomCenter(this, "格式不支持：" + mUrl);
//                vjPlayer = new VJPlayer(this);
//                vjPlayer.setURL(mUrl);
//                vjPlayer.start();
//                sendMessage(Play.SHOW_NET_SPEED);
//                sendMessage(Play.SHOW_EPG);
            } else if (mUrl.toLowerCase().startsWith("http") || mUrl.toLowerCase().startsWith("rtsp") || mUrl.toLowerCase().startsWith("rtmp")) {
                playUrl = mUrl;
                prepareSetVideoUri();
            } else {
                playUrl = mUrl;
                prepareSetVideoUri();
                needDecode = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //视频url
    private void prepareSetVideoUri() {
        if (playThread != null && playThread.isAlive()) return;
        playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                decodeURL();
            }
        });
        playThread.start();
    }

    //数据解密
    private void decodeURL() {
        //UA设置
        ijkVideoView.setUserAgent("mitv");
        ijkVideoView.setUserAgent("fhtv");
        sendMessage(Play.RELOAD);
        sendMessage(Play.SHOW_NET_SPEED);
        sendMessage(Play.SHOW_EPG);
    }

    //rtsp,rtmp格式解码
    private void setVideoUri() {
        playUrl = playUrl == null ? "" : playUrl.replace("，，，", ",");
        mVideoView.stopPlayback();
        ijkVideoView.stopPlayback();
        int codec = codemodel;
        if (codemodel == 0) {
            if (playUrl.startsWith("rtsp") || playUrl.startsWith("rtmp")) {
                codec = 1;
            }
        }

        if (codec == 1 || codec == 3 || codec == 4) {
            ijkVideoView.setHudView(new TableLayout(this));
            mVideoView.setVisibility(View.INVISIBLE);
            ijkVideoView.setVisibility(View.VISIBLE);
            try {
                ijkVideoView.setPlayerType(codec == 4 ? Settings.PV_PLAYER__IjkExoMediaPlayer : Settings.PV_PLAYER__IjkMediaPlayer);
                ijkVideoView.setMediacodec(codec == 1);
                String[] urls = playUrl.split(";");
                if (urls.length > 1 && urls[1].trim().startsWith("{") && urls[1].trim().endsWith("}")) {
                    Map<String, String> headers = HttpParser.getHeaders(playUrl);
                    ijkVideoView.setVideoURI(Uri.parse(urls[0]), headers);
                } else {
                    ijkVideoView.setVideoPath(playUrl);
                }
            } catch (Exception e) {
            }
        } else {
            mVideoView.setHudView(new TableLayout(this));
            ijkVideoView.setVisibility(View.INVISIBLE);
            mVideoView.setVisibility(View.VISIBLE);
            try {
                mVideoView.setVideoPath(playUrl);
            } catch (Exception e) {
            }
        }
    }

    private String getUrl(String mUrl, String port) {
        Uri uri = Uri.parse(mUrl);
        StringBuilder sb = new StringBuilder();
        sb.append("http://127.0.0.1:");
        sb.append(port);
        String pUrlString = sb.toString() + "/" + uri.getLastPathSegment();
        sb.append("/cmd.xml?cmd=switch_chan&server=");
        sb.append(uri.getHost());
        sb.append(":");
        sb.append(uri.getPort());
        sb.append("&id=");
        String idString = uri.getLastPathSegment();
        int index = idString.indexOf(".");
        if (index != -1) {
            sb.append(idString.substring(0, index));
        } else {
            sb.append(idString);
            pUrlString = pUrlString + ".ts";
        }
        if (uri.getQuery() != null) sb.append("&" + uri.getQuery());
        HttpRequest.doGet(sb.toString());
        return pUrlString;
    }

    //缓冲时换源
    private void changeSourceOnBuffering(int timeout) {
        if (rChangeSource != null) {
            handler.removeCallbacks(rChangeSource);
        } else {
            rChangeSource = new Runnable() {
                @Override
                public void run() {
                    if (channelDatas.size() > curType && channelDatas.get(curType).getName().contains("本地投屏")) {
                        Timber.d("changeSourceOnBuffering, 本地投屏");
                        ToastMgr.shortBottomCenter(PlayerActivity.this, "当前缓冲较慢。。。");
                        return;
                    }
                    if (mCanSeek || channelSourceArrayList.size() == 1) {
                        moveNext();
                    } else {
                        moveRight();
                    }
                }
            };
        }
        int rTime = timeout;
        handler.postDelayed(rChangeSource, rTime);
    }

    //获取当前频道名称
    private String getCurrentChannelName() {
        if (curType < channelDatas.size() && curChannelIndex < channelDatas.get(curType).data.size()) {
            return channelDatas.get(curType).data.get(curChannelIndex).name;
        } else {
            return "";
        }
    }

    //获取当前类别
    private String getcurrentCategory() {
        try {
            return channelDatas.get(curType).name;
        } catch (Exception e) {
            e.printStackTrace();
            curType = 0;
            try {
                return channelDatas.get(curType).name;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return "";
    }

    //清空数据列表
    private void getSettings() {
        SharedPreferences sp = getSharedPreferences("live", Context.MODE_PRIVATE);
        curType = sp.getInt("Typeindex", 1);
        curChannelIndex = sp.getInt("curChannel", 0);
        codemodel = sp.getInt("codemodel", decoder);
        autoStart = sp.getBoolean("autoStart", false);
        screenScale = sp.getInt("screenScale", 0);
        reverseUD = sp.getBoolean("reverseUD", false);
        bufferTimeOut = sp.getInt("bufferTimeOut", 15000);
        showNetOn = sp.getBoolean("showNet", false);
        fontSize = defaultfontsize;
    }

    //清除设置
    private void clearSetting() {
        mSettingWindow.dismiss();
        mySettings.clearSetting();
        Toast.makeText(getApplicationContext(), "已清空所有数据，请重启软件!", Toast.LENGTH_LONG).show();
    }

    //显示清除对话框
    private void showclearDialog() {
        TextView text = new TextView(this);
        text.setText("清空所有数据?");
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        text.setGravity(Gravity.CENTER);
        text.setPadding(10, 10, 10, 10);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("系统提示")
                .setView(text)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        clearSetting();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    //保存设置
    private void saveSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences("live", MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putInt("Typeindex", curType);
        editor.putInt("curChannel", curChannelIndex);
        editor.putInt(getCurrentChannelName(), curSourceIndex);

        editor.putInt("codemodel", codemodel);
        editor.putBoolean("autoStart", autoStart);
        editor.putInt("screenScale", screenScale);
        editor.putInt("fontSize", fontSize);
        editor.putBoolean("reverseUD", reverseUD);
        editor.putInt("bufferTimeOut", bufferTimeOut);
        editor.putBoolean("showNet", showNetOn);
        editor.commit();// 提交修改
        SharedPreferences.Editor edit = sharedPreferences.edit();

        if (!autoStart) {
            edit.putBoolean("Toggle0", true);
            edit.commit();
        }
    }

    //EPG显示资源列表
    private void initViews() {
        tip_chname = (ScrollTextView) findViewById(R.id.tv_channelname);
        ll_epg = (LinearLayout) findViewById(R.id.ll_epg);
        tip_epg1 = (ScrollTextView) findViewById(R.id.tv_epgcurrent);
        tip_epg2 = (ScrollTextView) findViewById(R.id.tv_epgnext);
        tv_curepg_left = (TextView) findViewById(R.id.tv_curepg_left);
        tv_nextepg_left = (TextView) findViewById(R.id.tv_nextepg_left);
        tv_srcinfo = (TextView) findViewById(R.id.tv_srcinfo);
        ll_epginfo = (LinearLayout) findViewById(R.id.ll_epginfo);
        tv_channelnum = (TextView) findViewById(R.id.tv_channelnum);
        tv_voluminfo = (TextView) findViewById(R.id.tv_voluminfo);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        tv_tiploading = (RelativeLayout) findViewById(R.id.tv_tiploading);
        ijkVideoView = (IJKVideoView) findViewById(R.id.ijkplayer);
        mVideoView = (XVideoView) findViewById(R.id.mplayer);
        tv_shownum = (TextView) findViewById(R.id.tv_shownum);
        tv_netspeedinfo = (TextView) findViewById(R.id.tv_netspeedinfo);
        tv_right_top_tipnetspeed = (TextView) findViewById(R.id.tv_right_top_tipnetspeed);
        tv_right_top_channel_name = (TextView) findViewById(R.id.tv_right_top_channel_name);
        iv_circle_bg = (ImageView) findViewById(R.id.iv_circle_bg);
        ijkVideoView.setOnPreparedListener(this);
        ijkVideoView.setOnErrorListener(this);
        ijkVideoView.setOnInfoListener(this);
        ijkVideoView.setOnCompletionListener(this);

        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnCompletionListener(this);

        gestureDetector = new GestureDetector(this, this);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //右上角图片旋转
        objectAnimator = ObjectAnimator.ofFloat(iv_circle_bg, "rotation", new float[]{360.0f});
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.start();

    }

    //完成时
    @Override
    public void onCompletion(IMediaPlayer mp) {
        if (mCanSeek) {
            mySettings.saveSetting(getcurrentCategory() + getCurrentChannelName(), (long) 0);
            if (curSourceIndex == channelSourceArrayList.size() - 1) {
                moveNext();
            } else {
                moveRight();
            }
        } else {
            playVideo();
        }

    }

    //出错时
    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        Timber.i("IMediaPlayer onError");
        try {
            if (channelDatas.size() > curType && channelDatas.get(curType).getName().contains("本地投屏")) {
                Timber.i("IMediaPlayer onError, 本地投屏");
                //本地投屏失败的不自动切换频道
                if (isShowError) {
                    return true;
                }
                isShowError = true;
                ToastMgr.shortBottomCenter(this, "播放失败，请换源");
                ijkVideoView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isShowError = false;
                    }
                }, 5000);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("IMediaPlayer onError", e);
        }
        try {
            handler.removeMessages(Play.RELOAD);
            Message msg = Message.obtain();
            msg.what = Play.RELOAD;
            handler.sendMessageDelayed(msg, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //关于信息
    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {

        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                sendMessage(Play.SHOW_EPG);
                changeSourceOnBuffering(bufferTimeOut);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                sendMessage(Play.HIDE_EPG);
                handler.removeCallbacks(rChangeSource);
                break;
            case -10000:
                changeSourceOnBuffering(5000);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                hideEPG(2000);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            default:
                break;
        }
        return false;
    }

    //准备就绪
    @Override
    public void onPrepared(IMediaPlayer mp) {
        setScreenScale(screenScale);
        handler.removeCallbacks(rChangeSource);
        handler.removeMessages(Play.RELOAD);
        hideEPG(2000);
        if (mp.getDuration() > 0) {
            mCanSeek = true;
        } else {
            mCanSeek = false;
        }
        if (mCanSeek) {
            long pos = mySettings.getSetting(getcurrentCategory() + getCurrentChannelName());
            ijkVideoView.seekTo((int) pos);
        }
        mp.start();
    }

    @Override
    public boolean onInfo(MediaPlayer arg0, int what, int arg2) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                sendMessage(Play.SHOW_EPG);
                changeSourceOnBuffering(bufferTimeOut);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                sendMessage(Play.HIDE_EPG);
                handler.removeCallbacks(rChangeSource);
                break;
            case -10000:
                changeSourceOnBuffering(5000);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                hideEPG(2000);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (mCanSeek) {
            mySettings.saveSetting(getcurrentCategory() + getCurrentChannelName(), (long) 0);
            if (curSourceIndex == channelSourceArrayList.size() - 1) {
                moveNext();
            } else {
                moveRight();
            }
        } else {
            playVideo();
        }
    }

    @Override
    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
        Timber.i("MediaPlayer onError");
        try {
            if (channelDatas.size() > curType && channelDatas.get(curType).getName().contains("本地投屏")) {
                Timber.i("MediaPlayer onError, 本地投屏");
                //本地投屏失败的不自动切换频道
                if (isShowError) {
                    return true;
                }
                isShowError = true;
                ToastMgr.shortBottomCenter(this, "播放失败，请换源");
                ijkVideoView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isShowError = false;
                    }
                }, 5000);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("MediaPlayer onError", e);
        }
        try {
            handler.removeMessages(Play.RELOAD);
            Message msg = Message.obtain();
            msg.what = Play.RELOAD;
            handler.sendMessageDelayed(msg, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer = mp;
        setScreenScale(screenScale);
        handler.removeCallbacks(rChangeSource);
        handler.removeMessages(Play.RELOAD);
        hideEPG(2000);
        if (mp.getDuration() > 0) {
            mCanSeek = true;
        } else {
            mCanSeek = false;
        }
        if (mCanSeek) {
            long pos = mySettings.getSetting(getcurrentCategory() + getCurrentChannelName());
            mVideoView.seekTo((int) pos);
        }
        mp.start();
    }

    private void setScreenScale(int scale) {
        if (ijkVideoView.getVisibility() == View.VISIBLE) {
            ijkVideoView.setScreenScale(scale);
            ijkVideoView.setVideoHeight(getScreenHeight());
        } else {
            mVideoView.setScreenScale(scale, mPlayer);
        }
    }

    private List<Float> getSpeedList() {
        return new ArrayList<>(Arrays.asList(1f, 1.2f, 1.5f, 2f, 3f, 4f));
    }

    //设置
    private void setSettingText(final int pos) {
        try {
            SettingOption settingOption = new SettingOption("直播选源");
            try {
                if (!(channelDatas.get(curType) == null || (channelDatas.get(curType)).data == null || (channelDatas.get(curType)).data.get(this.curChannelIndex) == null || ((channelDatas.get(curType)).data.get(this.curChannelIndex)).source == null)) {
                    ArrayList<String> arrayList = ((channelDatas.get(curType)).data.get(this.curChannelIndex)).source;
                    int pos2 = 0;
                    while (pos2 < arrayList.size()) {
                        ArrayList<String> arrayList2 = settingOption.mRightList;
                        StringBuilder sb = new StringBuilder();
                        sb.append("源");
                        pos2++;
                        sb.append(pos2);
                        arrayList2.add(sb.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SettingOption playOption = new SettingOption("播放控制");
            playOption.mRightList.add("播放");
            playOption.mRightList.add("暂停");
            SettingOption codecOption = new SettingOption("解码模式");
            codecOption.mRightList.add("智能解码");
            codecOption.mRightList.add("IJK硬解");
            codecOption.mRightList.add("原生解码");
            codecOption.mRightList.add("IJK软解");
            codecOption.mRightList.add("EXO内核");
            SettingOption screenOption = new SettingOption("屏幕比例");
            screenOption.mRightList.add("全屏");
            screenOption.mRightList.add("原始");
            screenOption.mRightList.add("4：3");
            screenOption.mRightList.add("16：9");

            SettingOption speedOption = new SettingOption("倍速播放");
            final List<Float> speeds = getSpeedList();
            for (Float speed : speeds) {
                speedOption.mRightList.add(String.format("X %.1f", speed));
            }

            SettingOption timeoutOption = new SettingOption("超时跳转");
            timeoutOption.mRightList.add("5秒");
            timeoutOption.mRightList.add("10秒");
            timeoutOption.mRightList.add("15秒");
            timeoutOption.mRightList.add("20秒");
            timeoutOption.mRightList.add("25秒");
            timeoutOption.mRightList.add("30秒");
            SettingOption shownetOption = new SettingOption("显示网速");
            shownetOption.mRightList.add("显示");
            shownetOption.mRightList.add("关闭");
            SettingOption dataOption = new SettingOption("数据管理");
//        dataOption.mRightList.add("更新节目列表");
//        dataOption.mRightList.add("清空所有数据");
            dataOption.mRightList.add("直播源管理");
            dataOption.mRightList.add("软件仅供学习");
            SettingOption setTimeOption = new SettingOption("时间显示");
            setTimeOption.mRightList.add("显示");
            setTimeOption.mRightList.add("关闭");
//        SettingOption channelOption = new SettingOption("屏蔽分类");
//        channelOption.mRightList.addAll(ChannelDatas.categoryList);
            settingArrayList = new ArrayList<>();
            settingArrayList.add(settingOption);
            settingArrayList.add(speedOption);
            settingArrayList.add(playOption);
            settingArrayList.add(codecOption);
            settingArrayList.add(screenOption);
            settingArrayList.add(timeoutOption);
            settingArrayList.add(shownetOption);
            settingArrayList.add(setTimeOption);
//        settingArrayList.add(channelOption);
            settingArrayList.add(dataOption);

            ArrayList<String> rightList = settingArrayList.get(pos).mRightList;
            mSettingValueList = (ListView) settingView.findViewById(R.id.lv_setting_right);
            ListViewAdapterSettingRight.fontSize = fontSize;
            mAdapterSettingValue = new ListViewAdapterSettingRight(this, rightList, 0);
            mSettingValueList.setAdapter(mAdapterSettingValue);

            switch (settingArrayList.get(pos).mLeftValue) {
                case "直播选源":
                    mAdapterSettingValue.setSelection(curSourceIndex);
                    break;
                case "播放控制":
                    mAdapterSettingValue.setSelection(ijkVideoView.isPlaying() ? 0 : 1);
                    break;
                case "解码模式":
                    mAdapterSettingValue.setSelection(codemodel);
                    break;
                case "倍速播放":
                    float speed = 1f;
                    if (codemodel == 1 || codemodel == 3 || codemodel == 4) {
                        speed = ijkVideoView.getSpeed();
                    }
                    List<Float> speeds2 = getSpeedList();
                    for (int i = 0; i < speeds2.size(); i++) {
                        if (speed == speeds2.get(i)) {
                            mAdapterSettingValue.setSelection(i);
                            break;
                        }
                    }
                    break;
                case "屏幕比例":
                    mAdapterSettingValue.setSelection(screenScale);
                    break;
                case "省份选择":
                    break;
                case "超时跳转":
                    mAdapterSettingValue.setSelection(bufferTimeOut / 5000 - 1);
                    break;
                case "显示网速":
                    mAdapterSettingValue.setSelection(showNetOn ? 0 : 1);
                    break;
                case "时间显示":
                    mAdapterSettingValue.setSelection(setTimeOn ? 0 : 1);
                    break;
                default:
                    break;
            }

            mSettingValueList.setOnItemClickListener(new OnItemClickListener() {
                //单击项目时
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int posval, long id) {
                    switch (settingArrayList.get(pos).mLeftValue) {
                        case "直播选源": {
                            curSourceIndex = posval;
                            try {
                                setVideo();
                            } catch (Exception unused_ex) {
                            }
                            mSettingWindow.dismiss();
                            break;
                        }
                        case "播放控制":
                            if (codemodel != 1 && codemodel != 3 && codemodel != 4) {
                                ToastMgr.shortBottomCenter(context, "当前解码模式不支持此操作");
                                return;
                            }
                            if (posval == 0) {
                                if (!ijkVideoView.isPlaying()) {
                                    ijkVideoView.start();
                                }
                                mSettingWindow.dismiss();
                            } else {
                                if (ijkVideoView.isPlaying()) {
                                    ijkVideoView.pause();
                                }
                            }
                            ToastMgr.shortBottomCenter(context, "按左右键然后按确认键即可快速暂停/恢复");
                            mAdapterSettingValue.setSelection(posval);
                            break;
                        case "解码模式":
                            codemodel = posval;
                            setVideoUri();
                            mySettings.saveSetting("codemodel", codemodel);
                            mAdapterSettingValue.setSelection(codemodel);
                            break;
                        case "屏幕比例":
                            screenScale = posval;
                            setScreenScale(posval);
                            mySettings.saveSetting("screenScale", screenScale);
                            mAdapterSettingValue.setSelection(screenScale);
                            break;
                        case "倍速播放":
                            ijkVideoView.setSpeed(getSpeedList().get(posval));
                            mAdapterSettingValue.setSelection(posval);
                            break;
                        case "省份选择":
                            mSettingWindow.dismiss();
                            break;
                        case "超时跳转":
                            bufferTimeOut = (posval + 1) * 5000;
                            mySettings.saveSetting("bufferTimeOut", bufferTimeOut);
                            mAdapterSettingValue.setSelection(bufferTimeOut / 5000 - 1);
                            break;
                        case "显示网速":
                            showNetOn = posval == 0;
                            mySettings.saveSetting("showNet", showNetOn);
                            mAdapterSettingValue.setSelection(showNetOn ? 0 : 1);
                            break;
                        case "时间显示":
                            setTimeOn = posval == 0;
                            mySettings.saveSetting("setTimeOn", setTimeOn);
                            mAdapterSettingValue.setSelection(setTimeOn ? 0 : 1);
                            break;
                        case "数据管理":
                            String url = "http://" + IPUtils.getIP(PlayerActivity.this) + ":" + WebServer.port;
                            try {
                                ClipboardUtil.copyToClipboard(PlayerActivity.this, url);
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastMgr.shortBottomCenter(PlayerActivity.this, "打开浏览器失败，地址：" + url);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新频道数据
    protected void updateChannelDataNoTip() {

    }

    //更新节目列表
    protected void updateChannelData() {

    }


    //显示设置
    private void showSetting() {
        settingView = PlayerActivity.this.getLayoutInflater().inflate(R.layout.setting, null);
        mSettingList = (ChannelListView) settingView.findViewById(R.id.lv_setting_left);
        setSettingText(0);
        mSettingList.requestFocus();
        ListViewAdapterSettingLeft.fontSize = fontSize;
        mAdapterSetting = new ListViewAdapterSettingLeft(this, settingArrayList, 0);
        mSettingList.pos = 0;
        mSettingList.setAdapter(mAdapterSetting);
        mSettingList.setSelection(0);

        tv_setting_title = (TextView) settingView.findViewById(R.id.tv_setting);
        tv_setting_title.setText("通用设置");
        tv_setting_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize + 3);
        tv_setting_title.setVisibility(View.GONE);


        mSettingValueList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int pos, long id) {
                if (rHideSetting != null) {
                    handler.removeCallbacks(rHideSetting);
                    handler.postDelayed(rHideSetting, 10000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        mSettingList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                setSettingText(position);
                mSettingPos = position;
                mAdapterSetting.setSelection(position);
                mSettingList.setSelect(position, 0);
                if (rHideSetting != null) {
                    handler.removeCallbacks(rHideSetting);
                    handler.postDelayed(rHideSetting, 10000);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSettingList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v,
                                    int pos, long id) {
                setSettingText(pos);
                mAdapterSetting.setSelection(pos);
                mSettingList.setSelect(pos, 0);
                if (rHideSetting != null) {
                    handler.removeCallbacks(rHideSetting);
                    handler.postDelayed(rHideSetting, 10000);
                }
            }

        });

        settingHeight = getScreenHeight() / 12 - mSettingList.getDividerHeight();

        mSettingWindow = new PopupWindow(settingView, getScreenWidth() / 2, LayoutParams.WRAP_CONTENT);
        mSettingWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSettingWindow.setFocusable(true);
        mSettingWindow.setOutsideTouchable(true);
        mSettingWindow.update();
        mSettingWindow.showAtLocation(ijkVideoView, Gravity.CENTER, 0, 0);

        if (rHideSetting != null) {
            handler.removeCallbacks(rHideSetting);
        } else {
            rHideSetting = new Runnable() {
                @Override
                public void run() {
                    mSettingWindow.dismiss();
                }
            };
        }
        handler.postDelayed(rHideSetting, 10000);
    }

    private float getTextViewWidth(String text, TextView tv) {
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tv.getTextSize());
        TextPaint textPaint = textView.getPaint();
        float textPaintWidth = textPaint.measureText(text);
        return textPaintWidth;
    }

    //分类密码
    private void showInputDialog() {

    }

    //显示频道列表
    private void showChannelList() {
        mLeftPos = curType;
        ListViewAdapterLeft.fontSize = fontSize;
        ListViewAdapterRight.fontSize = fontSize;
        menuView = PlayerActivity.this.getLayoutInflater().inflate(R.layout.channel_list, null);
        mLeftList = (ChannelListView) menuView.findViewById(R.id.lv_left);
        mRightList = (ChannelListView) menuView.findViewById(R.id.lv_right);
        mAdapterLeft = new ListViewAdapterLeft(this, channelDatas, curType);
        mLeftList.pos = curType;
        mLeftList.setAdapter(mAdapterLeft);
        mLeftList.setSelection(curType);

        if (channelDatas.size() > mLeftPos) {
            if (channelDatas.get(mLeftPos).hasPassed || channelDatas.get(mLeftPos).psw.isEmpty()) {
                setTempChannelList(mLeftPos);
            } else {
                if (mAdapterRight != null) mAdapterRight.clear();

            }
        }
        mRightList.requestFocus();
        leftHeight = (getScreenHeight() - 12) / leftLines - mLeftList.getDividerHeight() + 1;
        rightHeight = (getScreenHeight() - 12) / rightLines - mLeftList.getDividerHeight() + 1;

        mLeftList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int pos, long id) {
                mLeftPos = pos;
                mLeftList.pos = pos;
                mAdapterLeft.setCurrentSelectPos(pos);
                if (!channelDatas.get(pos).psw.isEmpty() && channelDatas.get(pos).hasPassed == false) {
                    if (mAdapterRight != null) mAdapterRight.clear();
                } else {
                    setTempChannelList(pos);
                }
                if (rHideMenu != null) {
                    handler.removeCallbacks(rHideMenu);
                    handler.postDelayed(rHideMenu, 10000);
                }
            }
        });

        mLeftList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int pos, long id) {
                mLeftPos = pos;
                mLeftList.setSelect(pos, (pos - mLeftList.getFirstVisiblePosition()) * leftHeight);
                mAdapterLeft.setCurrentSelectPos(pos);
                if (!channelDatas.get(pos).psw.isEmpty() && channelDatas.get(pos).hasPassed == false) {
                    if (mAdapterRight != null) mAdapterRight.clear();
                } else {
                    setTempChannelList(pos);
                }
                if (rHideMenu != null) {
                    handler.removeCallbacks(rHideMenu);
                    handler.postDelayed(rHideMenu, 10000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mLeftList.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keycode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    mAdapterLeft.notifyDataSetChanged();
                    switch (keycode) {
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (mLeftPos == mLeftList.getLastVisiblePosition() - 1 && mLeftList.getLastVisiblePosition() != mLeftList.getCount() - 1) {
                                mLeftList.smoothScrollBy(leftHeight, 0);
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if (mLeftPos == mLeftList.getFirstVisiblePosition() + 1 && mLeftList.getFirstVisiblePosition() != 0) {
                                mLeftList.smoothScrollBy(-leftHeight, 0);
                            }
                            ;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        mRightList.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keycode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keycode) {
                        case KeyEvent.KEYCODE_MENU:
                            favorChannel(mRightList.getSelectedItemPosition());
                            break;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (mRightPos == mRightList.getLastVisiblePosition() - 1) {
                                if (mRightList.getLastVisiblePosition() != mRightList.getCount() - 1) {
                                    mRightList.smoothScrollBy(leftHeight, 0);
                                }
                            }
                            if (mRightPos == mRightList.getLastVisiblePosition()) {
                                if (mRightList.getLastVisiblePosition() == mRightList.getCount() - 1) {
                                    mRightList.setSelection(0);
                                }
                            }


                            break;
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if (mRightPos == mRightList.getFirstVisiblePosition() + 1) {
                                if (mRightList.getFirstVisiblePosition() != 0) {
                                    mRightList.smoothScrollBy(-leftHeight, 0);
                                }
                            }
                            if (mRightPos == mRightList.getFirstVisiblePosition()) {
                                if (mRightList.getFirstVisiblePosition() == 0) {
                                    mRightList.setSelection(mRightList.getCount() - 1);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        mRightList.setOnItemLongClickListener(new OnItemLongClickListener() {
            //长按
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View v,
                                           int pos, long id) {
                favorChannel(pos);
                return true;
            }
        });

        mRightList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int pos, long id) {
                if (mAdapterRight.getchannelnum(pos) == 0) {
                    showInputDialog();
                } else {
                    curChannelIndex = pos;
                    curType = mLeftPos;
                    mAdapterRight.setSelection(pos);
                    curSourceIndex = mySettings.getCurrentSource(getCurrentChannelName());
                    playVideo();

                    mChannelSelectWindow.dismiss();
                }
            }
        });

        mRightList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int pos, long id) {
                mRightPos = pos;
                mRightList.setSelect(pos, (pos - mRightList.getFirstVisiblePosition()) * leftHeight);
                mAdapterRight.notifyDataSetChanged();
                if (rHideMenu != null) {
                    handler.removeCallbacks(rHideMenu);
                    handler.postDelayed(rHideMenu, 10000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mRightList.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                if (rHideMenu != null) {
                    handler.removeCallbacks(rHideMenu);
                    handler.postDelayed(rHideMenu, 10000);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        //频道列表显示
        float textSize = fontSize;
        TextView textView = new TextView(getApplicationContext());
        try {
            if (mLeftList.getAdapter().getCount() > 0) {
                TextView itemView = (TextView) mLeftList.getAdapter().getView(0, null, mLeftList);
                textSize = itemView.getTextSize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        TextPaint textPaint = textView.getPaint();
        float textPaintWidth = textPaint.measureText("收藏频道频道99收藏频道收藏频道频道频道收藏频道收藏频道频道频道");
        int pWidth = (int) textPaintWidth;

        mChannelSelectWindow = new PopupWindow(menuView, pWidth, getScreenHeight() - 12);
        mChannelSelectWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mChannelSelectWindow.setFocusable(true);
        mChannelSelectWindow.setOutsideTouchable(true);
        mChannelSelectWindow.update();
        mChannelSelectWindow.showAtLocation(ijkVideoView, Gravity.START | Gravity.CENTER_VERTICAL, 6, 0);

        if (rHideMenu != null) {
            handler.removeCallbacks(rHideMenu);
        } else {
            rHideMenu = new Runnable() {
                public void run() {
                    mChannelSelectWindow.dismiss();
                }
            };
        }
        handler.postDelayed(rHideMenu, 10000);
        sendMessage(Play.SHOW_NUM);
        mChannelSelectWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                sendMessage(Play.HIDE_NUM);
            }
        });

    }

    //频道列表
    protected void setTempChannelList(int pos) {
        int tmpPos = -1;
        if (curType == pos) {
            tmpPos = curChannelIndex;
        } else {
            tmpPos = -1;
        }

        mAdapterRight = new ListViewAdapterRight(this, channelDatas.get(mLeftPos).data, tmpPos);
        mRightList.pos = tmpPos;
        mRightList.setAdapter(mAdapterRight);
        mRightList.setSelection(tmpPos);
    }

    //获取屏幕高度
    private int getScreenHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        if (display == null) {
            return 0;
        }
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }

    //频道切换
    private int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenwidth = dm.widthPixels;
        return screenwidth;
    }

    //收藏频道
    private void favorChannel(int type, int pos) {
        favorList.add(new FavorObject(type, pos));
        channelDatas.get(0).data.add(channelDatas.get(type).data.get(pos));
        favorList.saveToFile(this);
    }

    //删除收藏频道
    private void delFavorChannel(int type, int pos) {
        if (channelDatas.get(mLeftPos).name.equals("我的收藏")) {
            favorList.delete(pos);
        } else {
            favorList.delete(type, pos);
        }
        channelDatas.get(0).data.remove(channelDatas.get(type).data.get(pos));
        favorList.saveToFile(this);
    }

    //收藏频道
    protected void favorChannel(int pos) {
        if (channelDatas.get(mLeftPos).data.size() <= pos) return;

        if (favorList.isChannelFavored(mLeftPos, pos) || channelDatas.get(mLeftPos).name.equals("我的收藏")) {
            delFavorChannel(mLeftPos, pos);
        } else {
            favorChannel(mLeftPos, pos);
        }
        mAdapterRight.notifyDataSetChanged();
    }

    //进入频道
    private void enterChannel(int num) {
        tv_shownum.setVisibility(View.INVISIBLE);
        mCount = 0;
        mNum = 0;
        if (num > channelDatas.getLastChannel() || num < 1) return;
        HashMap<String, Integer> map = channelDatas.getChannel(num);
        try {
            if (channelDatas.size() <= map.get("type")) {
                return;
            }
            if (!channelDatas.get(map.get("type")).psw.isEmpty() && !channelDatas.get(map.get("type")).hasPassed) {
                return;
            }
        } catch (Exception e) {
        }

        curType = map.get("type");
        curChannelIndex = map.get("pos");
        playVideo();
    }

    //按下返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (mCount > 0) {
                    sendMessage(Play.ENTERCHANNEL);
                } else {
                    showChannelList();
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                showSetting();
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                if (reverseUD) {
                    movePre();
                } else {
                    moveNext();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (reverseUD) {
                    moveNext();
                } else {
                    movePre();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mCanSeek) {
                    if (ijkVideoView.getVisibility() == View.VISIBLE) {
                        ijkVideoView.setSourceTitle(getCurrentChannelName());
                        ijkVideoView.showProgressBar(this, this);
                    } else {
                        mVideoView.showProgressBar(this, this);
                    }
                } else {
                    moveLeft();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (mCanSeek) {
                    if (ijkVideoView.getVisibility() == View.VISIBLE) {
                        ijkVideoView.setSourceTitle(getCurrentChannelName());
                        ijkVideoView.showProgressBar(this, this);
                    } else {
                        mVideoView.showProgressBar(this, this);
                    }
                } else {
                    moveRight();
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                /*if ((System.currentTimeMillis() - exitTime) > 2000) {
                 Toast.makeText(getApplicationContext(), "再按一次返回退出", Toast.LENGTH_SHORT).show();
                 exitTime = System.currentTimeMillis();
                 } else {
                 finish();
                 System.exit(0);
                 }*/
                showExitDialog();
                return true;

            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_9:

                if (mCount < 5) {
                    mNum = mNum * 10 + keyCode - 7;
                    tv_shownum.setTextSize(fontSize + 20);
                    tv_shownum.getPaint().setFakeBoldText(true);
                    tv_shownum.setText(mNum + "");
                    tv_shownum.setVisibility(View.VISIBLE);
                    mCount++;
                }
                if (rEnterChannel != null) {
                    handler.removeCallbacks(rEnterChannel);
                } else {
                    rEnterChannel = new Runnable() {
                        @Override
                        public void run() {
                            sendMessage(Play.ENTERCHANNEL);
                        }
                    };
                }
                if (mCount >= 5) {
                    handler.removeCallbacks(rEnterChannel);
                    sendMessage(Play.ENTERCHANNEL);
                } else {
                    handler.postDelayed(rEnterChannel, 2000);
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //显示退出对话框
    private void showExitDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View exitView = inflater.inflate(R.layout.exit_dialog, null);

        tv_isp = (TextView) exitView.findViewById(R.id.tv_isp);

        btn_exit = (Button) exitView.findViewById(R.id.btn_exit);
        btn_setting = (Button) exitView.findViewById(R.id.btn_setting);


        LayoutParams params = btn_exit.getLayoutParams();
        params.height = getScreenHeight() / 10;
        btn_exit.setLayoutParams(params);
        btn_setting.setLayoutParams(params);
        btn_exit.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 12 / 10);
        btn_setting.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 12 / 10);

        tv_isp.setText("确认退出软件？");
        tv_isp.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);

        tv_webadmin = (TextView) exitView.findViewById(R.id.tv_webadmin);
        tv_webadmin.setText(("本机：" + IPUtils.getIP(this) + ":" + WebServer.port));
        tv_webadmin.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 8 / 10);
        tv_webadmin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://" + IPUtils.getIP(PlayerActivity.this) + ":" + WebServer.port;
                ClipboardUtil.copyToClipboard(PlayerActivity.this, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btn_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                System.exit(0);
            }
        });
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showSetting();
                dialog.dismiss();
            }
        });
        btn_exit.requestFocus();

        dialog = new PopupWindow(exitView, getScreenWidth() * 3 / 5, LayoutParams.WRAP_CONTENT);
        dialog.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setFocusable(true);
        dialog.setOutsideTouchable(true);
        dialog.update();
        dialog.showAtLocation(ijkVideoView, Gravity.CENTER, 0, 0);
    }

    //向左滑动切换源
    private void moveLeft() {
        Timber.d("moveLeft, setVideo");
        if (channelSourceArrayList.size() == 1) return;
        if (curSourceIndex > 0) {
            curSourceIndex--;
        } else {
            curSourceIndex = channelSourceArrayList.size() - 1;
        }
        try {
            setVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //向右滑动切换源
    private void moveRight() {
        Timber.d("moveRight, setVideo");
        if (channelSourceArrayList.size() == 1) return;
        if (curSourceIndex < channelSourceArrayList.size() - 1) {
            curSourceIndex++;
        } else {
            curSourceIndex = 0;
        }
        try {
            setVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下滑到上一个频道
    private void movePre() {
        try {
            if (curType >= channelDatas.size()) {
                curChannelIndex = 0;
            } else if (curChannelIndex > 0) {
                curChannelIndex--;
            } else {
                curChannelIndex = channelDatas.get(curType).data.size() - 1;
            }
            tip_epg1.setText("暂无信息");
            tip_epg2.setText("暂无信息");
            playVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上划到下一个频道
    private void moveNext() {
        try {
            if (curType < channelDatas.size() && curChannelIndex < channelDatas.get(curType).data.size() - 1) {
                curChannelIndex++;
            } else {
                curChannelIndex = 0;
            }
            tip_epg1.setText("暂无信息");
            tip_epg2.setText("暂无信息");
            playVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送消息
    private void sendMessage(int what) {
        Message msg = new Message();
        msg.what = what;
        handler.sendMessage(msg);
    }

    private void sendMessage(int what, String obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }


    //显示EPG
    private void showEPG() {
        try {
            if (channelDatas.size() <= curType || channelDatas.get(curType).data.size() <= curChannelIndex) {
                return;
            }
            final ChannelObject ch = channelDatas.get(curType).data.get(curChannelIndex);
            tv_channelnum.setText(String.valueOf(channelDatas.get(curType).data.get(curChannelIndex).num));
            tip_chname.setText(channelDatas.get(curType).data.get(curChannelIndex).name);
            LayoutParams params = tip_chname.getLayoutParams();

            params.width = (int) getTextViewWidth("湖南卫视湖南卫视", tip_chname);
            tip_chname.setLayoutParams(params);

            tv_curepg_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize - 15);
            tv_nextepg_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize - 15);
            params = ll_epginfo.getLayoutParams();
            params.width = (int) getTextViewWidth("湖南卫视湖南卫视", tip_chname);
            ll_epginfo.setLayoutParams(params);
            tv_srcinfo.setText("[源" + (curSourceIndex + 1) + "/" + channelSourceArrayList.size() + "]");
            ll_loading.setVisibility(View.VISIBLE);
            tv_tiploading.setVisibility(View.VISIBLE);
            tv_right_top_channel_name.setText(ch.name);
            tv_right_top_channel_name.setVisibility(View.VISIBLE);
            ll_epg.setVisibility(View.VISIBLE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //隐藏EPG
    private void hideEPG(long delay) {
        try {
            ll_loading.setVisibility(View.GONE);
            tv_tiploading.setVisibility(View.GONE);
            if (rHideEpg != null) {
                handler.removeCallbacks(rHideEpg);
            } else {
                rHideEpg = new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(Play.HIDE_EPG);
                    }
                };
            }
            handler.postDelayed(rHideEpg, delay);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private String httpget(String geturl) {
        try {
            URL url = new URL(geturl);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "MTV");
            connection.connect();
            String result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
                           float arg3) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        showSetting();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        x1 = e1.getX();
        Math.max(distanceX, distanceY);
        if (startScroll) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                if (distanceX > 0) {
                    if (mCanSeek) {
                        if (ijkVideoView.getVisibility() == View.VISIBLE) {
                            ijkVideoView.setSourceTitle(getCurrentChannelName());
                            ijkVideoView.showProgressBar(this, this);
                        }
                    } else {
                        //向左滑动切换源
                        moveLeft();
                    }
                } else {
                    if (mCanSeek) {
                        if (ijkVideoView.getVisibility() == View.VISIBLE) {
                            ijkVideoView.setSourceTitle(getCurrentChannelName());
                            ijkVideoView.showProgressBar(this, this);
                        }
                    } else {
                        //向右滑动切换源
                        moveRight();
                    }
                }
            } else {
                if (x1 > getScreenWidth() / 2) {
                    //屏幕右侧
                    if (distanceY > 0) {
                        if (reverseUD) {
                            //下滑到上一个频道
                            movePre();
                        } else {
                            //上划到下一个频道
                            moveNext();
                        }
                    } else {
                        if (reverseUD) {
                            moveNext();
                        } else {
                            movePre();
                        }
                    }
                }

            }
        }
        if (Math.abs(distanceX) < Math.abs(distanceY) && x1 < getScreenWidth() / 2) {
            //屏幕左侧设置音量
            int delta = 0;
            if (Math.abs(distanceY) > 10) {
                if (distanceY < 0) {
                    delta = -1;
                } else {
                    delta = 1;
                }
            }

            currentVolume = Math.min(maxVolume, Math.max(0, currentVolume + delta));
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);

            tv_voluminfo.setText("音量   " + currentVolume * 100 / maxVolume + "%");
            tv_voluminfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 3 / 2);
            tv_voluminfo.setVisibility(View.VISIBLE);

            if (rVolume == null) {
                rVolume = new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(Play.HIDE_VOLUME);
                    }
                };
            } else {
                handler.removeCallbacks(rVolume);
            }
            handler.postDelayed(rVolume, 2000);
        }


        startScroll = false;
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        showChannelList();
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startScroll = true;
            currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return gestureDetector.onTouchEvent(event);// super.onTouchEvent(event);
    }


    @Override
    public void onUrlChanged(String url) {
        sendMessage(Play.PLAY_VJMS, url);
    }

    public static void playLastVideo(String type) {
        if (StringUtil.isEmpty(type)) {
            return;
        }
        for (int i = 0; i < channelDatas.size(); i++) {
            ChannelData channelData = channelDatas.get(i);
            if (type.equals(channelData.name)) {
                curType = i;
                try {
                    Timber.d("playLastVideo, setVideo");
                    ((PlayerActivity) PlayerActivity.context).setCurChannelIndex(channelDatas.get(curType).data.size() - 1);
                    ((PlayerActivity) PlayerActivity.context).setVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static void playThisVideo(String type, String title) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(title)) {
            return;
        }
        for (int i = 0; i < channelDatas.size(); i++) {
            ChannelData channelData = channelDatas.get(i);
            if (type.equals(channelData.name)) {
                curType = i;
                try {
                    Timber.d("playThisVideo, setVideo: %s", title);
                    for (int j = channelData.data.size() - 1; j >= 0; j--) {
                        if (title.equals(channelData.data.get(j).name)) {
                            ((PlayerActivity) PlayerActivity.context).setCurChannelIndex(j);
                            ((PlayerActivity) PlayerActivity.context).setVideo();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    private void setCurChannelIndex(int curChannelIndex) {
        this.curChannelIndex = curChannelIndex;
    }
}

