package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.ScenicXiuPicsViewpagerActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.ExhibitorListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.PhotoTypeBean;
import com.android.incongress.cd.conference.fragments.meeting_guide.MeetingGuideRoomMapFragment;
import com.android.incongress.cd.conference.model.Exhibitor;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.transformer.CircleTransform;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 参展商列表
 *
 * @author Jacky_Chen
 * @time 2014年11月25日
 */
public class ExhibitorsActionFragment extends BaseFragment {
    private ListView mListView;
    private ExhibitorListAdapter mAdapter;
    private TextView mNoDataView;
    private LinearLayout mLayout, mSessionLayout, mZWTLayout, mSFLayout;
    private TextView mSessioncn, mSessionen, mZWTcn, mZWTen;
    private ImageView mSessionimg, mZWTimg;
    private String sessionUrl, zwtUrls, sessionCn;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    public ExhibitorsActionFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.exhibitor_list_view, null);
        mListView = (ListView) view.findViewById(R.id.exhibitor_list);
        mNoDataView = (TextView) view.findViewById(R.id.exhibitor_no_data);
        mLayout = (LinearLayout) view.findViewById(R.id.title_layout);

        mSFLayout = (LinearLayout) view.findViewById(R.id.sf_layout);
        mSessionLayout = (LinearLayout) view.findViewById(R.id.session_layout);
        mZWTLayout = (LinearLayout) view.findViewById(R.id.zwt_layout);
        mSessionimg = (ImageView) view.findViewById(R.id.session_img);
        mZWTimg = (ImageView) view.findViewById(R.id.zwt_img);
        mSessioncn = (TextView) view.findViewById(R.id.session_cn);
        mSessionen = (TextView) view.findViewById(R.id.session_en);
        mZWTcn = (TextView) view.findViewById(R.id.zwt_cn);
        mZWTen = (TextView) view.findViewById(R.id.zwt_en);
        mAdapter = new ExhibitorListAdapter(this.getActivity());
        mListView.setAdapter(mAdapter);
        int count = mAdapter.getCount();
        if (count == 0) {
            mNoDataView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mLayout.setVisibility(View.GONE);
            mSFLayout.setVisibility(View.GONE);
        } else {
            mNoDataView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.VISIBLE);
        }
        getData();
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Exhibitor mBean = (Exhibitor) mAdapter.getItem(position);
                ExhibitorDetailActionFragment fragment = new ExhibitorDetailActionFragment();
                fragment.setExhibitor(mBean);
                action(fragment, R.string.exhibitor_detail_title, false, false, false);
            }

        });

        mSessionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewContainerActivity.startWebViewContainerActivity(getActivity(), sessionUrl + "&userId=" + AppApplication.userId + "&lan=" + AppApplication.getSystemLanuageCode(), sessionCn);
            }
        });
        mZWTLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] strPics = zwtUrls.split(",");
                ScenicXiuPicsViewpagerActivity.startViewPagerActivity(getActivity(), strPics, 0);
            }
        });
        return view;
    }

    private void getData() {
        CHYHttpClientUsage.getInstanse().doGetCzs(Constants.getConId() + "", new JsonHttpResponseHandler("gbk") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("photoAlbum", response.toString());
                sessionUrl = JSONCatch.parseString("sessionUrl", response);
                if (sessionUrl.equals("")) {
                    mSFLayout.setVisibility(View.GONE);
                } else {
                    mSFLayout.setVisibility(View.VISIBLE);
                    mNoDataView.setVisibility(View.GONE);
                    zwtUrls = JSONCatch.parseString("zwtUrls", response);
                    if (zwtUrls.equals("")) {
                        mZWTLayout.setVisibility(View.GONE);
                    } else {
                        mZWTLayout.setVisibility(View.VISIBLE);
                    }
                    sessionCn = JSONCatch.parseString("sessionCn", response);
                    PicUtils.loadImageUrl(getContext(), JSONCatch.parseString("zwtLogoUrl", response), mZWTimg);
                    mSessioncn.setText(sessionCn);
                    mSessionen.setText(JSONCatch.parseString("sessionEn", response));
                    mZWTcn.setText(JSONCatch.parseString("zwtCn", response));
                    mZWTen.setText(JSONCatch.parseString("zwtEn", response));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        MobclickAgent.onPageStart(Constants.FRAGMENT_EXHIBITORS);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_EXHIBITORS);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
