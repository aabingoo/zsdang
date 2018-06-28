package com.zsdang.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by aabingoo on 2017/9/5.
 */

public class Book implements Parcelable {

    private String mId;
    private String mName;
    private String mAuthor;
    private String mImg;
    private String mUrl;
    private String mDesc;
    private HashMap<String, String> mChapters;

    public Book() {
        mChapters = new HashMap<>();
    }

    public Book(String name, String url) {
        this();
        mName = name;
        mUrl = url;
    }

    public Book(String id, String name, String author, String img, String desc) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setIntroduction(String introduction) {
        mDesc = introduction;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDesc() {
        return mDesc;
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
        dest.writeString(mDesc);
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
        mDesc = source.readString();
        mChapters = source.readHashMap(HashMap.class.getClassLoader());
    }
}
