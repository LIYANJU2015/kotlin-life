package org.cuieney.videolife.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cuieney.videolife.common.api.DailymotionService;
import org.cuieney.videolife.common.api.OpApiService;
import org.cuieney.videolife.common.api.UrlManager;
import org.cuieney.videolife.common.api.VimeoService;
import org.cuieney.videolife.common.api.YoutubeService;
import org.cuieney.videolife.common.api.SoundCloudApiService;
import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.entity.DMVideosListBean;
import org.cuieney.videolife.entity.DailymotionDeserializer;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.entity.SoundCloudDeserializer;
import org.cuieney.videolife.entity.VimeoDeserializer;
import org.cuieney.videolife.entity.VimeoListBean;
import org.cuieney.videolife.entity.YouTubeListBean;
import org.cuieney.videolife.entity.YoutubeSnippetDeserializer;

import java.util.List;

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

    private YoutubeService kyApiService;
    private SoundCloudApiService wyApiService;
    private OpApiService opApiService;
    private DailymotionService dailymotionService;
    private VimeoService vimeoService;

    @Inject
    public RetrofitHelper(OkHttpClient client) {
        this.client = client;
        init();
    }

    private void init() {
        kyApiService = getApiService(UrlManager.YOUTUBE_HOST, YoutubeService.class);
        wyApiService = getApiService(UrlManager.SOUNDCLOUD_HOST, SoundCloudApiService.class);
        opApiService = getApiService(UrlManager.YIREN_HOST, OpApiService.class);
        dailymotionService = getApiService(UrlManager.DAILYMOTION_HOST, DailymotionService.class);
        vimeoService = getApiService(UrlManager.VIMEO_URL, VimeoService.class);
    }

    private <T> T getApiService(String baseUrl, Class<T> clz) {
        GsonConverterFactory gsonConverterFactory;
        if (UrlManager.YOUTUBE_HOST.equals(baseUrl)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(YouTubeListBean.class, new YoutubeSnippetDeserializer());
            Gson gson = gsonBuilder.create();
            gsonConverterFactory = GsonConverterFactory.create(gson);
        } else if (UrlManager.DAILYMOTION_HOST.equals(baseUrl)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(DMVideosListBean.class, new DailymotionDeserializer());
            Gson gson = gsonBuilder.create();
            gsonConverterFactory = GsonConverterFactory.create(gson);
        } else if (UrlManager.VIMEO_URL.equals(baseUrl)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(VimeoListBean.class, new VimeoDeserializer());
            Gson gson = gsonBuilder.create();
            gsonConverterFactory = GsonConverterFactory.create(gson);
        }else if (UrlManager.SOUNDCLOUD_HOST.equals(baseUrl)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(MusicListBean.class, new SoundCloudDeserializer());
            Gson gson = gsonBuilder.create();
            gsonConverterFactory = GsonConverterFactory.create(gson);
        }else {
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

    public YoutubeService getKyApiService() {
        return kyApiService;
    }

    public SoundCloudApiService getWyApiService(){
        return wyApiService;
    }

    public OpApiService getOpApiService() {
        return opApiService;
    }

    public DailymotionService getDailymotionService() {
        return dailymotionService;
    }

    public VimeoService getVimeoService() {
        return vimeoService;
    }
}
