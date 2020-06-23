package com.example.project2.chart.formatter;

import com.example.project2.chart.components.AxisBase;
import com.example.project2.chart.data.Entry;
import com.example.project2.chart.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class LargeValueFormatter implements IValueFormatter, IAxisValueFormatter
{

    private static String[] SUFFIX = new String[]{"", "k", "m", "b", "t"};
    private static final int MAX_LENGTH = 5;
    private DecimalFormat mFormat;
    private String mText = "";

    public LargeValueFormatter() {
        mFormat = new DecimalFormat("###E00");
    }

    public LargeValueFormatter(String appendix) {
        this();
        mText = appendix;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return makePretty(value) + mText;
    }

    // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return makePretty(value) + mText;
    }

    public void setAppendix(String appendix) {
        this.mText = appendix;
    }

    public void setSuffix(String[] suff) {
        SUFFIX = suff;
    }

    private String makePretty(double number) {

        String r = mFormat.format(number);

        int numericValue1 = Character.getNumericValue(r.charAt(r.length() - 1));
        int numericValue2 = Character.getNumericValue(r.charAt(r.length() - 2));
        int combined = Integer.valueOf(numericValue2 + "" + numericValue1);

        r = r.replaceAll("E[0-9][0-9]", SUFFIX[combined / 3]);

        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }

        return r;
    }

    public int getDecimalDigits() {
        return 0;
    }
}