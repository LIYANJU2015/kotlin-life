package org.cuieney.videolife.entity;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.cuieney.videolife.entity.wyBean.TracksBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuieney on 17/2/28.
 */

public class MusicListBean implements Parcelable {

    public String id;
    public String audiodownload;
    public String shareurl;
    public String duration;
    public String artist_name = "";
    public String album_name;
    public String license_ccurl;
    public String image = "";
    public String name;


    public MusicListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.audiodownload);
        dest.writeString(this.shareurl);
        dest.writeString(this.duration);
        dest.writeString(this.artist_name);
        dest.writeString(this.album_name);
        dest.writeString(this.license_ccurl);
        dest.writeString(this.image);
        dest.writeString(this.name);
    }

    protected MusicListBean(Parcel in) {
        this.id = in.readString();
        this.audiodownload = in.readString();
        this.shareurl = in.readString();
        this.duration = in.readString();
        this.artist_name = in.readString();
        this.album_name = in.readString();
        this.license_ccurl = in.readString();
        this.image = in.readString();
        this.name = in.readString();
    }

    public static final Creator<MusicListBean> CREATOR = new Creator<MusicListBean>() {
        @Override
        public MusicListBean createFromParcel(Parcel source) {
            return new MusicListBean(source);
        }

        @Override
        public MusicListBean[] newArray(int size) {
            return new MusicListBean[size];
        }
    };
}
