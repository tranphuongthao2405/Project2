package com.example.project2.chart.formatter;

import com.example.project2.chart.interfaces.dataprovider.LineDataProvider;
import com.example.project2.chart.interfaces.datasets.ILineDataSet;

public interface IFillFormatter
{
    float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider);
}