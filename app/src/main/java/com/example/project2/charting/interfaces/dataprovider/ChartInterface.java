package com.example.project2.charting.interfaces.dataprovider;

import android.graphics.RectF;

import com.example.project2.charting.data.ChartData;
import com.example.project2.charting.formatter.IValueFormatter;
import com.example.project2.charting.utils.MPPointF;

public interface ChartInterface {

    float getXChartMin();

    float getXChartMax();

    float getXRange();

    float getYChartMin();

    float getYChartMax();

    float getMaxHighlightDistance();

    int getWidth();

    int getHeight();

    MPPointF getCenterOfView();

    MPPointF getCenterOffsets();

    RectF getContentRect();

    IValueFormatter getDefaultValueFormatter();

    ChartData getData();

    int getMaxVisibleCount();
}
