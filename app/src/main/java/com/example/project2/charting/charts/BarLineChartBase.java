package com.example.project2.charting.charts;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.project2.charting.components.XAxis;
import com.example.project2.charting.components.YAxis;
import com.example.project2.charting.data.BarLineScatterCandleBubbleData;
import com.example.project2.charting.data.Entry;
import com.example.project2.charting.highlight.ChartHighlighter;
import com.example.project2.charting.highlight.Highlight;
import com.example.project2.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.example.project2.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.example.project2.charting.jobs.AnimatedMoveViewJob;
import com.example.project2.charting.jobs.AnimatedZoomJob;
import com.example.project2.charting.jobs.MoveViewJob;
import com.example.project2.charting.jobs.ZoomJob;
import com.example.project2.charting.listener.BarLineChartTouchListener;
import com.example.project2.charting.listener.OnDrawListener;
import com.example.project2.charting.render.XAxisRenderer;
import com.example.project2.charting.render.YAxisRenderer;
import com.example.project2.charting.utils.MPPointD;
import com.example.project2.charting.utils.MPPointF;
import com.example.project2.charting.utils.Transformer;
import com.example.project2.charting.utils.Utils;

@SuppressLint("RtlHardcoded")
public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends
        IBarLineScatterCandleBubbleDataSet<? extends Entry>>>
        extends Chart<T> implements BarLineScatterCandleBubbleDataProvider {

    protected int mMaxVisibleCount = 100;

    protected boolean mAutoScaleMinMaxEnabled = false;

    protected boolean mPinchZoomEnabled = false;

    protected boolean mDoubleTapToZoomEnabled = true;

    protected boolean mHighlightPerDragEnabled = true;

    private boolean mDragEnabled = true;

    private boolean mScaleXEnabled = true;
    private boolean mScaleYEnabled = true;

    protected Paint mGridBackgroundPaint;

    protected Paint mBorderPaint;

    protected boolean mDrawGridBackground = false;

    protected boolean mDrawBorders = false;

    protected boolean mClipValuesToContent = false;

    protected float mMinOffset = 15.f;

    protected boolean mKeepPositionOnRotation = false;

    protected OnDrawListener mDrawListener;

    protected YAxis mAxisLeft;

    protected YAxis mAxisRight;

    protected YAxisRenderer mAxisRendererLeft;
    protected YAxisRenderer mAxisRendererRight;

    protected Transformer mLeftAxisTransformer;
    protected Transformer mRightAxisTransformer;

    protected XAxisRenderer mXAxisRenderer;

    // private Approximator mApproximator;

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();

        mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);
        mAxisRight = new YAxis(YAxis.AxisDependency.RIGHT);

        mLeftAxisTransformer = new Transformer(mViewPortHandler);
        mRightAxisTransformer = new Transformer(mViewPortHandler);

        mAxisRendererLeft = new YAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        mAxisRendererRight = new YAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer);

        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);

        setHighlighter(new ChartHighlighter(this));

        mChartTouchListener = new BarLineChartTouchListener(this, mViewPortHandler.getMatrixTouch(), 3f);

        mGridBackgroundPaint = new Paint();
        mGridBackgroundPaint.setStyle(Style.FILL);
        // mGridBackgroundPaint.setColor(Color.WHITE);
        mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240)); // light
        // grey

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Style.STROKE);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));
    }

    // for performance tracking
    private long totalTime = 0;
    private long drawCycles = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null)
            return;

        long starttime = System.currentTimeMillis();

        // execute all drawing commands
        drawGridBackground(canvas);

        if (mAxisLeft.isEnabled())
            mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, mAxisLeft.isInverted());
        if (mAxisRight.isEnabled())
            mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum, mAxisRight.isInverted());
        if (mXAxis.isEnabled())
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        mXAxisRenderer.renderAxisLine(canvas);
        mAxisRendererLeft.renderAxisLine(canvas);
        mAxisRendererRight.renderAxisLine(canvas);

        if (mAutoScaleMinMaxEnabled) {
            autoScale();
        }

        mXAxisRenderer.renderGridLines(canvas);
        mAxisRendererLeft.renderGridLines(canvas);
        mAxisRendererRight.renderGridLines(canvas);

        if (mXAxis.isDrawLimitLinesBehindDataEnabled())
            mXAxisRenderer.renderLimitLines(canvas);

        if (mAxisLeft.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererLeft.renderLimitLines(canvas);

        if (mAxisRight.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererRight.renderLimitLines(canvas);

        // make sure the data cannot be drawn outside the content-rect
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mViewPortHandler.getContentRect());

        mRenderer.drawData(canvas);

        // if highlighting is enabled
        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);

        mRenderer.drawExtras(canvas);

        if (!mXAxis.isDrawLimitLinesBehindDataEnabled())
            mXAxisRenderer.renderLimitLines(canvas);

        if (!mAxisLeft.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererLeft.renderLimitLines(canvas);

        if (!mAxisRight.isDrawLimitLinesBehindDataEnabled())
            mAxisRendererRight.renderLimitLines(canvas);

        mXAxisRenderer.renderAxisLabels(canvas);
        mAxisRendererLeft.renderAxisLabels(canvas);
        mAxisRendererRight.renderAxisLabels(canvas);

        if (isClipValuesToContentEnabled()) {
            clipRestoreCount = canvas.save();
            canvas.clipRect(mViewPortHandler.getContentRect());

            mRenderer.drawValues(canvas);

            canvas.restoreToCount(clipRestoreCount);
        } else {
            mRenderer.drawValues(canvas);
        }

        mLegendRenderer.renderLegend(canvas);

        drawDescription(canvas);

        drawMarkers(canvas);

        if (mLogEnabled) {
            long drawtime = (System.currentTimeMillis() - starttime);
            totalTime += drawtime;
            drawCycles += 1;
            long average = totalTime / drawCycles;
            Log.i(LOG_TAG, "Drawtime: " + drawtime + " ms, average: " + average + " ms, cycles: "
                    + drawCycles);
        }
    }

    public void resetTracking() {
        totalTime = 0;
        drawCycles = 0;
    }

    protected void prepareValuePxMatrix() {

        if (mLogEnabled)
            Log.i(LOG_TAG, "Preparing Value-Px Matrix, xmin: " + mXAxis.mAxisMinimum + ", xmax: "
                    + mXAxis.mAxisMaximum + ", xdelta: " + mXAxis.mAxisRange);

        mRightAxisTransformer.prepareMatrixValuePx(mXAxis.mAxisMinimum,
                mXAxis.mAxisRange,
                mAxisRight.mAxisRange,
                mAxisRight.mAxisMinimum);
        mLeftAxisTransformer.prepareMatrixValuePx(mXAxis.mAxisMinimum,
                mXAxis.mAxisRange,
                mAxisLeft.mAxisRange,
                mAxisLeft.mAxisMinimum);
    }

    protected void prepareOffsetMatrix() {

        mRightAxisTransformer.prepareMatrixOffset(mAxisRight.isInverted());
        mLeftAxisTransformer.prepareMatrixOffset(mAxisLeft.isInverted());
    }

    @Override
    public void notifyDataSetChanged() {

        if (mData == null) {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing... DATA NOT SET.");
            return;
        } else {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing...");
        }

        if (mRenderer != null)
            mRenderer.initBuffers();

        calcMinMax();

        mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, mAxisLeft.isInverted());
        mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum, mAxisRight.isInverted());
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        if (mLegend != null)
            mLegendRenderer.computeLegend(mData);

        calculateOffsets();
    }

    protected void autoScale() {

        final float fromX = getLowestVisibleX();
        final float toX = getHighestVisibleX();

        mData.calcMinMaxY(fromX, toX);

        mXAxis.calculate(mData.getXMin(), mData.getXMax());

        // calculate axis range (min / max) according to provided data
        mAxisLeft.calculate(mData.getYMin(YAxis.AxisDependency.LEFT), mData.getYMax(YAxis.AxisDependency.LEFT));
        mAxisRight.calculate(mData.getYMin(YAxis.AxisDependency.RIGHT), mData.getYMax(YAxis.AxisDependency.RIGHT));

        calculateOffsets();
    }

    @Override
    protected void calcMinMax() {

        mXAxis.calculate(mData.getXMin(), mData.getXMax());

        // calculate axis range (min / max) according to provided data
        mAxisLeft.calculate(mData.getYMin(YAxis.AxisDependency.LEFT), mData.getYMax(YAxis.AxisDependency.LEFT));
        mAxisRight.calculate(mData.getYMin(YAxis.AxisDependency.RIGHT), mData.getYMax(YAxis.AxisDependency.RIGHT));
    }

    protected void calculateLegendOffsets(RectF offsets) {

        offsets.left = 0.f;
        offsets.right = 0.f;
        offsets.top = 0.f;
        offsets.bottom = 0.f;

        // setup offsets for legend
        if (mLegend != null && mLegend.isEnabled() && !mLegend.isDrawInsideEnabled()) {
            switch (mLegend.getOrientation()) {
                case VERTICAL:

                    switch (mLegend.getHorizontalAlignment()) {
                        case LEFT:
                            offsets.left += Math.min(mLegend.mNeededWidth,
                                    mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent())
                                    + mLegend.getXOffset();
                            break;

                        case RIGHT:
                            offsets.right += Math.min(mLegend.mNeededWidth,
                                    mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent())
                                    + mLegend.getXOffset();
                            break;

                        case CENTER:

                            switch (mLegend.getVerticalAlignment()) {
                                case TOP:
                                    offsets.top += Math.min(mLegend.mNeededHeight,
                                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent())
                                            + mLegend.getYOffset();
                                    break;

                                case BOTTOM:
                                    offsets.bottom += Math.min(mLegend.mNeededHeight,
                                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent())
                                            + mLegend.getYOffset();
                                    break;

                                default:
                                    break;
                            }
                    }

                    break;

                case HORIZONTAL:

                    switch (mLegend.getVerticalAlignment()) {
                        case TOP:
                            offsets.top += Math.min(mLegend.mNeededHeight,
                                    mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent())
                                    + mLegend.getYOffset();

                            if (getXAxis().isEnabled() && getXAxis().isDrawLabelsEnabled())
                                offsets.top += getXAxis().mLabelRotatedHeight;
                            break;

                        case BOTTOM:
                            offsets.bottom += Math.min(mLegend.mNeededHeight,
                                    mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent())
                                    + mLegend.getYOffset();

                            if (getXAxis().isEnabled() && getXAxis().isDrawLabelsEnabled())
                                offsets.bottom += getXAxis().mLabelRotatedHeight;
                            break;

                        default:
                            break;
                    }
                    break;
            }
        }
    }

    private RectF mOffsetsBuffer = new RectF();

    @Override
    public void calculateOffsets() {

        if (!mCustomViewPortEnabled) {

            float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;

            calculateLegendOffsets(mOffsetsBuffer);

            offsetLeft += mOffsetsBuffer.left;
            offsetTop += mOffsetsBuffer.top;
            offsetRight += mOffsetsBuffer.right;
            offsetBottom += mOffsetsBuffer.bottom;

            // offsets for y-labels
            if (mAxisLeft.needsOffset()) {
                offsetLeft += mAxisLeft.getRequiredWidthSpace(mAxisRendererLeft
                        .getPaintAxisLabels());
            }

            if (mAxisRight.needsOffset()) {
                offsetRight += mAxisRight.getRequiredWidthSpace(mAxisRendererRight
                        .getPaintAxisLabels());
            }

            if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {

                float xlabelheight = mXAxis.mLabelRotatedHeight + mXAxis.getYOffset();

                // offsets for x-labels
                if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {

                    offsetBottom += xlabelheight;

                } else if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {

                    offsetTop += xlabelheight;

                } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {

                    offsetBottom += xlabelheight;
                    offsetTop += xlabelheight;
                }
            }

            offsetTop += getExtraTopOffset();
            offsetRight += getExtraRightOffset();
            offsetBottom += getExtraBottomOffset();
            offsetLeft += getExtraLeftOffset();

            float minOffset = Utils.convertDpToPixel(mMinOffset);

            mViewPortHandler.restrainViewPort(
                    Math.max(minOffset, offsetLeft),
                    Math.max(minOffset, offsetTop),
                    Math.max(minOffset, offsetRight),
                    Math.max(minOffset, offsetBottom));

            if (mLogEnabled) {
                Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                        + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
                Log.i(LOG_TAG, "Content: " + mViewPortHandler.getContentRect().toString());
            }
        }

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    /**
     * draws the grid background
     */
    protected void drawGridBackground(Canvas c) {

        if (mDrawGridBackground) {

            // draw the grid background
            c.drawRect(mViewPortHandler.getContentRect(), mGridBackgroundPaint);
        }

        if (mDrawBorders) {
            c.drawRect(mViewPortHandler.getContentRect(), mBorderPaint);
        }
    }

    public Transformer getTransformer(YAxis.AxisDependency which) {
        if (which == YAxis.AxisDependency.LEFT)
            return mLeftAxisTransformer;
        else
            return mRightAxisTransformer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (mChartTouchListener == null || mData == null)
            return false;

        // check if touch gestures are enabled
        if (!mTouchEnabled)
            return false;
        else
            return mChartTouchListener.onTouch(this, event);
    }

    @Override
    public void computeScroll() {

        if (mChartTouchListener instanceof BarLineChartTouchListener)
            ((BarLineChartTouchListener) mChartTouchListener).computeScroll();
    }


    protected Matrix mZoomMatrixBuffer = new Matrix();

    public void zoomIn() {

        MPPointF center = mViewPortHandler.getContentCenter();

        mViewPortHandler.zoomIn(center.x, -center.y, mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        MPPointF.recycleInstance(center);

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    public void zoomOut() {

        MPPointF center = mViewPortHandler.getContentCenter();

        mViewPortHandler.zoomOut(center.x, -center.y, mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        MPPointF.recycleInstance(center);

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    public void resetZoom() {

        mViewPortHandler.resetZoom(mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    public void zoom(float scaleX, float scaleY, float x, float y) {

        mViewPortHandler.zoom(scaleX, scaleY, x, -y, mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    public void zoom(float scaleX, float scaleY, float xValue, float yValue, YAxis.AxisDependency axis) {

        Runnable job = ZoomJob.getInstance(mViewPortHandler, scaleX, scaleY, xValue, yValue, getTransformer(axis), axis, this);
        addViewportJob(job);
    }

    public void zoomToCenter(float scaleX, float scaleY) {

        MPPointF center = getCenterOffsets();

        Matrix save = mZoomMatrixBuffer;
        mViewPortHandler.zoom(scaleX, scaleY, center.x, -center.y, save);
        mViewPortHandler.refresh(save, this, false);
    }

    @TargetApi(11)
    public void zoomAndCenterAnimated(float scaleX, float scaleY, float xValue, float yValue, YAxis.AxisDependency axis,
                                      long duration) {

        if (android.os.Build.VERSION.SDK_INT >= 11) {

            MPPointD origin = getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), axis);

            Runnable job = AnimatedZoomJob.getInstance(mViewPortHandler, this, getTransformer(axis), getAxis(axis), mXAxis
                            .mAxisRange, scaleX, scaleY, mViewPortHandler.getScaleX(), mViewPortHandler.getScaleY(),
                    xValue, yValue, (float) origin.x, (float) origin.y, duration);
            addViewportJob(job);

            MPPointD.recycleInstance(origin);

        } else {
            Log.e(LOG_TAG, "Unable to execute zoomAndCenterAnimated(...) on API level < 11");
        }
    }

    protected Matrix mFitScreenMatrixBuffer = new Matrix();

    public void fitScreen() {
        Matrix save = mFitScreenMatrixBuffer;
        mViewPortHandler.fitScreen(save);
        mViewPortHandler.refresh(save, this, false);

        calculateOffsets();
        postInvalidate();
    }

    public void setScaleMinima(float scaleX, float scaleY) {
        mViewPortHandler.setMinimumScaleX(scaleX);
        mViewPortHandler.setMinimumScaleY(scaleY);
    }

    public void setVisibleXRangeMaximum(float maxXRange) {
        float xScale = mXAxis.mAxisRange / (maxXRange);
        mViewPortHandler.setMinimumScaleX(xScale);
    }

    public void setVisibleXRangeMinimum(float minXRange) {
        float xScale = mXAxis.mAxisRange / (minXRange);
        mViewPortHandler.setMaximumScaleX(xScale);
    }

    public void setVisibleXRange(float minXRange, float maxXRange) {
        float minScale = mXAxis.mAxisRange / minXRange;
        float maxScale = mXAxis.mAxisRange / maxXRange;
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
    }

    public void setVisibleYRangeMaximum(float maxYRange, YAxis.AxisDependency axis) {
        float yScale = getAxisRange(axis) / maxYRange;
        mViewPortHandler.setMinimumScaleY(yScale);
    }

    public void setVisibleYRangeMinimum(float minYRange, YAxis.AxisDependency axis) {
        float yScale = getAxisRange(axis) / minYRange;
        mViewPortHandler.setMaximumScaleY(yScale);
    }

    public void setVisibleYRange(float minYRange, float maxYRange, YAxis.AxisDependency axis) {
        float minScale = getAxisRange(axis) / minYRange;
        float maxScale = getAxisRange(axis) / maxYRange;
        mViewPortHandler.setMinMaxScaleY(minScale, maxScale);
    }

    public void moveViewToX(float xValue) {

        Runnable job = MoveViewJob.getInstance(mViewPortHandler, xValue, 0f,
                getTransformer(YAxis.AxisDependency.LEFT), this);

        addViewportJob(job);
    }

    public void moveViewTo(float xValue, float yValue, YAxis.AxisDependency axis) {

        float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();

        Runnable job = MoveViewJob.getInstance(mViewPortHandler, xValue, yValue + yInView / 2f,
                getTransformer(axis), this);

        addViewportJob(job);
    }

    @TargetApi(11)
    public void moveViewToAnimated(float xValue, float yValue, YAxis.AxisDependency axis, long duration) {

        if (android.os.Build.VERSION.SDK_INT >= 11) {

            MPPointD bounds = getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), axis);

            float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();

            Runnable job = AnimatedMoveViewJob.getInstance(mViewPortHandler, xValue, yValue + yInView / 2f,
                    getTransformer(axis), this, (float) bounds.x, (float) bounds.y, duration);

            addViewportJob(job);

            MPPointD.recycleInstance(bounds);
        } else {
            Log.e(LOG_TAG, "Unable to execute moveViewToAnimated(...) on API level < 11");
        }
    }

    public void centerViewToY(float yValue, YAxis.AxisDependency axis) {

        float valsInView = getAxisRange(axis) / mViewPortHandler.getScaleY();

        Runnable job = MoveViewJob.getInstance(mViewPortHandler, 0f, yValue + valsInView / 2f,
                getTransformer(axis), this);

        addViewportJob(job);
    }

    public void centerViewTo(float xValue, float yValue, YAxis.AxisDependency axis) {

        float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();
        float xInView = getXAxis().mAxisRange / mViewPortHandler.getScaleX();

        Runnable job = MoveViewJob.getInstance(mViewPortHandler,
                xValue - xInView / 2f, yValue + yInView / 2f,
                getTransformer(axis), this);

        addViewportJob(job);
    }

    @TargetApi(11)
    public void centerViewToAnimated(float xValue, float yValue, YAxis.AxisDependency axis, long duration) {

        if (android.os.Build.VERSION.SDK_INT >= 11) {

            MPPointD bounds = getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), axis);

            float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();
            float xInView = getXAxis().mAxisRange / mViewPortHandler.getScaleX();

            Runnable job = AnimatedMoveViewJob.getInstance(mViewPortHandler,
                    xValue - xInView / 2f, yValue + yInView / 2f,
                    getTransformer(axis), this, (float) bounds.x, (float) bounds.y, duration);

            addViewportJob(job);

            MPPointD.recycleInstance(bounds);
        } else {
            Log.e(LOG_TAG, "Unable to execute centerViewToAnimated(...) on API level < 11");
        }
    }

    private boolean mCustomViewPortEnabled = false;

    public void setViewPortOffsets(final float left, final float top,
                                   final float right, final float bottom) {

        mCustomViewPortEnabled = true;
        post(new Runnable() {

            @Override
            public void run() {

                mViewPortHandler.restrainViewPort(left, top, right, bottom);
                prepareOffsetMatrix();
                prepareValuePxMatrix();
            }
        });
    }

    public void resetViewPortOffsets() {
        mCustomViewPortEnabled = false;
        calculateOffsets();
    }

    protected float getAxisRange(YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT)
            return mAxisLeft.mAxisRange;
        else
            return mAxisRight.mAxisRange;
    }

    public void setOnDrawListener(OnDrawListener drawListener) {
        this.mDrawListener = drawListener;
    }

    public OnDrawListener getDrawListener() {
        return mDrawListener;
    }

    protected float[] mGetPositionBuffer = new float[2];


    public MPPointF getPosition(Entry e, YAxis.AxisDependency axis) {

        if (e == null)
            return null;

        mGetPositionBuffer[0] = e.getX();
        mGetPositionBuffer[1] = e.getY();

        getTransformer(axis).pointValuesToPixel(mGetPositionBuffer);

        return MPPointF.getInstance(mGetPositionBuffer[0], mGetPositionBuffer[1]);
    }

    public void setMaxVisibleValueCount(int count) {
        this.mMaxVisibleCount = count;
    }

    public int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }

    public void setHighlightPerDragEnabled(boolean enabled) {
        mHighlightPerDragEnabled = enabled;
    }

    public boolean isHighlightPerDragEnabled() {
        return mHighlightPerDragEnabled;
    }

    public void setGridBackgroundColor(int color) {
        mGridBackgroundPaint.setColor(color);
    }

    public void setDragEnabled(boolean enabled) {
        this.mDragEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return mDragEnabled;
    }

    public void setScaleEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }

    public void setScaleXEnabled(boolean enabled) {
        mScaleXEnabled = enabled;
    }

    public void setScaleYEnabled(boolean enabled) {
        mScaleYEnabled = enabled;
    }

    public boolean isScaleXEnabled() {
        return mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return mScaleYEnabled;
    }

    public void setDoubleTapToZoomEnabled(boolean enabled) {
        mDoubleTapToZoomEnabled = enabled;
    }

    public boolean isDoubleTapToZoomEnabled() {
        return mDoubleTapToZoomEnabled;
    }

    public void setDrawGridBackground(boolean enabled) {
        mDrawGridBackground = enabled;
    }

    public void setDrawBorders(boolean enabled) {
        mDrawBorders = enabled;
    }

    public boolean isDrawBordersEnabled() {
        return mDrawBorders;
    }

    public void setClipValuesToContent(boolean enabled) {
        mClipValuesToContent = enabled;
    }

    public boolean isClipValuesToContentEnabled() {
        return mClipValuesToContent;
    }

    public void setBorderWidth(float width) {
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(width));
    }

    public void setBorderColor(int color) {
        mBorderPaint.setColor(color);
    }

    public float getMinOffset() {
        return mMinOffset;
    }

    public void setMinOffset(float minOffset) {
        mMinOffset = minOffset;
    }

    public boolean isKeepPositionOnRotation() {
        return mKeepPositionOnRotation;
    }

    public void setKeepPositionOnRotation(boolean keepPositionOnRotation) {
        mKeepPositionOnRotation = keepPositionOnRotation;
    }

    public MPPointD getValuesByTouchPoint(float x, float y, YAxis.AxisDependency axis) {
        MPPointD result = MPPointD.getInstance(0, 0);
        getValuesByTouchPoint(x, y, axis, result);
        return result;
    }

    public void getValuesByTouchPoint(float x, float y, YAxis.AxisDependency axis, MPPointD outputPoint) {
        getTransformer(axis).getValuesByTouchPoint(x, y, outputPoint);
    }

    public MPPointD getPixelForValues(float x, float y, YAxis.AxisDependency axis) {
        return getTransformer(axis).getPixelForValues(x, y);
    }

    public Entry getEntryByTouchPoint(float x, float y) {
        Highlight h = getHighlightByTouchPoint(x, y);
        if (h != null) {
            return mData.getEntryForHighlight(h);
        }
        return null;
    }

    public IBarLineScatterCandleBubbleDataSet getDataSetByTouchPoint(float x, float y) {
        Highlight h = getHighlightByTouchPoint(x, y);
        if (h != null) {
            return mData.getDataSetByIndex(h.getDataSetIndex());
        }
        return null;
    }

    protected MPPointD posForGetLowestVisibleX = MPPointD.getInstance(0, 0);

    @Override
    public float getLowestVisibleX() {
        getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), posForGetLowestVisibleX);
        float result = (float) Math.max(mXAxis.mAxisMinimum, posForGetLowestVisibleX.x);
        return result;
    }

    protected MPPointD posForGetHighestVisibleX = MPPointD.getInstance(0, 0);

    @Override
    public float getHighestVisibleX() {
        getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(mViewPortHandler.contentRight(),
                mViewPortHandler.contentBottom(), posForGetHighestVisibleX);
        float result = (float) Math.min(mXAxis.mAxisMaximum, posForGetHighestVisibleX.x);
        return result;
    }

    public float getVisibleXRange() {
        return Math.abs(getHighestVisibleX() - getLowestVisibleX());
    }

    public float getScaleX() {
        if (mViewPortHandler == null)
            return 1f;
        else
            return mViewPortHandler.getScaleX();
    }

    public float getScaleY() {
        if (mViewPortHandler == null)
            return 1f;
        else
            return mViewPortHandler.getScaleY();
    }

    public boolean isFullyZoomedOut() {
        return mViewPortHandler.isFullyZoomedOut();
    }

    public YAxis getAxisLeft() {
        return mAxisLeft;
    }

    public YAxis getAxisRight() {
        return mAxisRight;
    }

    public YAxis getAxis(YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT)
            return mAxisLeft;
        else
            return mAxisRight;
    }

    @Override
    public boolean isInverted(YAxis.AxisDependency axis) {
        return getAxis(axis).isInverted();
    }

    public void setPinchZoom(boolean enabled) {
        mPinchZoomEnabled = enabled;
    }

    public boolean isPinchZoomEnabled() {
        return mPinchZoomEnabled;
    }

    public void setDragOffsetX(float offset) {
        mViewPortHandler.setDragOffsetX(offset);
    }

    public void setDragOffsetY(float offset) {
        mViewPortHandler.setDragOffsetY(offset);
    }

    public boolean hasNoDragOffset() {
        return mViewPortHandler.hasNoDragOffset();
    }

    public XAxisRenderer getRendererXAxis() {
        return mXAxisRenderer;
    }

    public void setXAxisRenderer(XAxisRenderer xAxisRenderer) {
        mXAxisRenderer = xAxisRenderer;
    }

    public YAxisRenderer getRendererLeftYAxis() {
        return mAxisRendererLeft;
    }

    public void setRendererLeftYAxis(YAxisRenderer rendererLeftYAxis) {
        mAxisRendererLeft = rendererLeftYAxis;
    }

    public YAxisRenderer getRendererRightYAxis() {
        return mAxisRendererRight;
    }

    public void setRendererRightYAxis(YAxisRenderer rendererRightYAxis) {
        mAxisRendererRight = rendererRightYAxis;
    }

    @Override
    public float getYChartMax() {
        return Math.max(mAxisLeft.mAxisMaximum, mAxisRight.mAxisMaximum);
    }

    @Override
    public float getYChartMin() {
        return Math.min(mAxisLeft.mAxisMinimum, mAxisRight.mAxisMinimum);
    }

    public boolean isAnyAxisInverted() {
        if (mAxisLeft.isInverted())
            return true;
        if (mAxisRight.isInverted())
            return true;
        return false;
    }

    public void setAutoScaleMinMaxEnabled(boolean enabled) {
        mAutoScaleMinMaxEnabled = enabled;
    }

    public boolean isAutoScaleMinMaxEnabled() {
        return mAutoScaleMinMaxEnabled;
    }

    @Override
    public void setPaint(Paint p, int which) {
        super.setPaint(p, which);

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                mGridBackgroundPaint = p;
                break;
        }
    }

    @Override
    public Paint getPaint(int which) {
        Paint p = super.getPaint(which);
        if (p != null)
            return p;

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                return mGridBackgroundPaint;
        }

        return null;
    }

    protected float[] mOnSizeChangedBuffer = new float[2];

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // Saving current position of chart.
        mOnSizeChangedBuffer[0] = mOnSizeChangedBuffer[1] = 0;

        if (mKeepPositionOnRotation) {
            mOnSizeChangedBuffer[0] = mViewPortHandler.contentLeft();
            mOnSizeChangedBuffer[1] = mViewPortHandler.contentTop();
            getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(mOnSizeChangedBuffer);
        }

        //Superclass transforms chart.
        super.onSizeChanged(w, h, oldw, oldh);

        if (mKeepPositionOnRotation) {

            //Restoring old position of chart.
            getTransformer(YAxis.AxisDependency.LEFT).pointValuesToPixel(mOnSizeChangedBuffer);
            mViewPortHandler.centerViewPort(mOnSizeChangedBuffer, this);
        } else {
            mViewPortHandler.refresh(mViewPortHandler.getMatrixTouch(), this, true);
        }
    }
}
