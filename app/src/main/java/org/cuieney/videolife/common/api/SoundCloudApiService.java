package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.entity.wyBean.TracksBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cuieney on 17/2/28.
 */

public interface SoundCloudApiService {

    String CLIENT_ID = "LegNTza81OuwVaDfYELQW1X71tY1sot8";

    @GET("playlists?limit=100&client_id=" + CLIENT_ID)
    Observable<List<MusicListBean>> getMusicList();

    @GET("tracks?limit=100&client_id=" + CLIENT_ID)
    Observable<List<MusicListBean>> getSearchMusicList(@Query("q") String query);
}
