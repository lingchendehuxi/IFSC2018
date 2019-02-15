package com.android.incongress.cd.conference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.AlarmUtils;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.android.incongress.cd.conference.widget.SharePopupWindow;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.VideoEnabledWebChromeClient;
import com.android.incongress.cd.conference.widget.VideoEnabledWebView;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

/**
 * 精彩回顾   //播放视频的web
 */
@SuppressLint("NewApi")
public class CollegeActivity extends BaseActivity {

    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;
    private boolean IsNetWorkOpen;

    private ImageView mIvBack, mIvClose;
    private TextView mIvTitle;
    private ImageView mIvShare;
    private LinearLayout mPb_loading;
    private RelativeLayout include;
    private TextView mItvNetError;

    private PhotoView mPhotoView;
    private SharePopupWindow mSharePopupWindow;

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TITLE = "extra_title";
    private String mUrl, mTitle, shareTitle;
    private int type, urlType, dataId;

    public static void startCitCollegeActivity(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context, CollegeActivity.class);
        if(url.endsWith(".mp4")){
            intent.putExtra(EXTRA_URL, url);
        }else {
            if(!url.contains("?")){
                url = url +"?";
            }
            intent.putExtra(EXTRA_URL,url+"&appVersion="+Constants.APP_VERSION+"&appType=2"+"&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode() + "&fromWhere=" + Constants.getFromWhere() + "&conId=" + Constants.getConId());
        }
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    public static void startCitCollegeActivity(Context context, String title, String url, int type) {
        Intent intent = new Intent();
        intent.setClass(context, CollegeActivity.class);
        if(url.endsWith(".mp4")){
            intent.putExtra(EXTRA_URL, url);
        }else {
            if(!url.contains("?")){
                url = url +"?";
            }
            intent.putExtra(EXTRA_URL,url+"&appVersion="+Constants.APP_VERSION+"&appType=2"+"&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode() + "&fromWhere=" + Constants.getFromWhere() + "&conId=" + Constants.getConId());
        }
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.webview_video_cit);

        try {
            mUrl = getIntent().getStringExtra(EXTRA_URL);
            mTitle = getIntent().getStringExtra(EXTRA_TITLE);
            type = getIntent().getIntExtra("type", 0);
        } catch (Exception e) {
            mUrl = "http://www.incongress.cn";
            mTitle = "official site";
        }
        mIvBack = (ImageView) findViewById(R.id.title_back);
        mIvClose = (ImageView) findViewById(R.id.title_close);
        mIvTitle = findViewById(R.id.title_text);
        mIvTitle.setText(mTitle);
        mIvShare = (ImageView) findViewById(R.id.iv_share);

        mPhotoView = (PhotoView) findViewById(R.id.photoview_url_img);
        mPhotoView.enable();
        mPb_loading = (LinearLayout) findViewById(R.id.pb_loading);
        include = (RelativeLayout) findViewById(R.id.include_title);

        mItvNetError = (TextView) findViewById(R.id.itv_net_error);

        mIvBack.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    if (mUrl.contains("method=getDataByDataId") || mUrl.contains("canShare")) {
                        mIvShare.setVisibility(View.VISIBLE);
                    } else if (type == 1) {
                        mIvShare.setVisibility(View.VISIBLE);
                    } else {
                        mIvShare.setVisibility(View.GONE);
                    }
                    webView.reload();
                    webView.goBack();
                    mIvTitle.setText(shareTitle);
                } else {
                    webView.destroy();
                    finish();
                }
				/*try {
					if(webView != null) {
						webView.loadUrl("javascript:clearCachc(2)");//区分精彩回顾 电子病历 cit
						webView.loadUrl("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}*/
            }
        });
        mIvClose.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                webView.destroy();
                finish();
            }
        });
        mPhotoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoView.setVisibility(View.GONE);
            }
        });
        // Save the web view
        webView = (VideoEnabledWebView) findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        final View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        // noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("app.incongress.cn/webapp/discussion/")||title.contains("https://")) {
                    shareTitle = mTitle;
                    mIvTitle.setText(mTitle);
                } else if(!title.contains("letv")){
                    shareTitle = title;
                    mIvTitle.setText(title);
                }
                mUrl = view.getUrl();
                if (mUrl.contains("method=getDataByDataId") || mUrl.contains("canShare")) {
                    mIvShare.setVisibility(View.VISIBLE);
                } else if (type == 1) {
                    mIvShare.setVisibility(View.VISIBLE);
                } else {
                    mIvShare.setVisibility(View.GONE);
                }
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @SuppressLint("NewApi")
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for
                // example showing and hiding the title bar. Example:
                if (fullscreen) {
                    //控制为必须横屏显示
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    //=======
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (Build.VERSION.SDK_INT >= 14) {
                        // noinspection all
                        getWindow()
                                .getDecorView()
                                .setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                    include.setVisibility(View.INVISIBLE);
                } else {
                    //控制为必须竖屏显示
                    include.setVisibility(View.VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (Build.VERSION.SDK_INT >= 14) {
                        // noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                    include.setVisibility(View.VISIBLE);
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.equals(mUrl)) {
                    if (url.contains("method=getDataByDataId") || url.contains("canShare")) {
                        mIvShare.setVisibility(View.VISIBLE);
                    } else if (type == 1) {
                        mIvShare.setVisibility(View.VISIBLE);
                    } else {
                        mIvShare.setVisibility(View.GONE);
                    }
                    mIvClose.setVisibility(View.VISIBLE);
                    mUrl = url;
                    webView.loadUrl(url);
                    //CollegeActivity.startCitCollegeActivity(CollegeActivity.this, mTitle, url);
                }
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new GetImgUrl(), "GetImgUrl");
        webView.addJavascriptInterface(new androidJS(), "androidJS");
        //  打开本地缓存提供JS调用
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT > 17) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        IsNetWorkOpen = NetWorkUtils.isNetworkConnected(this);

        if (IsNetWorkOpen) {
            webView.loadUrl(mUrl);
        } else {
            mPb_loading.setVisibility(View.GONE);
            videoLayout.setVisibility(View.GONE);
            nonVideoLayout.setVisibility(View.GONE);
            mItvNetError.setVisibility(View.VISIBLE);
        }

        mIvShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareTextWithUrl(CollegeActivity.this, shareTitle, " ", mUrl, null);

            }
        });

        if (mUrl.contains("method=getDataByDataId") || mUrl.contains("canShare")) {
            mIvShare.setVisibility(View.VISIBLE);
            //距离底部距离改成0
            include.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
//					params.setMargins(0, 50, 0, 0);
            params.addRule(RelativeLayout.BELOW, R.id.include_title); //动态修改布局:参照文章：http://www.cnblogs.com/angeldevil/p/3836256.html
            nonVideoLayout.setLayoutParams(params);
        } else if (type == 1 || type == 2) {
            mIvShare.setVisibility(View.VISIBLE);
        } else {
            mIvShare.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initViewsAction() {

    }

    public class GetImgUrl {
        @JavascriptInterface
        public void getUrl(String imgUrl) {
            mImgUrl = imgUrl;
            hand.sendEmptyMessage(1);
        }
    }

    public class androidJS {
        @JavascriptInterface
        public String getSessions() {

            String liveJson = SharePreferenceUtils.getAppString("liveYuyue");
            if (liveJson.equals("")) {
                liveJson = "{}";
            }
            return liveJson;
        }

        @JavascriptInterface
        public void liveYuyueV2(String sessionGroupNameAll, String startTime, String url) {
            try {
                String liveJson = SharePreferenceUtils.getAppString("liveYuyue");
                JSONObject jsonObject = new JSONObject();
                if (!liveJson.equals("")) {
                    jsonObject = new JSONObject(liveJson);
                    jsonObject.accumulate(sessionGroupNameAll, 1);
                    SharePreferenceUtils.saveAppString("liveYuyue", jsonObject.toString());
                    //AppApplication.setSPStringValue(sessionGroupNameAll,url);
                    AlarmUtils.addLiveYuyue(CollegeActivity.this, sessionGroupNameAll, startTime, url);
                } else {
                    jsonObject.accumulate(sessionGroupNameAll, 1);
                    SharePreferenceUtils.saveAppString("liveYuyue", jsonObject.toString());
                    //AppApplication.setSPStringValue(sessionGroupNameAll,url);
                    AlarmUtils.addLiveYuyue(CollegeActivity.this, sessionGroupNameAll, startTime, url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void h5GoApp(){
            finish();
        }
    }

    private String mImgUrl;
    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mPhotoView.setVisibility(View.VISIBLE);
                    Glide.with(CollegeActivity.this).load(mImgUrl).into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mPhotoView.setImageDrawable(resource);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (!webChromeClient.onBackPressed()) {
            if (webView.canGoBack()) {
                webView.loadUrl("javascript:clearCachc()");
                webView.goBack();
                mIvShare.setVisibility(View.GONE);
            } else {
                webView.loadUrl("javascript:clearCachc()");
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("H5_" + shareTitle);
    }

    /**
     * 当用户按下home键时靠它停止播放视频
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
//			webView.reload();
            webView.loadUrl("javascript:clearCachc()");
        }

        if (mUrl.contains("tk.do")) {
            finish();
        }
        MobclickAgent.onPageEnd("H5_" + shareTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.reload();
            webView.loadUrl("javascript:clearCachc()");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                mIvTitle.setText(shareTitle);
                if (mUrl.contains("method=getDataByDataId") || mUrl.contains("canShare")) {
                    mIvShare.setVisibility(View.VISIBLE);
                } else if (type == 1) {
                    mIvShare.setVisibility(View.VISIBLE);
                } else {
                    mIvShare.setVisibility(View.GONE);
                }
                webView.reload();
                webView.goBack();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return false;
    }

    /**
     * 内容区域变量
     */
    protected void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变暗
     */
    protected void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }
}
