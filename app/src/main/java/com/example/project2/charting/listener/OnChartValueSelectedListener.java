package com.example.project2.charting.listener;

import com.example.project2.charting.data.Entry;
import com.example.project2.charting.highlight.Highlight;

public interface OnChartValueSelectedListener {
    void onValueSelected(Entry e, Highlight h);

    void onNothingSelected();
}
