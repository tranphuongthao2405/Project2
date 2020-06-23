package com.example.project2.charting.formatter;

import com.example.project2.charting.data.BarEntry;
import com.example.project2.charting.data.Entry;
import com.example.project2.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class StackedValueFormatter implements IValueFormatter
{
    private boolean mDrawWholeStack;

    private String mAppendix;

    private DecimalFormat mFormat;

    public StackedValueFormatter(boolean drawWholeStack, String appendix, int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mAppendix = appendix;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < decimals; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if (!mDrawWholeStack && entry instanceof BarEntry) {

            BarEntry barEntry = (BarEntry) entry;
            float[] vals = barEntry.getYVals();

            if (vals != null) {

                // find out if we are on top of the stack
                if (vals[vals.length - 1] == value) {

                    // return the "sum" across all stack values
                    return mFormat.format(barEntry.getY()) + mAppendix;
                } else {
                    return ""; // return empty
                }
            }
        }

        // return the "proposed" value
        return mFormat.format(value) + mAppendix;
    }
}