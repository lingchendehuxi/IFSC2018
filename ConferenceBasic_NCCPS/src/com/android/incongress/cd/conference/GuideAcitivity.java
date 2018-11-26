package com.android.incongress.cd.conference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.widget.VerticalLinearLayout;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Jacky on 2016/2/19.
 * <p>
 * 引导页面
 */
public class GuideAcitivity extends BaseActivity {
    private VerticalLinearLayout mVerticalLayout;
    private TextView mTvGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVerticalLayout = (VerticalLinearLayout) findViewById(R.id.id_main_ly);
        mTvGo = (TextView) findViewById(R.id.tv_go);
        mVerticalLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                if (currentPage == 3) {
                    mTvGo.setVisibility(View.VISIBLE);
                }
            }

        });

        mTvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtils.saveAppString(Constants.NEED_GUIDE, Constants.NEED_GUIDE_FALSE+"");
                //广告页
                Intent intent = new Intent();
                intent.setClass(GuideAcitivity.this, AdvertisesActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initViewsAction() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.ACTIVITY_GUIDE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.ACTIVITY_GUIDE);
    }
}
