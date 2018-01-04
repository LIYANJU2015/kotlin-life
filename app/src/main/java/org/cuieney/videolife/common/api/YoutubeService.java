package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.YouTubeListBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by cuieney on 17/2/28.
 */

public interface YoutubeService {

    String DEVELOPER_KEY = "AIzaSyBUgQUzZJTZrdW9LAY0-hr__UYnKoQRRNU";

    @GET("videos?part=snippet,contentDetails,statistics&chart=mostPopular&maxResults=15&key=" + DEVELOPER_KEY)
    Observable<YouTubeListBean> getYoutubeVideoList(@Query("pageToken") String pageToken);

    @GET("search?part=snippet&safeSearch=none&type=video&maxResults=15&key=" + DEVELOPER_KEY)
    Observable<YouTubeListBean> getSearchVideos(@Query("pageToken") String pageToken, @Query("q") String queryContent);





}
