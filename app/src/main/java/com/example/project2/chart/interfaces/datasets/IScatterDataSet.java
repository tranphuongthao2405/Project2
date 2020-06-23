package com.example.project2.chart.interfaces.datasets;

import com.example.project2.chart.data.Entry;
import com.example.project2.chart.render.scatter.IShapeRenderer;

public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry> {
    float getScatterShapeSize();

    float getScatterShapeHoleRadius();

    int getScatterShapeHoleColor();

    IShapeRenderer getShapeRenderer();
}