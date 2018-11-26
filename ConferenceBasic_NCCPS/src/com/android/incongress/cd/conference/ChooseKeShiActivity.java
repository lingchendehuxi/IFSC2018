package com.android.incongress.cd.conference;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MarginDecoration;
import com.android.incongress.cd.conference.adapters.MyKeshiAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Jacky on 2016/12/6.
 * 选择科室
 */

public class ChooseKeShiActivity extends BaseActivity {
    @BindView(R.id.title_back)
    ImageView mIvBack;
    @BindView(R.id.title_text)
    TextView mTvTitle;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    private MyKeshiAdapter mKeshiAdapter;
    private boolean mIsOKClickable = false;
    private boolean mIsFromMe = false;

    @OnClick(R.id.tv_confirm)
    void onConfirmClick() {
        if(mIsOKClickable) {
            SharePreferenceUtils.saveAppBoolean(Constants.MY_FIELDS, true);

            if(!mIsFromMe) {
                if(AppApplication.isUserLogIn()) {
                    Intent intent = new Intent(ChooseKeShiActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ChooseKeShiActivity.this, ChooseIdentityActivity.class);
                    intent.putExtra("fromSplash", true);
                    startActivity(intent);
                }
            }
            finish();
        }
    }

    @BindView(R.id.rv_field)
    RecyclerView mRvFields;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_choose_keshi);
    }

    @Override
    protected void initViewsAction() {

        mIsFromMe = getIntent().getBooleanExtra(PersonCenterFragment.EXTRA_FROM_ME, false);

        mTvTitle.setText("选择科室");
        mIvBack.setVisibility(View.GONE);
        mRvFields.addItemDecoration(new MarginDecoration(this));
        mRvFields.setHasFixedSize(true);
        mRvFields.setLayoutManager(new GridLayoutManager(this, 2));

        mKeshiAdapter = new MyKeshiAdapter(ChooseKeShiActivity.this);
        mKeshiAdapter.setOnItemClickListener(new MyKeshiAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String keshi) {
                mKeshiAdapter.setCurrentKeshi(keshi);
                changeOkText();
            }
        });
        mRvFields.setAdapter(mKeshiAdapter);
        changeOkText();
    }

    private void changeOkText() {
        String keshi = SharePreferenceUtils.getAppString(Constants.MY_KESHI);
        if(!TextUtils.isEmpty(keshi)) {
            mIsOKClickable = true;
            mTvConfirm.setTextColor(getResources().getColor(R.color.theme_color));
        }else {
            mIsOKClickable = false;
            mTvConfirm.setTextColor(getResources().getColor(R.color.light_blue_500));
        }
    }
}
