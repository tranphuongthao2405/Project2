package com.example.project2.chart.render;

import com.example.project2.chart.utils.ViewPortHandler;

public abstract class Renderer {
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
