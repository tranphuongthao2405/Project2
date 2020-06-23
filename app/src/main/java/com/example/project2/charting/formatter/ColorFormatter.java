package com.example.project2.charting.formatter;

import com.example.project2.charting.data.Entry;
import com.example.project2.charting.interfaces.datasets.IDataSet;

public interface ColorFormatter {
    int getColor(int index, Entry e, IDataSet set);
}
