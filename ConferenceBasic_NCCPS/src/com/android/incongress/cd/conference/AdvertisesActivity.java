package com.android.incongress.cd.conference;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.Ad;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.widget.CountDownButtonHelper;
import com.android.incongress.cd.conference.widget.Rotatable;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jacky on 2016/12/23.
 * 广告页
 */

public class AdvertisesActivity extends BaseActivity {
    @BindView(R.id.rl_card_root)
    RelativeLayout rlCardRoot;
    @BindView(R.id.imageView_front)
    ImageView mIvFrontAD;
    @BindView(R.id.imageView_back)
    ImageView mIvBackAD;
    @BindView(R.id.tv_skip)
    TextView mBtSkip;

    //是否已经结束
    private boolean mIsFinish = false;
    //主页广告的数量
    private int mMainADNum = 0;

    //倒计时按钮
    CountDownButtonHelper mCountDownHelper;
    //广告列表
    private List<Ad> mAdList;

    private static final int FRONT_POSITION = 0;
    private static final int BACK_POSITION = 1;
    //0正面位置，1反面位置
    private int mCurrontPosition = FRONT_POSITION;

    //广告跳转
    private String mFrontUrl = "";
    private String mBackUrl = "";

    @OnClick(R.id.tv_skip)
    void onSkipCLick() {
        if(mMainADNum == 1) {
            goHomeActivity();
        }else {
            if (mCurrontPosition == FRONT_POSITION) {
                cardTurnover();
            }else {
                goHomeActivity();
            }
        }
    }

    private void goHomeActivity() {
        if(true) {
            Intent intent = new Intent();
            intent.setClass(AdvertisesActivity.this, HomeActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent();
            intent.setClass(AdvertisesActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
        mIsFinish = true;
    }

    @OnClick(R.id.imageView_front)
    void onFrontClick() {
        if (!TextUtils.isEmpty(mFrontUrl.trim())) {
            mCountDownHelper.stop();
            goHomeActivity();
            WebViewContainerActivity.startWebViewContainerActivity(AdvertisesActivity.this, mFrontUrl, "");
        }
    }

    @OnClick(R.id.imageView_back)
    void onBackClick() {
        if (!TextUtils.isEmpty(mBackUrl.trim())) {
            mCountDownHelper.stop();
            goHomeActivity();
            WebViewContainerActivity.startWebViewContainerActivity(AdvertisesActivity.this, mFrontUrl, "");
        }
    }

    private  List<Ad> mMainAdList = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_advertises);
    }

    @Override
    protected void initViewsAction() {
        mIvFrontAD.setImageResource(R.drawable.guide_1);
        mIvBackAD.setImageResource(R.drawable.guide_2);

        setCameraDistance();
        mIvFrontAD.setVisibility(View.VISIBLE);
        mIvBackAD.setVisibility(View.INVISIBLE);
        mAdList = ConferenceDbUtils.getAllAds();

        if (mAdList != null && mAdList.size() > 0) {
            for (Ad bean : mAdList) {
                if (bean.getAdLevel() == 1) {
                    mMainAdList.add(bean);
                }
            }

            mMainADNum = mMainAdList.size();
        }

        if(mMainADNum > 0) {
            if(mMainADNum == 1) {
                Ad ad = mMainAdList.get(0);
                String filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR + ad.getAdImage();
                File f = new File(filespath);
                if (f.exists()) {
                    Uri uri = Uri.fromFile(f);
                    mIvFrontAD.setImageURI(uri);
                    mIvBackAD.setImageURI(uri);

                    mFrontUrl = ad.getAdLink();
                    mBackUrl = ad.getAdLink();
                }
            }else {
                Ad ad = mMainAdList.get(0);
                String filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR + ad.getAdImage();

                File f = new File(filespath);
                if (f.exists()) {
                    mIvFrontAD.setImageBitmap( PicUtils.getSmallBitmap(filespath));
                    mFrontUrl = ad.getAdLink();
                }

                Ad ad2 = mMainAdList.get(1);
                String filespath2 = AppApplication.instance().getSDPath() + Constants.FILESDIR + ad2.getAdImage();
                File f2 = new File(filespath2);
                if (f2.exists()) {
                    mIvBackAD.setImageBitmap( PicUtils.getSmallBitmap(filespath2));
                    mBackUrl = ad2.getAdLink();
                }
            }

            mCountDownHelper = new CountDownButtonHelper(mBtSkip, getString(R.string.home_skip), 3, 1);
            mCountDownHelper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                @Override
                public void finish() {
                    if(!mIsFinish)
                        if(mMainADNum == 1) {
                            goHomeActivity();
                        }else {
                            if (mCurrontPosition == FRONT_POSITION) {
                                cardTurnover();
                            }else {
                                goHomeActivity();
                            }
                        }
                }
            });
            mCountDownHelper.start();
        }else {
            goHomeActivity();
        }
    }

    /**
     * 翻转到第二张广告
     */
    public void cardTurnover() {
        mCountDownHelper.stop();
        mBtSkip.setVisibility(View.GONE);
        Rotatable rotatable = new Rotatable.Builder(rlCardRoot)
                .sides(R.id.imageView_front, R.id.imageView_back)
                .direction(Rotatable.ROTATE_Y)
                .rotationCount(1)
                .build();
        rotatable.setTouchEnable(false);
        rotatable.rotate(Rotatable.ROTATE_Y, -180, 1500);
        ViewHelper.setRotationY(mIvBackAD, 180f);//先翻转180，转回来时就不是反转的了

        mCurrontPosition = BACK_POSITION;
        mCountDownHelper = new CountDownButtonHelper(mBtSkip, getString(R.string.home_skip), 3, 1);
        mCountDownHelper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
                if(!mIsFinish)
                    goHomeActivity();
            }
        });
        mCountDownHelper.start();
        mBtSkip.setVisibility(View.VISIBLE);
    }

    /**
     * 改变视角距离, 贴近屏幕
     */
    private void setCameraDistance() {
        int distance = 10000;
        float scale = getResources().getDisplayMetrics().density * distance;
        rlCardRoot.setCameraDistance(scale);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.ACTIVITY_ADVERTISE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.ACTIVITY_ADVERTISE);
    }
}
