package com.example.newcomerdemo.bean;

import com.example.newcomerdemo.model.PictureItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PictureUrlsJSONBean {

    @SerializedName("page")
    private int mCurrentPage;

    @SerializedName("page_count")
    private int mCurrentCount;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("total_counts")
    private int mTotalCount;

    @SerializedName("data")
    private ArrayList<PictureItem> pictureItemList;

    public int getmCurrentPage() {
        return mCurrentPage;
    }

    public void setmCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public int getmCurrentCount() {
        return mCurrentCount;
    }

    public void setmCurrentCount(int mCurrentCount) {
        this.mCurrentCount = mCurrentCount;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public int getmTotalCount() {
        return mTotalCount;
    }

    public void setmTotalCount(int mTotalCount) {
        this.mTotalCount = mTotalCount;
    }

    public List<PictureItem> getPictureItemList() {
        return pictureItemList;
    }

    public void setPictureItemList(ArrayList<PictureItem> pictureItemList) {
        this.pictureItemList = pictureItemList;
    }
}
