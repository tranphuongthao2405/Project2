package com.example.project2.charting.interfaces.dataprovider;

import com.example.project2.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}