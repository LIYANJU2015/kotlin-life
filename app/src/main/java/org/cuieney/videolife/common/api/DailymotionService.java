package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.DMVideosListBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by liyanju on 2018/1/3.
 */

public interface DailymotionService {

    @GET("videos")
    public Observable<DMVideosListBean> getVideos(@Query("page") int page, @QueryMap Map<String, String> options);

    @GET("videos")
    Observable<DMVideosListBean> getSearchVideos(@QueryMap Map<String, String> options, @Query("search") String search, @Query("page") int page);
}
