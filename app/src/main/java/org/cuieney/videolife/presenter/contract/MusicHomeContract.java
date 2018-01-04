package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;
import org.cuieney.videolife.entity.MusicListBean;

import java.util.List;

/**
 * Created by cuieney on 17/3/4.
 */

public interface MusicHomeContract {

    int SEARCH_SOUDN_CLOUD_TYPE = 11;

    interface View extends BaseView {
        void showContent(List<MusicListBean> musicListBean);
        void error(Throwable throwable);
    }

    interface Presenter extends BasePresenter<MusicHomeContract.View> {
        void getMusicData();
        void getSearchMusicList(String query);
    }
}
