package com.zsdang.data.web;

import android.support.annotation.NonNull;

/**
 * Created by BinyongSu on 2018/6/21.
 */

public interface DataRequestCallback {
    void onFailure();

    void onSuccess(@NonNull String result);
}
