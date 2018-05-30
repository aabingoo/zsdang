package com.wawgoo.wawgooreading.beans;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by aabingoo on 2017/9/5.
 */

public class Book {

    private String mName;
    private String mUrl;
    private String mIntroduction;
    private LinkedHashMap<String, String> mChapters;

    public Book() {
        mChapters = new LinkedHashMap<>();
    }

    public Book(String name, String url) {
        this();
        mName = name;
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setIntroduction(String introduction) {
        mIntroduction = introduction;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setChapters(LinkedHashMap<String, String> chapters) {
        mChapters = chapters;
    }

    public HashMap<String, String> getChapters() {
        return mChapters;
    }
}
