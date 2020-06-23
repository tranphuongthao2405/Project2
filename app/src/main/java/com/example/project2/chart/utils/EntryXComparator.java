package com.example.project2.chart.utils;

import com.example.project2.chart.data.Entry;

import java.util.Comparator;

public class EntryXComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry entry1, Entry entry2) {
        float diff = entry1.getX() - entry2.getX();

        if (diff == 0f) return 0;
        else {
            if (diff > 0f) return 1;
            else return -1;
        }
    }
}