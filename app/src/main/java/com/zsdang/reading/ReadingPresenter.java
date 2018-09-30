package com.zsdang.reading;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.zsdang.Utils;

public class ReadingPresenter {

    private ReadingActivity mReadingActivity;

    private boolean isBarsShown = false; // Hide StatusBar and NavigationBar by default.

    public ReadingPresenter(@NonNull ReadingActivity readingActivity) {
        mReadingActivity = readingActivity;
    }

    public void handleTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && event.getY() > 1800) {
            isBarsShown = !isBarsShown;
            handleMenuAndBarsVisibility();
        }
    }

    public void handleMenuAndBarsVisibility() {
        if (isBarsShown) {
            mReadingActivity.showMenu();
            Utils.showStatusAndNavigationBar(mReadingActivity);
        } else {
            mReadingActivity.hideMenu();
            Utils.hideStatusAndNavigationBar(mReadingActivity);
        }
    }
}
