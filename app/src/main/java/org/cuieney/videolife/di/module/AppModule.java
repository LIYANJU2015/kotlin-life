package org.cuieney.videolife.di.module;

import android.content.Context;


import org.cuieney.videolife.App;
import org.cuieney.videolife.common.api.VideoService;
import org.cuieney.videolife.common.api.OpApiService;
import org.cuieney.videolife.common.api.SoundCloudApiService;
import org.cuieney.videolife.di.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    public final App mApp;


    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApp;
    }


    @Provides
    VideoService proviesKyService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getKyApiService();
    }

    @Provides
    SoundCloudApiService proviesWyService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getWyApiService();
    }


    @Provides
    OpApiService proviesOpService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getOpApiService();
    }
}
