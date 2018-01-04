package org.cuieney.videolife.entity;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyanju on 2018/1/3.
 */

public class DMVideosListBean extends VideoListBean{


    private int page;

    private boolean has_more;

    public ArrayList<VideoListItemBean> list;

    public void setNextPage(int page) {
        page++;
        this.page = page;
    }

    @Override
    public boolean hasMore() {
        return has_more;
    }

    public void setHasMore(boolean more) {
        has_more = more;
    }

    @Override
    public List<VideoListItemBean> getItemList() {
        return list;
    }

    @Override
    public Object getNextPage() {
        return page;
    }


    public DMVideosListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.page);
        dest.writeByte(this.has_more ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.list);
    }

    protected DMVideosListBean(Parcel in) {
        super(in);
        this.page = in.readInt();
        this.has_more = in.readByte() != 0;
        this.list = in.createTypedArrayList(VideoListItemBean.CREATOR);
    }

    public static final Creator<DMVideosListBean> CREATOR = new Creator<DMVideosListBean>() {
        @Override
        public DMVideosListBean createFromParcel(Parcel source) {
            return new DMVideosListBean(source);
        }

        @Override
        public DMVideosListBean[] newArray(int size) {
            return new DMVideosListBean[size];
        }
    };
}
