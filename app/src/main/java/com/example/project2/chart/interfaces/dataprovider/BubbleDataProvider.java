package com.example.project2.chart.interfaces.dataprovider;

import com.example.project2.chart.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}