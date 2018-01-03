package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.YouTubeListBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cuieney on 17/2/28.
 */

public interface VideoService {

    String DEVELOPER_KEY = "AIzaSyBUgQUzZJTZrdW9LAY0-hr__UYnKoQRRNU";

    @GET("videos?part=snippet,contentDetails,statistics&chart=mostPopular&maxResults=15&key=" + DEVELOPER_KEY)
    Observable<YouTubeListBean> getVideoList(@Query("pageToken") String pageToken);

}
