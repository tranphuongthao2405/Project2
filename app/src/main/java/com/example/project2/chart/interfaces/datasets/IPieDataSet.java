package com.example.project2.chart.interfaces.datasets;

import com.example.project2.chart.data.PieDataSet;
import com.example.project2.chart.data.PieEntry;

public interface IPieDataSet extends IDataSet<PieEntry> {

    float getSliceSpace();

    boolean isAutomaticallyDisableSliceSpacingEnabled();

    float getSelectionShift();

    PieDataSet.ValuePosition getXValuePosition();
    PieDataSet.ValuePosition getYValuePosition();

    int getValueLineColor();

    float getValueLineWidth();

    float getValueLinePart1OffsetPercentage();

    float getValueLinePart1Length();

    float getValueLinePart2Length();

    boolean isValueLineVariableLength();

}