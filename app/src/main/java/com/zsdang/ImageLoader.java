package com.zsdang;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by BinyongSu on 2018/6/12.
 */

public class ImageLoader extends AsyncTask<String, String, String> {

    private TextView mTextView;

    public ImageLoader(TextView tv) {
        mTextView = tv;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            publishProgress("sleeping");
            Log.d("suby1", "sleep------");
            Thread.sleep(2000);
            Log.d("suby1", "sleep---end---");
        } catch (Exception e) {

        }
        return "wake up";
    }


    @Override
    protected void onPostExecute(String s) {
        mTextView.setText(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mTextView.setText(values[0]);
    }
}
