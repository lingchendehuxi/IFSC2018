package com.android.incongress.cd.conference;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.android.incongress.cd.conference.widget.VideoEnabledWebChromeClient;
import com.android.incongress.cd.conference.widget.VideoEnabledWebView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class WebViewVideoActivity extends BaseActivity {
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;

    private ImageView mIvBack;
    private IncongressTextView mIvTitle;
    private Button mHome;

    private LinearLayout mPb_loading;
    private IncongressTextView mItvNetError;
    private boolean IsNetWorkOpen;
    private RelativeLayout include;
    private String mUrl;
    private int type;

    @Override
    protected void setContentView() {
        if (getIntent().getBooleanExtra("fromMeeting", false)) {
            setContentView(R.layout.webview_video_meeting_voice);
        } else {
            setContentView(R.layout.webview_video);
        }

        mIvBack = (ImageView) findViewById(R.id.title_back);
        mIvBack.setImageResource(R.drawable.nav_btn_close);
        mIvTitle = (IncongressTextView) findViewById(R.id.title_text);
        mHome = (Button) findViewById(R.id.title_home);
        mHome.setVisibility(View.GONE);
        mPb_loading = (LinearLayout) findViewById(R.id.pb_loading);
        mItvNetError = (IncongressTextView) findViewById(R.id.itv_net_error);
        include = (RelativeLayout) findViewById(R.id.include_title);

        mIvBack.setOnClickListener(new OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                finish();
                try {
                    if (webView != null) {
                        webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
                        webView.loadUrl("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mUrl = getIntent().getStringExtra("url");
        type = getIntent().getIntExtra("type", -1);

        if (type == 1) {
            if (AppApplication.systemLanguage != 2) {
                mIvTitle.setText("View the video");
            } else {
                mIvTitle.setText("查看视频");
            }
        } else if (type == 2) {
            if (AppApplication.systemLanguage != 2) {
                mIvTitle.setText("View the PPT");
            } else {
                mIvTitle.setText("查看PPT");
            }
        } else {
            if (AppApplication.systemLanguage != 2) {
                mIvTitle.setText("See Detail");
            } else {
                mIvTitle.setText("查看详情");
            }
        }

        // Save the web view
        webView = (VideoEnabledWebView) findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own
        // view,
        // read
        // class
        // comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your
        // own
        // view,
        // read
        // class
        // comments
        // noinspection all
        View loadingView = getLayoutInflater().inflate(
                R.layout.view_loading_video, null); // Your own view, read class
        // comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout,
                videoLayout, loadingView, webView) // See all available
                // constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
            }
        };
        webChromeClient
                .setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
                    @SuppressLint("NewApi")
                    @Override
                    public void toggledFullscreen(boolean fullscreen) {
                        // Your code to handle the full-screen change, for
                        // example showing and hiding the title bar. Example:
                        if (fullscreen) {
                            WindowManager.LayoutParams attrs = getWindow()
                                    .getAttributes();
                            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                            attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            getWindow().setAttributes(attrs);
                            if (android.os.Build.VERSION.SDK_INT >= 14) {
                                // noinspection all
                                getWindow()
                                        .getDecorView()
                                        .setSystemUiVisibility(
                                                View.SYSTEM_UI_FLAG_LOW_PROFILE);
                            }
                            include.setVisibility(View.INVISIBLE);
                        } else {
                            WindowManager.LayoutParams attrs = getWindow()
                                    .getAttributes();
                            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                            attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            getWindow().setAttributes(attrs);
                            if (android.os.Build.VERSION.SDK_INT >= 14) {
                                // noinspection all
                                getWindow().getDecorView()
                                        .setSystemUiVisibility(
                                                View.SYSTEM_UI_FLAG_VISIBLE);
                            }
                            include.setVisibility(View.VISIBLE);
                        }

                    }
                });
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPb_loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mPb_loading.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDefaultTextEncodingName("GBK");
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // /*** 打开本地缓存提供JS调用 **/
        webView.getSettings().setDomStorageEnabled(true);
        // // Set cache size to 8 mb by default. should be more than enough
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        // // This next one is crazy. It's the DEFAULT location for your app's

        // // But it didn't work for me without this line.
        // // TYPE_UPDATE: no hardcoded path. Thanks to Kevin Hawkins
        String appCachePath = getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        //自动播放
        if (android.os.Build.VERSION.SDK_INT > 17) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        // if (AppApplication.systemLanguage == 2) {
        // url = url + "&lan=en";
        // }
        //
        IsNetWorkOpen = AppApplication.instance().NetWorkIsOpen();

        if (IsNetWorkOpen) {
            webView.loadUrl(mUrl);
        } else {
            mPb_loading.setVisibility(View.GONE);
            videoLayout.setVisibility(View.GONE);
            nonVideoLayout.setVisibility(View.GONE);
            mItvNetError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initViewsAction() {

    }

    @Override
    public void onBackPressed() {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it
        // doesn't handle it
        if (!webChromeClient.onBackPressed()) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                // Standard back button implementation (for example this could
                // close the app)
                super.onBackPressed();
            }
        }
    }

    /**
     * 当用户按下home键时靠它停止播放视频
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null)
            webView.reload();
    }

}
