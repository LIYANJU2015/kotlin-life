package org.cuieney.videolife.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by liyanju on 2018/1/3.
 */

public class VideoListBean implements Parcelable {

    public boolean hasMore() {
        return false;
    }

    public Object getNextPage(){
        return null;
    }


    public List<VideoListItemBean> getItemList() {
        return null;
    }


    public VideoListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected VideoListBean(Parcel in) {
    }

    public static final Creator<VideoListBean> CREATOR = new Creator<VideoListBean>() {
        @Override
        public VideoListBean createFromParcel(Parcel source) {
            return new VideoListBean(source);
        }

        @Override
        public VideoListBean[] newArray(int size) {
            return new VideoListBean[size];
        }
    };
}
