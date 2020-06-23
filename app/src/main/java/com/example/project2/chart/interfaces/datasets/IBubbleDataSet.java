package com.example.project2.chart.interfaces.datasets;

import com.example.project2.chart.data.BubbleEntry;

public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry> {
    void setHighlightCircleWidth(float width);

    float getMaxSize();

    boolean isNormalizeSizeEnabled();

    float getHighlightCircleWidth();
}