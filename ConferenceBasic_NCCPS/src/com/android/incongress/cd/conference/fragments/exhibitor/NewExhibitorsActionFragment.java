package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.MindInfoAdapter;
import com.android.incongress.cd.conference.adapters.NewExhibitorActionAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ExhibitorListInfoBean;
import com.android.incongress.cd.conference.beans.ExhibitorTitleBean;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.MyViewPager;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.mtablayout.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewExhibitorsActionFragment extends BaseFragment {
    private int mCurrentPage = 0;
    private CommonTabLayout tab_layout;
    private MyViewPager mViewPager;
    private NewExhibitorActionAdapter adapter;
    private ArrayList<CustomTabEntity> mTabEntities2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        View view = inflater.inflate(R.layout.new_exhibitors_action_fragment, null);
        mViewPager = view.findViewById(R.id.viewpager);
        tab_layout = view.findViewById(R.id.tab_layout);
        initView(view);
        tab_layout.setIndicatorWidth(24);
        getTopTitle();

        return view;
    }

    private void initView(View view) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tab_layout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab_layout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        MobclickAgent.onPageStart(Constants.FRAGMENT_MESSAGESTATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MESSAGESTATION);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    //获取首页列表
    private void getTopTitle() {
        CHYHttpClientUsage.getInstanse().doGetExhibitorTopInfo(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(JSONCatch.parseInt("state",response) == 1){
                    ExhibitorTitleBean bean = new Gson().fromJson(response.toString(), new TypeToken<ExhibitorTitleBean>() {
                    }.getType());
                    for(int i = 0;i<bean.getMenu().size();i++){
                        mTabEntities2.add(new TabEntity(bean.getMenu().get(i).getMenuName(), R.drawable.bottom_home, R.drawable.bottom_home));
                    }
                    adapter = new NewExhibitorActionAdapter(getChildFragmentManager(), bean.getMenu(), bean.getMenu().size());
                    mViewPager.setScrollble(true);
                    mViewPager.setAdapter(adapter);

                    //tab_layout.setViewPager(mViewPager);
                    tab_layout.setTabData(mTabEntities2);
                    mViewPager.setOffscreenPageLimit(4);
                    mViewPager.setCurrentItem(mCurrentPage);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
    }
}
