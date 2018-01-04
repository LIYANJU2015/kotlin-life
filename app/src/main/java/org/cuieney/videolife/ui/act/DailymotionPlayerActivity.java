package org.cuieney.videolife.ui.act;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.dailymotion.websdk.DMWebVideoView;
import com.dailymotion.websdk.DMWebViewEvent;
import com.jaeger.library.StatusBarUtil;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.ui.video.OnTransitionListener;

import java.util.HashMap;

/**
 * Created by liyanju on 2018/1/3.
 */

public class DailymotionPlayerActivity extends AppCompatActivity implements DMWebViewEvent {

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";
    public final static String DATA = "DATA";

    private DMWebVideoView mVideoView;

    private String vid;

    private boolean isTransition;

    private Transition transition;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black));

        setContentView(R.layout.dailymotion_player_layout);

        vid = getIntent().getStringExtra("vid");
        LogUtil.d("DailymotionPlayer vid " + vid);

        mVideoView = (DMWebVideoView) findViewById(R.id.dmWebVideoView);
        progressBar = (ProgressBar) findViewById(R.id.dm_progressbar);

        initTransition();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //释放所有
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }

    private void initVideoPlayer() {
        mVideoView.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.black));
        mVideoView.load(vid);
        mVideoView.setEventListener(new DMWebVideoView.EventListener() {
            @Override
            public void onEvent(String event, HashMap<String, String> map) {
                if ("start".equals(event)) {
                    progressBar.setVisibility(View.GONE);
                } else if (DMWebVideoView.EVENT_FULLSCREEN_TOGGLE_REQUESTED.equals(event)) {

                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener(){
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    initVideoPlayer();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(mVideoView, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            initVideoPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
    }

    @Override
    public void onStartVideo() {

    }

    @Override
    public void onLoadedmetadata() {

    }

    @Override
    public void onProgress(double bufferedTime) {

    }

    @Override
    public void onDurationchange(double duration) {

    }

    @Override
    public void onSeeking(double currentTime) {

    }

    @Override
    public void onRebuffer(boolean rebuffer) {

    }

    @Override
    public void qualitiesavailable() {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onPause(boolean paused) {

    }

    @Override
    public void onFullscreenchange(boolean fullscreen) {

    }

    @Override
    public void onEnd(boolean end) {

    }
}
