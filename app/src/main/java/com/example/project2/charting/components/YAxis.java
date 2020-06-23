package com.example.project2.charting.components;

import android.graphics.Color;
import android.graphics.Paint;

import com.example.project2.charting.utils.Utils;

public class YAxis extends AxisBase {

    private boolean mDrawBottomYLabelEntry = true;

    private boolean mDrawTopYLabelEntry = true;

    protected boolean mInverted = false;

    protected boolean mDrawZeroLine = false;

    protected int mZeroLineColor = Color.GRAY;

    protected float mZeroLineWidth = 1f;

    protected float mSpacePercentTop = 10f;

    protected float mSpacePercentBottom = 10f;

    private YAxisLabelPosition mPosition = YAxisLabelPosition.OUTSIDE_CHART;

    public enum YAxisLabelPosition {
        OUTSIDE_CHART, INSIDE_CHART
    }

    private AxisDependency mAxisDependency;

    protected float mMinWidth = 0.f;

    protected float mMaxWidth = Float.POSITIVE_INFINITY;

    public enum AxisDependency {
        LEFT, RIGHT
    }

    public YAxis() {
        super();

        // default left
        this.mAxisDependency = AxisDependency.LEFT;
        this.mYOffset = 0f;
    }

    public YAxis(AxisDependency position) {
        super();
        this.mAxisDependency = position;
        this.mYOffset = 0f;
    }

    public AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    public float getMinWidth() {
        return mMinWidth;
    }

    public void setMinWidth(float minWidth) {
        mMinWidth = minWidth;
    }

    public float getMaxWidth() {
        return mMaxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        mMaxWidth = maxWidth;
    }

    public YAxisLabelPosition getLabelPosition() {
        return mPosition;
    }

    public void setPosition(YAxisLabelPosition pos) {
        mPosition = pos;
    }

    public boolean isDrawTopYLabelEntryEnabled() {
        return mDrawTopYLabelEntry;
    }

    public boolean isDrawBottomYLabelEntryEnabled() {
        return mDrawBottomYLabelEntry;
    }

    public void setDrawTopYLabelEntry(boolean enabled) {
        mDrawTopYLabelEntry = enabled;
    }

    public void setInverted(boolean enabled) {
        mInverted = enabled;
    }

    public boolean isInverted() {
        return mInverted;
    }

    @Deprecated
    public void setStartAtZero(boolean startAtZero) {
        if (startAtZero)
            setAxisMinimum(0f);
        else
            resetAxisMinimum();
    }

    public void setSpaceTop(float percent) {
        mSpacePercentTop = percent;
    }

    public float getSpaceTop() {
        return mSpacePercentTop;
    }

    public void setSpaceBottom(float percent) {
        mSpacePercentBottom = percent;
    }

    public float getSpaceBottom() {
        return mSpacePercentBottom;
    }

    public boolean isDrawZeroLineEnabled() {
        return mDrawZeroLine;
    }

    public void setDrawZeroLine(boolean mDrawZeroLine) {
        this.mDrawZeroLine = mDrawZeroLine;
    }

    public int getZeroLineColor() {
        return mZeroLineColor;
    }

    public void setZeroLineColor(int color) {
        mZeroLineColor = color;
    }

    public float getZeroLineWidth() {
        return mZeroLineWidth;
    }

    public void setZeroLineWidth(float width) {
        this.mZeroLineWidth = Utils.convertDpToPixel(width);
    }

    public float getRequiredWidthSpace(Paint p) {

        p.setTextSize(mTextSize);

        String label = getLongestLabel();
        float width = (float) Utils.calcTextWidth(p, label) + getXOffset() * 2f;

        float minWidth = getMinWidth();
        float maxWidth = getMaxWidth();

        if (minWidth > 0.f)
            minWidth = Utils.convertDpToPixel(minWidth);

        if (maxWidth > 0.f && maxWidth != Float.POSITIVE_INFINITY)
            maxWidth = Utils.convertDpToPixel(maxWidth);

        width = Math.max(minWidth, Math.min(width, maxWidth > 0.0 ? maxWidth : width));

        return width;
    }

    public float getRequiredHeightSpace(Paint p) {

        p.setTextSize(mTextSize);

        String label = getLongestLabel();
        return (float) Utils.calcTextHeight(p, label) + getYOffset() * 2f;
    }

    public boolean needsOffset() {
        if (isEnabled() && isDrawLabelsEnabled() && getLabelPosition() == YAxisLabelPosition
                .OUTSIDE_CHART)
            return true;
        else
            return false;
    }

    @Override
    public void calculate(float dataMin, float dataMax) {

        // if custom, use value as is, else use data value
        float min = mCustomAxisMin ? mAxisMinimum : dataMin;
        float max = mCustomAxisMax ? mAxisMaximum : dataMax;

        // temporary range (before calculations)
        float range = Math.abs(max - min);

        // in case all values are equal
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }

        // bottom-space only effects non-custom min
        if (!mCustomAxisMin) {

            float bottomSpace = range / 100f * getSpaceBottom();
            this.mAxisMinimum = (min - bottomSpace);
        }

        // top-space only effects non-custom max
        if (!mCustomAxisMax) {

            float topSpace = range / 100f * getSpaceTop();
            this.mAxisMaximum = (max + topSpace);
        }

        // calc actual range
        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);
    }
}