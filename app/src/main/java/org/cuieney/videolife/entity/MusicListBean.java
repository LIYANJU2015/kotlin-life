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


    private String title;
    private String description;
    private String artwork_url;
    private List<TracksBean> tracks;

    public String getMname() {
        return title;
    }

    public String getMdesc() {
        return description;
    }

    public String getOphoto() {
        if (TextUtils.isEmpty(artwork_url) & tracks != null) {
            for (TracksBean tracksBean : tracks) {
                if (!TextUtils.isEmpty(tracksBean.getSongphoto())) {
                    return tracksBean.getSongphoto();
                }
            }
        } else {
            return artwork_url;
        }

        return "";
    }

    public List<TracksBean> getTracks() {
        return tracks;
    }

    public void setTracks(List<TracksBean> tracks) {
        this.tracks = tracks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.artwork_url);
        dest.writeList(this.tracks);
    }

    public MusicListBean() {
    }

    protected MusicListBean(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.artwork_url = in.readString();
        this.tracks = new ArrayList<TracksBean>();
        in.readList(this.tracks, TracksBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MusicListBean> CREATOR = new Parcelable.Creator<MusicListBean>() {
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
