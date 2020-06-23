package com.example.project2.chart.interfaces.dataprovider;

import com.example.project2.chart.data.CombinedData;

public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {

    CombinedData getCombinedData();
}
