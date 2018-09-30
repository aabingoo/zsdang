package com.zsdang.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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
    private String mCategoryId;
    private String mCategory;
    private String mFirstChapterId;
    private String mLatestChapterId;
    private String mLatestChapterTitle;
    private String mLatestChapterDate;
    private boolean mLatestChapterIsRead;
    private String mStatus;
    private boolean mIsReading;

    private boolean mSelect;

    private List<String> mChapterIdList;
    private List<String> mChapterTitleList;

    public Book() {
        mChapterIdList = new ArrayList<>();
        mChapterTitleList = new ArrayList<>();
        mSelect = false;
        mLatestChapterIsRead = true;
        mIsReading = false;
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

    public Book(String id, String name, String author, String img, String desc, String category) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
        mCategory = category;
    }

    public Book(String id, String name, String author, String img, String desc,
                String laestChapterId, String laestChapterName) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
        mLatestChapterId = laestChapterId;
        mLatestChapterTitle = laestChapterName;
    }

    public Book(String id, String name, String author, String img, String desc, String category,
                String laestChapterId, String laestChapterName) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
        mCategory = category;
        mLatestChapterId = laestChapterId;
        mLatestChapterTitle = laestChapterName;
    }

    public Book(String id, String name, String author, String img, String desc, String category,
                String latestChapterId, String latestChapterTitle, String latestChapterDate,
                String status) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
        mCategory = category;
        mLatestChapterId = latestChapterId;
        mLatestChapterTitle = latestChapterTitle;
        mLatestChapterDate = latestChapterDate;
        mStatus = status;
    }

    public Book(String id, String name, String author, String img, String desc, String categoryId,
                String category, String firstChapterId, String latestChapterId,
                String latestChapterTitle, String latestChapterDate, String status) {
        this();
        mId = id;
        mName = name;
        mAuthor = author;
        mImg = img;
        mDesc = desc;
        mCategoryId = categoryId;
        mCategory = category;
        mFirstChapterId = firstChapterId;
        mLatestChapterId = latestChapterId;
        mLatestChapterTitle = latestChapterTitle;
        mLatestChapterDate = latestChapterDate;
        mStatus = status;
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

    public String getCategoryId() {
        return mCategoryId;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getFirstChapterId() {
        return mFirstChapterId;
    }

    public void setFirstChapterId(String firstChapterId) {
        mFirstChapterId = firstChapterId;
    }

    public String getLatestChapterId() {
        return mLatestChapterId;
    }

    public void setLatestChapterId(String latestChapterId) {
        mLatestChapterId = latestChapterId;
    }

    public String getLatestChapterTitle() {
        return mLatestChapterTitle;
    }

    public void setLatestChapterTitle(String latestChapterTitle) {
        mLatestChapterTitle = latestChapterTitle;
    }

    public String getLatestChapterDate() {
        return mLatestChapterDate;
    }

    public void setLatestChapterDate(String latestChapterDate) {
        mLatestChapterDate = latestChapterDate;
    }

    public boolean getLatestChapterIsRead() {
        return mLatestChapterIsRead;
    }

    public void setLatestChapterIsRead(boolean latestChapterIsRead) {
        mLatestChapterIsRead = latestChapterIsRead;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public boolean getIsReading() {
        return mIsReading;
    }

    public void setIsReading(boolean isReading) {
        mIsReading = isReading;
    }



    public List<String> getChapterIdList() {
        return mChapterIdList;
    }

    public List<String> getChapterTitleList() {
        return mChapterTitleList;
    }



    public boolean getSelect() {
        return mSelect;
    }

    public void setSelect(boolean select) {
        mSelect = select;
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
