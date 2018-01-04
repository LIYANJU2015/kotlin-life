package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.VimeoListBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liyanju on 2018/1/4.
 */

public interface VimeoService {

    String CLIENT_ID = "5444eecbd168e6fe94b8365bc029eed20e3ed220";
    String CLIENT_SECRET = "GPaFzrIKvwwTFPTZOiKHD2sVn/ck3zQaMWUbIlPruLmS01KMGBbRa2QXrJrS6l30HT7/+0c313thjnXZ/SS9Padkus26aLmPpsnSuAZ20t7oP+d+4xG2u4XPMnvnaVz9";


    @GET("videos?per_page=15")
    Observable<VimeoListBean> searchVideo(@Header("Authorization") String authorization, @Query("query")String query, @Query("page") int page);
}
