package org.cuieney.videolife.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.cuieney.videolife.common.utils.DateUtil;

/**
 * Created by cuieney on 17/2/26.
 */

public class YoutubeItemListBean extends VideoListItemBean implements Parcelable {
    public String publishedAt;
    public String channelId;
    public String title;
    public String description;
    public ThumbnailsBean thumbnails;
    public String channelTitle;
    public String categoryId;

    public String vid;

    public ContentDetails contentDetails;

    public Statistics statistics;

    @Override
    public String getVideoId() {
        return vid;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getDuration() {
        if (contentDetails != null) {
            return DateUtil.convertDuration(contentDetails.duration);
        }
        return "";
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return channelTitle;
    }

    public YoutubeItemListBean() {
    }

    public String getMaxThumbnailUrl() {
        if (thumbnails != null && thumbnails.getMaxres() != null) {
            return thumbnails.getMaxres().getUrl();
        } else if (thumbnails != null && thumbnails.getHigh() != null) {
            return thumbnails.getHigh().getUrl();
        } else if (thumbnails != null && thumbnails.getDefaultX() != null) {
            return thumbnails.getDefaultX().getUrl();
        }
        return "";
    }

    public String getNormalThumbnailUrl() {
        if (thumbnails != null && thumbnails.getStandard() != null) {
            return thumbnails.getStandard().getUrl();
        } else if (thumbnails != null && thumbnails.getHigh() != null) {
            return thumbnails.getHigh().getUrl();
        } else if (thumbnails != null && thumbnails.getDefaultX() != null) {
            return thumbnails.getDefaultX().getUrl();
        }
        return "";
    }

    public static class ThumbnailsBean implements Parcelable{

        @SerializedName("default")
        private DefaultBean defaultX;
        private MediumBean medium;
        private HighBean high;
        private StandardBean standard;
        private MaxresBean maxres;

        public DefaultBean getDefaultX() {
            return defaultX;
        }

        public void setDefaultX(DefaultBean defaultX) {
            this.defaultX = defaultX;
        }

        public MediumBean getMedium() {
            return medium;
        }

        public void setMedium(MediumBean medium) {
            this.medium = medium;
        }

        public HighBean getHigh() {
            return high;
        }

        public void setHigh(HighBean high) {
            this.high = high;
        }

        public StandardBean getStandard() {
            return standard;
        }

        public void setStandard(StandardBean standard) {
            this.standard = standard;
        }

        public MaxresBean getMaxres() {
            return maxres;
        }

        public void setMaxres(MaxresBean maxres) {
            this.maxres = maxres;
        }



        public static class DefaultBean implements Parcelable{
            /**
             * url : https://i.ytimg.com/vi/6ajP1v4Dgfs/default.jpg
             * width : 120
             * height : 90
             */

            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.url);
                dest.writeInt(this.width);
                dest.writeInt(this.height);
            }

            public DefaultBean() {
            }

            protected DefaultBean(Parcel in) {
                this.url = in.readString();
                this.width = in.readInt();
                this.height = in.readInt();
            }

            public static final Creator<DefaultBean> CREATOR = new Creator<DefaultBean>() {
                @Override
                public DefaultBean createFromParcel(Parcel source) {
                    return new DefaultBean(source);
                }

                @Override
                public DefaultBean[] newArray(int size) {
                    return new DefaultBean[size];
                }
            };
        }

        public static class MediumBean implements Parcelable{
            /**
             * url : https://i.ytimg.com/vi/6ajP1v4Dgfs/mqdefault.jpg
             * width : 320
             * height : 180
             */

            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.url);
                dest.writeInt(this.width);
                dest.writeInt(this.height);
            }

            public MediumBean() {
            }

            protected MediumBean(Parcel in) {
                this.url = in.readString();
                this.width = in.readInt();
                this.height = in.readInt();
            }

            public static final Creator<MediumBean> CREATOR = new Creator<MediumBean>() {
                @Override
                public MediumBean createFromParcel(Parcel source) {
                    return new MediumBean(source);
                }

                @Override
                public MediumBean[] newArray(int size) {
                    return new MediumBean[size];
                }
            };
        }

        public static class HighBean implements Parcelable{
            /**
             * url : https://i.ytimg.com/vi/6ajP1v4Dgfs/hqdefault.jpg
             * width : 480
             * height : 360
             */

            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.url);
                dest.writeInt(this.width);
                dest.writeInt(this.height);
            }

            public HighBean() {
            }

            protected HighBean(Parcel in) {
                this.url = in.readString();
                this.width = in.readInt();
                this.height = in.readInt();
            }

            public static final Creator<HighBean> CREATOR = new Creator<HighBean>() {
                @Override
                public HighBean createFromParcel(Parcel source) {
                    return new HighBean(source);
                }

                @Override
                public HighBean[] newArray(int size) {
                    return new HighBean[size];
                }
            };
        }

        public static class StandardBean implements Parcelable{
            /**
             * url : https://i.ytimg.com/vi/6ajP1v4Dgfs/sddefault.jpg
             * width : 640
             * height : 480
             */

            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.url);
                dest.writeInt(this.width);
                dest.writeInt(this.height);
            }

            public StandardBean() {
            }

            protected StandardBean(Parcel in) {
                this.url = in.readString();
                this.width = in.readInt();
                this.height = in.readInt();
            }

            public static final Creator<StandardBean> CREATOR = new Creator<StandardBean>() {
                @Override
                public StandardBean createFromParcel(Parcel source) {
                    return new StandardBean(source);
                }

                @Override
                public StandardBean[] newArray(int size) {
                    return new StandardBean[size];
                }
            };
        }

        public static class MaxresBean implements Parcelable{
            /**
             * url : https://i.ytimg.com/vi/6ajP1v4Dgfs/maxresdefault.jpg
             * width : 1280
             * height : 720
             */

            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.url);
                dest.writeInt(this.width);
                dest.writeInt(this.height);
            }

            public MaxresBean() {
            }

            protected MaxresBean(Parcel in) {
                this.url = in.readString();
                this.width = in.readInt();
                this.height = in.readInt();
            }

            public static final Creator<MaxresBean> CREATOR = new Creator<MaxresBean>() {
                @Override
                public MaxresBean createFromParcel(Parcel source) {
                    return new MaxresBean(source);
                }

                @Override
                public MaxresBean[] newArray(int size) {
                    return new MaxresBean[size];
                }
            };
        }

        public ThumbnailsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.defaultX, flags);
            dest.writeParcelable(this.medium, flags);
            dest.writeParcelable(this.high, flags);
            dest.writeParcelable(this.standard, flags);
            dest.writeParcelable(this.maxres, flags);
        }

        protected ThumbnailsBean(Parcel in) {
            this.defaultX = in.readParcelable(DefaultBean.class.getClassLoader());
            this.medium = in.readParcelable(MediumBean.class.getClassLoader());
            this.high = in.readParcelable(HighBean.class.getClassLoader());
            this.standard = in.readParcelable(StandardBean.class.getClassLoader());
            this.maxres = in.readParcelable(MaxresBean.class.getClassLoader());
        }

        public static final Creator<ThumbnailsBean> CREATOR = new Creator<ThumbnailsBean>() {
            @Override
            public ThumbnailsBean createFromParcel(Parcel source) {
                return new ThumbnailsBean(source);
            }

            @Override
            public ThumbnailsBean[] newArray(int size) {
                return new ThumbnailsBean[size];
            }
        };
    }

    public static class Statistics implements Parcelable{
        private String viewCount;
        private String likeCount;
        private String dislikeCount;
        private String favoriteCount;
        private String commentCount;

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(String dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public String getFavoriteCount() {
            return favoriteCount;
        }

        public void setFavoriteCount(String favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.viewCount);
            dest.writeString(this.likeCount);
            dest.writeString(this.dislikeCount);
            dest.writeString(this.favoriteCount);
            dest.writeString(this.commentCount);
        }

        public Statistics() {
        }

        protected Statistics(Parcel in) {
            this.viewCount = in.readString();
            this.likeCount = in.readString();
            this.dislikeCount = in.readString();
            this.favoriteCount = in.readString();
            this.commentCount = in.readString();
        }

        public static final Creator<Statistics> CREATOR = new Creator<Statistics>() {
            @Override
            public Statistics createFromParcel(Parcel source) {
                return new Statistics(source);
            }

            @Override
            public Statistics[] newArray(int size) {
                return new Statistics[size];
            }
        };
    }

    public static class ContentDetails implements Parcelable{
        public String duration;
        public String dimension;
        public String definition;
        public String caption;
        public boolean licensedContent;
        public String projection;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.duration);
            dest.writeString(this.dimension);
            dest.writeString(this.definition);
            dest.writeString(this.caption);
            dest.writeByte(this.licensedContent ? (byte) 1 : (byte) 0);
            dest.writeString(this.projection);
        }

        public ContentDetails() {
        }

        protected ContentDetails(Parcel in) {
            this.duration = in.readString();
            this.dimension = in.readString();
            this.definition = in.readString();
            this.caption = in.readString();
            this.licensedContent = in.readByte() != 0;
            this.projection = in.readString();
        }

        public static final Creator<ContentDetails> CREATOR = new Creator<ContentDetails>() {
            @Override
            public ContentDetails createFromParcel(Parcel source) {
                return new ContentDetails(source);
            }

            @Override
            public ContentDetails[] newArray(int size) {
                return new ContentDetails[size];
            }
        };
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof YoutubeItemListBean
                && (((YoutubeItemListBean) obj).title.equals(title) && (((YoutubeItemListBean) obj).description
                .equals(description)))) {
            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.publishedAt);
        dest.writeString(this.channelId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(this.thumbnails, flags);
        dest.writeString(this.channelTitle);
        dest.writeString(this.categoryId);
        dest.writeString(this.vid);
        dest.writeParcelable(this.contentDetails, flags);
        dest.writeParcelable(this.statistics, flags);
    }

    protected YoutubeItemListBean(Parcel in) {
        this.publishedAt = in.readString();
        this.channelId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.thumbnails = in.readParcelable(ThumbnailsBean.class.getClassLoader());
        this.channelTitle = in.readString();
        this.categoryId = in.readString();
        this.vid = in.readString();
        this.contentDetails = in.readParcelable(ContentDetails.class.getClassLoader());
        this.statistics = in.readParcelable(Statistics.class.getClassLoader());
    }

    public static final Creator<YoutubeItemListBean> CREATOR = new Creator<YoutubeItemListBean>() {
        @Override
        public YoutubeItemListBean createFromParcel(Parcel source) {
            return new YoutubeItemListBean(source);
        }

        @Override
        public YoutubeItemListBean[] newArray(int size) {
            return new YoutubeItemListBean[size];
        }
    };
}
