package com.example.project2.charting.components;

import android.graphics.Color;
import android.graphics.Typeface;

import com.example.project2.charting.utils.Utils;

public abstract class ComponentBase {

    protected boolean mEnabled = true;

    protected float mXOffset = 5f;

    protected float mYOffset = 5f;

    protected Typeface mTypeface = null;

    protected float mTextSize = 10f;

    protected int mTextColor = Color.BLACK;

    public ComponentBase() {

    }

    public float getXOffset() {
        return mXOffset;
    }

    public void setXOffset(float xOffset) {
        mXOffset = Utils.convertDpToPixel(xOffset);
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = Utils.convertDpToPixel(yOffset);
    }

    public Typeface getTypeface() {
        return mTypeface;
    }

    public void setTypeface(Typeface tf) {
        mTypeface = tf;
    }

    public void setTextSize(float size) {

        if (size > 24f)
            size = 24f;
        if (size < 6f)
            size = 6f;

        mTextSize = Utils.convertDpToPixel(size);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

}