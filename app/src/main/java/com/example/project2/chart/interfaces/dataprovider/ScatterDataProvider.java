package com.example.project2.chart.interfaces.dataprovider;

import com.example.project2.chart.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}