package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
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
    private int exhibitorsId;
    private ImageView logo;
    private TextView address, phone, internet, location, info, tv_name;
    private static String BUNDLE_EXHIBITOR_ID = "exhibitor_id";

    public static NewExhibitorDetailActionFragment getInstanceFragment(int exhibitorsId) {
        NewExhibitorDetailActionFragment fragment = new NewExhibitorDetailActionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_EXHIBITOR_ID, exhibitorsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_exhibitor_detail_view, null);
        initViews(view);
        exhibitorsId = getArguments().getInt(BUNDLE_EXHIBITOR_ID);
        if (exhibitorsId != 0) {
            getExhibitorDetail(exhibitorsId);
        }
        return view;
    }

    private void initViews(View view) {
        logo = (ImageView) view.findViewById(R.id.exhibitor_detail_logo);
        address = (TextView) view.findViewById(R.id.exhibitor_detail_address);
        phone = (TextView) view.findViewById(R.id.exhibitor_detail_phone);
        internet = (TextView) view.findViewById(R.id.exhibitor_detail_internet);
        location = (TextView) view.findViewById(R.id.exhibitor_detail_location);
        info = (TextView) view.findViewById(R.id.exhibitor_detail_info);
        tv_name = view.findViewById(R.id.tv_name);
    }

    //获取展商信息
    private void getExhibitorDetail(int exhibitorsId) {
        CHYHttpClientUsage.getInstanse().doGetExhibitorDetailInfo(exhibitorsId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String logoUrl = JSONCatch.parseString("logoUrl", response);
                if (!StringUtils.isEmpty(logoUrl)) {
                    PicUtils.loadImageUrl(getContext(), logoUrl, logo);
                } else {
                    logo.setImageResource(R.drawable.default_load_bg);
                }
                StringUtils.setTextShow(tv_name, JSONCatch.parseString("name", response));
                if (AppApplication.systemLanguage == 1) {
                    location.setText("展位： " + JSONCatch.parseString("location", response));
                } else {
                    location.setText("stand： " + JSONCatch.parseString("location", response));
                }
                StringUtils.setTextShow(address, JSONCatch.parseString("address", response));
                StringUtils.setTextShow(phone, JSONCatch.parseString("phone", response));
                StringUtils.setTextShow(internet, JSONCatch.parseString("net", response));
                StringUtils.setTextShow(info, JSONCatch.parseString("content", response));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
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
