package com.example.project2.charting.formatter;

import com.example.project2.charting.components.AxisBase;

public interface IAxisValueFormatter
{
    String getFormattedValue(float value, AxisBase axis);
}
