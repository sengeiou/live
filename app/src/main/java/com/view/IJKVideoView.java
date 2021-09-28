package com.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.media.IjkVideoView;
import com.vv.live.ui.PlayerActivity;
import com.vv.live.R;
import com.vv.live.util.ScreenUtil;
import com.vv.live.view.TVSeekbar;

public class IJKVideoView extends IjkVideoView {

    private TextView tv_currentpos;
    private TVSeekbar sBar;
    private CountDownTimer countDownTimer;
    private PopupWindow dialog;
    private View iv_playpause;
    private View iv_play;
    private View ll_play;
    private int videoHeight;

    private String sourceTitle;

    public IJKVideoView(Context context) {
        super(context);
    }

    public IJKVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IJKVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoHeight(int height) {
        videoHeight = height;
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
        tv_currentpos = pView.findViewById(R.id.tv_currentpos);
        TextView tv_duration = pView.findViewById(R.id.tv_duration);
        TextView titleView = pView.findViewById(R.id.txt_item);
        if (sourceTitle != null && sourceTitle.length() > 0) {
            titleView.setText(sourceTitle);
        } else {
            titleView.setText("");
        }
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtil.getScreenWidth(PlayerActivity.context) / 42 + 8);
        titleView.setVisibility(VISIBLE);
        iv_playpause = pView.findViewById(R.id.iv_playpause);
        iv_play = pView.findViewById(R.id.iv_play);
        ll_play = pView.findViewById(R.id.ll_play);

        sBar = pView.findViewById(R.id.pb_progressbar);
        sBar.setMax(getDuration());
        sBar.setProgress(getCurrentPosition());
        tv_currentpos.setText(durationToString(getCurrentPosition()));
        tv_duration.setText(durationToString(getDuration()));

        android.view.ViewGroup.LayoutParams lp = iv_play.getLayoutParams();
        lp.width = videoHeight / 7;
        lp.height = videoHeight / 7;

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

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }
}
