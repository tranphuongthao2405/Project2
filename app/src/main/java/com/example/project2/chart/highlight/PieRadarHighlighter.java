package com.example.project2.chart.highlight;

import com.example.project2.chart.charts.PieChart;
import com.example.project2.chart.charts.PieRadarChartBase;

import java.util.ArrayList;
import java.util.List;

public abstract class PieRadarHighlighter<T extends PieRadarChartBase> implements IHighlighter
{

    protected T mChart;

    protected List<Highlight> mHighlightBuffer = new ArrayList<Highlight>();

    public PieRadarHighlighter(T chart) {
        this.mChart = chart;
    }

    @Override
    public Highlight getHighlight(float x, float y) {

        float touchDistanceToCenter = mChart.distanceToCenter(x, y);

        // check if a slice was touched
        if (touchDistanceToCenter > mChart.getRadius()) {

            // if no slice was touched, highlight nothing
            return null;

        } else {

            float angle = mChart.getAngleForPoint(x, y);

            if (mChart instanceof PieChart) {
                angle /= mChart.getAnimator().getPhaseY();
            }

            int index = mChart.getIndexForAngle(angle);

            // check if the index could be found
            if (index < 0 || index >= mChart.getData().getMaxEntryCountSet().getEntryCount()) {
                return null;

            } else {
                return getClosestHighlight(index, x, y);
            }
        }
    }

    protected abstract Highlight getClosestHighlight(int index, float x, float y);
}