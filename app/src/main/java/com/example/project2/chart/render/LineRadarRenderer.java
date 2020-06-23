package com.example.project2.chart.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import com.example.project2.chart.animation.ChartAnimator;
import com.example.project2.chart.utils.Utils;
import com.example.project2.chart.utils.ViewPortHandler;

public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer {

    public LineRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    protected void drawFilledPath(Canvas c, Path filledPath, Drawable drawable) {

        if (clipPathSupported()) {

            int save = c.save();
            c.clipPath(filledPath);

            drawable.setBounds((int) mViewPortHandler.contentLeft(),
                    (int) mViewPortHandler.contentTop(),
                    (int) mViewPortHandler.contentRight(),
                    (int) mViewPortHandler.contentBottom());
            drawable.draw(c);

            c.restoreToCount(save);
        } else {
            throw new RuntimeException("Fill-drawables not (yet) supported below API level 18, " +
                    "this code was run on API level " + Utils.getSDKInt() + ".");
        }
    }

    protected void drawFilledPath(Canvas c, Path filledPath, int fillColor, int fillAlpha) {

        int color = (fillAlpha << 24) | (fillColor & 0xffffff);

        if (clipPathSupported()) {

            int save = c.save();

            c.clipPath(filledPath);

            c.drawColor(color);
            c.restoreToCount(save);
        } else {

            // save
            Paint.Style previous = mRenderPaint.getStyle();
            int previousColor = mRenderPaint.getColor();

            // set
            mRenderPaint.setStyle(Paint.Style.FILL);
            mRenderPaint.setColor(color);

            c.drawPath(filledPath, mRenderPaint);

            // restore
            mRenderPaint.setColor(previousColor);
            mRenderPaint.setStyle(previous);
        }
    }

    private boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }
}
