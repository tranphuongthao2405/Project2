package com.example.project2.charting.render.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.project2.charting.interfaces.datasets.IScatterDataSet;
import com.example.project2.charting.utils.ViewPortHandler;

public interface IShapeRenderer
{
    void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                     float posX, float posY, Paint renderPaint);
}
