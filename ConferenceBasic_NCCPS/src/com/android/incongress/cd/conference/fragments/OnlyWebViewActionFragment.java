package com.android.incongress.cd.conference.fragments;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.ProgressWebView;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * 所有和webview的类都是用该类,通过传入bundle  mUrl建立相应的webview即可
 * Created by Jacky on 2016/1/15.
 */
public class OnlyWebViewActionFragment extends BaseFragment {
    private ProgressWebView mOnlyWebview;
    private PhotoView mPhotoView;
    private String mUrl;

    private static final String BUNDLE_URL = "url";

    public OnlyWebViewActionFragment(){}

    public static final OnlyWebViewActionFragment getInstance(String url) {
        OnlyWebViewActionFragment fragment  = new OnlyWebViewActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(BUNDLE_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onlywebview,null);
        mOnlyWebview = (ProgressWebView) view.findViewById(R.id.wv_only);
        mPhotoView = (PhotoView) view.findViewById(R.id.photoview);

        mPhotoView.enable();
        clearCache();
        initWebViewSetting();
        loadUrl(mUrl);
//        loadUrl("https://kyfw.12306.cn/otn/");
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoView.setVisibility(View.GONE);
            }
        });
        return view;
    }

    /**
     * 缓存清理
     */
    private void clearCache() {
        mOnlyWebview.loadUrl("javascript:clearCachc()");
    }

    private void loadUrl(String url) {
        mOnlyWebview.loadUrl(url);
    }
    /**
     * 分享
     * @param view
     */
    public void setRightView(View view) {
        if(view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppApplication.systemLanguage == 1)
                        ShareUtils.shareTextWithUrl(getActivity(),"IFSC基本信息","参会指南", getActivity().getString(Constants.get_MEETING_GUIDE(), AppApplication.conId, AppApplication.getSystemLanuageCode()),null);
                    else
                        ShareUtils.shareTextWithUrl(getActivity(),"IFSC Infomation","Meeting Guide", getActivity().getString(Constants.get_MEETING_GUIDE(), AppApplication.conId, AppApplication.getSystemLanuageCode()),null);
                }
            });
        }
    }
    /**
     * webview配置
     */
    private void initWebViewSetting() {
        mOnlyWebview.getSettings().setJavaScriptEnabled(true);//允许webview进行js调用
        //只有在系统版本号低于18的情况下才调用该方法
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mOnlyWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        mOnlyWebview.getSettings().setAllowFileAccess(true);
        mOnlyWebview.getSettings().setLoadWithOverviewMode(true);
        mOnlyWebview.getSettings().setSupportZoom(true);
        mOnlyWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mOnlyWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mOnlyWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mOnlyWebview.getSettings().setLoadsImagesAutomatically(true);
        mOnlyWebview.getSettings().setBuiltInZoomControls(true);
        mOnlyWebview.getSettings().setUseWideViewPort(true);

        // /*** 打开本地缓存提供JS调用 这里是因为js需要调用本地缓存而设置的权限**/
        mOnlyWebview.getSettings().setDomStorageEnabled(true);
        mOnlyWebview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        mOnlyWebview.getSettings().setAppCachePath(appCachePath);
        mOnlyWebview.getSettings().setAllowFileAccess(true);
        mOnlyWebview.getSettings().setAppCacheEnabled(true);
        mOnlyWebview.getSettings().setJavaScriptEnabled(true);

        mOnlyWebview.setWebViewClient(new WebViewClient(){
            //支持SSL验证，handler.proceed 通过所有的ssl验证，super.onReceiveSslErro这个方法要忽略掉
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        mOnlyWebview.getSettings().setJavaScriptEnabled(true);
        mOnlyWebview.getSettings().setDefaultTextEncodingName("gb2312");

        //设置回退规则
        mOnlyWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mOnlyWebview.canGoBack()) {
                        mOnlyWebview.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public class GetImgUrl{
        @JavascriptInterface
        public void getUrl(String Imgurl){
            mImgUrl = Imgurl;
            hand.sendEmptyMessage(1);
        }
    }

    private String mImgUrl;
    @Override
    public void onPause() {
        super.onPause();
        clearCache();
        mOnlyWebview.reload();
    }
    Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mPhotoView.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(mImgUrl).placeholder(R.drawable.default_load_bg).into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mPhotoView.setImageDrawable(resource);
                        }
                    });
                    break;
            }
        }
    };
}
