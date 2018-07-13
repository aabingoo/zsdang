package com.zsdang;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zsdang.data.web.server.DataServiceManager;

/**
 * Created by BinyongSu on 2018/6/12.
 */

public class ImageLoader /*extends AsyncTask<String, String, String>*/ {

    public static void loadImgInto(Context context, String sourceImgName, ImageView target) {
        String sourceUrl = String.format(DataServiceManager.HOST_BOOK_COVER, sourceImgName);
        Glide.with(context).load(sourceUrl).into(target);
    }

//    private TextView mTextView;
//
//    public ImageLoader(TextView tv) {
//        mTextView = tv;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        try {
//            publishProgress("sleeping");
//            Log.d("suby1", "sleep------");
//            Thread.sleep(2000);
//            Log.d("suby1", "sleep---end---");
//        } catch (Exception e) {
//
//        }
//        return "wake up";
//    }
//
//
//    @Override
//    protected void onPostExecute(String s) {
//        mTextView.setText(s);
//    }
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        mTextView.setText(values[0]);
//    }
}
