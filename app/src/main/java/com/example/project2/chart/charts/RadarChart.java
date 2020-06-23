package com.example.project2.chart.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.project2.chart.components.YAxis;
import com.example.project2.chart.data.RadarData;
import com.example.project2.chart.highlight.RadarHighlighter;
import com.example.project2.chart.render.RadarChartRenderer;
import com.example.project2.chart.render.XAxisRendererRadarChart;
import com.example.project2.chart.render.YAxisRendererRadarChart;
import com.example.project2.chart.utils.Utils;

public class RadarChart extends PieRadarChartBase<RadarData> {

    private float mWebLineWidth = 2.5f;

    private float mInnerWebLineWidth = 1.5f;

    private int mWebColor = Color.rgb(122, 122, 122);

    private int mWebColorInner = Color.rgb(122, 122, 122);

    private int mWebAlpha = 150;

    private boolean mDrawWeb = true;

    private int mSkipWebLineCount = 0;

    private YAxis mYAxis;

    protected YAxisRendererRadarChart mYAxisRenderer;
    protected XAxisRendererRadarChart mXAxisRenderer;

    public RadarChart(Context context) {
        super(context);
    }

    public RadarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mYAxis = new YAxis(YAxis.AxisDependency.LEFT);

        mWebLineWidth = Utils.convertDpToPixel(1.5f);
        mInnerWebLineWidth = Utils.convertDpToPixel(0.75f);

        mRenderer = new RadarChartRenderer(this, mAnimator, mViewPortHandler);
        mYAxisRenderer = new YAxisRendererRadarChart(mViewPortHandler, mYAxis, this);
        mXAxisRenderer = new XAxisRendererRadarChart(mViewPortHandler, mXAxis, this);

        mHighlighter = new RadarHighlighter(this);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        mYAxis.calculate(mData.getYMin(YAxis.AxisDependency.LEFT), mData.getYMax(YAxis.AxisDependency.LEFT));
        mXAxis.calculate(0, mData.getMaxEntryCountSet().getEntryCount());
    }

    @Override
    public void notifyDataSetChanged() {
        if (mData == null)
            return;

        calcMinMax();

        mYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum, mYAxis.isInverted());
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        if (mLegend != null && !mLegend.isLegendCustom())
            mLegendRenderer.computeLegend(mData);

        calculateOffsets();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null)
            return;

//        if (mYAxis.isEnabled())
//            mYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum, mYAxis.isInverted());

        if (mXAxis.isEnabled())
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        mXAxisRenderer.renderAxisLabels(canvas);

        if (mDrawWeb)
            mRenderer.drawExtras(canvas);

        mYAxisRenderer.renderLimitLines(canvas);

        mRenderer.drawData(canvas);

        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        mYAxisRenderer.renderAxisLabels(canvas);

        mRenderer.drawValues(canvas);

        mLegendRenderer.renderLegend(canvas);

        drawDescription(canvas);

        drawMarkers(canvas);
    }

    public float getFactor() {
        RectF content = mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2f, content.height() / 2f) / mYAxis.mAxisRange;
    }

    public float getSliceAngle() {
        return 360f / (float) mData.getMaxEntryCountSet().getEntryCount();
    }

    @Override
    public int getIndexForAngle(float angle) {

        // take the current angle of the chart into consideration
        float a = Utils.getNormalizedAngle(angle - getRotationAngle());

        float sliceangle = getSliceAngle();

        int max = mData.getMaxEntryCountSet().getEntryCount();

        int index = 0;

        for (int i = 0; i < max; i++) {

            float referenceAngle = sliceangle * (i + 1) - sliceangle / 2f;

            if (referenceAngle > a) {
                index = i;
                break;
            }
        }

        return index;
    }

    public YAxis getYAxis() {
        return mYAxis;
    }

    public void setWebLineWidth(float width) {
        mWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidth() {
        return mWebLineWidth;
    }

    public void setWebLineWidthInner(float width) {
        mInnerWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidthInner() {
        return mInnerWebLineWidth;
    }

    public void setWebAlpha(int alpha) {
        mWebAlpha = alpha;
    }

    public int getWebAlpha() {
        return mWebAlpha;
    }

    public void setWebColor(int color) {
        mWebColor = color;
    }

    public int getWebColor() {
        return mWebColor;
    }

    public void setWebColorInner(int color) {
        mWebColorInner = color;
    }

    public int getWebColorInner() {
        return mWebColorInner;
    }

    public void setDrawWeb(boolean enabled) {
        mDrawWeb = enabled;
    }

    public void setSkipWebLineCount(int count) {

        mSkipWebLineCount = Math.max(0, count);
    }

    public int getSkipWebLineCount() {
        return mSkipWebLineCount;
    }

    @Override
    protected float getRequiredLegendOffset() {
        return mLegendRenderer.getLabelPaint().getTextSize() * 4.f;
    }

    @Override
    protected float getRequiredBaseOffset() {
        return mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled() ?
                mXAxis.mLabelRotatedWidth :
                Utils.convertDpToPixel(10f);
    }

    @Override
    public float getRadius() {
        RectF content = mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2f, content.height() / 2f);
    }

    public float getYChartMax() {
        return mYAxis.mAxisMaximum;
    }

    public float getYChartMin() {
        return mYAxis.mAxisMinimum;
    }

    public float getYRange() {
        return mYAxis.mAxisRange;
    }
}