package org.cuieney.videolife.ui.act;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.api.YoutubeService;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.VideoListItemBean;
import org.cuieney.videolife.ui.video.OnTransitionListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 单独的视频播放页面
 * Created by cuieney on 2016/11/11.
 */
public class PlayActivity extends YouTubeFailureRecoveryActivity {

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";
    public final static String DATA = "DATA";

    @BindView(R.id.video_player)
    YouTubePlayerView videoPlayerView;
    @BindView(R.id.activity_play)
    RelativeLayout playerRelativeLayout;

    private boolean isTransition;

    private Transition transition;

    private VideoListItemBean dataBean;

    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        dataBean = getIntent().getExtras().getParcelable(DATA);
        init();
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return videoPlayerView;
    }

    private boolean isFullScreen;

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.loadVideo(dataBean.getVideoId());
        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullScreen = b;
                LogUtil.d("onFullscreen isFullScreen " + isFullScreen);
            }
        });
    }

    private void init() {
        //过渡动画
        initTransition();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (isFullScreen && youTubePlayer != null) {
            youTubePlayer.setFullscreen(false);
            return;
        }

        //释放所有
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }

    private void initializeYoutubePlayer() {
        try {
            videoPlayerView.initialize(YoutubeService.DEVELOPER_KEY, this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayerView, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            initializeYoutubePlayer();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener(){
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    initializeYoutubePlayer();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }
}
