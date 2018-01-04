package org.cuieney.videolife.ui.act;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.cuieney.videolife.R;

/**
 * Created by liyanju on 2018/1/4.
 */

public class VimeoPlayerActivity extends AppCompatActivity{

    private String vid;

    private ProgressBar progressBar;

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vimeo_player_layout);

        if (savedInstanceState == null) {
            vid = getIntent().getStringExtra("vid");
        } else {
            vid = savedInstanceState.getString("vid");
        }

        progressBar = (ProgressBar)findViewById(R.id.loading);
        webView = (WebView) findViewById(R.id.web_view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("vid", vid);
    }
}
