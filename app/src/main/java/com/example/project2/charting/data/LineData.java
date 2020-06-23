package com.example.project2.charting.data;

import com.example.project2.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

public class LineData extends BarLineScatterCandleBubbleData<ILineDataSet> {

    public LineData() {
        super();
    }

    public LineData(ILineDataSet... dataSets) {
        super(dataSets);
    }

    public LineData(List<ILineDataSet> dataSets) {
        super(dataSets);
    }
}