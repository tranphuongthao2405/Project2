package com.example.project2.charting.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;

import com.example.project2.charting.components.Legend;
import com.example.project2.charting.components.YAxis;
import com.example.project2.charting.formatter.IValueFormatter;
import com.example.project2.charting.interfaces.datasets.IDataSet;
import com.example.project2.charting.utils.ColorTemplate;
import com.example.project2.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataSet<T extends Entry> implements IDataSet<T> {

    protected List<Integer> mColors = null;

    protected List<Integer> mValueColors = null;

    private String mLabel = "DataSet";

    protected YAxis.AxisDependency mAxisDependency = YAxis.AxisDependency.LEFT;

    protected boolean mHighlightEnabled = true;

    protected transient IValueFormatter mValueFormatter;

    protected Typeface mValueTypeface;

    private Legend.LegendForm mForm = Legend.LegendForm.DEFAULT;
    private float mFormSize = Float.NaN;
    private float mFormLineWidth = Float.NaN;
    private DashPathEffect mFormLineDashEffect = null;

    protected boolean mDrawValues = true;

    protected float mValueTextSize = 17f;

    protected boolean mVisible = true;

    public BaseDataSet() {
        mColors = new ArrayList<Integer>();
        mValueColors = new ArrayList<Integer>();

        // default color
        mColors.add(Color.rgb(140, 234, 255));
        mValueColors.add(Color.BLACK);
    }

    public BaseDataSet(String label) {
        this();
        this.mLabel = label;
    }

    public void notifyDataSetChanged() {
        calcMinMax();
    }

    @Override
    public List<Integer> getColors() {
        return mColors;
    }

    public List<Integer> getValueColors() {
        return mValueColors;
    }

    @Override
    public int getColor() {
        return mColors.get(0);
    }

    @Override
    public int getColor(int index) {
        return mColors.get(index % mColors.size());
    }

    public void setColors(List<Integer> colors) {
        this.mColors = colors;
    }

    public void setColors(int... colors) {
        this.mColors = ColorTemplate.createColors(colors);
    }

    public void setColors(int[] colors, Context c) {

        if(mColors == null){
            mColors = new ArrayList<>();
        }

        mColors.clear();

        for (int color : colors) {
            mColors.add(c.getResources().getColor(color));
        }
    }

    public void addColor(int color) {
        if (mColors == null)
            mColors = new ArrayList<Integer>();
        mColors.add(color);
    }

    public void setColor(int color) {
        resetColors();
        mColors.add(color);
    }

    public void setColor(int color, int alpha) {
        setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }

    public void setColors(int[] colors, int alpha) {
        resetColors();
        for (int color : colors) {
            addColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }

    public void resetColors() {
        if(mColors == null) {
            mColors = new ArrayList<Integer>();
        }
        mColors.clear();
    }

    @Override
    public void setLabel(String label) {
        mLabel = label;
    }

    @Override
    public String getLabel() {
        return mLabel;
    }

    @Override
    public void setHighlightEnabled(boolean enabled) {
        mHighlightEnabled = enabled;
    }

    @Override
    public boolean isHighlightEnabled() {
        return mHighlightEnabled;
    }

    @Override
    public void setValueFormatter(IValueFormatter f) {

        if (f == null)
            return;
        else
            mValueFormatter = f;
    }

    @Override
    public IValueFormatter getValueFormatter() {
        if (needsFormatter())
            return Utils.getDefaultValueFormatter();
        return mValueFormatter;
    }

    @Override
    public boolean needsFormatter() {
        return mValueFormatter == null;
    }

    @Override
    public void setValueTextColor(int color) {
        mValueColors.clear();
        mValueColors.add(color);
    }

    @Override
    public void setValueTextColors(List<Integer> colors) {
        mValueColors = colors;
    }

    @Override
    public void setValueTypeface(Typeface tf) {
        mValueTypeface = tf;
    }

    @Override
    public void setValueTextSize(float size) {
        mValueTextSize = Utils.convertDpToPixel(size);
    }

    @Override
    public int getValueTextColor() {
        return mValueColors.get(0);
    }

    @Override
    public int getValueTextColor(int index) {
        return mValueColors.get(index % mValueColors.size());
    }

    @Override
    public Typeface getValueTypeface() {
        return mValueTypeface;
    }

    @Override
    public float getValueTextSize() {
        return mValueTextSize;
    }

    public void setForm(Legend.LegendForm form) {
        mForm = form;
    }

    @Override
    public Legend.LegendForm getForm() {
        return mForm;
    }

    public void setFormSize(float formSize) {
        mFormSize = formSize;
    }

    @Override
    public float getFormSize() {
        return mFormSize;
    }

    public void setFormLineWidth(float formLineWidth) {
        mFormLineWidth = formLineWidth;
    }

    @Override
    public float getFormLineWidth() {
        return mFormLineWidth;
    }

    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        mFormLineDashEffect = dashPathEffect;
    }

    @Override
    public DashPathEffect getFormLineDashEffect() {
        return mFormLineDashEffect;
    }

    @Override
    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

    @Override
    public boolean isDrawValuesEnabled() {
        return mDrawValues;
    }

    @Override
    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    @Override
    public boolean isVisible() {
        return mVisible;
    }

    @Override
    public YAxis.AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    @Override
    public void setAxisDependency(YAxis.AxisDependency dependency) {
        mAxisDependency = dependency;
    }

    @Override
    public int getIndexInEntries(int xIndex) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (xIndex == getEntryForIndex(i).getX())
                return i;
        }

        return -1;
    }

    @Override
    public boolean removeFirst() {

        if (getEntryCount() > 0) {

            T entry = getEntryForIndex(0);
            return removeEntry(entry);
        } else
            return false;
    }

    @Override
    public boolean removeLast() {

        if (getEntryCount() > 0) {

            T e = getEntryForIndex(getEntryCount() - 1);
            return removeEntry(e);
        } else
            return false;
    }

    @Override
    public boolean removeEntryByXValue(float xValue) {

        T e = getEntryForXValue(xValue, Float.NaN);
        return removeEntry(e);
    }

    @Override
    public boolean removeEntry(int index) {

        T e = getEntryForIndex(index);
        return removeEntry(e);
    }

    @Override
    public boolean contains(T e) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (getEntryForIndex(i).equals(e))
                return true;
        }

        return false;
    }
}
