package org.cuieney.videolife.presenter;

import org.cuieney.videolife.common.api.SoundCloudApiService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.common.utils.RxUtil;
import org.cuieney.videolife.presenter.contract.MusicHomeContract;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicHomePresenter extends RxPresenter<MusicHomeContract.View> implements MusicHomeContract.Presenter {
    private SoundCloudApiService mRetrofitHelper;

    @Inject
    public MusicHomePresenter(SoundCloudApiService mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getSearchMusicList(String query) {
        Subscription rxSubscription = mRetrofitHelper.getSearchMusicList(query)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(musicListBean -> {
                    mView.showContent(musicListBean);
                }, throwable -> {
                    mView.error(throwable);
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getMusicData() {
        Subscription rxSubscription = mRetrofitHelper.getMusicList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(musicListBean -> {
                    mView.showContent(musicListBean);
                }, throwable -> {
                    mView.error(throwable);
                });
        addSubscrebe(rxSubscription);
    }
}
