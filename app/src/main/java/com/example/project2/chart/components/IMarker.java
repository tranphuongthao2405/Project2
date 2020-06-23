package com.example.project2.chart.components;

import android.graphics.Canvas;

import com.example.project2.chart.data.Entry;
import com.example.project2.chart.highlight.Highlight;
import com.example.project2.chart.utils.MPPointF;

public interface IMarker {

    MPPointF getOffset();

    MPPointF getOffsetForDrawingAtPoint(float posX, float posY);

    void refreshContent(Entry e, Highlight highlight);

    void draw(Canvas canvas, float posX, float posY);
}