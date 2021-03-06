package com.example.project2.charting.formatter;

import com.example.project2.charting.components.AxisBase;
import com.example.project2.charting.data.Entry;
import com.example.project2.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class PercentFormatter implements IValueFormatter, IAxisValueFormatter {

    protected DecimalFormat mFormat;

    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    public PercentFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " %";
    }

    // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " %";
    }

    public int getDecimalDigits() {
        return 1;
    }
}
