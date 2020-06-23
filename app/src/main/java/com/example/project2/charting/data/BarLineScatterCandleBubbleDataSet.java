package com.example.project2.charting.data;

import android.graphics.Color;

import com.example.project2.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

import java.util.List;


public abstract class BarLineScatterCandleBubbleDataSet<T extends Entry> extends DataSet<T> implements IBarLineScatterCandleBubbleDataSet<T> {

    protected int mHighLightColor = Color.rgb(255, 187, 115);

    public BarLineScatterCandleBubbleDataSet(List<T> yVals, String label) {
        super(yVals, label);
    }

    public void setHighLightColor(int color) {
        mHighLightColor = color;
    }

    @Override
    public int getHighLightColor() {
        return mHighLightColor;
    }
}