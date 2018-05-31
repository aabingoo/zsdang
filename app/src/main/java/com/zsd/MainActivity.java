package com.zsd;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zsd.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager. beginTransaction();
        transaction.replace(R.id.fragment_fl, homeFragment).commit();

//        Intent intent = new Intent(this, BookDetailActivity.class);
//        startActivity(intent);


//        final TextView tv = (TextView) findViewById(R.id.text);
//        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
//        tv.post(new Runnable() {
//            @Override
//            public void run() {
//                HtmlCrawler htmlCrawler = new HtmlCrawler();
//                htmlCrawler.crawlBody("http://www.qu.la/book/401/302281.html",
//                        new HtmlCrawler.CrawlerListener() {
//                            @Override
//                            public void crawlerDone(String content) {
//                                tv.setText(content);
//                            }
//                        });
//            }
//        });

    }
}
