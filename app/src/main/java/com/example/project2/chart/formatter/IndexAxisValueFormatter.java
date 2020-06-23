package com.example.project2.chart.formatter;

import com.example.project2.chart.components.AxisBase;

import java.util.Collection;

public class IndexAxisValueFormatter implements IAxisValueFormatter
{
    private String[] mValues = new String[] {};
    private int mValueCount = 0;

    public IndexAxisValueFormatter() {
    }

    public IndexAxisValueFormatter(String[] values) {
        if (values != null)
            setValues(values);
    }

    public IndexAxisValueFormatter(Collection<String> values) {
        if (values != null)
            setValues(values.toArray(new String[values.size()]));
    }

    public String getFormattedValue(float value, AxisBase axis) {
        int index = Math.round(value);

        if (index < 0 || index >= mValueCount || index != (int)value)
            return "";

        return mValues[index];
    }

    public String[] getValues()
    {
        return mValues;
    }

    public void setValues(String[] values)
    {
        if (values == null)
            values = new String[] {};

        this.mValues = values;
        this.mValueCount = values.length;
    }
}