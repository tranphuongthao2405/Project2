package com.example.project2.chart.animation;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public interface EasingFunction extends TimeInterpolator {

    @Override
    float getInterpolation(float input);
}
