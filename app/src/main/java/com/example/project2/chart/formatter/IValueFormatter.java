package com.example.project2.chart.formatter;

import com.example.project2.chart.data.Entry;
import com.example.project2.chart.utils.ViewPortHandler;

public interface IValueFormatter
{
    String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler);
}