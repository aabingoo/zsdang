package com.zsdang;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zsdang.bookshelf.BookShelfFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationBar.OnTabSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private final int DEFAULT_FRAGMENT_INDEX = 0;
    private final int FRAGMENT_INDEX_MUSIC = 1;
    private final int FRAGMENT_INDEX_TV = 2;
    private final int FRAGMENT_INDEX_GAME = 3;

    private List<Fragment> mFragments;

    private BottomNavigationBar mBottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragments = new ArrayList<>();
        mFragments.add(new BookShelfFragment());
        mFragments.add(new BookShelfFragment());
        mFragments.add(new BookShelfFragment());
        mFragments.add(new BookShelfFragment());

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
            .addItem(new BottomNavigationItem(R.drawable.ic_home, "Home").setActiveColor(Color.RED))
            .addItem(new BottomNavigationItem(R.drawable.ic_music, "Music").setActiveColor(Color.GREEN))
            .addItem(new BottomNavigationItem(R.drawable.ic_tv, "TV").setActiveColor(Color.BLUE))
            .addItem(new BottomNavigationItem(R.drawable.ic_videogame, "Game").setActiveColor(Color.YELLOW))
            .setFirstSelectedPosition(DEFAULT_FRAGMENT_INDEX)
            .initialise();

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager. beginTransaction();
        transaction.replace(R.id.fragment_container, mFragments.get(DEFAULT_FRAGMENT_INDEX)).commit();
    }

    @Override
    public void onTabSelected(int position) {
        LogUtils.d(TAG, "onTabSelected:" + position);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager. beginTransaction();
        transaction.replace(R.id.fragment_container, mFragments.get(position)).commit();
    }

    @Override
    public void onTabUnselected(int position) {
        LogUtils.d(TAG, "onTabUnselected:" + position);
    }

    @Override
    public void onTabReselected(int position) {
        LogUtils.d(TAG, "onTabReselected:" + position);
    }
}
