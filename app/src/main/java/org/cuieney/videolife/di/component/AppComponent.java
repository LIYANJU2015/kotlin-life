package org.cuieney.videolife.di.component;


import android.content.Context;

import org.cuieney.videolife.common.api.DailymotionService;
import org.cuieney.videolife.common.api.VimeoService;
import org.cuieney.videolife.common.api.YoutubeService;
import org.cuieney.videolife.common.api.OpApiService;
import org.cuieney.videolife.common.api.SoundCloudApiService;
import org.cuieney.videolife.di.module.AppModule;
import org.cuieney.videolife.di.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by cuieney on 16/8/7.
 */

@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class})
public interface AppComponent {
    Context getContext();
    YoutubeService getKyApiService();
    SoundCloudApiService getWyApiService();
    OpApiService getOpApiService();
    DailymotionService getDailymotionService();
    VimeoService getVimeoService();
}
