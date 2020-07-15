package com.example.newcomerdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PictureItem implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mCreateTime);
        dest.writeString(this.mUrl);
        dest.writeInt(this.mViewNumber);
        dest.writeString(this.mDescription);
    }

    public PictureItem() {
    }

    protected PictureItem(Parcel in) {
        this.mId = in.readString();
        this.mCreateTime = in.readString();
        this.mUrl = in.readString();
        this.mViewNumber = in.readInt();
        this.mDescription = in.readString();
    }

    public static final Creator<PictureItem> CREATOR = new Creator<PictureItem>() {
        @Override
        public PictureItem createFromParcel(Parcel source) {
            return new PictureItem(source);
        }

        @Override
        public PictureItem[] newArray(int size) {
            return new PictureItem[size];
        }
    };
}
