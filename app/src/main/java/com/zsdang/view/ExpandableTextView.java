package com.zsdang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsdang.LogUtils;
import com.zsdang.R;

public class ExpandableTextView extends AppCompatTextView implements View.OnClickListener {

    private final int DEFAULT_COLLAPSED_LINE = 3;
    private final String DEFAULT_EXPAND_TEXT = "展开>";
    private final String DEFAULT_COLLAPSED_TEXT= "<收起";

    private String mText;

    private int mWidth;

    // Check if need collapse
    private boolean mIsCollapsed = true;

    // The collapsed line number
    private int mCollapsedLines;// = DEFAULT_COLLAPSED_LINE;

    private String mExpandText;
    private String mCollapsedText = DEFAULT_COLLAPSED_TEXT;
    private SpannableString mExpandSpanStr;

    private int mExpandTextColor;// = Color.BLUE;
    private int mCollapsedTextColor;// = Color.GREEN;

    public ExpandableTextView(Context context) {
        super(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        setOnClickListener(this);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextViewAttrs);
        mCollapsedLines = typedArray.getInteger(R.styleable.ExpandableTextViewAttrs_collapsed_line, DEFAULT_COLLAPSED_LINE);
        mIsCollapsed = typedArray.getBoolean(R.styleable.ExpandableTextViewAttrs_isCollapsed, true);
        mExpandText = typedArray.getString(R.styleable.ExpandableTextViewAttrs_expand_text);
        if (TextUtils.isEmpty(mExpandText)) {
            mExpandText = DEFAULT_EXPAND_TEXT;
        }
        mCollapsedText = typedArray.getString(R.styleable.ExpandableTextViewAttrs_collpased_text);
        mExpandTextColor = typedArray.getColor(R.styleable.ExpandableTextViewAttrs_expand_text_color, Color.BLUE);
        mCollapsedTextColor = typedArray.getColor(R.styleable.ExpandableTextViewAttrs_collpased_text_color, Color.GREEN);

        mExpandSpanStr = new SpannableString(mExpandText);
        mExpandSpanStr.setSpan(new ForegroundColorSpan(mExpandTextColor), 0, mExpandSpanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        typedArray.recycle();
    }

    @Override
    public void onClick(View v) {
        mIsCollapsed = !mIsCollapsed;
        setOurText();
    }

    public void setText(String text) {
        Log.d("suby1", "setText:" + text);
        if (!TextUtils.isEmpty(text)) {
            if (!text.equals(mText)) {
                mText = text;
                int width = getWidth();
                if (width > 0) {
                    mWidth = width - getCompoundPaddingStart() - getCompoundPaddingEnd();
                    setOurText();
                }
            }
        }
    }

    private void setOurText() {
        Log.d("suby1", "setOurText:" + TextUtils.isEmpty(mText) + "  wid:" + mWidth);
        super.setText(mText);
        if (mIsCollapsed) {
            StaticLayout staticLayout = getWorkingLayout(mText);
            if (staticLayout != null) {
                String showText = mText;
                if (staticLayout.getLineCount() > mCollapsedLines) {
                    String processText = showText.substring(0, staticLayout.getLineEnd(mCollapsedLines - 1)).trim();
                    String appendStr = "... " + mExpandSpanStr;
                    showText = processText + appendStr;
                    StaticLayout checkLayout = getWorkingLayout(showText);
                    while (checkLayout.getLineCount() > mCollapsedLines) {
                        int lastIndex = processText.length() - 1;
                        if (lastIndex == -1) {
                            break;
                        }
                        processText = processText.substring(0, lastIndex);
                        showText = processText + appendStr;
                        checkLayout = getWorkingLayout(showText);
                    }
                    super.setText(processText + "... ");
                    super.append(mExpandSpanStr);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("suby1", "onMeasure");
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthSize > 0) {
            mWidth = widthSize - getCompoundPaddingStart() - getCompoundPaddingEnd();
            setOurText();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private StaticLayout getWorkingLayout(String text) {

        if (TextUtils.isEmpty(text) || mWidth <= 0) return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new StaticLayout(text, getPaint(), mWidth,
                    Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), false);
        } else {
            return new StaticLayout(text, getPaint(), mWidth,
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
    }
}
