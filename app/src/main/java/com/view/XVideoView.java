package com.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.content.ContextCompat;

import com.vv.live.R;
import com.vv.live.ui.PlayerActivity;
import com.vv.live.view.TVSeekbar;

public class XVideoView extends VideoView {

    private int videoWidth;
    private int screenScale;
    private int videoHeight;
    private TextView tv_currentpos;
    private TVSeekbar sBar;
    private CountDownTimer countDownTimer;
    private PopupWindow dialog;
    private View iv_playpause;
    private View iv_play;
    private View ll_play;

    public XVideoView(Context context) {
        super(context);
    }

    public XVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScreenScale(int screenScale, MediaPlayer mp) {
        this.screenScale = screenScale;
        try {
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();
        } catch (Exception e) {
        }
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenwidth = getDefaultSize(0, widthMeasureSpec);
        int screenheight = getDefaultSize(0, heightMeasureSpec);

        switch (screenScale) {
            case 0:
                videoWidth = screenwidth;
                videoHeight = screenheight;
                break;
            case 3:
                videoWidth = screenwidth;
                videoHeight = screenwidth * 9 / 16;
                break;
            case 2:
                videoHeight = screenheight;
                videoWidth = screenheight * 4 / 3;
                break;
            case 1:
                if (videoWidth == 0 || videoHeight == 0) {
                    videoWidth = screenwidth;
                    videoHeight = screenheight;
                } else {
                    if (videoWidth / screenwidth > videoHeight / screenheight) {
                        videoHeight = videoHeight * screenwidth / videoWidth;
                        videoWidth = screenwidth;
                    } else {
                        videoWidth = videoWidth * screenheight / videoHeight;
                        videoHeight = screenheight;
                    }
                }
                break;

            default:
                break;
        }
        setMeasuredDimension(videoWidth, videoHeight);
    }


    private String durationToString(int duration) {
        String result = "";
        int dur = duration / 1000;
        int hour = dur / 3600;
        int min = (dur / 60) % 60;
        int sec = dur % 60;
        if (hour > 0) {
            if (min > 9) {
                if (sec > 9) {
                    result = hour + ":" + min + ":" + sec;
                } else {
                    result = hour + ":" + min + ":0" + sec;
                }
            } else {
                if (sec > 9) {
                    result = hour + ":" + "0" + min + ":" + sec;
                } else {
                    result = hour + ":" + "0" + min + ":0" + sec;
                }
            }
        } else {
            if (min > 9) {
                if (sec > 9) {
                    result = min + ":" + sec;
                } else {
                    result = min + ":0" + sec;
                }
            } else {
                if (sec > 9) {
                    result = "0" + min + ":" + sec;
                } else {
                    result = "0" + min + ":0" + sec;
                }
            }
        }
        return result;
    }

    public void showProgressBar(Context context, Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View pView = inflater.inflate(R.layout.progress_bar, null);
        tv_currentpos = (TextView) pView.findViewById(R.id.tv_currentpos);
        TextView tv_duration = (TextView) pView.findViewById(R.id.tv_duration);
        iv_playpause = pView.findViewById(R.id.iv_playpause);
        iv_play = pView.findViewById(R.id.iv_play);
        ll_play = pView.findViewById(R.id.ll_play);


        LayoutParams lp = iv_play.getLayoutParams();
        lp.width = videoHeight / 7;
        lp.height = videoHeight / 7;

        sBar = (TVSeekbar) pView.findViewById(R.id.pb_progressbar);
        sBar.setMax(getDuration());
        sBar.setProgress(getCurrentPosition());
        tv_currentpos.setText(durationToString(getCurrentPosition()));
        tv_duration.setText(durationToString(getDuration()));

        sBar.requestFocus();

        ll_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isPlaying()) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        });

        iv_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                start();
                iv_play.setVisibility(View.INVISIBLE);
                countDownTimer.start();
                iv_playpause.setBackground(ContextCompat.getDrawable(PlayerActivity.context, R.drawable.icon_pause));
            }
        });

        iv_playpause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isPlaying()) {
                    pause();
                    countDownTimer.cancel();
                    iv_play.setVisibility(View.VISIBLE);
                    iv_playpause.setBackground(ContextCompat.getDrawable(PlayerActivity.context, R.drawable.icon_play));
                } else {
                    start();
                    iv_play.setVisibility(View.INVISIBLE);
                    countDownTimer.start();
                    iv_playpause.setBackground(ContextCompat.getDrawable(PlayerActivity.context, R.drawable.icon_pause));
                }
            }
        });
        sBar.setKeyListener(new TVSeekbar.KeyListener() {
            @Override
            public void increment() {
                onSeekBarKeyChange(true);
            }

            @Override
            public void decrement() {
                onSeekBarKeyChange(false);
            }
        });
        sBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromuser) {
                if (fromuser) {
                    if (countDownTimer != null) {
                        seekTo(progress);
                        countDownTimer.cancel();
                        countDownTimer.start();
                    }
                }
            }
        });
        sBar.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int keycode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keycode == KeyEvent.KEYCODE_DPAD_CENTER || keycode == KeyEvent.KEYCODE_ENTER) {
                        if (isPlaying()) {
                            pause();
                            countDownTimer.cancel();
                            iv_play.setVisibility(View.VISIBLE);
                            iv_playpause.setBackground(ContextCompat.getDrawable(PlayerActivity.context, R.drawable.icon_play));
                        } else {
                            start();
                            iv_play.setVisibility(View.INVISIBLE);
                            countDownTimer.start();
                            iv_playpause.setBackground(ContextCompat.getDrawable(PlayerActivity.context, R.drawable.icon_pause));
                        }
                    }
                }
                return false;
            }
        });
        if (isPlaying()) {
            iv_play.setVisibility(View.INVISIBLE);
            iv_playpause.setBackground(ContextCompat.getDrawable(PlayerActivity.context, R.drawable.icon_pause));
        } else {
            iv_play.setVisibility(View.VISIBLE);
            iv_playpause.setBackground(ContextCompat.getDrawable(PlayerActivity.context, R.drawable.icon_play));
        }
        dialog = new PopupWindow(pView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        dialog.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setFocusable(true);
        dialog.setOutsideTouchable(true);
        dialog.update();
        dialog.showAtLocation(this, Gravity.BOTTOM, 0, 40);


        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(6000, 1000) {

                @Override
                public void onTick(long arg0) {

                    sBar.setProgress(getCurrentPosition());
                    tv_currentpos.setText(durationToString(getCurrentPosition()));

                }

                @Override
                public void onFinish() {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            };
        } else {
            countDownTimer.cancel();
        }
        countDownTimer.start();
    }

    public void setUserAgent(String userAgent) {

    }

    public void setHudView(TableLayout tableLayout) {

    }


    private void onSeekBarKeyChange(boolean increment) {
        int total = getDuration();
        int current = sBar.getProgress();
        //每次跳转30秒
        int gap = 30 * 1000;
        int next;
        if (!increment) {
            next = current - gap;
        } else {
            next = current + gap;
        }
        if (next < 0) {
            next = 0;
        } else if (next > total) {
            next = current;
        }
        sBar.setProgress(next);
        seekTo(next);
    }
}
