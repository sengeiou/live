package com.vv.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.SeekBar;

/**
 * 作者：By 15968
 * 日期：On 2021/5/9
 * 时间：At 17:07
 */

public class TVSeekbar extends SeekBar {
    private KeyListener keyListener;

    public TVSeekbar(Context context) {
        super(context);
    }

    public TVSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TVSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TVSeekbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyListener != null) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_MINUS:
                    keyListener.decrement();
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                case KeyEvent.KEYCODE_PLUS:
                case KeyEvent.KEYCODE_EQUALS:
                    keyListener.increment();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public interface KeyListener {
        void increment();

        void decrement();
    }
}
