package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleRoomLocationActionFragment;
import com.android.incongress.cd.conference.model.Exhibitor;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 参展商详情
 */
public class ExhibitorDetailActionFragment extends BaseFragment {
    private Exhibitor mBean;
    private ImageView logo;
    private TextView title,address,phone,fax,internet,location,info;
    private LinearLayout mllLearningCenter;
    private TextView mTvReadMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exhibitor_detail_view, null);
        initViews(view);

        if(!StringUtils.isEmpty(mBean.getLogo())) {
            Glide.with(getActivity()).load(Constants.getPreUrl() + mBean.getLogo()).placeholder(R.drawable.default_load_bg).into(logo);
        }else{
            logo.setImageResource(R.drawable.default_load_bg);
        }

        if (AppApplication.systemLanguage == 1) {
            title.setText(mBean.getTitle());
        } else if (AppApplication.systemLanguage == 2) {
            if (mBean.getTitleEn() != null && !"".equals(mBean.getTitleEn())) {
                title.setText(mBean.getTitleEn());
            } else {
                title.setText(mBean.getTitle());
            }
        }
        if (AppApplication.systemLanguage == 1) {
            address.setText(mBean.getAddress());
            info.setText(mBean.getInfo());
        } else if (AppApplication.systemLanguage == 2) {
            if (mBean.getAddressEn() != null && !"".equals(mBean.getAddressEn())) {
                address.setText(mBean.getAddressEn());
            } else {
                address.setText(mBean.getAddress());
            }

            if (mBean.getInfoEn() != null && !"".equals(mBean.getInfoEn())) {
                info.setText(mBean.getInfoEn());
            } else {
                info.setText(mBean.getInfo());
            }
        }
        phone.setText(mBean.getPhone());
        fax.setText(mBean.getFax());
        internet.setText(mBean.getNet());
        location.setText(this.getResources().getString(R.string.exhibitor_detail_zanwei, mBean.getLocation()));

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingScheduleRoomLocationActionFragment locationFragment = new MeetingScheduleRoomLocationActionFragment();
                locationFragment.setRoomBean(null, mBean, MeetingScheduleRoomLocationActionFragment.TYPE_EXHIBITOR);
                action(locationFragment, R.string.plane,false, false, false);
            }
        });

        if(TextUtils.isEmpty(mBean.getOtherUrl())) {
            mllLearningCenter.setVisibility(View.GONE);
        }else {
            mllLearningCenter.setVisibility(View.VISIBLE);

            mTvReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebViewContainerActivity.startWebViewContainerActivity(getActivity(),mBean.getOtherUrl(),mBean.getTitle());
                }
            });
        }
        return view;
    }

    private void initViews(View view) {
        logo = (ImageView) view.findViewById(R.id.exhibitor_detail_logo);
        title = (TextView) view.findViewById(R.id.exhibitor_detail_title);
        address = (TextView) view.findViewById(R.id.exhibitor_detail_address);
        phone = (TextView) view.findViewById(R.id.exhibitor_detail_phone);
        fax = (TextView) view.findViewById(R.id.exhibitor_detail_fax);
        internet = (TextView) view.findViewById(R.id.exhibitor_detail_internet);
        location = (TextView) view.findViewById(R.id.exhibitor_detail_location);
        info = (TextView) view.findViewById(R.id.exhibitor_detail_info);
        mllLearningCenter = (LinearLayout) view.findViewById(R.id.ll_learning_more);
        mTvReadMore = (TextView) view.findViewById(R.id.tv_read_more);
    }

    public void setExhibitor(Exhibitor mBean) {
        this.mBean = mBean;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_EXHIBITORSDETAIL);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_EXHIBITORSDETAIL);
    }
}
