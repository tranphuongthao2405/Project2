package com.example.project2.charting.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.example.project2.charting.components.AxisBase;
import com.example.project2.charting.utils.MPPointD;
import com.example.project2.charting.utils.Transformer;
import com.example.project2.charting.utils.Utils;
import com.example.project2.charting.utils.ViewPortHandler;

public abstract class AxisRenderer extends Renderer {

    protected AxisBase mAxis;

    protected Transformer mTrans;

    protected Paint mGridPaint;

    protected Paint mAxisLabelPaint;

    protected Paint mAxisLinePaint;

    protected Paint mLimitLinePaint;

    public AxisRenderer(ViewPortHandler viewPortHandler, Transformer trans, AxisBase axis) {
        super(viewPortHandler);

        this.mTrans = trans;
        this.mAxis = axis;

        if(mViewPortHandler != null) {

            mAxisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mGridPaint = new Paint();
            mGridPaint.setColor(Color.GRAY);
            mGridPaint.setStrokeWidth(1f);
            mGridPaint.setStyle(Style.STROKE);
            mGridPaint.setAlpha(90);

            mAxisLinePaint = new Paint();
            mAxisLinePaint.setColor(Color.BLACK);
            mAxisLinePaint.setStrokeWidth(1f);
            mAxisLinePaint.setStyle(Style.STROKE);

            mLimitLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLimitLinePaint.setStyle(Style.STROKE);
        }
    }

    public Paint getPaintAxisLabels() {
        return mAxisLabelPaint;
    }

    public Paint getPaintGrid() {
        return mGridPaint;
    }

    public Paint getPaintAxisLine() {
        return mAxisLinePaint;
    }

    public Transformer getTransformer() {
        return mTrans;
    }

    public void computeAxis(float min, float max, boolean inverted) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        if (mViewPortHandler != null && mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {

            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom());

            if (!inverted) {

                min = (float) p2.y;
                max = (float) p1.y;
            } else {

                min = (float) p1.y;
                max = (float) p2.y;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        computeAxisValues(min, max);
    }

    protected void computeAxisValues(float min, float max) {

        float yMin = min;
        float yMax = max;

        int labelCount = mAxis.getLabelCount();
        double range = Math.abs(yMax - yMin);

        if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
            mAxis.mEntries = new float[]{};
            mAxis.mCenteredEntries = new float[]{};
            mAxis.mEntryCount = 0;
            return;
        }

        // Find out how much spacing (in y value space) between axis values
        double rawInterval = range / labelCount;
        double interval = Utils.roundToNextSignificant(rawInterval);

        // If granularity is enabled, then do not allow the interval to go below specified granularity.
        // This is used to avoid repeated values when rounding values for display.
        if (mAxis.isGranularityEnabled())
            interval = interval < mAxis.getGranularity() ? mAxis.getGranularity() : interval;

        // Normalize interval
        double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10, (int) Math.log10(interval)));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or
            // 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        int n = mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;

        // force label count
        if (mAxis.isForceLabelsEnabled()) {

            interval = (float) range / (float) (labelCount - 1);
            mAxis.mEntryCount = labelCount;

            if (mAxis.mEntries.length < labelCount) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[labelCount];
            }

            float v = min;

            for (int i = 0; i < labelCount; i++) {
                mAxis.mEntries[i] = v;
                v += interval;
            }

            n = labelCount;

            // no forced count
        } else {

            double first = interval == 0.0 ? 0.0 : Math.ceil(yMin / interval) * interval;
            if(mAxis.isCenterAxisLabelsEnabled()) {
                first -= interval;
            }

            double last = interval == 0.0 ? 0.0 : Utils.nextUp(Math.floor(yMax / interval) * interval);

            double f;
            int i;

            if (interval != 0.0) {
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            }

            mAxis.mEntryCount = n;

            if (mAxis.mEntries.length < n) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[n];
            }

            for (f = first, i = 0; i < n; f += interval, ++i) {

                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0;

                mAxis.mEntries[i] = (float) f;
            }
        }

        // set decimals
        if (interval < 1) {
            mAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mAxis.mDecimals = 0;
        }

        if (mAxis.isCenterAxisLabelsEnabled()) {

            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }

            float offset = (float)interval / 2f;

            for (int i = 0; i < n; i++) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset;
            }
        }
    }

    public abstract void renderAxisLabels(Canvas c);

    public abstract void renderGridLines(Canvas c);

    public abstract void renderAxisLine(Canvas c);

    public abstract void renderLimitLines(Canvas c);
}