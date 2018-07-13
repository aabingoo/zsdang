package com.zsdang.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by aabingoo on 2017/9/5.
 */

public class Book implements Parcelable {

    private String mId;
    private String mName;
    private String mAuthor;
    private String mImg;
    private String mDesc;
    private List<String> mChapterIdList;
    private List<String> mChapterTitleList;
    private String mLaestChapterId;
    private String mLaestChapterName;

    public Book() {
        mChapterIdList = new ArrayList<>();
        mChapterTitleList = new ArrayList<>();
    }

    public Book(String name, String img) {
        this();
        mName = name;
        mImg = img;
    }

    public Book(String id, String name, String img) {
        this();
        mId = id;
        mName = name;
        mImg = img;
    }

    public Book(String id, String name, String author, String img, String desc) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
    }

    public Book(String id, String name, String author, String img, String desc,
                String laestChapterId, String laestChapterName) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
        mLaestChapterId = laestChapterId;
        mLaestChapterName = laestChapterName;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getImg() {
        return mImg;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDesc() {
        return mDesc;
    }

    public List<String> getChapterIdList() {
        return mChapterIdList;
    }

    public List<String> getChapterTitleList() {
        return mChapterTitleList;
    }

    public String getLaestChapterId() {
        return mLaestChapterId;
    }

    public String getLaestChapterName() {
        return mLaestChapterName;
    }

    public void setLaestChapterName(String laestChapterName) {
        mLaestChapterName = laestChapterName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mAuthor);
        dest.writeString(mImg);
        dest.writeString(mDesc);
        dest.writeStringList(mChapterIdList);
        dest.writeStringList(mChapterTitleList);
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
        this();
        mId = source.readString();
        mName = source.readString();
        mAuthor = source.readString();
        mImg = source.readString();
        mDesc = source.readString();
        source.readStringList(mChapterIdList);
        source.readStringList(mChapterTitleList);
    }
}
