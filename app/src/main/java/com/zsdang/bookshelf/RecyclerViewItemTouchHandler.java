package com.zsdang.bookshelf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class RecyclerViewItemTouchHandler implements RecyclerView.OnItemTouchListener {

    private static final String TAG = "RecyclerViewItemTouchHandler";

    public interface OnItemClickListener {
        public void onItemClick(View view, int pos);
        public void onItemLongClick(View view, int pos);
    }

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private GestureDetector mGestureDetector;
    private RecycleViewGestureListener mRecycleViewGestureListener;

    public RecyclerViewItemTouchHandler(Context context, OnItemClickListener listener) {
        mContext = context;
        mOnItemClickListener = listener;
        mRecycleViewGestureListener = new RecycleViewGestureListener();
        mGestureDetector = new GestureDetector(context, mRecycleViewGestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //LogUtils.d(TAG, "onInterceptTouchEvent");
        mRecycleViewGestureListener.setRecyclerView(rv);
        return mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        //LogUtils.d(TAG, "onTouchEvent");

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class RecycleViewGestureListener extends  GestureDetector.SimpleOnGestureListener{

        private RecyclerView mRecyclerView;

        public void setRecyclerView(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mRecyclerView != null && mOnItemClickListener != null) {
                View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                int pos = mRecyclerView.getChildAdapterPosition(view);
                mOnItemClickListener.onItemClick(view, pos);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mRecyclerView != null && mOnItemClickListener != null) {
                View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                int pos = mRecyclerView.getChildAdapterPosition(view);
                mOnItemClickListener.onItemLongClick(view, pos);
            }
        }
    }
}
