package com.wawgoo.wawgooreading;

import android.util.Log;

/**
 * Created by aabingoo on 2017/8/12.
 */

public class LogUtils {

    private static final String TAG = "ReadingLog";
    private static final boolean DEBUG = true;

    public static void i(String className, String msg) {
        if (DEBUG) Log.i(TAG, "[" + className + "]" + msg);
    }

    public static void d(String className, String msg) {
        if (DEBUG) Log.d(TAG, "[" + className + "]" + msg);
    }

    public static void e(String className, String msg) {
        if (DEBUG) Log.e(TAG, "[" + className + "]" + msg);
    }

}
