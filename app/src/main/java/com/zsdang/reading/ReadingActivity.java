package com.zsdang.reading;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zsdang.R;
import com.zsdang.data.GlobalConstant;

/**
 * Created by BinyongSu on 2018/6/1.
 */

public class ReadingActivity extends AppCompatActivity {

    private static final String TAG = "ReadingActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Intent intent = getIntent();
        if (intent != null) {
            ReadingFragment readingFragment = ReadingFragment.newInstance(
                    intent.getStringExtra(GlobalConstant.EXTRA_BOOK_ID),
                    intent.getStringExtra(GlobalConstant.EXTRA_CHAPTER_ID));
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.reading_fragment_fl, readingFragment).commit();
        }
    }
}
