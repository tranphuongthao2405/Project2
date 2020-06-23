package com.example.project2.chart.highlight;

import com.example.project2.chart.components.YAxis;
import com.example.project2.chart.data.BarLineScatterCandleBubbleData;
import com.example.project2.chart.data.DataSet;
import com.example.project2.chart.data.Entry;
import com.example.project2.chart.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.example.project2.chart.interfaces.datasets.IDataSet;
import com.example.project2.chart.utils.MPPointD;

import java.util.ArrayList;
import java.util.List;

public class ChartHighlighter<T extends BarLineScatterCandleBubbleDataProvider> implements IHighlighter
{
    protected T mChart;

    protected List<Highlight> mHighlightBuffer = new ArrayList<Highlight>();

    public ChartHighlighter(T chart) {
        this.mChart = chart;
    }

    @Override
    public Highlight getHighlight(float x, float y) {

        MPPointD pos = getValsForTouch(x, y);
        float xVal = (float) pos.x;
        MPPointD.recycleInstance(pos);

        Highlight high = getHighlightForX(xVal, x, y);
        return high;
    }

    protected MPPointD getValsForTouch(float x, float y) {

        // take any transformer to determine the x-axis value
        MPPointD pos = mChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(x, y);
        return pos;
    }

    protected Highlight getHighlightForX(float xVal, float x, float y) {

        List<Highlight> closestValues = getHighlightsAtXValue(xVal, x, y);

        if(closestValues.isEmpty()) {
            return null;
        }

        float leftAxisMinDist = getMinimumDistance(closestValues, y, YAxis.AxisDependency.LEFT);
        float rightAxisMinDist = getMinimumDistance(closestValues, y, YAxis.AxisDependency.RIGHT);

        YAxis.AxisDependency axis = leftAxisMinDist < rightAxisMinDist ? YAxis.AxisDependency.LEFT : YAxis.AxisDependency.RIGHT;

        Highlight detail = getClosestHighlightByPixel(closestValues, x, y, axis, mChart.getMaxHighlightDistance());

        return detail;
    }

    protected float getMinimumDistance(List<Highlight> closestValues, float pos, YAxis.AxisDependency axis) {

        float distance = Float.MAX_VALUE;

        for (int i = 0; i < closestValues.size(); i++) {

            Highlight high = closestValues.get(i);

            if (high.getAxis() == axis) {

                float tempDistance = Math.abs(getHighlightPos(high) - pos);
                if (tempDistance < distance) {
                    distance = tempDistance;
                }
            }
        }

        return distance;
    }

    protected float getHighlightPos(Highlight h) {
        return h.getYPx();
    }

    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {

        mHighlightBuffer.clear();

        BarLineScatterCandleBubbleData data = getData();

        if (data == null)
            return mHighlightBuffer;

        for (int i = 0, dataSetCount = data.getDataSetCount(); i < dataSetCount; i++) {

            IDataSet dataSet = data.getDataSetByIndex(i);

            // don't include DataSets that cannot be highlighted
            if (!dataSet.isHighlightEnabled())
                continue;

            mHighlightBuffer.addAll(buildHighlights(dataSet, i, xVal, DataSet.Rounding.CLOSEST));
        }

        return mHighlightBuffer;
    }

    protected List<Highlight> buildHighlights(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {

        ArrayList<Highlight> highlights = new ArrayList<>();

        //noinspection unchecked
        List<Entry> entries = set.getEntriesForXValue(xVal);
        if (entries.size() == 0) {
            // Try to find closest x-value and take all entries for that x-value
            final Entry closest = set.getEntryForXValue(xVal, Float.NaN, rounding);
            if (closest != null)
            {
                //noinspection unchecked
                entries = set.getEntriesForXValue(closest.getX());
            }
        }

        if (entries.size() == 0)
            return highlights;

        for (Entry e : entries) {
            MPPointD pixels = mChart.getTransformer(
                    set.getAxisDependency()).getPixelForValues(e.getX(), e.getY());

            highlights.add(new Highlight(
                    e.getX(), e.getY(),
                    (float) pixels.x, (float) pixels.y,
                    dataSetIndex, set.getAxisDependency()));
        }

        return highlights;
    }

    public Highlight getClosestHighlightByPixel(List<Highlight> closestValues, float x, float y,
                                                YAxis.AxisDependency axis, float minSelectionDistance) {

        Highlight closest = null;
        float distance = minSelectionDistance;

        for (int i = 0; i < closestValues.size(); i++) {

            Highlight high = closestValues.get(i);

            if (axis == null || high.getAxis() == axis) {

                float cDistance = getDistance(x, y, high.getXPx(), high.getYPx());

                if (cDistance < distance) {
                    closest = high;
                    distance = cDistance;
                }
            }
        }

        return closest;
    }

    protected float getDistance(float x1, float y1, float x2, float y2) {
        //return Math.abs(y1 - y2);
        //return Math.abs(x1 - x2);
        return (float) Math.hypot(x1 - x2, y1 - y2);
    }

    protected BarLineScatterCandleBubbleData getData() {
        return mChart.getData();
    }
}
