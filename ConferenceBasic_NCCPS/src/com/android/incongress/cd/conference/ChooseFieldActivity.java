package com.android.incongress.cd.conference;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MarginDecoration;
import com.android.incongress.cd.conference.adapters.MyFieldAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MyFieldBean;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jacky on 2016/12/6.
 * 选择领域
 */

public class ChooseFieldActivity extends BaseActivity {
    @BindView(R.id.title_back)
    ImageView mIvBack;
    @BindView(R.id.title_text)
    TextView mTvTitle;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    private MyFieldAdapter mFieldAdapter;
    private boolean mIsOKClickable = false;
    private boolean mIsFromMe = false;

    @OnClick(R.id.tv_confirm)
    void onConfirmClick() {
        if(mIsOKClickable) {
            SharePreferenceUtils.saveAppBoolean(Constants.MY_FIELDS,true);
            //AppApplication.setSPBooleanValue(Constants.MY_FIELDS, true);
            if(!mIsFromMe) {
                startActivity(new Intent(ChooseFieldActivity.this, ChooseKeShiActivity.class));
            }
            finish();
        }else {
            ToastUtils.showToast("请选择至少一个领域");
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
        setContentView(R.layout.activity_choose_field);
    }

    @Override
    protected void initViewsAction() {

        mIsFromMe = getIntent().getBooleanExtra(PersonCenterFragment.EXTRA_FROM_ME, false);
        mTvTitle.setText("选择领域");
        mIvBack.setVisibility(View.GONE);
        mRvFields.addItemDecoration(new MarginDecoration(this));
        mRvFields.setHasFixedSize(true);
        mRvFields.setLayoutManager(new GridLayoutManager(this, 2));

        mFieldAdapter = new MyFieldAdapter(ChooseFieldActivity.this);
        mFieldAdapter.setOnItemClickListener(new MyFieldAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, MyFieldBean bean) {
                if(bean.isFieldState() == 1) {
                    bean.setFieldState(0);
                }else {
                    bean.setFieldState(1);
                }
                mFieldAdapter.notifyDataSetChanged();
                ConferenceDbUtils.changeMyFieldState(bean.getFieldName(), bean.isFieldState());
                changeOkText();
            }
        });
        mRvFields.setAdapter(mFieldAdapter);
        changeOkText();
    }

    private void changeOkText() {
        if(ConferenceDbUtils.isChooseMyField()) {
            mIsOKClickable = true;
            mTvConfirm.setTextColor(getResources().getColor(R.color.theme_color));
        }else {
            mIsOKClickable = false;
            mTvConfirm.setTextColor(getResources().getColor(R.color.light_blue_500));
        }
    }
}
