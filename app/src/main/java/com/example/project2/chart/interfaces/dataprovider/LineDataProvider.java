package com.example.project2.chart.interfaces.dataprovider;

import com.example.project2.chart.components.YAxis;
import com.example.project2.chart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
