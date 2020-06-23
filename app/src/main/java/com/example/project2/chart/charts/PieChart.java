package com.example.project2.chart.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.project2.chart.components.XAxis;
import com.example.project2.chart.data.PieData;
import com.example.project2.chart.highlight.Highlight;
import com.example.project2.chart.highlight.PieHighlighter;
import com.example.project2.chart.interfaces.datasets.IPieDataSet;
import com.example.project2.chart.render.PieChartRenderer;
import com.example.project2.chart.utils.MPPointF;
import com.example.project2.chart.utils.Utils;

import java.util.List;

public class PieChart extends PieRadarChartBase<PieData> {

    private RectF mCircleBox = new RectF();

    private boolean mDrawEntryLabels = true;

    private float[] mDrawAngles = new float[1];

    private float[] mAbsoluteAngles = new float[1];

    private boolean mDrawHole = true;

    private boolean mDrawSlicesUnderHole = false;

    private boolean mUsePercentValues = false;

    private boolean mDrawRoundedSlices = false;

    private CharSequence mCenterText = "";

    private MPPointF mCenterTextOffset = MPPointF.getInstance(0, 0);

    private float mHoleRadiusPercent = 50f;

    protected float mTransparentCircleRadiusPercent = 55f;

    private boolean mDrawCenterText = true;

    private float mCenterTextRadiusPercent = 100.f;

    protected float mMaxAngle = 360f;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new PieChartRenderer(this, mAnimator, mViewPortHandler);
        mXAxis = null;

        mHighlighter = new PieHighlighter(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null)
            return;

        mRenderer.drawData(canvas);

        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        mRenderer.drawExtras(canvas);

        mRenderer.drawValues(canvas);

        mLegendRenderer.renderLegend(canvas);

        drawDescription(canvas);

        drawMarkers(canvas);
    }

    @Override
    public void calculateOffsets() {
        super.calculateOffsets();

        // prevent nullpointer when no data set
        if (mData == null)
            return;

        float diameter = getDiameter();
        float radius = diameter / 2f;

        MPPointF c = getCenterOffsets();

        float shift = mData.getDataSet().getSelectionShift();

        // create the circle box that will contain the pie-chart (the bounds of
        // the pie-chart)
        mCircleBox.set(c.x - radius + shift,
                c.y - radius + shift,
                c.x + radius - shift,
                c.y + radius - shift);

        MPPointF.recycleInstance(c);
    }

    @Override
    protected void calcMinMax() {
        calcAngles();
    }

    @Override
    protected float[] getMarkerPosition(Highlight highlight) {

        MPPointF center = getCenterCircleBox();
        float r = getRadius();

        float off = r / 10f * 3.6f;

        if (isDrawHoleEnabled()) {
            off = (r - (r / 100f * getHoleRadius())) / 2f;
        }

        r -= off; // offset to keep things inside the chart

        float rotationAngle = getRotationAngle();

        int entryIndex = (int) highlight.getX();

        // offset needed to center the drawn text in the slice
        float offset = mDrawAngles[entryIndex] / 2;

        // calculate the text position
        float x = (float) (r
                * Math.cos(Math.toRadians((rotationAngle + mAbsoluteAngles[entryIndex] - offset)
                * mAnimator.getPhaseY())) + center.x);
        float y = (float) (r
                * Math.sin(Math.toRadians((rotationAngle + mAbsoluteAngles[entryIndex] - offset)
                * mAnimator.getPhaseY())) + center.y);

        MPPointF.recycleInstance(center);
        return new float[]{x, y};
    }

    private void calcAngles() {

        int entryCount = mData.getEntryCount();

        if (mDrawAngles.length != entryCount) {
            mDrawAngles = new float[entryCount];
        } else {
            for (int i = 0; i < entryCount; i++) {
                mDrawAngles[i] = 0;
            }
        }
        if (mAbsoluteAngles.length != entryCount) {
            mAbsoluteAngles = new float[entryCount];
        } else {
            for (int i = 0; i < entryCount; i++) {
                mAbsoluteAngles[i] = 0;
            }
        }

        float yValueSum = mData.getYValueSum();

        List<IPieDataSet> dataSets = mData.getDataSets();

        int cnt = 0;

        for (int i = 0; i < mData.getDataSetCount(); i++) {

            IPieDataSet set = dataSets.get(i);

            for (int j = 0; j < set.getEntryCount(); j++) {

                mDrawAngles[cnt] = calcAngle(Math.abs(set.getEntryForIndex(j).getY()), yValueSum);

                if (cnt == 0) {
                    mAbsoluteAngles[cnt] = mDrawAngles[cnt];
                } else {
                    mAbsoluteAngles[cnt] = mAbsoluteAngles[cnt - 1] + mDrawAngles[cnt];
                }

                cnt++;
            }
        }

    }

    public boolean needsHighlight(int index) {

        // no highlight
        if (!valuesToHighlight())
            return false;

        for (int i = 0; i < mIndicesToHighlight.length; i++)

            // check if the xvalue for the given dataset needs highlight
            if ((int) mIndicesToHighlight[i].getX() == index)
                return true;

        return false;
    }

    private float calcAngle(float value) {
        return calcAngle(value, mData.getYValueSum());
    }

    private float calcAngle(float value, float yValueSum) {
        return value / yValueSum * mMaxAngle;
    }

    @Deprecated
    @Override
    public XAxis getXAxis() {
        throw new RuntimeException("PieChart has no XAxis");
    }

    @Override
    public int getIndexForAngle(float angle) {

        // take the current angle of the chart into consideration
        float a = Utils.getNormalizedAngle(angle - getRotationAngle());

        for (int i = 0; i < mAbsoluteAngles.length; i++) {
            if (mAbsoluteAngles[i] > a)
                return i;
        }

        return -1; // return -1 if no index found
    }

    public int getDataSetIndexForIndex(int xIndex) {

        List<IPieDataSet> dataSets = mData.getDataSets();

        for (int i = 0; i < dataSets.size(); i++) {
            if (dataSets.get(i).getEntryForXValue(xIndex, Float.NaN) != null)
                return i;
        }

        return -1;
    }

    public float[] getDrawAngles() {
        return mDrawAngles;
    }

    public float[] getAbsoluteAngles() {
        return mAbsoluteAngles;
    }

    public void setHoleColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintHole().setColor(color);
    }

    public void setDrawSlicesUnderHole(boolean enable) {
        mDrawSlicesUnderHole = enable;
    }

    public boolean isDrawSlicesUnderHoleEnabled() {
        return mDrawSlicesUnderHole;
    }

    public void setDrawHoleEnabled(boolean enabled) {
        this.mDrawHole = enabled;
    }

    public boolean isDrawHoleEnabled() {
        return mDrawHole;
    }

    public void setCenterText(CharSequence text) {
        if (text == null)
            mCenterText = "";
        else
            mCenterText = text;
    }

    public CharSequence getCenterText() {
        return mCenterText;
    }

    public void setDrawCenterText(boolean enabled) {
        this.mDrawCenterText = enabled;
    }

    public boolean isDrawCenterTextEnabled() {
        return mDrawCenterText;
    }

    @Override
    protected float getRequiredLegendOffset() {
        return mLegendRenderer.getLabelPaint().getTextSize() * 2.f;
    }

    @Override
    protected float getRequiredBaseOffset() {
        return 0;
    }

    @Override
    public float getRadius() {
        if (mCircleBox == null)
            return 0;
        else
            return Math.min(mCircleBox.width() / 2f, mCircleBox.height() / 2f);
    }

    public RectF getCircleBox() {
        return mCircleBox;
    }

    public MPPointF getCenterCircleBox() {
        return MPPointF.getInstance(mCircleBox.centerX(), mCircleBox.centerY());
    }

    public void setCenterTextTypeface(Typeface t) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setTypeface(t);
    }

    public void setCenterTextSize(float sizeDp) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setTextSize(
                Utils.convertDpToPixel(sizeDp));
    }

    public void setCenterTextSizePixels(float sizePixels) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setTextSize(sizePixels);
    }

    public void setCenterTextOffset(float x, float y) {
        mCenterTextOffset.x = Utils.convertDpToPixel(x);
        mCenterTextOffset.y = Utils.convertDpToPixel(y);
    }

    public MPPointF getCenterTextOffset() {
        return MPPointF.getInstance(mCenterTextOffset.x, mCenterTextOffset.y);
    }

    public void setCenterTextColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setColor(color);
    }

    public void setHoleRadius(final float percent) {
        mHoleRadiusPercent = percent;
    }

    public float getHoleRadius() {
        return mHoleRadiusPercent;
    }

    public void setTransparentCircleColor(int color) {

        Paint p = ((PieChartRenderer) mRenderer).getPaintTransparentCircle();
        int alpha = p.getAlpha();
        p.setColor(color);
        p.setAlpha(alpha);
    }

    public void setTransparentCircleRadius(final float percent) {
        mTransparentCircleRadiusPercent = percent;
    }

    public float getTransparentCircleRadius() {
        return mTransparentCircleRadiusPercent;
    }

    public void setTransparentCircleAlpha(int alpha) {
        ((PieChartRenderer) mRenderer).getPaintTransparentCircle().setAlpha(alpha);
    }

    @Deprecated
    public void setDrawSliceText(boolean enabled) {
        mDrawEntryLabels = enabled;
    }

    public void setDrawEntryLabels(boolean enabled) {
        mDrawEntryLabels = enabled;
    }

    public boolean isDrawEntryLabelsEnabled() {
        return mDrawEntryLabels;
    }

    public void setEntryLabelColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setColor(color);
    }

    public void setEntryLabelTypeface(Typeface tf) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setTypeface(tf);
    }

    public void setEntryLabelTextSize(float size) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setTextSize(Utils.convertDpToPixel(size));
    }

    public boolean isDrawRoundedSlicesEnabled() {
        return mDrawRoundedSlices;
    }

    public void setUsePercentValues(boolean enabled) {
        mUsePercentValues = enabled;
    }

    public boolean isUsePercentValuesEnabled() {
        return mUsePercentValues;
    }

    public void setCenterTextRadiusPercent(float percent) {
        mCenterTextRadiusPercent = percent;
    }

    public float getCenterTextRadiusPercent() {
        return mCenterTextRadiusPercent;
    }

    public float getMaxAngle() {
        return mMaxAngle;
    }

    public void setMaxAngle(float maxangle) {

        if (maxangle > 360)
            maxangle = 360f;

        if (maxangle < 90)
            maxangle = 90f;

        this.mMaxAngle = maxangle;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof PieChartRenderer) {
            ((PieChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}