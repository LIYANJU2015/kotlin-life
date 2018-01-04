package org.cuieney.videolife.entity;

import android.os.Parcel;

import org.cuieney.videolife.common.utils.DateUtil;

/**
 * Created by liyanju on 2018/1/3.
 */

public class DMVideoItemListBean extends VideoListItemBean{


    private String channel;

    private String description;

    private int duration;

    private String title;

    private String thumbnail_url;

    private String id;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDuration() {
        return DateUtil.stringForTime(duration * 1000);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return channel;
    }

    @Override
    public String getVideoId() {
        return id;
    }

    @Override
    public String getMaxThumbnailUrl() {
        return thumbnail_url;
    }

    @Override
    public String getNormalThumbnailUrl() {
        return thumbnail_url;
    }


    public DMVideoItemListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.channel);
        dest.writeString(this.description);
        dest.writeInt(this.duration);
        dest.writeString(this.title);
        dest.writeString(this.thumbnail_url);
        dest.writeString(this.id);
    }

    protected DMVideoItemListBean(Parcel in) {
        super(in);
        this.channel = in.readString();
        this.description = in.readString();
        this.duration = in.readInt();
        this.title = in.readString();
        this.thumbnail_url = in.readString();
        this.id = in.readString();
    }

    public static final Creator<DMVideoItemListBean> CREATOR = new Creator<DMVideoItemListBean>() {
        @Override
        public DMVideoItemListBean createFromParcel(Parcel source) {
            return new DMVideoItemListBean(source);
        }

        @Override
        public DMVideoItemListBean[] newArray(int size) {
            return new DMVideoItemListBean[size];
        }
    };
}
