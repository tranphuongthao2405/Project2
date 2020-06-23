package com.example.project2.charting.interfaces.dataprovider;


import com.example.project2.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}