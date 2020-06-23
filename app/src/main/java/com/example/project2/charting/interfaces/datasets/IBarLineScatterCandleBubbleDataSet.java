package com.example.project2.charting.interfaces.datasets;

import com.example.project2.charting.data.Entry;

public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {
    int getHighLightColor();
}
