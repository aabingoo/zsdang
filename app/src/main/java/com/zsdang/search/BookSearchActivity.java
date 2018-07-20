package com.zsdang.search;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.zsdang.R;
import com.zsdang.Utils;

public class BookSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBarTransparent(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        BookSearchFragment bookSearchFragment = BookSearchFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_fl, bookSearchFragment).commit();
    }
}
