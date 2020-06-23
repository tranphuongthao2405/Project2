package com.example.project2.charting.interfaces.datasets;


import com.example.project2.charting.data.BarEntry;

public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry> {
    boolean isStacked();

    int getStackSize();

    int getBarShadowColor();

    float getBarBorderWidth();

    int getBarBorderColor();

    int getHighLightAlpha();

    String[] getStackLabels();
}
