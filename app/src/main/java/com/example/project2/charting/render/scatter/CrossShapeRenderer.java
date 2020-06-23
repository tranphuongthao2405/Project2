package com.example.project2.charting.render.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.project2.charting.interfaces.datasets.IScatterDataSet;
import com.example.project2.charting.utils.Utils;
import com.example.project2.charting.utils.ViewPortHandler;

public class CrossShapeRenderer implements IShapeRenderer {
    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                            float posX, float posY, Paint renderPaint) {

        final float shapeHalf = dataSet.getScatterShapeSize() / 2f;

        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));

        c.drawLine(
                posX - shapeHalf,
                posY,
                posX + shapeHalf,
                posY,
                renderPaint);
        c.drawLine(
                posX,
                posY - shapeHalf,
                posX,
                posY + shapeHalf,
                renderPaint);

    }
}