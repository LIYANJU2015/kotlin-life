package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;
import org.cuieney.videolife.entity.VideoListBean;
import org.cuieney.videolife.entity.YouTubeListBean;

/**
 * Created by cuieney on 17/2/24.
 */

public interface VideoHomeContract {

    interface View extends BaseView{
        void showContent(VideoListBean videoListBean);
        void error(Throwable throwable);
    }

    int YOUTUBE_TYPE = 1;
    int DAILYMOTION_TYPE = 2;
    int VIMEN_TYPE = 3;
    int SEARCH_YOUTUBE_TYPE = 4;
    int SEARCH_DAILYMOTION_TYPE = 5;
    int SEARCH_VIMEN_TYPE = 6;

    interface Presenter extends BasePresenter<View> {

        void getVideoData(Object nextPage, int requstType);

        void searchVideoData(String query, Object nextPage, int searchType);

    }
}
