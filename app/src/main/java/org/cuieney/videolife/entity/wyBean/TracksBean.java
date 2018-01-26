package org.cuieney.videolife.entity.wyBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cuieney on 17/3/4.
 */

public class TracksBean implements Parcelable {


    public int id;
    public String title;
    public String stream_url;
    public String artwork_url;
    public long duration;
    public String singer;
    public String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSonger() {
        return singer;
    }

    public String getSongname() {
        return title;
    }

    public void setSongname(String title) {
        this.title = title;
    }

    public String getFilename() {
        return stream_url;
    }

    public void setFilename(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getSongphoto() {
        return artwork_url;
    }

    public void setSongphoto(String artwork_url) {
        this.artwork_url = artwork_url;
    }

    public long getTime() {
        return duration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.stream_url);
        dest.writeString(this.artwork_url);
        dest.writeLong(this.duration);
        dest.writeString(this.singer);
    }

    public TracksBean() {
    }

    protected TracksBean(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.stream_url = in.readString();
        this.artwork_url = in.readString();
        this.duration = in.readLong();
        this.singer = in.readString();
    }

    public static final Creator<TracksBean> CREATOR = new Creator<TracksBean>() {
        @Override
        public TracksBean createFromParcel(Parcel source) {
            return new TracksBean(source);
        }

        @Override
        public TracksBean[] newArray(int size) {
            return new TracksBean[size];
        }
    };
}
