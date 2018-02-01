package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.MusicListBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cuieney on 17/2/28.
 */

public interface SoundCloudApiService {


    @GET("api/search.php")
    Observable<List<MusicListBean>> getSearchMusicList(@Query("q") String query);
}
