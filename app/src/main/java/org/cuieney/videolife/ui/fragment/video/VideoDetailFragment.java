package org.cuieney.videolife.ui.fragment.video;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.admodule.AdModule;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.library.StatusBarUtil;
import com.konifar.fab_transformation.FabTransformation;
import com.tubewebplayer.WebViewPlayerActivity;

import org.cuieney.videolife.FacebookReportUtils;
import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseBackFragment;
import org.cuieney.videolife.common.image.ImageLoader;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.DMVideoItemListBean;
import org.cuieney.videolife.entity.VideoListItemBean;
import org.cuieney.videolife.entity.VimeoItemListBean;
import org.cuieney.videolife.entity.YoutubeItemListBean;
import org.cuieney.videolife.ui.video.JumpUtils;


/**
 * Created by cuieney on 17/2/25.
 */

public class VideoDetailFragment extends BaseBackFragment {

    ImageView mImgDetail;
    Toolbar mToolbar;
    ImageView bgImage;
    TextView title;
    TextView type;
    TextView description;
    FloatingActionButton mFab;


    private VideoListItemBean dataBean;
    private CollapsingToolbarLayout collToolBar;


    public static VideoDetailFragment newInstance(VideoListItemBean dataBean) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, dataBean);
        VideoDetailFragment fragment = new VideoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBean = getArguments().getParcelable(ARG_ITEM);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_home_detail_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = ((Toolbar) view.findViewById(R.id.toolbar));
        collToolBar = ((CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout));
        mImgDetail = (ImageView) view.findViewById(R.id.img_detail);
        bgImage = (ImageView) view.findViewById(R.id.bg_image);
        title = (TextView) view.findViewById(R.id.title);
        type = (TextView) view.findViewById(R.id.type);
        description = (TextView) view.findViewById(R.id.description);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);


        mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00}));

        mToolbar.setTitle("");
        initToolbarNav(mToolbar);
        ImageLoader.loadAll(getActivity(), dataBean.getMaxThumbnailUrl(), mImgDetail);

        title.setText(dataBean.getTitle());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#").append(dataBean.getCategory())
                .append(" ")
                .append(" / ")
                .append(" ")
                .append(dataBean.getDuration());
        type.setText(stringBuilder.toString());
        description.setText(dataBean.getDescription());

        mFab.setOnClickListener(v -> {
            if (mFab.getVisibility() == View.VISIBLE) {
                FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
                    @Override
                    public void onStartTransform() {
                    }

                    @Override
                    public void onEndTransform() {
                        if (dataBean instanceof DMVideoItemListBean) {
                            WebViewPlayerActivity.launch(getContext(), "http://www.dailymotion.com/video/" + dataBean.getVideoId(),
                                    dataBean.getTitle());
                        } else if (dataBean instanceof YoutubeItemListBean) {
                            JumpUtils.goToYoutubePlayer(getActivity(), mImgDetail, dataBean);
                        } else {
                            WebViewPlayerActivity.launch(getContext(), "https://vimeo.com/" + dataBean.getVideoId(),
                                    dataBean.getTitle());
                        }
                    }
                }).transformTo(mImgDetail);
            }
        });

        initColor();

        if (dataBean instanceof YoutubeItemListBean) {
            FacebookReportUtils.logSentPageShow("youtube_detail");
        } else if (dataBean instanceof DMVideoItemListBean) {
            FacebookReportUtils.logSentPageShow("dm_video_detail");
        } else if (dataBean instanceof VimeoItemListBean) {
            FacebookReportUtils.logSentPageShow("vimeo_video_detail");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AdModule.getInstance().getAdMob().showInterstitialAd();
            }
        }, 600);
    }

    int color = 0xffffcc00;

    private void initColor() {

        Glide.with(getContext()).load(dataBean.getMaxThumbnailUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                Palette.from(resource).generate(palette -> {
                    try {
                        color = palette.getDarkMutedSwatch().getRgb();
                    } catch (Exception e) {
                        LogUtil.d(e.getMessage());
                    }
                    try {
                        collToolBar.setContentScrimColor(color);
                        StatusBarUtil.setColor(getActivity(), color);
                        mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{color}));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
            @Override
            public void onStartTransform() {

            }

            @Override
            public void onEndTransform() {
                if (mImgDetail.getVisibility() == View.INVISIBLE) {
                    mImgDetail.setVisibility(View.VISIBLE);
                }
            }
        }).transformFrom(mImgDetail);
    }

}
