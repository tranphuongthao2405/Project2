package com.example.project2.charting.interfaces.dataprovider;

import com.example.project2.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}