package com.android.incongress.cd.conference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.fragments.OnlyWebViewActionFragment;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Jacky on 2016/1/15.
 */
public class WebViewContainerActivity extends FragmentActivity {

    private static final String BUNDLE_URL = "url";
    private static final String BUNDLE_TITLE_NAME = "titleName";
    private static final String BUNDLE_TYPE = "type";

    private String mUrl,mTitleName;
    private TextView mTvTitle;
    private ImageView mIvBack,mIvShare;
    private int type;

    public WebViewContainerActivity(){}

    public static final void startWebViewContainerActivity(Context ctx, String url, String titleName) {
        Intent intent = new Intent();
        intent.setClass(ctx, WebViewContainerActivity.class);
        intent.putExtra(BUNDLE_URL, url);
        intent.putExtra(BUNDLE_TITLE_NAME, titleName);
        ctx.startActivity(intent);
    }
    public static final void startWebViewContainerActivity(int type,Context ctx, String url, String titleName) {
        Intent intent = new Intent();
        intent.setClass(ctx, WebViewContainerActivity.class);
        intent.putExtra(BUNDLE_URL, url);
        intent.putExtra(BUNDLE_TITLE_NAME, titleName);
        intent.putExtra(BUNDLE_TYPE, type);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrl = getIntent().getStringExtra(BUNDLE_URL);
        mTitleName = getIntent().getStringExtra(BUNDLE_TITLE_NAME);
        type = getIntent().getIntExtra(BUNDLE_TYPE,0);

        setContentView(R.layout.fragment_webview_container);
        mTvTitle = (TextView) findViewById(R.id.title_text);
        mIvBack = (ImageView) findViewById(R.id.title_back);
        mIvShare = (ImageView) findViewById(R.id.iv_share);

        if(mTitleName!= null){
            mTvTitle.setText(mTitleName);
        }
        if(type ==1 || type == 2){
            mIvShare.setVisibility(View.VISIBLE);
        }
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewContainerActivity.this.finish();
            }
        });
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareTextWithUrl(WebViewContainerActivity.this, mTitleName, " ",
                        mUrl, null);

            }
        });
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fl_container,  OnlyWebViewActionFragment.getInstance(mUrl)).commit();
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
    @Override
    public void onResume() {
        super.onResume();
        if(type ==1 || type == 2){
            MobclickAgent.onPageStart("推送消息_"+mTitleName);
        }
    }
    /**
     * 当用户按下home键时靠它停止播放视频
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(type ==1 || type == 2){
            MobclickAgent.onPageEnd("推送消息_"+mTitleName);
        }
    }
}
