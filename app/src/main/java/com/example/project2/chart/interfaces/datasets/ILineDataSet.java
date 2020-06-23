package com.example.project2.chart.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.example.project2.chart.data.Entry;
import com.example.project2.chart.data.LineDataSet;
import com.example.project2.chart.formatter.IFillFormatter;

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