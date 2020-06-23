package com.example.project2.charting.interfaces.datasets;

import com.example.project2.charting.data.PieDataSet;
import com.example.project2.charting.data.PieEntry;

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