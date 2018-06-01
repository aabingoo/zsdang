package com.zsdang.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by aabingoo on 2017/9/5.
 */

public class Book implements Parcelable {

    private String mName;
    private String mUrl;
    private String mIntroduction;
    private HashMap<String, String> mChapters;

    public Book() {
        mChapters = new HashMap<>();
    }

    public Book(String name, String url) {
        this();
        mName = name;
        mUrl = url;
    }

    public String getName() {
        return mName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mUrl);
        dest.writeString(mIntroduction);
        dest.writeMap(mChapters);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    protected Book(Parcel source) {
        mName = source.readString();
        mUrl = source.readString();
        mIntroduction = source.readString();
        mChapters = source.readHashMap(HashMap.class.getClassLoader());
    }
}
