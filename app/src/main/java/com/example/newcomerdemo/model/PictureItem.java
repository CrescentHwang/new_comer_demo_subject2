package com.example.newcomerdemo.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PictureItem {
    @SerializedName("_id")
    private String mId;

    @SerializedName("createdAt")
    private String mCreateTime;

    @SerializedName("url")
    private String mUrl;

    @SerializedName("views")
    private int mViewNumber;

    @SerializedName("desc")
    private String mDescription;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmCreateTime() {
        return mCreateTime;
    }

    public void setmCreateTime(String mCreateTime) {
        this.mCreateTime = mCreateTime;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public int getmViewNumber() {
        return mViewNumber;
    }

    public void setmViewNumber(int mViewNumber) {
        this.mViewNumber = mViewNumber;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nmId : " + mId
                + "\nmCreateTime : " + mCreateTime
                + "\nmUrl : " + mUrl
                + "\nmViewNumber : " + mViewNumber
                + "\nmDescription : " + mDescription;
    }
}
