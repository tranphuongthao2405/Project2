package com.example.project2.chart.render.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.project2.chart.interfaces.datasets.IScatterDataSet;
import com.example.project2.chart.utils.ViewPortHandler;

public interface IShapeRenderer
{
    void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                     float posX, float posY, Paint renderPaint);
}
