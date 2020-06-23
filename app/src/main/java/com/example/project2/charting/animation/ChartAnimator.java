package com.example.project2.charting.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

public class ChartAnimator {
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener;
    public ChartAnimator() {

    }

    public ChartAnimator(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.animatorUpdateListener = animatorUpdateListener;
    }

    protected float phaseX;
    protected float phaseY;


    // METHODS FOR CUSTOM EASING

    public void animateXY(int durationMillisX, int durationMillisY, EasingFunction easingX,
                          EasingFunction easingY) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setInterpolator(easingY);
        animatorY.setDuration(durationMillisY);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(easingX);
        animatorX.setDuration(durationMillisX);

        // make sure only one animator produces update-callbacks (which then
        // call invalidate())
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(animatorUpdateListener);
        } else {
            animatorY.addUpdateListener(animatorUpdateListener);
        }

        animatorX.start();
        animatorY.start();
    }

    // Animates the rendering of the chart on the x-axis with the specified animation time
    public void animateX(int durationMillis, EasingFunction easing) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(easing);
        animatorX.setDuration(durationMillis);
        animatorX.addUpdateListener(animatorUpdateListener);
        animatorX.start();
    }

    // Animates the rendering of the chart on the y-axis with the specified animation time
    public void animateY(int durationMillis, EasingFunction easing) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setInterpolator(easing);
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(animatorUpdateListener);
        animatorY.start();
    }

    // METHODS FOR PREDEFINED EASING

    // Animates the drawing / rendering of the chart on both x- and y-axis with the specified animation time
    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingOption easingX,
                          Easing.EasingOption easingY) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setInterpolator(Easing.getEasingFunctionFromOption(easingY));
        animatorY.setDuration(durationMillisY);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(Easing.getEasingFunctionFromOption(easingX));
        animatorX.setDuration(durationMillisX);

        // make sure only one animator produces update-callbacks (which then call invalidate())
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(animatorUpdateListener);
        } else {
            animatorY.addUpdateListener(animatorUpdateListener);
        }

        animatorX.start();
        animatorY.start();
    }

    public void animateX(int durationMillis, Easing.EasingOption easing) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(Easing.getEasingFunctionFromOption(easing));
        animatorX.setDuration(durationMillis);
        animatorX.addUpdateListener(animatorUpdateListener);
        animatorX.start();
    }

    public void animateY(int durationMillis, Easing.EasingOption easing) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setInterpolator(Easing.getEasingFunctionFromOption(easing));
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(animatorUpdateListener);
        animatorY.start();
    }


    // METHODS FOR ANIMATION WITHOUT EASING

    public void animateXY(int durationMillisX, int durationMillisY) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillisY);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillisX);

        // make sure only one animator produces update-callbacks (which then call invalidate())
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(animatorUpdateListener);
        } else {
            animatorY.addUpdateListener(animatorUpdateListener);
        }

        animatorX.start();
        animatorY.start();
    }

    public void animateX(int durationMillis) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillis);
        animatorX.addUpdateListener(animatorUpdateListener);
        animatorX.start();
    }

    public void animateY(int durationMillis) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(animatorUpdateListener);
        animatorY.start();
    }

    public float getPhaseX() {
        return phaseX;
    }

    public float getPhaseY() {
        return phaseY;
    }

    public void setPhaseX(float phaseX) {
        this.phaseX = phaseX;
    }

    public void setPhaseY(float phaseY) {
        this.phaseY = phaseY;
    }
}
