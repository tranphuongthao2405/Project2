package com.example.project2.chart.interfaces.dataprovider;


import com.example.project2.chart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}