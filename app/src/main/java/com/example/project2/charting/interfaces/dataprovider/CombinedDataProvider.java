package com.example.project2.charting.interfaces.dataprovider;

import com.example.project2.charting.data.CombinedData;

public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {

    CombinedData getCombinedData();
}
