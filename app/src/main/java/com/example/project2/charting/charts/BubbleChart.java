package com.example.project2.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.example.project2.charting.data.BubbleData;
import com.example.project2.charting.interfaces.dataprovider.BubbleDataProvider;
import com.example.project2.charting.render.BubbleChartRenderer;

public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider {

    public BubbleChart(Context context) {
        super(context);
    }

    public BubbleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new BubbleChartRenderer(this, mAnimator, mViewPortHandler);
    }

    public BubbleData getBubbleData() {
        return mData;
    }
}