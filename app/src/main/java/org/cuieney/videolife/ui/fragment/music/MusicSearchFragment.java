package org.cuieney.videolife.ui.fragment.music;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admodule.AdModule;
import com.admodule.admob.AdMobBanner;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdListener;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.utils.AdViewWrapperAdapter;
import org.cuieney.videolife.common.utils.Constants;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.presenter.MusicHomePresenter;
import org.cuieney.videolife.presenter.contract.MusicHomeContract;
import org.cuieney.videolife.ui.adapter.MusicAdapter;
import org.cuieney.videolife.ui.widget.DownloadBottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicSearchFragment extends BaseFragment<MusicHomePresenter> implements MusicHomeContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.loading_mb)
    ProgressBar loadingPB;
    @BindView(R.id.error_tv)
    TextView errorTv;

    private List<MusicListBean> mMusicList = new ArrayList<>();
    private MusicAdapter adapter;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static MusicSearchFragment newInstance(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        MusicSearchFragment musicHomeFragment = new MusicSearchFragment();
        musicHomeFragment.setArguments(bundle);
        return musicHomeFragment;
    }

    public void setArguments(String query) {
        this.query = query;
        initEventAndData();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.music_search_fragment;
    }

    private String query;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("query", query);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        query = getArguments().getString("query");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private AdViewWrapperAdapter adViewWrapperAdapter;

    @Override
    protected void initEventAndData() {

        LinearLayoutManager layout = new LinearLayoutManager(getActivity());

        recycler.setLayoutManager(layout);

        adapter = new MusicAdapter(getActivity(),null, false);

        adViewWrapperAdapter = new AdViewWrapperAdapter(adapter);
        recycler.setAdapter(adViewWrapperAdapter);

        adapter.setOnItemClickListener((position, view, vh) -> {
                DownloadBottomSheetDialog.newInstance(mMusicList.get(position))
                        .showBottomSheetFragment(getChildFragmentManager());
        });

        loadingPB.setVisibility(View.VISIBLE);

        AdModule.getInstance().getFacebookAd().loadAd(false,
                Constants.NATIVE_LIST_ITEM_ADID);
        initBannerView();

        mPresenter.getSearchMusicList(query);
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

        adViewWrapperAdapter.clearAdView();
        adapter.clear();
        mMusicList.clear();

        int positionStart = adViewWrapperAdapter.getItemCount();
        adapter.addAll2(musicListBean);
        mMusicList.addAll(musicListBean);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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
