package com.example.project2.chart.listener;

import com.example.project2.chart.data.Entry;
import com.example.project2.chart.highlight.Highlight;

public interface OnChartValueSelectedListener {
    void onValueSelected(Entry e, Highlight h);

    void onNothingSelected();
}
