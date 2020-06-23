package com.example.project2.chart.interfaces.dataprovider;

import com.example.project2.chart.components.YAxis;
import com.example.project2.chart.data.BarLineScatterCandleBubbleData;
import com.example.project2.chart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);

    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
