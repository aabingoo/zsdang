package com.zsdang.reading;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zsdang.R;
import com.zsdang.Utils;
import com.zsdang.data.GlobalConstant;

/**
 * Created by BinyongSu on 2018/6/1.
 */

public class ReadingActivity extends Activity {

    private static final String TAG = "ReadingActivity";

    private boolean isBarsShown = false;
    private Toolbar mToolBar;
    private View mBottomMenu;
    private ReadingPresenter mReadingPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setStatusBarColor(this, R.color.color_black);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        initMenu();

        Intent intent = getIntent();
        if (intent != null) {
            ReadingFragment readingFragment = ReadingFragment.newInstance(
                    intent.getStringExtra(GlobalConstant.EXTRA_BOOK_ID),
                    intent.getStringExtra(GlobalConstant.EXTRA_CHAPTER_ID));
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.reading_fragment_fl, readingFragment).commit();
        }

        mReadingPresenter = new ReadingPresenter(this);
    }

    private void initMenu() {
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.inflateMenu(R.menu.toolbar_menu_reading);
        mToolBar.setNavigationIcon(R.drawable.ic_back_black);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBottomMenu = findViewById(R.id.bottom_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mReadingPresenter.handleMenuAndBarsVisibility();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mReadingPresenter.handleTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    public void showMenu() {
        mToolBar.setVisibility(View.VISIBLE);
        mBottomMenu.setVisibility(View.VISIBLE);
    }

    public void hideMenu() {
        mToolBar.setVisibility(View.GONE);
        mBottomMenu.setVisibility(View.GONE);
    }

}
