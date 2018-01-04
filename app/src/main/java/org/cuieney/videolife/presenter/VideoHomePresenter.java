package org.cuieney.videolife.presenter;

import org.cuieney.videolife.common.api.DailymotionService;
import org.cuieney.videolife.common.api.VimeoService;
import org.cuieney.videolife.common.api.YoutubeService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.common.utils.RxUtil;
import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.presenter.contract.VideoHomeContract;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by cuieney on 17/2/24.
 */

public class VideoHomePresenter extends RxPresenter<VideoHomeContract.View> implements VideoHomeContract.Presenter{

    private YoutubeService mYoutubeService;
    private DailymotionService mDailymotionService;
    private VimeoService mVimeoService;

    private String authorization;

    private static final HashMap<String, String> sWatchVideosMap = new HashMap<>(4);
    private static final HashMap<String, String> sSearchVideosMap = new HashMap<>(4);
    static {
        sWatchVideosMap.put("list", "what-to-watch");
        sWatchVideosMap.put("sort", "visited");
        sWatchVideosMap.put("fields", "title,channel,channel.id,description,duration,id,thumbnail_url,updated_time");
        sWatchVideosMap.put("limit", "15");

        sSearchVideosMap.put("fields", "title,channel,channel.id,description,duration,id,thumbnail_url,updated_time");
        sSearchVideosMap.put("sort", "visited");
        sSearchVideosMap.put("limit", "15");
    }


    @Inject
    public VideoHomePresenter(YoutubeService mRetrofitHelper,
                              DailymotionService dailymotionService, VimeoService vimeoService) {
        mYoutubeService = mRetrofitHelper;
        mDailymotionService = dailymotionService;
        mVimeoService = vimeoService;
        authorization = Utils.basic(VimeoService.CLIENT_ID, VimeoService.CLIENT_SECRET);
    }

    @Override
    public void searchVideoData(String query, Object nextPage, int searchType) {
        Subscription rxSubscription = null;
        if (searchType == VideoHomeContract.SEARCH_YOUTUBE_TYPE) {
            if (nextPage == null) {
                nextPage = "";
            }
            rxSubscription = mYoutubeService.getSearchVideos((String)nextPage, query)
                    .compose(RxUtil.rxSchedulerHelper())
                    .subscribe(videoListBean -> {
                        mView.showContent(videoListBean);
                    }, throwable -> {
                        mView.error(throwable);
                    });
        } else if (searchType == VideoHomeContract.SEARCH_DAILYMOTION_TYPE) {
            if (nextPage == null) {
                nextPage = 1;
            }
            rxSubscription = mDailymotionService.getSearchVideos(sSearchVideosMap, query, (int)nextPage)
                    .compose(RxUtil.rxSchedulerHelper())
                    .subscribe(videoListBean -> {
                        mView.showContent(videoListBean);
                    }, throwable -> {
                        mView.error(throwable);
                    });
        } else if (searchType == VideoHomeContract.SEARCH_VIMEN_TYPE) {
            if (nextPage == null) {
                nextPage = 1;
            }
            rxSubscription = mVimeoService.searchVideo(authorization, query, (int)nextPage)
                    .compose(RxUtil.rxSchedulerHelper())
                    .subscribe(videoListBean -> {
                        mView.showContent(videoListBean);
                    }, throwable -> {
                        mView.error(throwable);
                    });
        }
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getVideoData(Object nextPage, int requstType) {
        Subscription rxSubscription = null;
        if (requstType == VideoHomeContract.YOUTUBE_TYPE) {
            if (nextPage == null) {
                nextPage = "";
            }
            rxSubscription = mYoutubeService.getYoutubeVideoList((String)nextPage)
                    .compose(RxUtil.rxSchedulerHelper())
                    .subscribe(videoListBean -> {
                        mView.showContent(videoListBean);
                    }, throwable -> {
                        mView.error(throwable);
                    });
        } else if (requstType == VideoHomeContract.DAILYMOTION_TYPE) {
            if (nextPage == null) {
                nextPage = 1;
            }
            rxSubscription = mDailymotionService.getVideos((int)nextPage, sWatchVideosMap)
                    .compose(RxUtil.rxSchedulerHelper())
                    .subscribe(videoListBean -> {
                        mView.showContent(videoListBean);
                    }, throwable -> {
                        mView.error(throwable);
                    });
        } else if (requstType == VideoHomeContract.VIMEN_TYPE) {
            if (nextPage == null) {
                nextPage = 1;
            }
            rxSubscription = mVimeoService.searchVideo(authorization,
                    "funny", (int)nextPage)
                    .compose(RxUtil.rxSchedulerHelper())
                    .subscribe(videoListBean -> {
                        mView.showContent(videoListBean);
                    }, throwable -> {
                        mView.error(throwable);
                    });
        }
        addSubscrebe(rxSubscription);
    }
}
