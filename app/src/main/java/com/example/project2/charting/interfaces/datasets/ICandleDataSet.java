package com.example.project2.charting.interfaces.datasets;

import android.graphics.Paint;

import com.example.project2.charting.data.CandleEntry;

public interface ICandleDataSet extends ILineScatterCandleRadarDataSet<CandleEntry> {
    float getBarSpace();

    boolean getShowCandleBar();

    float getShadowWidth();

    int getShadowColor();

    int getNeutralColor();

    int getIncreasingColor();

    int getDecreasingColor();

    Paint.Style getIncreasingPaintStyle();

    Paint.Style getDecreasingPaintStyle();

    boolean getShadowColorSameAsCandle();
}