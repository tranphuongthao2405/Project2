package com.example.project2.chart.formatter;

import com.example.project2.chart.data.Entry;
import com.example.project2.chart.interfaces.datasets.IDataSet;

public interface ColorFormatter {
    int getColor(int index, Entry e, IDataSet set);
}
