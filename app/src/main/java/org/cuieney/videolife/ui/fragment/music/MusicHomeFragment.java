package org.cuieney.videolife.ui.fragment.music;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.data.MangoDataHandler;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.presenter.MusicHomePresenter;
import org.cuieney.videolife.presenter.contract.MusicHomeContract;
import org.cuieney.videolife.ui.adapter.MusicAdapter;
import org.cuieney.videolife.common.base.DetailTransition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicHomeFragment extends BaseFragment<MusicHomePresenter> implements MusicHomeContract.View, MangoDataHandler.CallBack {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.loading_mb)
    ProgressBar loadingPB;
    @BindView(R.id.error_tv)
    TextView errorTv;

    private List<MusicListBean> mMusicList = null;
    private MusicAdapter adapter;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static MusicHomeFragment newInstance(int type, String query) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("query", query);
        MusicHomeFragment musicHomeFragment = new MusicHomeFragment();
        musicHomeFragment.setArguments(bundle);
        return musicHomeFragment;
    }

    public void setArguments(int type, String query) {
        this.type = type;
        this.query = query;

        initEventAndData();
    }

    public static MusicHomeFragment newInstance() {
        Bundle bundle = new Bundle();
        MusicHomeFragment musicHomeFragment = new MusicHomeFragment();
        musicHomeFragment.setArguments(bundle);
        return musicHomeFragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.music_home_fragment;
    }

    public int type;
    private String query;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type", type);
        outState.putString("query", query);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("type");
        query = getArguments().getString("query");
    }

    @Override
    protected void initEventAndData() {

        RecyclerView.LayoutManager layout;
        if (type == MusicFragment.SONGS_TYPE || type == MusicFragment.SONGS_SEARCH_TYPE) {
            layout = new LinearLayoutManager(getActivity());
        } else {
            layout = new GridLayoutManager(getActivity(), 2);
        }
        recycler.setLayoutManager(layout);

        adapter = new MusicAdapter(getActivity(),null, layout instanceof GridLayoutManager);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener((position, view, vh) -> {
            startChildFragment(mMusicList.get(position), vh);
        });

        loadingPB.setVisibility(View.VISIBLE);

        Utils.runSingleThread(new Runnable() {
            @Override
            public void run() {
                if (type == MusicFragment.SONGS_TYPE || type == MusicFragment.SONGS_SEARCH_TYPE) {
                    MangoDataHandler.registerSongDataListener(MusicHomeFragment.this);
                } else if (type == MusicFragment.ARTISTS_TYPE || type == MusicFragment.ARTISTS_SEARCH_TYPE) {
                    MangoDataHandler.registerArtistsDataListener(MusicHomeFragment.this);
                } else {
                    MangoDataHandler.registerAlbumDataListener(MusicHomeFragment.this);
                }
            }
        });

    }

    @Override
    public void onCallBack(final ArrayList<MusicListBean> list) {
        if (!isAdded()) {
            return;
        }

        switch (type) {
            case MusicFragment.ALBUM_SEARCH_TYPE:
                mMusicList = MangoDataHandler.searchData(list, query);
                break;
            case MusicFragment.ARTISTS_SEARCH_TYPE:
                mMusicList = MangoDataHandler.searchData(list, query);
                break;
            case MusicFragment.SONGS_SEARCH_TYPE:
                mMusicList = MangoDataHandler.searchData(list, query);
                break;
            default:
                mMusicList = list;
                break;
        }

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                showContent(mMusicList);
            }
        });
    }

    @Override
    public void showContent(List<MusicListBean> musicListBean) {
        loadingPB.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);

        adapter.addAll(musicListBean);
    }


    private void startChildFragment(MusicListBean musicListBean, RecyclerView.ViewHolder vh) {
        EventUtil.sendEvent(true + "");
        MusicDetailFragment fragment = MusicDetailFragment.newInstance(
                musicListBean
        );
        // 这里是使用SharedElement的用例

        // LOLLIPOP(5.0)系统的 SharedElement支持有 系统BUG， 这里判断大于 > LOLLIPOP
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setExitTransition(new Fade());
            fragment.setEnterTransition(new Slide());
            fragment.setSharedElementReturnTransition(new DetailTransition());
            fragment.setSharedElementEnterTransition(new DetailTransition());

            // 25.1.0以下的support包,Material过渡动画只有在进栈时有,返回时没有;
            // 25.1.0+的support包，SharedElement正常
            fragment.transaction()
                    .addSharedElement(((MusicAdapter.MyViewHoler) vh).imageView, getString(R.string.image_transition))
                    .commit();
        }
        start(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        MangoDataHandler.unRegisterAlbumDataListener();
        MangoDataHandler.unRegisterArtistsDataListener();
        MangoDataHandler.unRegisterSongDataListener();
    }

    @Override
    public void error(Throwable throwable) {
        throwable.printStackTrace();
        loadingPB.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
    }
}
