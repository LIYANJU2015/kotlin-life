package org.cuieney.videolife.entity;

import org.cuieney.videolife.common.utils.DateUtil;
import org.cuieney.videolife.common.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by liyanju on 2018/1/4.
 */

public class VimeoItemListBean extends VideoListItemBean {

    private String name;

    private int duration;

    private String description;

    private String uri;

    private Pictures pictures;

    public class Pictures {

        public ArrayList<Size> sizes = new ArrayList<>();

        public class Size {
            public String link;
        }
    }

    @Override
    public String getNormalThumbnailUrl() {
        if (pictures != null && pictures.sizes.size() > 3) {
            return pictures.sizes.get(pictures.sizes.size() - 2).link;
        }
        return "";
    }

    @Override
    public String getMaxThumbnailUrl() {
        if (pictures != null && pictures.sizes.size() > 2) {
            return pictures.sizes.get(pictures.sizes.size() - 1).link;
        }
        return "";
    }

    @Override
    public String getVideoId() {
        String target = "/videos/";
        if (uri != null && uri.contains(target)) {
            uri = uri.substring(uri.indexOf(target)+target.length() , uri.length());
        }
        LogUtil.d("getVideoId uri " + uri);
        return uri;
    }

    @Override
    public String getCategory() {
        return "funny";
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getDuration() {
        return DateUtil.stringForTime(duration * 1000);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
