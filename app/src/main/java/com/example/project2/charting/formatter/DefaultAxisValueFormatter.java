package com.example.project2.charting.formatter;

import com.example.project2.charting.components.AxisBase;

import java.text.DecimalFormat;

public class DefaultAxisValueFormatter implements IAxisValueFormatter {

    protected DecimalFormat mFormat;

    protected int digits = 0;

    public DefaultAxisValueFormatter(int digits) {
        this.digits = digits;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // avoid memory allocations here (for performance)
        return mFormat.format(value);
    }

    public int getDecimalDigits() {
        return digits;
    }
}