package com.example.project2.charting.interfaces.dataprovider;

import com.example.project2.charting.components.YAxis;
import com.example.project2.charting.data.BarLineScatterCandleBubbleData;
import com.example.project2.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);

    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
