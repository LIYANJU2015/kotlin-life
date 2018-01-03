package org.cuieney.videolife.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cuieney.videolife.common.api.OpApiService;
import org.cuieney.videolife.common.api.UrlManager;
import org.cuieney.videolife.common.api.VideoService;
import org.cuieney.videolife.common.api.SoundCloudApiService;
import org.cuieney.videolife.entity.YouTubeListBean;
import org.cuieney.videolife.entity.YoutubeSnippetDeserializer;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cuieney on 17/2/28.
 */

public class RetrofitHelper {

    OkHttpClient client;

    private VideoService kyApiService;
    private SoundCloudApiService wyApiService;
    private OpApiService opApiService;

    @Inject
    public RetrofitHelper(OkHttpClient client) {
        this.client = client;
        init();
    }

    private void init() {
        kyApiService = getApiService(UrlManager.YOUTUBE_HOST,VideoService.class);
        wyApiService = getApiService(UrlManager.SOUNDCLOUD_HOST,SoundCloudApiService.class);
        opApiService = getApiService(UrlManager.YIREN_HOST,OpApiService.class);

    }
    private <T> T getApiService(String baseUrl, Class<T> clz) {
        GsonConverterFactory gsonConverterFactory;
        if (UrlManager.YOUTUBE_HOST.equals(baseUrl)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(YouTubeListBean.class, new YoutubeSnippetDeserializer());
            Gson gson = gsonBuilder.create();
            gsonConverterFactory = GsonConverterFactory.create(gson);
        } else {
            gsonConverterFactory = GsonConverterFactory.create();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(clz);
    }

    public VideoService getKyApiService() {
        return kyApiService;
    }

    public SoundCloudApiService getWyApiService(){
        return wyApiService;
    }

    public OpApiService getOpApiService() {
        return opApiService;
    }
}
