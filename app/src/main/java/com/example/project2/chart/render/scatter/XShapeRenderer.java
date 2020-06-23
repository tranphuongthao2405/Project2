package com.example.project2.chart.render.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.project2.chart.interfaces.datasets.IScatterDataSet;
import com.example.project2.chart.utils.Utils;
import com.example.project2.chart.utils.ViewPortHandler;

public class XShapeRenderer implements IShapeRenderer
{
    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                            float posX, float posY, Paint renderPaint) {

        final float shapeHalf = dataSet.getScatterShapeSize() / 2f;

        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));

        c.drawLine(
                posX - shapeHalf,
                posY - shapeHalf,
                posX + shapeHalf,
                posY + shapeHalf,
                renderPaint);
        c.drawLine(
                posX + shapeHalf,
                posY - shapeHalf,
                posX - shapeHalf,
                posY + shapeHalf,
                renderPaint);

    }

}
