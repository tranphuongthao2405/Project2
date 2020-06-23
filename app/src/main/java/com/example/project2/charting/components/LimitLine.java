package com.example.project2.charting.components;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.example.project2.charting.utils.Utils;

public class LimitLine extends ComponentBase {

    /** limit / maximum (the y-value or xIndex) */
    private float mLimit = 0f;

    /** the width of the limit line */
    private float mLineWidth = 2f;

    /** the color of the limit line */
    private int mLineColor = Color.rgb(237, 91, 91);

    /** the style of the label text */
    private Paint.Style mTextStyle = Paint.Style.FILL_AND_STROKE;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";

    /** the path effect of this LimitLine that makes dashed lines possible */
    private DashPathEffect mDashPathEffect = null;

    /** indicates the position of the LimitLine label */
    private LimitLabelPosition mLabelPosition = LimitLabelPosition.RIGHT_TOP;

    /** enum that indicates the position of the LimitLine label */
    public enum LimitLabelPosition {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    public LimitLine(float limit) {
        mLimit = limit;
    }

    public LimitLine(float limit, String label) {
        mLimit = limit;
        mLabel = label;
    }

    public float getLimit() {
        return mLimit;
    }

    public void setLineWidth(float width) {

        if (width < 0.2f)
            width = 0.2f;
        if (width > 12.0f)
            width = 12.0f;
        mLineWidth = Utils.convertDpToPixel(width);
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineColor(int color) {
        mLineColor = color;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        mDashPathEffect = new DashPathEffect(new float[] {
                lineLength, spaceLength
        }, phase);
    }

    public void disableDashedLine() {
        mDashPathEffect = null;
    }

    public boolean isDashedLineEnabled() {
        return mDashPathEffect == null ? false : true;
    }

    public DashPathEffect getDashPathEffect() {
        return mDashPathEffect;
    }

    public void setTextStyle(Paint.Style style) {
        this.mTextStyle = style;
    }

    public Paint.Style getTextStyle() {
        return mTextStyle;
    }

    public void setLabelPosition(LimitLabelPosition pos) {
        mLabelPosition = pos;
    }

    public LimitLabelPosition getLabelPosition() {
        return mLabelPosition;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getLabel() {
        return mLabel;
    }
}