package com.example.project2.chart.components;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.example.project2.chart.utils.ColorTemplate;
import com.example.project2.chart.utils.FSize;
import com.example.project2.chart.utils.Utils;
import com.example.project2.chart.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;


public class Legend extends ComponentBase {

    @Deprecated
    public enum LegendPosition {
        RIGHT_OF_CHART, RIGHT_OF_CHART_CENTER, RIGHT_OF_CHART_INSIDE,
        LEFT_OF_CHART, LEFT_OF_CHART_CENTER, LEFT_OF_CHART_INSIDE,
        BELOW_CHART_LEFT, BELOW_CHART_RIGHT, BELOW_CHART_CENTER,
        ABOVE_CHART_LEFT, ABOVE_CHART_RIGHT, ABOVE_CHART_CENTER,
        PIECHART_CENTER
    }

    public enum LegendForm {
        NONE,
        EMPTY,
        DEFAULT,
        SQUARE,
        CIRCLE,
        LINE
    }

    public enum LegendHorizontalAlignment {
        LEFT, CENTER, RIGHT
    }

    public enum LegendVerticalAlignment {
        TOP, CENTER, BOTTOM
    }

    public enum LegendOrientation {
        HORIZONTAL, VERTICAL
    }

    public enum LegendDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    private LegendEntry[] mEntries = new LegendEntry[]{};

    private LegendEntry[] mExtraEntries;

    private boolean mIsLegendCustom = false;

    private LegendHorizontalAlignment mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
    private LegendVerticalAlignment mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
    private LegendOrientation mOrientation = LegendOrientation.HORIZONTAL;
    private boolean mDrawInside = false;

    private LegendDirection mDirection = LegendDirection.LEFT_TO_RIGHT;

    private LegendForm mShape = LegendForm.SQUARE;

    private float mFormSize = 8f;

    private float mFormLineWidth = 3f;

    private DashPathEffect mFormLineDashEffect = null;

    private float mXEntrySpace = 6f;

    private float mYEntrySpace = 0f;

    private float mFormToTextSpace = 5f;

    private float mStackSpace = 3f;

    private float mMaxSizePercent = 0.95f;

    public Legend() {
        this.mTextSize = Utils.convertDpToPixel(10f);
        this.mXOffset = Utils.convertDpToPixel(5f);
        this.mYOffset = Utils.convertDpToPixel(3f); // 2
    }

    public Legend(LegendEntry[] entries) {
        this();

        if (entries == null) {
            throw new IllegalArgumentException("entries array is NULL");
        }

        this.mEntries = entries;
    }

    @Deprecated
    public Legend(int[] colors, String[] labels) {
        this();

        if (colors == null || labels == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        }

        if (colors.length != labels.length) {
            throw new IllegalArgumentException(
                    "colors array and labels array need to be of same size");
        }

        List<LegendEntry> entries = new ArrayList<>();

        for (int i = 0; i < Math.min(colors.length, labels.length); i++) {
            final LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = labels[i];

            if (entry.formColor == ColorTemplate.COLOR_SKIP)
                entry.form = LegendForm.NONE;
            else if (entry.formColor == ColorTemplate.COLOR_NONE ||
                    entry.formColor == 0)
                entry.form = LegendForm.EMPTY;

            entries.add(entry);
        }

        mEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    @Deprecated
    public Legend(List<Integer> colors, List<String> labels) {
        this(Utils.convertIntegers(colors), Utils.convertStrings(labels));
    }

    public void setEntries(List<LegendEntry> entries) {
        mEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public LegendEntry[] getEntries() {
        return mEntries;
    }

    public float getMaximumEntryWidth(Paint p) {

        float max = 0f;
        float maxFormSize = 0f;
        float formToTextSpace = Utils.convertDpToPixel(mFormToTextSpace);

        for (LegendEntry entry : mEntries) {
            final float formSize = Utils.convertDpToPixel(
                    Float.isNaN(entry.formSize)
                            ? mFormSize : entry.formSize);
            if (formSize > maxFormSize)
                maxFormSize = formSize;

            String label = entry.label;
            if (label == null) continue;

            float length = (float) Utils.calcTextWidth(p, label);

            if (length > max)
                max = length;
        }

        return max + maxFormSize + formToTextSpace;
    }

    public float getMaximumEntryHeight(Paint p) {

        float max = 0f;

        for (LegendEntry entry : mEntries) {
            String label = entry.label;
            if (label == null) continue;

            float length = (float) Utils.calcTextHeight(p, label);

            if (length > max)
                max = length;
        }

        return max;
    }

    @Deprecated
    public int[] getColors() {

        int[] old = new int[mEntries.length];
        for (int i = 0; i < mEntries.length; i++) {
            old[i] = mEntries[i].form == LegendForm.NONE ? ColorTemplate.COLOR_SKIP :
                    (mEntries[i].form == LegendForm.EMPTY ? ColorTemplate.COLOR_NONE :
                            mEntries[i].formColor);
        }
        return old;
    }

    @Deprecated
    public String[] getLabels() {

        String[] old = new String[mEntries.length];
        for (int i = 0; i < mEntries.length; i++) {
            old[i] = mEntries[i].label;
        }
        return old;
    }

    @Deprecated
    public int[] getExtraColors() {

        int[] old = new int[mExtraEntries.length];
        for (int i = 0; i < mExtraEntries.length; i++) {
            old[i] = mExtraEntries[i].form == LegendForm.NONE ? ColorTemplate.COLOR_SKIP :
                    (mExtraEntries[i].form == LegendForm.EMPTY ? ColorTemplate.COLOR_NONE :
                            mExtraEntries[i].formColor);
        }
        return old;
    }

    @Deprecated
    public String[] getExtraLabels() {

        String[] old = new String[mExtraEntries.length];
        for (int i = 0; i < mExtraEntries.length; i++) {
            old[i] = mExtraEntries[i].label;
        }
        return old;
    }

    public LegendEntry[] getExtraEntries() {

        return mExtraEntries;
    }

    public void setExtra(List<LegendEntry> entries) {
        mExtraEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public void setExtra(LegendEntry[] entries) {
        if (entries == null)
            entries = new LegendEntry[]{};
        mExtraEntries = entries;
    }

    @Deprecated
    public void setExtra(List<Integer> colors, List<String> labels) {
        setExtra(Utils.convertIntegers(colors), Utils.convertStrings(labels));
    }

    public void setExtra(int[] colors, String[] labels) {

        List<LegendEntry> entries = new ArrayList<>();

        for (int i = 0; i < Math.min(colors.length, labels.length); i++) {
            final LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = labels[i];

            if (entry.formColor == ColorTemplate.COLOR_SKIP ||
                    entry.formColor == 0)
                entry.form = LegendForm.NONE;
            else if (entry.formColor == ColorTemplate.COLOR_NONE)
                entry.form = LegendForm.EMPTY;

            entries.add(entry);
        }

        mExtraEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public void setCustom(LegendEntry[] entries) {

        mEntries = entries;
        mIsLegendCustom = true;
    }

    public void setCustom(List<LegendEntry> entries) {

        mEntries = entries.toArray(new LegendEntry[entries.size()]);
        mIsLegendCustom = true;
    }

    public void resetCustom() {
        mIsLegendCustom = false;
    }

    public boolean isLegendCustom() {
        return mIsLegendCustom;
    }

    @Deprecated
    public LegendPosition getPosition() {

        if (mOrientation == LegendOrientation.VERTICAL
                && mHorizontalAlignment == LegendHorizontalAlignment.CENTER
                && mVerticalAlignment == LegendVerticalAlignment.CENTER) {
            return LegendPosition.PIECHART_CENTER;
        } else if (mOrientation == LegendOrientation.HORIZONTAL) {
            if (mVerticalAlignment == LegendVerticalAlignment.TOP)
                return mHorizontalAlignment == LegendHorizontalAlignment.LEFT
                        ? LegendPosition.ABOVE_CHART_LEFT
                        : (mHorizontalAlignment == LegendHorizontalAlignment.RIGHT
                        ? LegendPosition.ABOVE_CHART_RIGHT
                        : LegendPosition.ABOVE_CHART_CENTER);
            else
                return mHorizontalAlignment == LegendHorizontalAlignment.LEFT
                        ? LegendPosition.BELOW_CHART_LEFT
                        : (mHorizontalAlignment == LegendHorizontalAlignment.RIGHT
                        ? LegendPosition.BELOW_CHART_RIGHT
                        : LegendPosition.BELOW_CHART_CENTER);
        } else {
            if (mHorizontalAlignment == LegendHorizontalAlignment.LEFT)
                return mVerticalAlignment == LegendVerticalAlignment.TOP && mDrawInside
                        ? LegendPosition.LEFT_OF_CHART_INSIDE
                        : (mVerticalAlignment == LegendVerticalAlignment.CENTER
                        ? LegendPosition.LEFT_OF_CHART_CENTER
                        : LegendPosition.LEFT_OF_CHART);
            else
                return mVerticalAlignment == LegendVerticalAlignment.TOP && mDrawInside
                        ? LegendPosition.RIGHT_OF_CHART_INSIDE
                        : (mVerticalAlignment == LegendVerticalAlignment.CENTER
                        ? LegendPosition.RIGHT_OF_CHART_CENTER
                        : LegendPosition.RIGHT_OF_CHART);
        }
    }

    @Deprecated
    public void setPosition(LegendPosition newValue) {

        switch (newValue) {
            case LEFT_OF_CHART:
            case LEFT_OF_CHART_INSIDE:
            case LEFT_OF_CHART_CENTER:
                mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
                mVerticalAlignment = newValue == LegendPosition.LEFT_OF_CHART_CENTER
                        ? LegendVerticalAlignment.CENTER
                        : LegendVerticalAlignment.TOP;
                mOrientation = LegendOrientation.VERTICAL;
                break;

            case RIGHT_OF_CHART:
            case RIGHT_OF_CHART_INSIDE:
            case RIGHT_OF_CHART_CENTER:
                mHorizontalAlignment = LegendHorizontalAlignment.RIGHT;
                mVerticalAlignment = newValue == LegendPosition.RIGHT_OF_CHART_CENTER
                        ? LegendVerticalAlignment.CENTER
                        : LegendVerticalAlignment.TOP;
                mOrientation = LegendOrientation.VERTICAL;
                break;

            case ABOVE_CHART_LEFT:
            case ABOVE_CHART_CENTER:
            case ABOVE_CHART_RIGHT:
                mHorizontalAlignment = newValue == LegendPosition.ABOVE_CHART_LEFT
                        ? LegendHorizontalAlignment.LEFT
                        : (newValue == LegendPosition.ABOVE_CHART_RIGHT
                        ? LegendHorizontalAlignment.RIGHT
                        : LegendHorizontalAlignment.CENTER);
                mVerticalAlignment = LegendVerticalAlignment.TOP;
                mOrientation = LegendOrientation.HORIZONTAL;
                break;

            case BELOW_CHART_LEFT:
            case BELOW_CHART_CENTER:
            case BELOW_CHART_RIGHT:
                mHorizontalAlignment = newValue == LegendPosition.BELOW_CHART_LEFT
                        ? LegendHorizontalAlignment.LEFT
                        : (newValue == LegendPosition.BELOW_CHART_RIGHT
                        ? LegendHorizontalAlignment.RIGHT
                        : LegendHorizontalAlignment.CENTER);
                mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
                mOrientation = LegendOrientation.HORIZONTAL;
                break;

            case PIECHART_CENTER:
                mHorizontalAlignment = LegendHorizontalAlignment.CENTER;
                mVerticalAlignment = LegendVerticalAlignment.CENTER;
                mOrientation = LegendOrientation.VERTICAL;
                break;
        }

        mDrawInside = newValue == LegendPosition.LEFT_OF_CHART_INSIDE
                || newValue == LegendPosition.RIGHT_OF_CHART_INSIDE;
    }

    public LegendHorizontalAlignment getHorizontalAlignment() {
        return mHorizontalAlignment;
    }

    public void setHorizontalAlignment(LegendHorizontalAlignment value) {
        mHorizontalAlignment = value;
    }

    public LegendVerticalAlignment getVerticalAlignment() {
        return mVerticalAlignment;
    }

    public void setVerticalAlignment(LegendVerticalAlignment value) {
        mVerticalAlignment = value;
    }

    public LegendOrientation getOrientation() {
        return mOrientation;
    }

    public void setOrientation(LegendOrientation value) {
        mOrientation = value;
    }

    public boolean isDrawInsideEnabled() {
        return mDrawInside;
    }

    public void setDrawInside(boolean value) {
        mDrawInside = value;
    }

    public LegendDirection getDirection() {
        return mDirection;
    }

    public void setDirection(LegendDirection pos) {
        mDirection = pos;
    }

    public LegendForm getForm() {
        return mShape;
    }

    public void setForm(LegendForm shape) {
        mShape = shape;
    }

    public void setFormSize(float size) {
        mFormSize = size;
    }

    public float getFormSize() {
        return mFormSize;
    }

    public void setFormLineWidth(float size) {
        mFormLineWidth = size;
    }

    public float getFormLineWidth() {
        return mFormLineWidth;
    }

    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        mFormLineDashEffect = dashPathEffect;
    }

    public DashPathEffect getFormLineDashEffect() {
        return mFormLineDashEffect;
    }

    public float getXEntrySpace() {
        return mXEntrySpace;
    }

    public void setXEntrySpace(float space) {
        mXEntrySpace = space;
    }

    public float getYEntrySpace() {
        return mYEntrySpace;
    }

    public void setYEntrySpace(float space) {
        mYEntrySpace = space;
    }

    public float getFormToTextSpace() {
        return mFormToTextSpace;
    }

    public void setFormToTextSpace(float space) {
        this.mFormToTextSpace = space;
    }

    public float getStackSpace() {
        return mStackSpace;
    }

    public void setStackSpace(float space) {
        mStackSpace = space;
    }

    public float mNeededWidth = 0f;

    public float mNeededHeight = 0f;

    public float mTextHeightMax = 0f;

    public float mTextWidthMax = 0f;

    private boolean mWordWrapEnabled = false;

    public void setWordWrapEnabled(boolean enabled) {
        mWordWrapEnabled = enabled;
    }

    public boolean isWordWrapEnabled() {
        return mWordWrapEnabled;
    }

    public float getMaxSizePercent() {
        return mMaxSizePercent;
    }

    public void setMaxSizePercent(float maxSize) {
        mMaxSizePercent = maxSize;
    }

    private List<FSize> mCalculatedLabelSizes = new ArrayList<>(16);
    private List<Boolean> mCalculatedLabelBreakPoints = new ArrayList<>(16);
    private List<FSize> mCalculatedLineSizes = new ArrayList<>(16);

    public List<FSize> getCalculatedLabelSizes() {
        return mCalculatedLabelSizes;
    }

    public List<Boolean> getCalculatedLabelBreakPoints() {
        return mCalculatedLabelBreakPoints;
    }

    public List<FSize> getCalculatedLineSizes() {
        return mCalculatedLineSizes;
    }

    public void calculateDimensions(Paint labelpaint, ViewPortHandler viewPortHandler) {

        float defaultFormSize = Utils.convertDpToPixel(mFormSize);
        float stackSpace = Utils.convertDpToPixel(mStackSpace);
        float formToTextSpace = Utils.convertDpToPixel(mFormToTextSpace);
        float xEntrySpace = Utils.convertDpToPixel(mXEntrySpace);
        float yEntrySpace = Utils.convertDpToPixel(mYEntrySpace);
        boolean wordWrapEnabled = mWordWrapEnabled;
        LegendEntry[] entries = mEntries;
        int entryCount = entries.length;

        mTextWidthMax = getMaximumEntryWidth(labelpaint);
        mTextHeightMax = getMaximumEntryHeight(labelpaint);

        switch (mOrientation) {
            case VERTICAL: {

                float maxWidth = 0f, maxHeight = 0f, width = 0f;
                float labelLineHeight = Utils.getLineHeight(labelpaint);
                boolean wasStacked = false;

                for (int i = 0; i < entryCount; i++) {

                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize)
                            ? defaultFormSize
                            : Utils.convertDpToPixel(e.formSize);
                    String label = e.label;

                    if (!wasStacked)
                        width = 0.f;

                    if (drawingForm) {
                        if (wasStacked)
                            width += stackSpace;
                        width += formSize;
                    }

                    // grouped forms have null labels
                    if (label != null) {

                        // make a step to the left
                        if (drawingForm && !wasStacked)
                            width += formToTextSpace;
                        else if (wasStacked) {
                            maxWidth = Math.max(maxWidth, width);
                            maxHeight += labelLineHeight + yEntrySpace;
                            width = 0.f;
                            wasStacked = false;
                        }

                        width += Utils.calcTextWidth(labelpaint, label);

                        if (i < entryCount - 1)
                            maxHeight += labelLineHeight + yEntrySpace;
                    } else {
                        wasStacked = true;
                        width += formSize;
                        if (i < entryCount - 1)
                            width += stackSpace;
                    }

                    maxWidth = Math.max(maxWidth, width);
                }

                mNeededWidth = maxWidth;
                mNeededHeight = maxHeight;

                break;
            }
            case HORIZONTAL: {

                float labelLineHeight = Utils.getLineHeight(labelpaint);
                float labelLineSpacing = Utils.getLineSpacing(labelpaint) + yEntrySpace;
                float contentWidth = viewPortHandler.contentWidth() * mMaxSizePercent;

                // Start calculating layout
                float maxLineWidth = 0.f;
                float currentLineWidth = 0.f;
                float requiredWidth = 0.f;
                int stackedStartIndex = -1;

                mCalculatedLabelBreakPoints.clear();
                mCalculatedLabelSizes.clear();
                mCalculatedLineSizes.clear();

                for (int i = 0; i < entryCount; i++) {

                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize)
                            ? defaultFormSize
                            : Utils.convertDpToPixel(e.formSize);
                    String label = e.label;

                    mCalculatedLabelBreakPoints.add(false);

                    if (stackedStartIndex == -1) {
                        // we are not stacking, so required width is for this label
                        // only
                        requiredWidth = 0.f;
                    } else {
                        // add the spacing appropriate for stacked labels/forms
                        requiredWidth += stackSpace;
                    }

                    // grouped forms have null labels
                    if (label != null) {

                        mCalculatedLabelSizes.add(Utils.calcTextSize(labelpaint, label));
                        requiredWidth += drawingForm ? formToTextSpace + formSize : 0.f;
                        requiredWidth += mCalculatedLabelSizes.get(i).width;
                    } else {

                        mCalculatedLabelSizes.add(FSize.getInstance(0.f, 0.f));
                        requiredWidth += drawingForm ? formSize : 0.f;

                        if (stackedStartIndex == -1) {
                            // mark this index as we might want to break here later
                            stackedStartIndex = i;
                        }
                    }

                    if (label != null || i == entryCount - 1) {

                        float requiredSpacing = currentLineWidth == 0.f ? 0.f : xEntrySpace;

                        if (!wordWrapEnabled // No word wrapping, it must fit.
                                // The line is empty, it must fit
                                || currentLineWidth == 0.f
                                // It simply fits
                                || (contentWidth - currentLineWidth >=
                                requiredSpacing + requiredWidth)) {
                            // Expand current line
                            currentLineWidth += requiredSpacing + requiredWidth;
                        } else { // It doesn't fit, we need to wrap a line

                            // Add current line size to array
                            mCalculatedLineSizes.add(FSize.getInstance(currentLineWidth, labelLineHeight));
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth);

                            // Start a new line
                            mCalculatedLabelBreakPoints.set(
                                    stackedStartIndex > -1 ? stackedStartIndex
                                            : i, true);
                            currentLineWidth = requiredWidth;
                        }

                        if (i == entryCount - 1) {
                            // Add last line size to array
                            mCalculatedLineSizes.add(FSize.getInstance(currentLineWidth, labelLineHeight));
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                        }
                    }

                    stackedStartIndex = label != null ? -1 : stackedStartIndex;
                }

                mNeededWidth = maxLineWidth;
                mNeededHeight = labelLineHeight
                        * (float) (mCalculatedLineSizes.size())
                        + labelLineSpacing *
                        (float) (mCalculatedLineSizes.size() == 0
                                ? 0
                                : (mCalculatedLineSizes.size() - 1));

                break;
            }
        }

        mNeededHeight += mYOffset;
        mNeededWidth += mXOffset;
    }
}