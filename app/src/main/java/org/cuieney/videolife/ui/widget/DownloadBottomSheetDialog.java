package org.cuieney.videolife.ui.widget;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.admodule.AdModule;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.NativeAd;

import org.cuieney.videolife.FacebookReportUtils;
import org.cuieney.videolife.FileDownloaderHelper;
import org.cuieney.videolife.R;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.entity.wyBean.TracksBean;

/**
 * Created by liyanju on 2018/1/15.
 */

public class DownloadBottomSheetDialog extends BaseBottomSheetFragment {

    public static DownloadBottomSheetDialog newInstance(MusicListBean tracksBean) {
        DownloadBottomSheetDialog fragment = new DownloadBottomSheetDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("song", tracksBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    private MusicListBean mSong;

    @Override
    public int getLayoutResId() {
        return R.layout.download_bs_dialog;
    }

    @Override
    public void initView() {
        mSong = getArguments().getParcelable("song");

        TextView titleTV = (TextView) rootView.findViewById(R.id.title_tv);
        titleTV.setText(mSong.name);

        rootView.findViewById(R.id.download_linear).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
                try {
                    FileDownloaderHelper.addDownloadTask(mSong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FacebookReportUtils.logSongDownload(mSong.name);
            }
        });

        rootView.findViewById(R.id.play_linear).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
                try {
                    String extension = MimeTypeMap.getFileExtensionFromUrl(mSong.audiodownload);
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setDataAndType(Uri.parse(mSong.audiodownload), mimeType);
                    startActivity(it);
                    FacebookReportUtils.logSongPlay(mSong.name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        FrameLayout adContainer = (FrameLayout) rootView.findViewById(R.id.ad_container);
        NativeAd nativeAd = AdModule.getInstance().getFacebookAd().nextNativieAd();
        if (nativeAd != null && nativeAd.isAdLoaded()) {
            adContainer.removeAllViews();
            adContainer.addView(setUpNativeAdView(nativeAd));
        }
    }

    private View setUpNativeAdView(NativeAd nativeAd) {
        nativeAd.unregisterView();

        View adView = LayoutInflater.from(mContext).inflate(R.layout.home_list_ad_item2, null);

        FrameLayout adChoicesFrame = (FrameLayout) adView.findViewById(R.id.fb_adChoices2);
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.image_ad);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.title);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.text);
        TextView nativeAdCallToAction = (TextView) adView.findViewById(R.id.call_btn_tv);

        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdTitle.setTextColor(ContextCompat.getColor(mContext, R.color.material_black));
        nativeAdBody.setText(nativeAd.getAdBody());
        nativeAdBody.setTextColor(Color.parseColor("#6a6a6a"));

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Add adChoices icon
        AdChoicesView adChoicesView = new AdChoicesView(mContext, nativeAd, true);
        adChoicesFrame.addView(adChoicesView, 0);
        adChoicesFrame.setVisibility(View.VISIBLE);

        nativeAd.registerViewForInteraction(adView);

        return adView;
    }

    public void showBottomSheetFragment(FragmentManager manager) {
        try {
            if (manager.findFragmentByTag(DownloadBottomSheetDialog.class.getName()) == null) {
                show(manager, DownloadBottomSheetDialog.class.getName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
