package com.example.project2.charting.interfaces.datasets;

import android.graphics.drawable.Drawable;

import com.example.project2.charting.data.Entry;

public interface ILineRadarDataSet<T extends Entry> extends ILineScatterCandleRadarDataSet<T> {

    int getFillColor();

    Drawable getFillDrawable();

    int getFillAlpha();

    float getLineWidth();

    boolean isDrawFilledEnabled();

    void setDrawFilled(boolean enabled);
}
