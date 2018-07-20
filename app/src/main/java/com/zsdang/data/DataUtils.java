package com.zsdang.data;

import com.zsdang.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BinyongSu on 2018/6/28.
 */

public class DataUtils {

    private static final String TAG = "DataUtils";

    public static JSONObject getJSONObjectFromJSONArray(JSONArray jsonArray, int index) {
        JSONObject jsonObject = null;
        try {
            if (jsonArray != null) {
                jsonObject = jsonArray.getJSONObject(index);
            }
        } catch (JSONException e) {
            LogUtils.d(TAG, "Exception on getJSONObjectFromJSONArray:" + e.toString());
        } finally {
            return jsonObject;
        }
    }

    public static JSONArray getJSONArrayFromJSONObject(JSONObject jsonObject, String key) {
        JSONArray jsonArray = null;
        try {
            if (jsonObject != null) {
                jsonArray = jsonObject.getJSONArray(key);
            }
        } catch (JSONException e) {
            LogUtils.d(TAG, "Exception on getJSONArrayFromJSONObject:" + e.toString());
        } finally {
            return jsonArray;
        }
    }
}
