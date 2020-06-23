package com.example.project2.chart.interfaces.dataprovider;

import android.graphics.RectF;

import com.example.project2.chart.data.ChartData;
import com.example.project2.chart.formatter.IValueFormatter;
import com.example.project2.chart.utils.MPPointF;

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
