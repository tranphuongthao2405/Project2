package com.example.project2.chart.listener;

import com.example.project2.chart.data.DataSet;
import com.example.project2.chart.data.Entry;

public interface OnDrawListener {

    void onEntryAdded(Entry entry);

    void onEntryMoved(Entry entry);

    void onDrawFinished(DataSet<?> dataSet);

}
