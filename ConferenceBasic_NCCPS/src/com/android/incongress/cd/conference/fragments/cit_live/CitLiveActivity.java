package com.android.incongress.cd.conference.fragments.cit_live;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.android.incongress.cd.conference.widget.VideoEnabledWebChromeClient;
import com.android.incongress.cd.conference.widget.VideoEnabledWebView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

@SuppressLint("NewApi") 
public class CitLiveActivity extends FragmentActivity {

	private VideoEnabledWebView webView;
	private VideoEnabledWebChromeClient webChromeClient;

	private ImageView mIvBack;
	private TextView mIvTitle;

	private LinearLayout mPb_loading;
	private IncongressTextView mItvNetError;
	private boolean IsNetWorkOpen;
	private RelativeLayout include;
	private static String URL_STRING = "url_string";
	public static void getInstance(Activity activity,String urlString){
		Intent intent = new Intent(activity,CitLiveActivity.class);
		intent.putExtra(URL_STRING,urlString);
		activity.startActivity(intent);
	}
	@Override @SuppressLint("NewApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.webview_video_cit);

		mIvBack = (ImageView) findViewById(R.id.title_back);
		mIvBack.setImageResource(R.drawable.nav_btn_close);
		mIvTitle = findViewById(R.id.title_text);
		mIvTitle.setText(R.string.home_cit_live);
		mPb_loading = (LinearLayout) findViewById(R.id.pb_loading);
		mItvNetError = (IncongressTextView)findViewById(R.id.itv_net_error);
		include = (RelativeLayout) findViewById(R.id.include_title);

		mIvBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				try {
					if(webView != null) {
						webView.loadUrl("javascript:clearCachc(1)");
						webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
						webView.loadUrl("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Save the web view
		webView = (VideoEnabledWebView) findViewById(R.id.webView);

		// Initialize the VideoEnabledWebChromeClient and set event handlers
		final View nonVideoLayout = findViewById(R.id.nonVideoLayout);
		ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout);
		// noinspection all
		View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class
		webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available
		{
			@Override
			public void onProgressChanged(WebView view, int progress) {
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
					if (android.os.Build.VERSION.SDK_INT >= 14) {
						// noinspection all
						getWindow()
								.getDecorView()
								.setSystemUiVisibility(
										View.SYSTEM_UI_FLAG_LOW_PROFILE);
					}
					include.setVisibility(View.GONE);
				} else {
					//控制为必须竖屏显示
					include.setVisibility(View.VISIBLE);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					//=======
					WindowManager.LayoutParams attrs = getWindow().getAttributes();
					attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
					attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
					getWindow().setAttributes(attrs);
					if (android.os.Build.VERSION.SDK_INT >= 14) {
						// noinspection all
						getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
					}
				}
			}

				});
		webView.setWebChromeClient(webChromeClient);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url,	Bitmap favicon) {
				mPb_loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mPb_loading.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				if(url != null) {
					//距离底部距离改成0
					include.setVisibility(View.VISIBLE);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
							(RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT);
//					params.setMargins(0, 50, 0, 0);
					params.addRule(RelativeLayout.BELOW, R.id.include_title); //动态修改布局:参照文章：http://www.cnblogs.com/angeldevil/p/3836256.html
					nonVideoLayout.setLayoutParams(params);
				} 
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
		webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getCacheDir().getAbsolutePath();
		webView.getSettings().setAppCachePath(appCachePath);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		
		if(android.os.Build.VERSION.SDK_INT > 17) {
			webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
		}
		String url = getIntent().getStringExtra(URL_STRING);//"&appVersion=" + Constants.APP_VERSION + "&appType=2" + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode() + "&fromWhere=" + Constants.getFromWhere() + "&conId=" + Constants.getConId();

		IsNetWorkOpen = AppApplication.instance().NetWorkIsOpen();
		
		if(IsNetWorkOpen) {
			webView.loadUrl(url);
		}else {
			mPb_loading.setVisibility(View.GONE);
			videoLayout.setVisibility(View.GONE);
			nonVideoLayout.setVisibility(View.GONE);
			mItvNetError.setVisibility(View.VISIBLE);
		}

	}
	
	@Override
	public void onBackPressed() {
		// Notify the VideoEnabledWebChromeClient, and handle it ourselves if it
		// doesn't handle it
		if (!webChromeClient.onBackPressed()) {
			if (webView.canGoBack()) {
				webView.goBack();
				webView.loadUrl("javascript:clearCachc()");
			} else {
				// Standard back button implementation (for example this could
				// close the app)
				webView.loadUrl("javascript:clearCachc()");
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
		if (webView != null) {
			webView.reload();
			webView.loadUrl("javascript:clearCachc()");
		}
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

}
