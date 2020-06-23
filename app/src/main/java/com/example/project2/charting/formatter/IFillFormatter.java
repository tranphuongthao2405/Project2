package com.example.project2.charting.formatter;

import com.example.project2.charting.interfaces.dataprovider.LineDataProvider;
import com.example.project2.charting.interfaces.datasets.ILineDataSet;

public interface IFillFormatter
{
    float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider);
}