package org.cuieney.videolife.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.admodule.AdModule;
import com.admodule.adfb.IFacebookAd;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jaeger.library.StatusBarUtil;
import com.rating.RatingActivity;

import org.cuieney.videolife.App;
import org.cuieney.videolife.FacebookReportUtils;
import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseMainFragment;
import org.cuieney.videolife.common.base.SimpleActivity;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.common.utils.PreferenceUtil;
import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.presenter.contract.MusicHomeContract;
import org.cuieney.videolife.ui.fragment.music.DownloadFragment;
import org.cuieney.videolife.ui.fragment.music.MusicFragment;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_DEFAULT;
import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_STATIC;
import static com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED;

public class MainActivity extends SimpleActivity implements BaseMainFragment.OnBackToFirstListener {


    private static final String TAG = "Main";
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mNavigationView;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 111;

    List<SupportFragment> mFragments;

    @Override
    protected int getLayout() {
        return R.layout.design_layout;
    }

    private void startVoiceRecognitionActivity() {
        try {
            // 通过Intent传递语音识别的模式，开启语音
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            // 语言模式和自由模式的语音识别
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            // 提示语音开始
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.start_speech));
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().getLanguage());
            // 开始语音识别
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                doSearch(results.get(0));
            }
        }
    }

    @Override
    protected void initEventAndData() {
        mFragments = new ArrayList<>();
        mFragments.add(MusicFragment.newInstance(MusicFragment.ALBUM_TYPE));
        mFragments.add(MusicFragment.newInstance(MusicFragment.ARTISTS_TYPE));
        mFragments.add(MusicFragment.newInstance(MusicFragment.SONGS_TYPE));
        mFragments.add(DownloadFragment.newInstance());
        loadMultipleRootFragment(R.id.act_container, 0
                , mFragments.get(0)
                , mFragments.get(1)
                , mFragments.get(2)
                , mFragments.get(3)

        );

        initView();
        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentSupportVisible(SupportFragment fragment) {
                Log.i("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_voice_rec) {
                    startVoiceRecognitionActivity();
                }
            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                doSearch(currentQuery);
            }
        });

        if (App.sIsCoolStart) {
            App.sIsCoolStart = false;
            AdModule.getInstance().getFacebookAd().setLoadListener(new IFacebookAd.FacebookAdListener() {
                @Override
                public void onLoadedAd(View view) {
                    AdModule.getInstance().getFacebookAd().setLoadListener(null);
                    AdModule.getInstance().createMaterialDialog().showAdDialog(MainActivity.this, view);
                    AdModule.getInstance().getFacebookAd().loadAd(false, "146773445984805_146773949318088");
                }

                @Override
                public void onStartLoadAd(View view) {
                }

                @Override
                public void onLoadAdFailed(int i, String s) {
                    AdModule.getInstance().getFacebookAd().setLoadListener(null);
                    AdModule.getInstance().getAdMob().showInterstitialAd();
                    AdModule.getInstance().getFacebookAd().loadAd(false, "146773445984805_146773949318088");
                }
            });
            AdModule.getInstance().getFacebookAd().loadAd(false, "146773445984805_146773862651430");
        } else {
            AdModule.getInstance().getAdMob().requestNewInterstitial();
            AdModule.getInstance().getFacebookAd().loadAd(false, "146773445984805_146773949318088");
        }
    }

    private void doSearch(String query) {
        LogUtil.d("onSearchAction currentType " + currentType);
        SupportFragment supportFragment = mFragments.get(currentPostion);
        if (supportFragment instanceof MusicFragment) {
            ((MusicFragment) supportFragment).addSearchFragment(currentType, query);
        }
        FacebookReportUtils.logSentPageShow("search soundcloud query " + query);

    }

    public static int sCurrentStatusColor = Color.parseColor("#E64A19");

    private int currentType = MusicFragment.ALBUM_SEARCH_TYPE;
    private int currentPostion;

    private void initView() {
        mNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_main_bt_album, R.string.main_album_text)
                        .setActiveColorResource(R.color.colorPrimaryDark))
                .setBarBackgroundColor(R.color.material_white)
                .addItem(new BottomNavigationItem(R.drawable.ic_main_bt_album, R.string.main_artists_text)
                        .setActiveColorResource(R.color.colorPrimaryDark))
                .setBarBackgroundColor(R.color.material_white)
                .addItem(new BottomNavigationItem(R.drawable.ic_main_bt_album, R.string.main_song_text)
                        .setActiveColorResource(R.color.material_white))
                .setBarBackgroundColor(R.color.colorPrimary)
                .addItem(new BottomNavigationItem(R.drawable.ic_main_bt_download, R.string.main_download_text)
                        .setActiveColorResource(R.color.material_white))
                .setBarBackgroundColor(R.color.colorPrimary)
                .initialise();
        mNavigationView.setMode(MODE_FIXED);
        mNavigationView.setAutoHideEnabled(true);
        mNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                showHideFragment(mFragments.get(position));
                currentPostion = position;

                AdModule.getInstance().getFacebookAd().loadAd(false, "146773445984805_146773949318088");

                StatusBarUtil.setColor(MainActivity.this, sCurrentStatusColor);

                if (position == 3) {
                    mSearchView.setVisibility(View.INVISIBLE);
                } else {
                    mSearchView.setVisibility(View.VISIBLE);
                }

                switch (position) {
                    case 0:
                        currentType = MusicFragment.ALBUM_SEARCH_TYPE;
                        break;
                    case 1:
                        currentType = MusicFragment.ARTISTS_SEARCH_TYPE;
                        break;
                    case 2:
                        currentType = MusicFragment.SONGS_SEARCH_TYPE;
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
        isShowRating = PreferenceUtil.getInstance(this).isShowRating();
    }

    private boolean isShowRating = false;
    private boolean isShow = false;

    @Override
    public void finish() {
        if (isShow) {
            return;
        }

        if (isShowRating) {
            isShow = true;
            Utils.runSingleThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isShow = false;
                    isShowRating = false;
                    PreferenceUtil.getInstance(getApplicationContext()).notShowRating();
                }
            });
            RatingActivity.launch(this);
        } else {
            super.finish();
        }
    }

    @Subscribe
    public void hide(String isHide) {
        LogUtil.d("hide isHide :" + isHide);
        if (isHide.equals("true")) {
            mSearchView.setVisibility(View.GONE);
            mNavigationView.hide();
        } else {
            mSearchView.setVisibility(View.VISIBLE);
            mNavigationView.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventUtil.register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
    }

    @Override
    public void onBackToFirstFragment() {
        mNavigationView.selectTab(0);
    }
}
