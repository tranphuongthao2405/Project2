package com.example.project2.charting.interfaces.datasets;

import com.example.project2.charting.data.Entry;
import com.example.project2.charting.render.scatter.IShapeRenderer;

public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry> {
    float getScatterShapeSize();

    float getScatterShapeHoleRadius();

    int getScatterShapeHoleColor();

    IShapeRenderer getShapeRenderer();
}