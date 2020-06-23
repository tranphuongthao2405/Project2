package com.example.project2.chart.data;


import com.example.project2.chart.charts.ScatterChart;
import com.example.project2.chart.interfaces.datasets.IScatterDataSet;
import com.example.project2.chart.render.scatter.ChevronDownShapeRenderer;
import com.example.project2.chart.render.scatter.ChevronUpShapeRenderer;
import com.example.project2.chart.render.scatter.CircleShapeRenderer;
import com.example.project2.chart.render.scatter.CrossShapeRenderer;
import com.example.project2.chart.render.scatter.IShapeRenderer;
import com.example.project2.chart.render.scatter.SquareShapeRenderer;
import com.example.project2.chart.render.scatter.TriangleShapeRenderer;
import com.example.project2.chart.render.scatter.XShapeRenderer;
import com.example.project2.chart.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ScatterDataSet extends LineScatterCandleRadarDataSet<Entry> implements IScatterDataSet {

    private float mShapeSize = 15f;

    protected IShapeRenderer mShapeRenderer = new SquareShapeRenderer();

    private float mScatterShapeHoleRadius = 0f;

    private int mScatterShapeHoleColor = ColorTemplate.COLOR_NONE;

    public ScatterDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public DataSet<Entry> copy() {

        List<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < mValues.size(); i++) {
            yVals.add(mValues.get(i).copy());
        }

        ScatterDataSet copied = new ScatterDataSet(yVals, getLabel());
        copied.mDrawValues = mDrawValues;
        copied.mValueColors = mValueColors;
        copied.mColors = mColors;
        copied.mShapeSize = mShapeSize;
        copied.mShapeRenderer = mShapeRenderer;
        copied.mScatterShapeHoleRadius = mScatterShapeHoleRadius;
        copied.mScatterShapeHoleColor = mScatterShapeHoleColor;
        copied.mHighlightLineWidth = mHighlightLineWidth;
        copied.mHighLightColor = mHighLightColor;
        copied.mHighlightDashPathEffect = mHighlightDashPathEffect;

        return copied;
    }

    public void setScatterShapeSize(float size) {
        mShapeSize = size;
    }

    @Override
    public float getScatterShapeSize() {
        return mShapeSize;
    }

    public void setScatterShape(ScatterChart.ScatterShape shape) {
        mShapeRenderer = getRendererForShape(shape);
    }

    public void setShapeRenderer(IShapeRenderer shapeRenderer) {
        mShapeRenderer = shapeRenderer;
    }

    @Override
    public IShapeRenderer getShapeRenderer() {
        return mShapeRenderer;
    }

    public void setScatterShapeHoleRadius(float holeRadius) {
        mScatterShapeHoleRadius = holeRadius;
    }

    @Override
    public float getScatterShapeHoleRadius() {
        return mScatterShapeHoleRadius;
    }

    public void setScatterShapeHoleColor(int holeColor) {
        mScatterShapeHoleColor = holeColor;
    }

    @Override
    public int getScatterShapeHoleColor() {
        return mScatterShapeHoleColor;
    }

    public static IShapeRenderer getRendererForShape(ScatterChart.ScatterShape shape) {

        switch (shape) {
            case SQUARE: return new SquareShapeRenderer();
            case CIRCLE: return new CircleShapeRenderer();
            case TRIANGLE: return new TriangleShapeRenderer();
            case CROSS: return new CrossShapeRenderer();
            case X: return new XShapeRenderer();
            case CHEVRON_UP: return new ChevronUpShapeRenderer();
            case CHEVRON_DOWN: return new ChevronDownShapeRenderer();
        }

        return null;
    }
}