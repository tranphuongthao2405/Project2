package com.example.project2.charting.formatter;

import com.example.project2.charting.data.Entry;
import com.example.project2.charting.utils.ViewPortHandler;

public interface IValueFormatter
{
    String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler);
}