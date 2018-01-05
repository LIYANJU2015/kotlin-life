package org.cuieney.videolife.ui.fragment.video;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admodule.AdModule;
import com.admodule.admob.AdMobBanner;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdListener;

import org.cuieney.videolife.App;
import org.cuieney.videolife.FacebookReportUtils;
import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.common.utils.AdViewWrapperAdapter;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.VideoListBean;
import org.cuieney.videolife.entity.VideoListItemBean;
import org.cuieney.videolife.presenter.VideoHomePresenter;
import org.cuieney.videolife.presenter.contract.VideoHomeContract;
import org.cuieney.videolife.ui.adapter.VideoAdapter;
import org.cuieney.videolife.common.base.DetailTransition;
import org.cuieney.videolife.ui.widget.EndLessOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cuieney on 17/2/27.
 */

public class VideoHomeFragment extends BaseFragment<VideoHomePresenter> implements VideoHomeContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.loading_mb)
    ProgressBar loadingPB;
    @BindView(R.id.error_tv)
    TextView errorTv;

    private VideoAdapter adapter;
    private List<VideoListItemBean> sVideoListBean;
    private static SparseArray<VideoHomeBean> sVideoMap = new SparseArray<>();

    private Object sNextPageToken;

    private int requestType = 0;
    private String queryContent;

    public static VideoHomeFragment newInstance(int requestType) {
        Bundle bundle = new Bundle();
        VideoHomeFragment videoFragment = new VideoHomeFragment();
        bundle.putInt("request_type", requestType);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    public static VideoHomeFragment newInstance(int requestType, String query) {
        Bundle bundle = new Bundle();
        VideoHomeFragment videoFragment = new VideoHomeFragment();
        bundle.putInt("request_type", requestType);
        bundle.putString("query", query);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    public class VideoHomeBean {
        public VideoHomeBean(Object nextPageToken, List<VideoListItemBean> videoListItemBeans) {
            this.nextPageToken = nextPageToken;
            this.videoListBean = videoListItemBeans;
        }

        public Object nextPageToken;

        private  List<VideoListItemBean> videoListBean;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_home_fragment;
    }

    private AdViewWrapperAdapter adViewWrapperAdapter;

    @Override
    protected void initEventAndData() {
        requestType = getArguments().getInt("request_type", VideoHomeContract.YOUTUBE_TYPE);
        queryContent = getArguments().getString("query", "");

        initBannerView();

        if (requestType > VideoHomeContract.VIMEN_TYPE) {
            sVideoListBean = new ArrayList<>();
        } else if (sVideoMap.get(requestType) == null) {
            sVideoListBean = new ArrayList<>();
            VideoHomeBean videoHomeBean = new VideoHomeBean(sNextPageToken, sVideoListBean);
            sVideoMap.put(requestType, videoHomeBean);
        } else {
            VideoHomeBean videoHomeBean = sVideoMap.get(requestType);
            sVideoListBean = videoHomeBean.videoListBean;
            sNextPageToken = videoHomeBean.nextPageToken;
        }
        LogUtil.d("initEventAndData requestType " + requestType);

        refresh.setProgressViewOffset(false, 100, 200);
        refresh.setOnRefreshListener(() -> {
            sNextPageToken = null;
            if (requestType > VideoHomeContract.VIMEN_TYPE) {
                mPresenter.searchVideoData(queryContent, sNextPageToken, requestType);
            } else {
                mPresenter.getVideoData(sNextPageToken, requestType);
            }
        });

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layout);

        adapter = new VideoAdapter(getActivity(), sVideoListBean);
        adapter.setOnItemClickListener((position, view, vh) -> startChildFragment(sVideoListBean.get(position), (VideoAdapter.MyHolder) vh));
        adViewWrapperAdapter = new AdViewWrapperAdapter(adapter);
        adapter.setParnetAdapter(adViewWrapperAdapter);
        recycler.setAdapter(adViewWrapperAdapter);
        recycler.addOnScrollListener(new EndLessOnScrollListener(layout,0) {
            @Override
            public void onLoadMore() {
                LogUtil.d("onLoadMore sNextPageToken " + sNextPageToken);
                if (sNextPageToken == null) {
                    return;
                }
                if (requestType > VideoHomeContract.VIMEN_TYPE) {
                    mPresenter.searchVideoData(queryContent, sNextPageToken, requestType);
                } else {
                    mPresenter.getVideoData(sNextPageToken, requestType);
                }
            }
        });

        if (sVideoListBean.size() == 0) {
            if (requestType > VideoHomeContract.VIMEN_TYPE) {
                mPresenter.searchVideoData(queryContent, sNextPageToken, requestType);
            } else {
                mPresenter.getVideoData(sNextPageToken, requestType);
            }

            loadingPB.setVisibility(View.VISIBLE);
            errorTv.setVisibility(View.GONE);
        } else if (sVideoListBean.size() > 3) {
            NativeAd nativeAd = AdModule.getInstance().getFacebookAd().getNativeAd();
            if (nativeAd!= null && nativeAd.isAdLoaded()) {
                adViewWrapperAdapter.addAdView(22, new AdViewWrapperAdapter.
                        AdViewItem(setUpNativeAdView(nativeAd), 2));
            }
        }
    }

    private AdMobBanner adMobBanner;

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

                if (adViewWrapperAdapter == null || adViewWrapperAdapter.isAddAdView()) {
                    return;
                }

                if (adViewWrapperAdapter.getItemCount() > 4 && adMobBanner != null) {
                    adMobBanner.getAdView().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT));
                    adViewWrapperAdapter.addAdView(22, new AdViewWrapperAdapter.
                            AdViewItem(adMobBanner.getAdView(), 2));
                    adViewWrapperAdapter.notifyItemInserted(2);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adMobBanner != null) {
            adMobBanner.destroy();
            adMobBanner = null;
        }
    }

    private View setUpNativeAdView(NativeAd nativeAd) {
        nativeAd.unregisterView();

        View adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_video_ad_item, null);

        FrameLayout adChoicesFrame = (FrameLayout) adView.findViewById(R.id.fb_adChoices);
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.fb_half_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.fb_banner_title);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.fb_banner_desc);
        TextView nativeAdCallToAction = (TextView) adView.findViewById(R.id.fb_half_download);
        MediaView nativeAdMedia = (MediaView) adView.findViewById(com.admodule.R.id.fb_half_mv);

        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and setting the cover image.
        NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        nativeAdMedia.setNativeAd(nativeAd);

        // Add adChoices icon
        AdChoicesView adChoicesView = new AdChoicesView(getActivity(), nativeAd, true);
        adChoicesFrame.addView(adChoicesView, 0);
        adChoicesFrame.setVisibility(View.VISIBLE);

        nativeAd.registerViewForInteraction(adView);

        return adView;
    }

    @Override
    public void showContent(VideoListBean videoListBean) {
        loadingPB.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);

        if (videoListBean.hasMore()) {
            sNextPageToken = videoListBean.getNextPage();
        } else {
            sNextPageToken = null;
        }

        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            sVideoListBean.clear();
            adapter.clear();
            adapter.addAll(videoListBean.getItemList());
            adViewWrapperAdapter.clearAdView();

            NativeAd nativeAd = AdModule.getInstance().getFacebookAd().getNativeAd();
            if (nativeAd!= null && nativeAd.isAdLoaded() && videoListBean.getItemList().size() > 3) {
                adViewWrapperAdapter.addAdView(22, new AdViewWrapperAdapter.
                        AdViewItem(setUpNativeAdView(nativeAd), 2));
                if (requestType == VideoHomeContract.DAILYMOTION_TYPE) {
                    FacebookReportUtils.logSentFBAdShow("DMPage");
                } else if (requestType == VideoHomeContract.YOUTUBE_TYPE) {
                    FacebookReportUtils.logSentFBAdShow("YoutubePage");
                } else {
                    FacebookReportUtils.logSentFBAdShow("VimeoPage");
                }
            }

            recycler.setAdapter(adViewWrapperAdapter);
        }else{
            NativeAd nativeAd = AdModule.getInstance().getFacebookAd().getNativeAd();
            if (nativeAd!= null && nativeAd.isAdLoaded() && videoListBean.getItemList().size() > 3) {
                adViewWrapperAdapter.addAdView(22, new AdViewWrapperAdapter.
                        AdViewItem(setUpNativeAdView(nativeAd), 2));
            }            adapter.addAll(videoListBean.getItemList());
        }
        sVideoListBean.addAll(videoListBean.getItemList());
    }

    private void startChildFragment(VideoListItemBean videoListBean, VideoAdapter.MyHolder vh) {
        EventUtil.sendEvent(true + "");
        VideoDetailFragment fragment = VideoDetailFragment.newInstance(
                videoListBean);
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
                    .addSharedElement(vh.imageView, getString(R.string.image_transition))
//                        .addSharedElement(((VideoAdapter.MyHolder) vh).textView,"tv")
                    .commit();
        }
        start(fragment);
    }

    @Override
    public void error(Throwable throwable) {
        Log.e("oye", "error: ", throwable);
        loadingPB.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);

        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
    }
}
