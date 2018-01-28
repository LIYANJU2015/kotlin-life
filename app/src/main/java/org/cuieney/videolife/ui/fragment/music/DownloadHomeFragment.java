package org.cuieney.videolife.ui.fragment.music;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admodule.AdModule;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.NativeAd;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.provider.DownloadProvider;
import org.cuieney.videolife.ui.adapter.DownloadCursorAdapter;

import butterknife.BindView;

/**
 * Created by liyanju on 2018/1/23.
 */

public class DownloadHomeFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.loading_mb)
    ProgressBar loadingPB;
    @BindView(R.id.error_tv)
    TextView errorTv;
    @BindView(R.id.toolbar_layout)
    Toolbar toolbar;

    private DownloadCursorAdapter cursorAdapter;

    @Override
    protected void initInject() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.download_home_fragment;
    }

    @Override
    protected void initEventAndData() {
        getLoaderManager().initLoader(1, null, this);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setHasFixedSize(true);
        cursorAdapter = new DownloadCursorAdapter(getActivity(), null, 0);
        recycler.setAdapter(cursorAdapter);

        NativeAd nativeAd = AdModule.getInstance().getFacebookAd().nextNativieAd();
        if (nativeAd != null && nativeAd.isAdLoaded()) {
            toolbar.setVisibility(View.VISIBLE);
            toolbar.removeAllViews();
            toolbar.addView(setUpNativeAdView(nativeAd));
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    private View setUpNativeAdView(NativeAd nativeAd) {
        nativeAd.unregisterView();

        View adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_ad_item2, recycler, false);

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, DownloadProvider.DownloadedTableInfo.URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            errorTv.setVisibility(View.INVISIBLE);
            loadingPB.setVisibility(View.INVISIBLE);
        } else {
            errorTv.setVisibility(View.VISIBLE);
            loadingPB.setVisibility(View.INVISIBLE);
        }
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
