package com.example.project2.charting.listener;

import com.example.project2.charting.data.DataSet;
import com.example.project2.charting.data.Entry;

public interface OnDrawListener {

    void onEntryAdded(Entry entry);

    void onEntryMoved(Entry entry);

    void onDrawFinished(DataSet<?> dataSet);

}
