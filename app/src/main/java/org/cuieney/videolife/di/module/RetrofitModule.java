package org.cuieney.videolife.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cuieney.videolife.common.okhttp.CacheInterceptor;
import org.cuieney.videolife.common.okhttp.CookiesManager;
import org.cuieney.videolife.common.utils.AppConfig;
import org.cuieney.videolife.di.HttpLoggingInterceptor;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;


@Module
public class RetrofitModule {
    private final Context context;

    public RetrofitModule(Context context) {
        this.context = context;
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder().
                serializeNulls().
                create();
    }

    @Provides
    public OkHttpClient provideOkhttpClient(Cache cache, CacheInterceptor cacheInterceptor, CookiesManager cookiesManager) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .cache(cache)//添加缓存
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(cacheInterceptor)
//                .cookieJar(cookiesManager)
                .build();

    }

    @Provides
    public CacheInterceptor providesCacheInterceptor() {
        return new CacheInterceptor(context);
    }


    @Provides
    public Cache provideCache() {
        return new Cache(context.getExternalFilesDir(AppConfig.DEFAULT_JOSN_CACHE), AppConfig.DEFAULT_CACHE_SIZE);
    }

    @Provides
    public CookiesManager providesCookies() {
        return new CookiesManager();
    }




}
