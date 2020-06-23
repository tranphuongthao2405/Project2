package com.example.project2.charting.highlight;

import com.example.project2.charting.charts.PieChart;
import com.example.project2.charting.data.Entry;
import com.example.project2.charting.interfaces.datasets.IPieDataSet;

public class PieHighlighter extends PieRadarHighlighter<PieChart> {

    public PieHighlighter(PieChart chart) {
        super(chart);
    }

    @Override
    protected Highlight getClosestHighlight(int index, float x, float y) {

        IPieDataSet set = mChart.getData().getDataSet();

        final Entry entry = set.getEntryForIndex(index);

        return new Highlight(index, entry.getY(), x, y, 0, set.getAxisDependency());
    }
}