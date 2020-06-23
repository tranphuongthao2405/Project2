package com.example.project2.chart.data;

import android.annotation.SuppressLint;

@SuppressLint("ParcelCreator")
public class CandleEntry extends Entry {

    /** shadow-high value */
    private float mShadowHigh = 0f;

    /** shadow-low value */
    private float mShadowLow = 0f;

    /** close value */
    private float mClose = 0f;

    /** open value */
    private float mOpen = 0f;

    public CandleEntry(float x, float shadowH, float shadowL, float open, float close) {
        super(x, (shadowH + shadowL) / 2f);

        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    public CandleEntry(float x, float shadowH, float shadowL, float open, float close,
                       Object data) {
        super(x, (shadowH + shadowL) / 2f, data);

        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    public float getShadowRange() {
        return Math.abs(mShadowHigh - mShadowLow);
    }

    public float getBodyRange() {
        return Math.abs(mOpen - mClose);
    }

    @Override
    public float getY() {
        return super.getY();
    }

    public CandleEntry copy() {

        CandleEntry c = new CandleEntry(getX(), mShadowHigh, mShadowLow, mOpen,
                mClose, getData());

        return c;
    }

    public float getHigh() {
        return mShadowHigh;
    }

    public void setHigh(float mShadowHigh) {
        this.mShadowHigh = mShadowHigh;
    }

    public float getLow() {
        return mShadowLow;
    }

    public void setLow(float mShadowLow) {
        this.mShadowLow = mShadowLow;
    }

    public float getClose() {
        return mClose;
    }

    public void setClose(float mClose) {
        this.mClose = mClose;
    }

    public float getOpen() {
        return mOpen;
    }

    public void setOpen(float mOpen) {
        this.mOpen = mOpen;
    }
}