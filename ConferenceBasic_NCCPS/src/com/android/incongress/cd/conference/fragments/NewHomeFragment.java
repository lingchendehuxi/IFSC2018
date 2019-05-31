package com.android.incongress.cd.conference.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoadDataActivity;
import com.android.incongress.cd.conference.PolyvVideoPlayDetailActivity;
import com.android.incongress.cd.conference.VideoPlayDetailActivity;
import com.android.incongress.cd.conference.adapters.HomeHotAdapter;
import com.android.incongress.cd.conference.adapters.HomeNormalAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.HomeLiveBean;
import com.android.incongress.cd.conference.beans.HomeMeetBean;
import com.android.incongress.cd.conference.fragments.college.CollegeHomeFragment;
import com.android.incongress.cd.conference.fragments.live.HomeLiveAdapter;
import com.android.incongress.cd.conference.fragments.meet_register.HotMeetFragment;
import com.android.incongress.cd.conference.fragments.meet_register.MeetRegisterFragment;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.refresh_view.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by 13008 on 2019/4/10.
 */

public class NewHomeFragment extends BaseFragment implements HomeLiveAdapter.itemOnclick, HomeLiveAdapter.showMoreOnclick, HomeHotAdapter.OnItemClickListener {
    @BindView(R.id.roll_view)
    RollPagerView rollPagerView;
    @BindView(R.id.ll_meet)
    LinearLayout ll_meet;
    @BindView(R.id.ll_show_all)
    LinearLayout ll_show_all;
    @BindView(R.id.ll_attend)
    LinearLayout ll_attend;
    @BindView(R.id.ll_contribute)
    LinearLayout ll_contribute;
    @BindView(R.id.ll_hotel)
    LinearLayout ll_hotel;
    @BindView(R.id.ll_courseware_all)
    LinearLayout ll_courseware_all;
    @BindView(R.id.h_list)
    RecyclerView h_list;
    @BindView(R.id.courseware_recycler)
    RecyclerView courseware_recycler;
    @BindView(R.id.custom_view)
    XRefreshView custom_view;
    @BindView(R.id.ll_live)
    LinearLayout ll_live;
    private final float IMG_MARGIN = 11f;
    private HomeLiveAdapter mAdapter;
    private HomeHotAdapter mCourSewareAdapter;
    private List<HomeMeetBean> listMeet = new ArrayList<>();
    private List<BookCoursePlayBean.VideoArrayBean> videoList = new ArrayList<>();
    private List<HomeLiveBean> listLive = new ArrayList<>();
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;
    private final int backCode = 10001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        View view = inflater.inflate(R.layout.fragment_new_home, null);
        ButterKnife.bind(this, view);
        initView();
        getHomeInfo();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isBackView) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
    }

    //配置头部显示
    private void initView() {
        List<Drawable> list = new ArrayList<>();
        Drawable drawable = getResources().getDrawable(R.drawable.new_home_top);
        list.add(drawable);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (DensityUtil.getScreenSize(getActivity())[0] / 2));
        rollPagerView.setLayoutParams(lp);
        rollPagerView.setAdapter(new HomeNormalAdapter(list, null, getActivity(), ""));
        rollPagerView.setHintView(null);
        custom_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                if (!NetWorkUtils.isNetworkConnected(getActivity())) {
                    custom_view.stopRefresh();
                    ToastUtils.showToast(getString(R.string.connect_network));
                    return;
                }
                getHomeInfo();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
            }
        });
    }

    /**
     * 获取首页信息
     */
    private void getHomeInfo() {
        CHYHttpClientUsage.getInstanse().doGetHomeResource(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                final List<HomeMeetBean> listBean = new Gson().fromJson(JSONCatch.parseJsonarray("esmoArray", response).toString(), new TypeToken<List<HomeMeetBean>>() {
                }.getType());
                listMeet.clear();
                listMeet.addAll(listBean);
                List<HomeLiveBean> liveListBean = new Gson().fromJson(JSONCatch.parseJsonarray("liveArray", response).toString(), new TypeToken<List<HomeLiveBean>>() {
                }.getType());
                listLive.clear();
                listLive.addAll(liveListBean);
                List<BookCoursePlayBean.VideoArrayBean> HotListBean = new Gson().fromJson(JSONCatch.parseJsonarray("dataHotArray", response).toString(), new TypeToken<List<BookCoursePlayBean.VideoArrayBean>>() {
                }.getType());
                videoList.clear();
                videoList.addAll(HotListBean);
                if (HotListBean != null && HotListBean.size() > 0) {
                    if (HotListBean.size() > 4) {
                        ll_courseware_all.setVisibility(View.VISIBLE);
                    }
                    courseware_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    courseware_recycler.setItemAnimator(new DefaultItemAnimator());
                    mCourSewareAdapter = new HomeHotAdapter(HotListBean, getActivity(), (int) (DensityUtil.getScreenSize(getActivity())[0] / 2), NewHomeFragment.this);
                    courseware_recycler.setAdapter(mCourSewareAdapter);
                }
                if (liveListBean != null && liveListBean.size() > 0) {
                    h_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    h_list.setAdapter(new HomeLiveAdapter(getActivity(), liveListBean, NewHomeFragment.this, NewHomeFragment.this));
                }else {
                    ll_live.setVisibility(View.GONE);
                }
                if (listBean != null && listBean.size() > 0) {
                    ll_meet.removeAllViews();
                    switch (listBean.size()) {
                        case 1:
                            ll_meet.addView(getInnerView(listBean.get(0).getIconUrl(), 0));
                            break;
                        case 2:
                            ll_meet.addView(getInnerLinearLayout(listBean.get(0).getIconUrl(), listBean.get(1).getIconUrl(), 0, 1));
                            break;
                        case 3:
                            ll_meet.addView(getInnerLinearLayout(listBean.get(0).getIconUrl(), listBean.get(1).getIconUrl(), 0, 1));
                            break;
                        case 4:
                            ll_meet.addView(getInnerLinearLayout(listBean.get(0).getIconUrl(), listBean.get(1).getIconUrl(), 0, 1));
                            ll_meet.addView(getInnerLinearLayout(listBean.get(2).getIconUrl(), listBean.get(3).getIconUrl(), 2, 3));
                            break;
                        default:
                            break;
                    }
                    if (listBean.size() > 2 && listBean.size() != 4) {
                        ll_show_all.setVisibility(View.VISIBLE);
                    }
                }
                custom_view.stopRefresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }

    //设置内部会议linearlayout
    private LinearLayout getInnerLinearLayout(String url1, String url2, int position1, int position2) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView imageView1 = new ImageView(getActivity());
        ImageView imageView2 = new ImageView(getActivity());
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams((DensityUtil.getScreenSize(getActivity())[0] - 3 * DensityUtil.dip2px(getActivity(), IMG_MARGIN)) / 2,
                (DensityUtil.getScreenSize(getActivity())[0] - 3 * DensityUtil.dip2px(getActivity(), IMG_MARGIN)) * 3 / 8);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams((DensityUtil.getScreenSize(getActivity())[0] - 3 * DensityUtil.dip2px(getActivity(), IMG_MARGIN)) / 2,
                (DensityUtil.getScreenSize(getActivity())[0] - 3 * DensityUtil.dip2px(getActivity(), IMG_MARGIN)) * 3 / 8);
        int spMarginBottom = DensityUtil.dip2px(getActivity(), IMG_MARGIN);
        params1.setMargins(spMarginBottom, spMarginBottom, spMarginBottom/2, spMarginBottom);
        imageView1.setLayoutParams(params1);
        imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
        params2.setMargins(spMarginBottom/2, spMarginBottom, spMarginBottom, spMarginBottom);
        imageView2.setLayoutParams(params2);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        PicUtils.loadImageUrl(getActivity(), url1, imageView1);
        setImagClick(imageView1, position1);
        linearLayout.addView(imageView1);
        PicUtils.loadImageUrl(getActivity(), url2, imageView2);
        setImagClick(imageView2, position2);
        linearLayout.addView(imageView2);
        return linearLayout;
    }

    //设置内部会议ImageView
    private ImageView getInnerView(String url, int position) {
        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((DensityUtil.getScreenSize(getActivity())[0] - 2 * DensityUtil.dip2px(getActivity(), IMG_MARGIN)),
                (DensityUtil.getScreenSize(getActivity())[0] - 2 * DensityUtil.dip2px(getActivity(), IMG_MARGIN)) * 3 / 4);
        int spMarginBottom = DensityUtil.dip2px(getActivity(), IMG_MARGIN);
        params.setMargins(spMarginBottom*2, spMarginBottom, spMarginBottom, spMarginBottom);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        PicUtils.loadImageUrl(getActivity(), url, imageView);
        setImagClick(imageView, position);
        return imageView;
    }

    private void setImagClick(ImageView imageView, final int position) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeMeetBean bean = listMeet.get(position);
                if(bean.getState()==1){
                    if(bean.getType() == 2){
                        CollegeActivity.startCitCollegeActivity(getActivity(), bean.getConferencesName(), bean.getHtmlUrl());
                    }else {
                        if (SharePreferenceUtils.getDataBoolean(String.valueOf(bean.getTotalConId()), false)) {
                            ((HomeActivity) getActivity()).mNavigationBar.selectTab(2);
                        } else {
                            checkUpdate(bean.getTotalConId(), bean.getConId(), bean.getFromWhere());
                        }
                    }
                }else {
                    ToastUtils.showToast("大会暂未开始，敬请期待");
                }
            }
        });
    }

    /**
     * 查看是否有新的安装包
     */
    private void checkUpdate(final int totalConId, final int conId, final String fromWhere) {
        if(SharePreferenceUtils.getDataBoolean(Constants.DB_frist_COMPASS + totalConId, true)){
            Intent intent = new Intent(getActivity(), LoadDataActivity.class);
            intent.putExtra("totalConId", totalConId);
            intent.putExtra("fromWhere", fromWhere);
            intent.putExtra("conId", conId);
            startActivityForResult(intent, backCode);
            return;
        }
        int mDbVersion = SharePreferenceUtils.getDataInt(Constants.PREFERENCE_DB_VERSION + totalConId, 0);
        //检查更新数据
        CHYHttpClientUsage.getInstanse().doGetInitData(conId, mDbVersion, AppApplication.instance().getVersionName(), totalConId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray array = JSONCatch.parseJsonarray("version", response);
                if (array != null && array.length() > 0) {
                    Intent intent = new Intent(getActivity(), LoadDataActivity.class);
                    intent.putExtra("totalConId", totalConId);
                    intent.putExtra("fromWhere", fromWhere);
                    intent.putExtra("conId", conId);
                    startActivityForResult(intent, backCode);
                } else {
                    action(NewHomeCurrentFragment.getInstance(totalConId, conId, fromWhere), null);
                }
            }

        });
    }

    //参会注册
    @OnClick(R.id.ll_show_all)
    void clickMoreHotMeet() {
        action(HotMeetFragment.getInstance(listMeet), R.string.meet_show_all, false, false, false);
    }

    //参会注册
    @OnClick(R.id.ll_attend)
    void clickAttend() {
        action(MeetRegisterFragment.getInstance(1), R.string.attend_register, false, false, false);
    }

    //投稿
    @OnClick(R.id.ll_contribute)
    void clickContribute() {
        action(MeetRegisterFragment.getInstance(2), R.string.paper_contribute, false, false, false);
    }

    //酒店预订
    @OnClick(R.id.ll_hotel)
    void clickHotel() {
        action(MeetRegisterFragment.getInstance(3), R.string.hotel_reservation, false, false, false);
    }

    //直播item链接
    @Override
    public void onItemClick(int position) {
        CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.live), listLive.get(position).getLiveUrl(), 1);
    }

    @OnClick(R.id.ll_courseware_all)
    void clickAllCourseware() {
        action(CollegeHomeFragment.getInstance(getString(R.string.home_cit_college)), null);
    }

    @Override
    public void onShowMoreClick() {

    }
    //会议课件链接

    @Override
    public void onHotItemClick(View view, int position) {
        if (videoList.get(position).getVideoType() == 3) {
            String[] string = videoList.get(position).getTitle().split(",");
            if (AppApplication.systemLanguage == 1) {
                CollegeActivity.startCitCollegeActivity(getActivity(), string[0], videoList.get(position).getVideoUrl());
            } else {
                CollegeActivity.startCitCollegeActivity(getActivity(), string[1], videoList.get(position).getVideoUrl());
            }
        } else if (videoList.get(position).getVideoType() == 2) {
            Intent intent = new Intent(getActivity(), VideoPlayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("video_play_bean", videoList.get(position));
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        } else if (videoList.get(position).getVideoType() == 1) {
            Intent intent = new Intent(getActivity(), PolyvVideoPlayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("video_play_bean", videoList.get(position));
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == backCode) {
            action(NewHomeCurrentFragment.getInstance(data.getIntExtra("totalConId", 0), data.getIntExtra("conId", 0), data.getStringExtra("fromWhere")), null);
        }
    }
}
