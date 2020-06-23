package com.example.project2.charting.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.project2.charting.animation.ChartAnimator;
import com.example.project2.charting.data.Entry;
import com.example.project2.charting.formatter.IValueFormatter;
import com.example.project2.charting.highlight.Highlight;
import com.example.project2.charting.interfaces.dataprovider.ChartInterface;
import com.example.project2.charting.interfaces.datasets.IDataSet;
import com.example.project2.charting.utils.Utils;
import com.example.project2.charting.utils.ViewPortHandler;

public abstract class DataRenderer extends Renderer {

    protected ChartAnimator mAnimator;

    protected Paint mRenderPaint;

    protected Paint mHighlightPaint;

    protected Paint mDrawPaint;

    protected Paint mValuePaint;

    public DataRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
        this.mAnimator = animator;

        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRenderPaint.setStyle(Paint.Style.FILL);

        mDrawPaint = new Paint(Paint.DITHER_FLAG);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(Color.rgb(63, 63, 63));
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(Utils.convertDpToPixel(9f));

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(2f);
        mHighlightPaint.setColor(Color.rgb(255, 187, 115));
    }

    protected boolean isDrawingValuesAllowed(ChartInterface chart) {
        return chart.getData().getEntryCount() < chart.getMaxVisibleCount()
                * mViewPortHandler.getScaleX();
    }

    public Paint getPaintValues() {
        return mValuePaint;
    }

    public Paint getPaintHighlight() {
        return mHighlightPaint;
    }

    public Paint getPaintRender() {
        return mRenderPaint;
    }

    protected void applyValueTextStyle(IDataSet set) {

        mValuePaint.setTypeface(set.getValueTypeface());
        mValuePaint.setTextSize(set.getValueTextSize());
    }

    public abstract void initBuffers();

    public abstract void drawData(Canvas c);

    public abstract void drawValues(Canvas c);

    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.drawText(formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler), x, y, mValuePaint);
    }

    public abstract void drawExtras(Canvas c);

    public abstract void drawHighlighted(Canvas c, Highlight[] indices);
}