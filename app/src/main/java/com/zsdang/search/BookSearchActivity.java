package com.zsdang.search;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.Utils;

public class BookSearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    private final String TAG = "BookSearchActivity";

    private Toolbar mToolbar;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBarColor(this, R.color.color_theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        initToolbar();

        openBookSearchFragment();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.search_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.search_toolbar_menu);

        mSearchView = mToolbar.findViewById(R.id.searchview_item);
        // Background and LayoutParams
        mSearchView.setBackground(getDrawable(R.drawable.bg_searchview));
        int searchWidth = (int) getResources().getDimension(R.dimen.searchview_width);
        int searchHeight = (int) getResources().getDimension(R.dimen.searchview_height);
        LayoutParams params = new LayoutParams(searchWidth, searchHeight);
        params.gravity = Gravity.CENTER;
        mSearchView.setLayoutParams(params);

        // Expand default
        mSearchView.setIconifiedByDefault(false);

        // Hint
        mSearchView.setQueryHint("搜索书名或作者");

        // Set listener
        mSearchView.setOnQueryTextListener(this);

        // Text style
        TextView textView = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextSize(14);

        // Search icon
        ImageView searchIcon = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        int searchIconSize = (int) (textView.getTextSize() * 1.25);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(searchIconSize, searchIconSize);
        lp.gravity = Gravity.CENTER;
        searchIcon.setLayoutParams(lp);
        searchIcon.setImageResource(R.drawable.ic_search);
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        LogUtils.d(TAG, "onQueryTextSubmit:" + keyword);
        keyword = keyword.trim();
        if (!TextUtils.isEmpty(keyword)) {
//            startSearchBooksByPage(keyword, 1);

            BookSearchResultFragment fragment = BookSearchResultFragment.newInstance(keyword);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_fl, fragment).commit();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        LogUtils.d(TAG, "onQueryTextChange:" + s);
        if (TextUtils.isEmpty(s)) {
            openBookSearchFragment();
        }
        return false;
    }

    private void openBookSearchFragment() {
        BookSearchFragment bookSearchResultFragment = BookSearchFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_fl, bookSearchResultFragment).commit();
    }
}
