package com.example.project2.charting.data;

public abstract class BaseEntry {

    private float y = 0f;

    private Object mData = null;

    public BaseEntry() {

    }

    public BaseEntry(float y) {
        this.y = y;
    }

    public BaseEntry(float y, Object data) {
        this(y);
        this.mData = data;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        this.mData = data;
    }
}