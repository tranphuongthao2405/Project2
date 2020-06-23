package com.example.project2.charting.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.example.project2.charting.data.Entry;
import com.example.project2.charting.data.LineDataSet;
import com.example.project2.charting.formatter.IFillFormatter;

public interface ILineDataSet extends ILineRadarDataSet<Entry> {

    LineDataSet.Mode getMode();

    float getCubicIntensity();

    @Deprecated
    boolean isDrawCubicEnabled();

    @Deprecated
    boolean isDrawSteppedEnabled();

    float getCircleRadius();

    float getCircleHoleRadius();

    int getCircleColor(int index);

    int getCircleColorCount();

    boolean isDrawCirclesEnabled();

    int getCircleHoleColor();

    boolean isDrawCircleHoleEnabled();

    DashPathEffect getDashPathEffect();

    boolean isDashedLineEnabled();

    IFillFormatter getFillFormatter();
}