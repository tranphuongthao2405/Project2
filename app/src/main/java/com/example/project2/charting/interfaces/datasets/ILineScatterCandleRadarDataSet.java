package com.example.project2.charting.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.example.project2.charting.data.Entry;

public interface ILineScatterCandleRadarDataSet<T extends Entry> extends IBarLineScatterCandleBubbleDataSet<T> {

    boolean isVerticalHighlightIndicatorEnabled();

    boolean isHorizontalHighlightIndicatorEnabled();

    float getHighlightLineWidth();

    DashPathEffect getDashPathEffectHighlight();
}
