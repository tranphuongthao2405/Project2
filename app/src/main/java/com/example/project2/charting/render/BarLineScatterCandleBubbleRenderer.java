package com.example.project2.charting.render;

import com.example.project2.charting.animation.ChartAnimator;
import com.example.project2.charting.data.DataSet;
import com.example.project2.charting.data.Entry;
import com.example.project2.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.example.project2.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.example.project2.charting.interfaces.datasets.IDataSet;
import com.example.project2.charting.utils.ViewPortHandler;

public abstract class BarLineScatterCandleBubbleRenderer extends DataRenderer {

    protected XBounds mXBounds = new XBounds();

    public BarLineScatterCandleBubbleRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    protected boolean shouldDrawValues(IDataSet set) {
        return set.isVisible() && set.isDrawValuesEnabled();
    }

    protected boolean isInBoundsX(Entry e, IBarLineScatterCandleBubbleDataSet set) {

        if (e == null)
            return false;

        float entryIndex = set.getEntryIndex(e);

        if (e == null || entryIndex >= set.getEntryCount() * mAnimator.getPhaseX()) {
            return false;
        } else {
            return true;
        }
    }

    protected class XBounds {

        public int min;

        public int max;

        public int range;

        public void set(BarLineScatterCandleBubbleDataProvider chart, IBarLineScatterCandleBubbleDataSet dataSet) {
            float phaseX = Math.max(0.f, Math.min(1.f, mAnimator.getPhaseX()));

            float low = chart.getLowestVisibleX();
            float high = chart.getHighestVisibleX();

            Entry entryFrom = dataSet.getEntryForXValue(low, Float.NaN, DataSet.Rounding.DOWN);
            Entry entryTo = dataSet.getEntryForXValue(high, Float.NaN, DataSet.Rounding.UP);

            min = entryFrom == null ? 0 : dataSet.getEntryIndex(entryFrom);
            max = entryTo == null ? 0 : dataSet.getEntryIndex(entryTo);
            range = (int) ((max - min) * phaseX);
        }
    }
}
