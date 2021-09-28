package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class XProgressBar extends View {

    private Paint mPaint;
    private float x = 0;
    private Handler handler = new Handler();

    public XProgressBar(Context context) {
        super(context);
    }

    public XProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        x = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        mPaint = new Paint();
        x += 8;
        if (x > w) {
            x = 0;
        }
        mPaint.setStrokeWidth(h);

        mPaint.setColor(Color.argb(255, 0, 38, 34));
        mPaint.setStyle(Style.FILL);
        canvas.drawLine(0, 0, w, 0, mPaint);

        mPaint.setColor(Color.argb(255, 49, 196, 203));
        mPaint.setStyle(Style.FILL);

        canvas.drawLine(x, 0, x + w / 4, 0, mPaint);
        canvas.save();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 10);
    }


}
