package org.cuieney.videolife.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liyanju on 2018/1/3.
 */

public class VideoListItemBean implements Parcelable{


    public String getTitle() {
        return "";
    }

    public String getDescription() {
        return "";
    }

    public String getVideoId() {
        return "";
    }

    public String getCategory() {
        return "";
    }

    public String getDuration() {
        return "";
    }

    public String getMaxThumbnailUrl() {
        return "";
    }

    public String getNormalThumbnailUrl() {
        return "";
    }


    public VideoListItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected VideoListItemBean(Parcel in) {
    }

    public static final Creator<VideoListItemBean> CREATOR = new Creator<VideoListItemBean>() {
        @Override
        public VideoListItemBean createFromParcel(Parcel source) {
            return new VideoListItemBean(source);
        }

        @Override
        public VideoListItemBean[] newArray(int size) {
            return new VideoListItemBean[size];
        }
    };
}
