package org.cuieney.videolife.entity;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyanju on 2018/1/4.
 */

public class VimeoListBean extends VideoListBean {

    private int total;
    private int page;
    private int per_page;

    private List<VideoListItemBean> data = new ArrayList<>();

    public void setNextPage(int page) {
       this.page = page;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPerPage(int per_page) {
        this.per_page = per_page;
    }

    @Override
    public Object getNextPage() {
        if (hasMore()) {
            page++;
            return page;
        }
        return page;
    }

    @Override
    public List<VideoListItemBean> getItemList() {
        return data;
    }

    @Override
    public boolean hasMore() {
        return page * per_page < total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.total);
        dest.writeInt(this.page);
        dest.writeInt(this.per_page);
        dest.writeTypedList(this.data);
    }

    public VimeoListBean() {
    }

    protected VimeoListBean(Parcel in) {
        super(in);
        this.total = in.readInt();
        this.page = in.readInt();
        this.per_page = in.readInt();
        this.data = in.createTypedArrayList(VimeoItemListBean.CREATOR);
    }

    public static final Creator<VimeoListBean> CREATOR = new Creator<VimeoListBean>() {
        @Override
        public VimeoListBean createFromParcel(Parcel source) {
            return new VimeoListBean(source);
        }

        @Override
        public VimeoListBean[] newArray(int size) {
            return new VimeoListBean[size];
        }
    };
}
