package com.zsdang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zsdang.R;

public class TabLayout extends ViewGroup {

    private final int DEFAULT_ITEM_HORIZONTAL_MARGIN = 36;
    private final int DEFAULT_ITEM_VERTICAL_MARGIN = 40;

    private int mItemHorizontalMargin = DEFAULT_ITEM_HORIZONTAL_MARGIN;
    private int mItemVerticalMargin = DEFAULT_ITEM_VERTICAL_MARGIN;

    public interface OnItemClickListener {
        void onClick(View view, int pos);
    }

    private OnItemClickListener mOnItemClickListener;

    public TabLayout(Context context) {
        super(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextViewAttrs);

        mItemHorizontalMargin = typedArray.getInteger(R.styleable.ExpandableTextViewAttrs_collapsed_line, DEFAULT_ITEM_HORIZONTAL_MARGIN);
        mItemVerticalMargin = typedArray.getInteger(R.styleable.ExpandableTextViewAttrs_collapsed_line, DEFAULT_ITEM_VERTICAL_MARGIN);

        typedArray.recycle();
    }

    public void addView(View view, OnClickListener listener) {
        addView(view);
        view.setOnClickListener(listener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("suby1", "onFinishInflate:" + getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            final int index = i;
            final View child = getChildAt(i);
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(view, index);
                    }
                }
            });
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // Measure all children
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST||heightMode == MeasureSpec.UNSPECIFIED) {
            final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                    getDesiredHeight(widthSize), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {

        final int contentWidth = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        int remainWidth = contentWidth;
        int childLeft = 0;
        int rowTop = getPaddingTop();
        int maxHeightPerRow = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() == VISIBLE) {
                final int childWidth = child.getMeasuredWidth() + mItemHorizontalMargin;
                final int childHeight = child.getMeasuredHeight() + mItemVerticalMargin;

                if (remainWidth < childWidth) {
                    // New row
                    childLeft = getPaddingStart();
                    rowTop += maxHeightPerRow;
                    remainWidth = contentWidth;
                    maxHeightPerRow = 0;
                }

                // It is not the first at per row next.
                int childTop = rowTop;
                child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());

                // Prepare to calculate next child
                remainWidth -= childWidth;
                childLeft += childWidth;
                maxHeightPerRow = Math.max(maxHeightPerRow, childHeight);
            }
        }

    }

    private int getDesiredHeight(int width) {
        final int contentWidth = width - getPaddingStart() - getPaddingEnd();
        int remainWidth = contentWidth;
        int height = getPaddingTop() + getPaddingBottom();
        int rowHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth() + mItemHorizontalMargin;
            final int childHeight = child.getMeasuredHeight() + mItemVerticalMargin;

            if (remainWidth < childWidth) {
                // One row over, calculate height
                height += rowHeight;

                // Reset
                remainWidth = contentWidth;
                rowHeight = 0;
            }
            remainWidth -= childWidth;
            rowHeight = Math.max(rowHeight, childHeight);
        }

        // Last row
        height += rowHeight;

        return height;
    }
}
