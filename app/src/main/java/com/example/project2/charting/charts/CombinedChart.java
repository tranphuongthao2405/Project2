package com.example.project2.charting.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.example.project2.charting.data.BarData;
import com.example.project2.charting.data.BubbleData;
import com.example.project2.charting.data.CandleData;
import com.example.project2.charting.data.CombinedData;
import com.example.project2.charting.data.LineData;
import com.example.project2.charting.data.ScatterData;
import com.example.project2.charting.highlight.CombinedHighlighter;
import com.example.project2.charting.highlight.Highlight;
import com.example.project2.charting.interfaces.dataprovider.CombinedDataProvider;
import com.example.project2.charting.render.CombinedChartRenderer;

public class CombinedChart extends BarLineChartBase<CombinedData> implements CombinedDataProvider {

    private boolean mDrawValueAboveBar = true;

    protected boolean mHighlightFullBarEnabled = false;

    private boolean mDrawBarShadow = false;

    protected DrawOrder[] mDrawOrder;

    public enum DrawOrder {
        BAR, BUBBLE, LINE, CANDLE, SCATTER
    }

    public CombinedChart(Context context) {
        super(context);
    }

    public CombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        // Default values are not ready here yet
        mDrawOrder = new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER
        };

        setHighlighter(new CombinedHighlighter(this, this));

        // Old default behaviour
        setHighlightFullBarEnabled(true);

        mRenderer = new CombinedChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    public CombinedData getCombinedData() {
        return mData;
    }

    @Override
    public void setData(CombinedData data) {
        super.setData(data);
        setHighlighter(new CombinedHighlighter(this, this));
        ((CombinedChartRenderer)mRenderer).createRenderers();
        mRenderer.initBuffers();
    }

    @Override
    public Highlight getHighlightByTouchPoint(float x, float y) {

        if (mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.");
            return null;
        } else {
            Highlight h = getHighlighter().getHighlight(x, y);
            if (h == null || !isHighlightFullBarEnabled()) return h;

            // For isHighlightFullBarEnabled, remove stackIndex
            return new Highlight(h.getX(), h.getY(),
                    h.getXPx(), h.getYPx(),
                    h.getDataSetIndex(), -1, h.getAxis());
        }
    }

    @Override
    public LineData getLineData() {
        if (mData == null)
            return null;
        return mData.getLineData();
    }

    @Override
    public BarData getBarData() {
        if (mData == null)
            return null;
        return mData.getBarData();
    }

    @Override
    public ScatterData getScatterData() {
        if (mData == null)
            return null;
        return mData.getScatterData();
    }

    @Override
    public CandleData getCandleData() {
        if (mData == null)
            return null;
        return mData.getCandleData();
    }

    @Override
    public BubbleData getBubbleData() {
        if (mData == null)
            return null;
        return mData.getBubbleData();
    }

    @Override
    public boolean isDrawBarShadowEnabled() {
        return mDrawBarShadow;
    }

    @Override
    public boolean isDrawValueAboveBarEnabled() {
        return mDrawValueAboveBar;
    }

    public void setDrawValueAboveBar(boolean enabled) {
        mDrawValueAboveBar = enabled;
    }

    public void setDrawBarShadow(boolean enabled) {
        mDrawBarShadow = enabled;
    }

    public void setHighlightFullBarEnabled(boolean enabled) {
        mHighlightFullBarEnabled = enabled;
    }

    @Override
    public boolean isHighlightFullBarEnabled() {
        return mHighlightFullBarEnabled;
    }

    public DrawOrder[] getDrawOrder() {
        return mDrawOrder;
    }

    public void setDrawOrder(DrawOrder[] order) {
        if (order == null || order.length <= 0)
            return;
        mDrawOrder = order;
    }
}