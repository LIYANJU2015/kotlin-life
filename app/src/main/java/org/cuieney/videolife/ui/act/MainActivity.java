package org.cuieney.videolife.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jaeger.library.StatusBarUtil;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseMainFragment;
import org.cuieney.videolife.common.base.SimpleActivity;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.presenter.contract.MusicHomeContract;
import org.cuieney.videolife.presenter.contract.VideoHomeContract;
import org.cuieney.videolife.ui.fragment.music.MusicFragment;
import org.cuieney.videolife.ui.fragment.music.MusicHomeFragment;
import org.cuieney.videolife.ui.fragment.video.VideoFragment;
import org.cuieney.videolife.ui.fragment.video.VideoHomeFragment;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_RIPPLE;
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
        mFragments.add(VideoFragment.newInstance(VideoHomeContract.YOUTUBE_TYPE));
        mFragments.add(MusicFragment.newInstance());
        mFragments.add(VideoFragment.newInstance(VideoHomeContract.DAILYMOTION_TYPE));
        mFragments.add(VideoFragment.newInstance(VideoHomeContract.VIMEN_TYPE));
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
    }

    private void doSearch(String query) {
        LogUtil.d("onSearchAction currentType " + currentType);
        if (currentType == MusicHomeContract.SEARCH_SOUDN_CLOUD_TYPE) {
            SupportFragment supportFragment = mFragments.get(currentPostion);
            if (supportFragment instanceof MusicFragment) {
                ((MusicFragment)supportFragment).addSearchFragment(currentType, query);
            }
        } else {
            SupportFragment supportFragment = mFragments.get(currentPostion);
            if (supportFragment instanceof VideoFragment) {
                ((VideoFragment)supportFragment).addSearchFragment(currentType, query);
            }
        }
    }

    public static int sCurrentStatusColor = Color.parseColor("#E64A19");

    private int currentType = VideoHomeContract.SEARCH_YOUTUBE_TYPE;
    private int currentPostion;

    private void initView() {
        mNavigationView
                .addItem(new BottomNavigationItem(R.drawable.youtube_icon, "YouTube").setActiveColor("#E64A19")
                        .setInActiveColor("#CCCCCC"))
                .addItem(new BottomNavigationItem(R.drawable.soundcloud_icon, "SoundCloud").setActiveColor("#f8600f"))
                .addItem(new BottomNavigationItem(R.drawable.dailymotion_icon, "Dailymotion").setActiveColor("#1166dc"))
                .addItem(new BottomNavigationItem(R.drawable.vimeo_icon, "Vimeo").setActiveColor("#01adef"))
                .initialise();
        mNavigationView.setBackgroundStyle(BACKGROUND_STYLE_RIPPLE);
        mNavigationView.setMode(MODE_FIXED);
        mNavigationView.setAutoHideEnabled(true);

        mNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                showHideFragment(mFragments.get(position));
                currentPostion = position;
                switch (position) {
                    case 0:
                        currentType = VideoHomeContract.SEARCH_YOUTUBE_TYPE;
                        sCurrentStatusColor = Color.parseColor("#E64A19");
                        break;
                    case 1:
                        currentType = MusicHomeContract.SEARCH_SOUDN_CLOUD_TYPE;
                        sCurrentStatusColor = Color.parseColor("#f8600f");
                        break;
                    case 2:
                        currentType = VideoHomeContract.SEARCH_DAILYMOTION_TYPE;
                        sCurrentStatusColor = Color.parseColor("#1166dc");
                        break;
                    case 3:
                        currentType = VideoHomeContract.SEARCH_VIMEN_TYPE;
                        sCurrentStatusColor = Color.parseColor("#01adef");
                        break;
                }

                StatusBarUtil.setColor(MainActivity.this, sCurrentStatusColor);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });

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
