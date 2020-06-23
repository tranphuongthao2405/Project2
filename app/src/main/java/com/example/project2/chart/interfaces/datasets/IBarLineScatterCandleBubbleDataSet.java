package com.example.project2.chart.interfaces.datasets;

import com.example.project2.chart.data.Entry;

public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {
    int getHighLightColor();
}
