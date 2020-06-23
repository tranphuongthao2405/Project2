package com.example.project2.charting.components;

import android.graphics.Paint;

import com.example.project2.charting.utils.MPPointF;
import com.example.project2.charting.utils.Utils;

public class Description extends ComponentBase {

    private String text = "Description Label";

    private MPPointF mPosition;

    private Paint.Align mTextAlign = Paint.Align.RIGHT;

    public Description() {
        super();

        // default size
        mTextSize = Utils.convertDpToPixel(8f);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setPosition(float x, float y) {
        if (mPosition == null) {
            mPosition = MPPointF.getInstance(x, y);
        } else {
            mPosition.x = x;
            mPosition.y = y;
        }
    }

    public MPPointF getPosition() {
        return mPosition;
    }

    public void setTextAlign(Paint.Align align) {
        this.mTextAlign = align;
    }

    public Paint.Align getTextAlign() {
        return mTextAlign;
    }
}