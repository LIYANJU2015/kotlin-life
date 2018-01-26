package org.cuieney.videolife.ui.fragment.music;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.admodule.AdModule;
import com.admodule.admob.AdMobBanner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdListener;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.konifar.fab_transformation.FabTransformation;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.cuieney.videolife.App;
import org.cuieney.videolife.FacebookReportUtils;
import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseBackFragment;
import org.cuieney.videolife.common.image.ImageLoader;
import org.cuieney.videolife.common.utils.AdViewWrapperAdapter;
import org.cuieney.videolife.common.utils.Constants;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.common.utils.PreferenceUtil;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.ui.adapter.MusicItemAdapter;
import org.cuieney.videolife.ui.video.JumpUtils;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicDetailFragment extends BaseBackFragment {

    ImageView mImgDetail;
    Toolbar mToolbar;
    FloatingActionButton mFab;
    private CollapsingToolbarLayout collToolBar;
    private XRecyclerView recycler;
    private MusicListBean dataBean;

    public static MusicDetailFragment newInstance(MusicListBean dataBean) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, dataBean);
        MusicDetailFragment fragment = new MusicDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBean = getArguments().getParcelable(ARG_ITEM);
        PreferenceUtil.getInstance(getContext()).putString("JSON",new Gson().toJson(dataBean.getTracks()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_home_detail_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = ((Toolbar) view.findViewById(R.id.toolbar));
        collToolBar = ((CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout));
        mImgDetail = (ImageView) view.findViewById(R.id.img_detail);
//        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        recycler = ((XRecyclerView) view.findViewById(R.id.recycler));

        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        initColor();
        mToolbar.setTitle("");
        initToolbarNav(mToolbar);
        ImageLoader.loadAll(getActivity(), dataBean.getOphoto(), mImgDetail);
        recycler.setLoadingMoreEnabled(false);
        recycler.setPullRefreshEnabled(false);
        recycler.setAdapter(new MusicItemAdapter(getActivity(), dataBean.getTracks(),
                getChildFragmentManager()));
        initHeadView();

//        mFab.setOnClickListener(v -> {
//            if (mFab.getVisibility() == View.VISIBLE) {
//                FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
//                    @Override
//                    public void onStartTransform() {
//                    }
//
//                    @Override
//                    public void onEndTransform() {
//                        JumpUtils.goToMusicPlayer(getActivity(), mImgDetail, dataBean);
//                    }
//                }).transformTo(mImgDetail);
//            }
//        });

        initBannerView();

        FacebookReportUtils.logSentPageShow("music_detail");
    }

    private View setUpNativeAdView(NativeAd nativeAd) {
        nativeAd.unregisterView();

        View adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_ad_item2, null);

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
    public void onDestroyView() {
        super.onDestroyView();
        AdModule.getInstance().getFacebookAd().loadAd(false, Constants.NATIVE_LIST_ITEM_ADID);
        if (adMobBanner != null) {
            adMobBanner.destroy();
            adMobBanner = null;
        }
    }

    private View headerView;

    private FrameLayout adFrame;

    private void initHeadView() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.music_detial_top_item, null);
        TextView title = (TextView) headerView.findViewById(R.id.title);
        ExpandableTextView expandableTextView = (ExpandableTextView) headerView.findViewById(R.id.expand_text_view);
        expandableTextView.setText(dataBean.getMdesc());
        title.setText(dataBean.getMname());

        adFrame = (FrameLayout) headerView.findViewById(R.id.ad_view_frame);
        adFrame.removeAllViews();
        NativeAd nativeAd = AdModule.getInstance().getFacebookAd().getNativeAd();
        if (nativeAd == null || !nativeAd.isAdLoaded()) {
            nativeAd = AdModule.getInstance().getFacebookAd().nextNativieAd();
        }
        if (nativeAd != null && nativeAd.isAdLoaded()) {
            FacebookReportUtils.logSentFBAdShow("musicDetail");
            adFrame.addView(setUpNativeAdView(nativeAd));
        }

        recycler.addHeaderView(headerView);
    }

    private AdMobBanner adMobBanner;

    @Override
    public void onPause() {
        super.onPause();
        if (adMobBanner != null) {
            adMobBanner.pause();
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

                if (headerView == null || !isAdded()) {
                    return;
                }

                if (adMobBanner != null && adFrame.getChildCount() == 0) {
                    adMobBanner.getAdView().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT));
                    adFrame.addView(adMobBanner.getAdView());
                }
            }
        });
    }

    int color = 0xffffcc00;

    private void initColor() {

        Glide.with(getContext()).load(dataBean.getOphoto()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                Palette.from(resource).generate(palette -> {
                    if (!isAdded() || getActivity() == null) {
                        return;
                    }
                    try {
                        color = palette.getLightMutedSwatch().getRgb();
                    } catch (Exception e) {
                        LogUtil.d(e.getMessage());
                    }
                    collToolBar.setContentScrimColor(color);
                    StatusBarUtil.setColor(getActivity(), color);
//                    mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{color}));

                });

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adMobBanner != null) {
            adMobBanner.resume();
        }

//        FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
//            @Override
//            public void onStartTransform() {
//
//            }
//
//            @Override
//            public void onEndTransform() {
//                if (mImgDetail.getVisibility() == View.INVISIBLE) {
//                    mImgDetail.setVisibility(View.VISIBLE);
//                }
//            }
//        }).transformFrom(mImgDetail);
    }

}