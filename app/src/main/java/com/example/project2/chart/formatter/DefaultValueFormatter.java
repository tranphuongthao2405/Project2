package com.example.project2.chart.formatter;

import com.example.project2.chart.data.Entry;
import com.example.project2.chart.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class DefaultValueFormatter implements IValueFormatter {

    protected DecimalFormat mFormat;

    protected int mDecimalDigits;

    public DefaultValueFormatter(int digits) {
        setup(digits);
    }

    public void setup(int digits) {

        this.mDecimalDigits = digits;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        // put more logic here ...
        // avoid memory allocations here (for performance reasons)

        return mFormat.format(value);
    }

    public int getDecimalDigits() {
        return mDecimalDigits;
    }
}