package org.cuieney.videolife.common.api;

public class UrlManager {
    public static final String YOUTUBE_HOST = "https://www.googleapis.com/youtube/v3/" ;
    public static final String SOUNDCLOUD_HOST = "http://api.soundcloud.com/";
    public static final String YIREN_HOST = "http://v3.wufazhuce.com:8000/api/";
    public static final String DAILYMOTION_HOST = "https://api.dailymotion.com/";

    public final static String VIMEO_URL = "https://api.vimeo.com/";

    public static String getKuulaCover(String uuid){
        return "https://storage.kuula.co/"+uuid+"/01-cover.jpg";
    }
}
