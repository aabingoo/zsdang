package com.zsdang;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zsdang.bookshelf.BookShelfFragment;
import com.zsdang.bookstore.BookStoreFragment;
import com.zsdang.my.MyFragment;
import com.zsdang.search.BookSearchActivity;
import com.zsdang.test.TestFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationBar.OnTabSelectedListener, Toolbar.OnMenuItemClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private final int DEFAULT_FRAGMENT_INDEX = 0;
    private final int FRAGMENT_INDEX_MUSIC = 1;
    private final int FRAGMENT_INDEX_TV = 2;
    private final int FRAGMENT_INDEX_GAME = 3;

    private List<Fragment> mFragments;
    private int currentPos = -1;

    private BottomNavigationBar mBottomNavigationBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBarTransparent(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setOnMenuItemClickListener(this);

        mFragments = new ArrayList<>();
        mFragments.add(BookShelfFragment.newInstance());
        mFragments.add(BookStoreFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        mFragments.add(TestFragment.newInstance());

        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        initBottomNavigationBar();

//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);
    }

    private void initBottomNavigationBar() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar
            .addItem(new BottomNavigationItem(R.drawable.icon_home_bookshelf, "书架").setActiveColor(R.color.color_theam))
            .addItem(new BottomNavigationItem(R.drawable.icon_home_bookstore, "书城").setActiveColor(R.color.color_theam))
            .addItem(new BottomNavigationItem(R.drawable.ic_home_my, "我的").setActiveColor(R.color.color_theam))
            .addItem(new BottomNavigationItem(R.drawable.ic_videogame, "Test").setActiveColor(R.color.color_theam))
            .setFirstSelectedPosition(DEFAULT_FRAGMENT_INDEX)
            .initialise();

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        currentPos = DEFAULT_FRAGMENT_INDEX;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager. beginTransaction();
        transaction.add(R.id.fragment_container, mFragments.get(DEFAULT_FRAGMENT_INDEX)).commit();
    }

    @Override
    public void onTabSelected(int position) {
        LogUtils.d(TAG, "onTabSelected:" + position);
        if (currentPos != position) {
            FragmentTransaction transaction = getFragmentManager(). beginTransaction();
            Fragment fragment = mFragments.get(position);
            if (!fragment.isAdded()) {
                transaction.hide(mFragments.get(currentPos))
                        .add(R.id.fragment_container, fragment).commit();
            } else {
                transaction.hide(mFragments.get(currentPos))
                        .show(fragment).commit();
            }
            currentPos = position;
        }
    }

    @Override
    public void onTabUnselected(int position) {
        LogUtils.d(TAG, "onTabUnselected:" + position);
    }

    @Override
    public void onTabReselected(int position) {
        LogUtils.d(TAG, "onTabReselected:" + position);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                Intent intent = new Intent(this, BookSearchActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

//    /**
//     * Set the color of status bar while enter action mode
//     * @param callback
//     * @return
//     */
//    @Nullable
//    @Override
//    public android.view.ActionMode startActionMode(android.view.ActionMode.Callback callback) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_actionmode));
//        }
//        return super.startActionMode(callback);
//    }


    @Override
    public void onActionModeStarted(ActionMode mode) {
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_theam));
        mBottomNavigationBar.setVisibility(View.GONE);
        super.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_theam));
        mBottomNavigationBar.setVisibility(View.VISIBLE);
        super.onActionModeFinished(mode);
    }
}
