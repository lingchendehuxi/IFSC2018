package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ExhibitorListInfoBean;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 参展商详情
 */
public class NewExhibitorDetailActionFragment extends BaseFragment {
    private ExhibitorListInfoBean.ResultBean exhibitorBean;
    private ImageView logo;
    private TextView address, phone, internet, location, info, tv_name;
    private static String BUNDLE_EXHIBITOR_ID = "exhibitor_id";

    public static NewExhibitorDetailActionFragment getInstanceFragment(ExhibitorListInfoBean.ResultBean exhibitorBean) {
        NewExhibitorDetailActionFragment fragment = new NewExhibitorDetailActionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_EXHIBITOR_ID, exhibitorBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_exhibitor_detail_view, null);
        initViews(view);
        exhibitorBean = (ExhibitorListInfoBean.ResultBean)getArguments().getSerializable(BUNDLE_EXHIBITOR_ID);
        if (exhibitorBean != null) {
            String logoUrl = exhibitorBean.getLogoImg();
            if (!StringUtils.isEmpty(logoUrl)) {
                PicUtils.loadImageUrl(getContext(), logoUrl, logo);
            } else {
                logo.setVisibility(View.GONE);
            }
            StringUtils.setTextShow(tv_name, exhibitorBean.getTitle());
            location.setText(getString(R.string.exhibitor_Location)+exhibitorBean.getLocation());
            StringUtils.setTextShow(address, exhibitorBean.getCompanyAddress());
            StringUtils.setTextShow(phone, exhibitorBean.getPhone());
            StringUtils.setTextShow(internet, exhibitorBean.getUrl());
            String conString = exhibitorBean.getContent();
            if(!TextUtils.isEmpty(conString)){
                StringUtils.setTextShow(info, conString);
            }
        }
        return view;
    }

    private void initViews(View view) {
        logo =  view.findViewById(R.id.exhibitor_detail_logo);
        address =  view.findViewById(R.id.exhibitor_detail_address);
        phone =  view.findViewById(R.id.exhibitor_detail_phone);
        internet =  view.findViewById(R.id.exhibitor_detail_internet);
        location =  view.findViewById(R.id.exhibitor_detail_location);
        info =  view.findViewById(R.id.exhibitor_detail_info);
        tv_name = view.findViewById(R.id.tv_name);
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
