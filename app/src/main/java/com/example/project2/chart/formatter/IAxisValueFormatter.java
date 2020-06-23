package com.example.project2.chart.formatter;

import com.example.project2.chart.components.AxisBase;

public interface IAxisValueFormatter
{
    String getFormattedValue(float value, AxisBase axis);
}
