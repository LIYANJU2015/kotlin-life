package org.cuieney.videolife.entity;

import android.os.Parcel;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuieney on 17/2/24.
 */

public class YouTubeListBean extends VideoListBean {


    public String nextPageToken;
    public List<VideoListItemBean> itemList = new ArrayList<>();

    @Override
    public String getNextPage() {
        return nextPageToken;
    }

    @Override
    public List<VideoListItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<VideoListItemBean> itemList) {
        this.itemList = itemList;
    }

    public YouTubeListBean() {
    }

    @Override
    public boolean hasMore() {
        return !TextUtils.isEmpty(nextPageToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.nextPageToken);
        dest.writeTypedList(this.itemList);
    }

    protected YouTubeListBean(Parcel in) {
        super(in);
        this.nextPageToken = in.readString();
        this.itemList = in.createTypedArrayList(VideoListItemBean.CREATOR);
    }

    public static final Creator<YouTubeListBean> CREATOR = new Creator<YouTubeListBean>() {
        @Override
        public YouTubeListBean createFromParcel(Parcel source) {
            return new YouTubeListBean(source);
        }

        @Override
        public YouTubeListBean[] newArray(int size) {
            return new YouTubeListBean[size];
        }
    };
}
