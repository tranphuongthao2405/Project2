package com.example.project2.charting.interfaces.dataprovider;

import com.example.project2.charting.components.YAxis;
import com.example.project2.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
