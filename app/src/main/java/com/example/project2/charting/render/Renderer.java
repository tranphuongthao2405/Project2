package com.example.project2.charting.render;

import com.example.project2.charting.utils.ViewPortHandler;

public abstract class Renderer {
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
