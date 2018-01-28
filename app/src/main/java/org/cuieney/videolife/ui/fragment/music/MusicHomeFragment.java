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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admodule.AdModule;
import com.admodule.admob.AdMobBanner;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdListener;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.common.utils.AdViewWrapperAdapter;
import org.cuieney.videolife.common.utils.Constants;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.data.MangoDataHandler;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.presenter.MusicHomePresenter;
import org.cuieney.videolife.presenter.contract.MusicHomeContract;
import org.cuieney.videolife.ui.adapter.MusicAdapter;
import org.cuieney.videolife.common.base.DetailTransition;
import org.cuieney.videolife.ui.widget.DownloadBottomSheetDialog;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        query = getArguments().getString("query");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private AdViewWrapperAdapter adViewWrapperAdapter;

    @Override
    protected void initEventAndData() {
        LogUtil.d("initEventAndData type: " + type);
        RecyclerView.LayoutManager layout;
        if (type == MusicFragment.SONGS_TYPE || type == MusicFragment.SONGS_SEARCH_TYPE) {
            layout = new LinearLayoutManager(getActivity());
        } else {
            layout = new GridLayoutManager(getActivity(), 2);
        }
        recycler.setLayoutManager(layout);

        adapter = new MusicAdapter(getActivity(),null, layout instanceof GridLayoutManager);

        if (type == MusicFragment.SONGS_TYPE || type == MusicFragment.SONGS_SEARCH_TYPE) {
            adViewWrapperAdapter = new AdViewWrapperAdapter(adapter);
            recycler.setAdapter(adViewWrapperAdapter);
        } else {
            recycler.setAdapter(adapter);
        }

        adapter.setOnItemClickListener((position, view, vh) -> {
            if (type == MusicFragment.SONGS_TYPE || type == MusicFragment.SONGS_SEARCH_TYPE) {
                DownloadBottomSheetDialog.newInstance(mMusicList.get(position).getTracks().get(0))
                        .showBottomSheetFragment(getChildFragmentManager());
            } else {
                startChildFragment(mMusicList.get(position), vh);
            }
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

        if (type == MusicFragment.SONGS_TYPE || type == MusicFragment.SONGS_SEARCH_TYPE) {
            AdModule.getInstance().getFacebookAd().loadAd(false,
                    Constants.NATIVE_LIST_ITEM_ADID);
            initBannerView();
        }
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

    private AdMobBanner adMobBanner;

    private void initBannerView() {
        adMobBanner = AdModule.getInstance().getAdMob().createBannerAdView();
        adMobBanner.setAdRequest(AdModule.getInstance().getAdMob().createAdRequest());
        adMobBanner.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (getActivity() != null && getActivity().isFinishing()) {
                    return;
                }

                if (!isAdded()) {
                    return;
                }

                if (adMobBanner != null && !adViewWrapperAdapter.isAddAdView() && adapter.getItemCount() > 4) {
                    adMobBanner.getAdView().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT));
                    adViewWrapperAdapter.addAdView(22,
                            new AdViewWrapperAdapter.AdViewItem( adMobBanner.getAdView(), 4));
                    adViewWrapperAdapter.notifyItemInserted(4);
                }
            }
        });
    }

    private View setUpNativeAdView(NativeAd nativeAd) {
        nativeAd.unregisterView();

        View adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_ad_item3, recycler, false);

        FrameLayout adChoicesFrame = (FrameLayout) adView.findViewById(R.id.fb_adChoices2);
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.image_ad);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.title);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.text);
        TextView nativeAdCallToAction = (TextView) adView.findViewById(R.id.call_btn_tv);

        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Add adChoices icon
        AdChoicesView adChoicesView = new AdChoicesView(getActivity(), nativeAd, true);
        adChoicesFrame.addView(adChoicesView, 0);
        adChoicesFrame.setVisibility(View.VISIBLE);

        nativeAd.registerViewForInteraction(adView);

        return adView;
    }

    @Override
    public void showContent(List<MusicListBean> musicListBean) {
        if (musicListBean.size() == 0) {
            errorTv.setVisibility(View.VISIBLE);
            loadingPB.setVisibility(View.GONE);
        } else {
            loadingPB.setVisibility(View.GONE);
            errorTv.setVisibility(View.GONE);
        }

        if (type == MusicFragment.SONGS_TYPE || type == MusicFragment.SONGS_SEARCH_TYPE) {
            int positionStart = adViewWrapperAdapter.getItemCount();
            adapter.addAll2(musicListBean);
            NativeAd nativeAd = AdModule.getInstance().getFacebookAd().getNativeAd();
            if (nativeAd == null || !nativeAd.isAdLoaded()) {
                nativeAd = AdModule.getInstance().getFacebookAd().nextNativieAd();
            }

            if (nativeAd != null && nativeAd.isAdLoaded()
                    && !adViewWrapperAdapter.isAddAdView() && adapter.getItemCount() > 4) {
                adViewWrapperAdapter.addAdView(22,
                        new AdViewWrapperAdapter.AdViewItem(setUpNativeAdView(nativeAd), 4));
            }

            adViewWrapperAdapter.notifyItemRangeInserted(positionStart,
                    adViewWrapperAdapter.getItemCount());
        } else {
            adapter.addAll(musicListBean);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adMobBanner != null) {
            adMobBanner.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adMobBanner != null) {
            adMobBanner.resume();
        }
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

        if (adMobBanner != null) {
            adMobBanner.destroy();
            adMobBanner = null;
        }
    }

    @Override
    public void error(Throwable throwable) {
        throwable.printStackTrace();
        loadingPB.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
    }
}
