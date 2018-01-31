package org.cuieney.videolife.ui.fragment.music;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import org.cuieney.videolife.common.net.NetWorkUtil;
import org.cuieney.videolife.common.utils.AdViewWrapperAdapter;

import java.io.File;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicHomeFragment extends BaseFragment {

    public static MusicHomeFragment newInstance() {
        return new MusicHomeFragment();
    }

    @Override
    protected void initInject() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.music_home_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private AdViewWrapperAdapter adViewWrapperAdapter;

    private ImageView searchIV;
    private FrameLayout webContent;
    private WebView webView;
    private View webLinear;
    private ProgressBar progressBar;

    public static final String WEB_URL = "https://www.baidu.com/";

    @Override
    protected void initEventAndData() {
        searchIV = (ImageView) getView().findViewById(R.id.search_tip_iv);
        webContent = (FrameLayout) getView().findViewById(R.id.webview_content);
        webLinear = getView().findViewById(R.id.web_linear);
        progressBar = (ProgressBar) getView().findViewById(R.id.wb_loading);

        addAndInitWebView();
    }

    private void addAndInitWebView() {
        searchIV.setVisibility(View.GONE);
        webLinear.setVisibility(View.VISIBLE);
        webView = new WebView(mContext);
        webContent.addView(webView);

        initWebViewSettings(webView);

        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = 21)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @RequiresApi(api = 21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl() + "");
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String fileName  = URLUtil.guessFileName(url, contentDisposition, mimetype);
            }
        });

        webView.loadUrl(WEB_URL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (webView != null) {
                webView.getSettings().setBuiltInZoomControls(true);
                webView.setVisibility(View.GONE);

                webView.destroy();
                webView = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initWebViewSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

        webSettings.setDefaultTextEncodingName("UTF-8");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        File cacheFile = new File(webView.getContext().getCacheDir(), "appcache_name");
        String path = cacheFile.getAbsolutePath();

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(path);
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

                if (adMobBanner != null && !adViewWrapperAdapter.isAddAdView()) {
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

        View adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_ad_item3, null, false);

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
}
