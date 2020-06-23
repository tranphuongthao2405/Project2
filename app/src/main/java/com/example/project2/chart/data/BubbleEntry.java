package com.example.project2.chart.data;

import android.annotation.SuppressLint;

@SuppressLint("ParcelCreator")
public class BubbleEntry extends Entry {

    private float mSize = 0f;

    public BubbleEntry(float x, float y, float size) {
        super(x, y);
        this.mSize = size;
    }

    public BubbleEntry(float x, float y, float size, Object data) {
        super(x, y, data);
        this.mSize = size;
    }

    public BubbleEntry copy() {

        BubbleEntry c = new BubbleEntry(getX(), getY(), mSize, getData());
        return c;
    }

    public float getSize() {
        return mSize;
    }

    public void setSize(float size) {
        this.mSize = size;
    }

}