package org.cuieney.videolife.ui.fragment.music;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.presenter.MusicHomePresenter;
import org.cuieney.videolife.presenter.contract.MusicHomeContract;
import org.cuieney.videolife.ui.adapter.MusicAdapter;
import org.cuieney.videolife.common.base.DetailTransition;
import org.cuieney.videolife.ui.widget.EndLessOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicHomeFragment extends BaseFragment<MusicHomePresenter> implements MusicHomeContract.View  {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.loading_mb)
    ProgressBar loadingPB;
    @BindView(R.id.error_tv)
    TextView errorTv;

    private List<MusicListBean> mMusicList;
    private MusicAdapter adapter;

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

    @Override
    protected void initEventAndData() {

        refresh.setProgressViewOffset(false,100,200);
        refresh.setOnRefreshListener(() -> mPresenter.getMusicData());
        GridLayoutManager layout = new GridLayoutManager(getActivity(), 2);
        recycler.setLayoutManager(layout);
//        recycler.addOnScrollListener(new EndLessOnScrollListener(layout,1) {
//            @Override
//            public void onLoadMore() {
//                mPresenter.getMusicData(pager+"");
//            }
//        });
        mMusicList = new ArrayList<>();
        adapter = new MusicAdapter(getActivity(),mMusicList);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener((position, view, vh) -> {
            startChildFragment(mMusicList.get(position), vh);
        });

        loadingPB.setVisibility(View.VISIBLE);
        mPresenter.getMusicData();
    }

    @Override
    public void showContent(List<MusicListBean> musicListBean) {
        loadingPB.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);

//        musicListBean.add(0,new MusicListBean());
//        musicListBean.add(0,new MusicListBean());
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            adapter.clear();
            mMusicList.clear();
            adapter.addAll(musicListBean);
            recycler.setAdapter(adapter);
        }else{
            adapter.addAll(musicListBean);
        }
        mMusicList.addAll(musicListBean);
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
    public void error(Throwable throwable) {
        throwable.printStackTrace();
        loadingPB.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
    }
}
