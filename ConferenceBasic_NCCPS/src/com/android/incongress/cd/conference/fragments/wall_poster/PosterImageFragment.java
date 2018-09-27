package com.android.incongress.cd.conference.fragments.wall_poster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.MakeQuestionActivity;
import com.android.incongress.cd.conference.ScenicXiuPicsViewpagerActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 壁报详情页面
 */
public class PosterImageFragment extends BaseActivity {

    private ImageView mIvSmallPic;
    private TextView mTvDiscussion;
    private DZBBBean mBean;
    private static final int MSG_PRAISE_SUCCESS = 2;
    private boolean mIsPraise = false;//是否点赞
    private ImageView mIvPraise,ivback;
    private LinearLayout mLlAskQuestion;
    private IncongressTextView title;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int result = msg.what;
            if (result == MSG_PRAISE_SUCCESS) {
                mIsPraise = true;
                ToastUtils.showShorToast("点赞成功");
                mIvPraise.setImageResource(R.drawable.postter_zan);
                ConferenceDbUtils.addPraisePoster(mBean.getPosterId() + "");
            }
        }
    };

    private String mPicUrl;

    /**
     * 点赞
     */
    private void doPraise(final String posterId) {
        CHYHttpClientUsage.getInstanse().doZanPoster(posterId, new JsonHttpResponseHandler("GBK") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mHandler.sendEmptyMessage(MSG_PRAISE_SUCCESS);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_POSTERDETAIL);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_POSTERDETAIL);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.fragment_dzbb_image);
    }

    @Override
    protected void initViewsAction() {

        mBean = (DZBBBean) getIntent().getSerializableExtra("bean");
        mIvSmallPic = (ImageView) findViewById(R.id.iv_small_pic);
        title = (IncongressTextView) findViewById(R.id.title_text);
        mTvDiscussion = (TextView)findViewById(R.id.bt_discussion);
        mIvPraise = (ImageView)findViewById(R.id.iv_praise);
        ivback = (ImageView) findViewById(R.id.title_back);

        title.setText(mBean.getTitle());

        mLlAskQuestion = (LinearLayout)findViewById(R.id.ll_ask_question);

        mTvDiscussion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PosterImageFragment.this, PosterDiscussFragment.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",mBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mPicUrl = mBean.getPosterPicUrl();
        if(mPicUrl.contains("https:")) {
            mPicUrl = mPicUrl.replaceFirst("s","");
        }

        mIvSmallPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ScenicXiuPicsViewpagerActivity.startViewPagerActivity(PosterImageFragment.this,new String[]{mPicUrl},0);
            }
        });

        mIsPraise = ConferenceDbUtils.isPraisePosterExist(mBean.getPosterId() + "");

        //提问
        mLlAskQuestion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PosterImageFragment.this, MakeQuestionActivity.class);
                intent.putExtra("title",mBean.getTitle());
                intent.putExtra("id",mBean.getPosterId());
                intent.putExtra("name", mBean.getAuthor());
                startActivity(intent);
            }
        });

        if (mIsPraise)
            mIvPraise.setImageResource(R.drawable.postter_zan);

        mIvPraise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsPraise) {
                    ToastUtils.showShorToast("已经点赞成功哦");
                } else {
                    doPraise(mBean.getPosterId() + "");
                }
            }
        });

        Glide.with(this).load(mPicUrl).placeholder(R.drawable.dzbb_defaultimage).into(mIvSmallPic);
        ivback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
