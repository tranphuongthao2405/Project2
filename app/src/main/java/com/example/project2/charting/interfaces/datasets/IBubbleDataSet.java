package com.example.project2.charting.interfaces.datasets;

import com.example.project2.charting.data.BubbleEntry;

public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry> {
    void setHighlightCircleWidth(float width);

    float getMaxSize();

    boolean isNormalizeSizeEnabled();

    float getHighlightCircleWidth();
}