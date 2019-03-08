package com.android.incongress.cd.conference.fragments.college;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.PolyvVideoPlayDetailActivity;
import com.android.incongress.cd.conference.VideoPlayDetailActivity;
import com.android.incongress.cd.conference.adapters.CollegeListFragmentAdapter;
import com.android.incongress.cd.conference.adapters.CollegeViewGridViewAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClient;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeTitleListBean;
import com.android.incongress.cd.conference.beans.CollegeVideoBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.beans.MessageBean;
import com.android.incongress.cd.conference.utils.CacheManager;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.TimeUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.MyViewPager;
import com.android.incongress.cd.conference.widget.SpaceItemDecoration;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.blws.PolyvKeyBoardUtils;
import com.android.incongress.cd.conference.widget.cardview.LCardView;
import com.android.incongress.cd.conference.widget.popup.InputMethodUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Jacky on 2016/1/31.
 */
public class CollegeHomeFragment extends BaseFragment implements XRecyclerView.LoadingListener, CollegeViewGridViewAdapter.VideoItemClick {
    private MyViewPager mViewPager;
    private CollegeListFragmentAdapter mPageAdapter;
    private ArrayList<String> mSessionDaysList = new ArrayList<>();
    private ArrayList<String> mSessionIDsList = new ArrayList<>();
    private TextView mTitle;
    private ImageView title_back;
    private LinearLayout ll_date_select;
    private int mCurrentPage = 0;
    private EditText et_search;
    //缓存
    private CacheManager cacheManager;
    private static final String CACHE_COLLEGE_TITLE = "college_title_list";
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;
    private FrameLayout fl_search;
    private static final int EDIT_OK = 104;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        View view = inflater.inflate(R.layout.fragment_college_home, null, false);
        mViewPager = view.findViewById(R.id.viewpager);
        title_back = view.findViewById(R.id.title_back);
        mTitle = view.findViewById(R.id.title_text);
        ll_date_select = view.findViewById(R.id.ll_date_select);
        et_search = view.findViewById(R.id.et_search);
        initSearchView(view);
        cacheManager = CacheManager.getInstance().open(CACHE_COLLEGE_TITLE, 1);
        mTitle.setText(getString(R.string.home_cit_college));
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
                et_search.clearFocus();
                mSearchString = "";
                InputMethodUtils.hideSoftInput(getContext(), et_search);
                if (mViewPager.getVisibility() == View.VISIBLE) {
                    ((HomeActivity) getActivity()).performBackClick();
                } else {
                    fl_search.setVisibility(View.GONE);
                    ll_date_select.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.VISIBLE);
                }
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                mSearchString = textView.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(mSearchString)) {
                        ToastUtils.showToast("请先输入搜索内容");
                        return false;
                    }
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            ll_date_select.setVisibility(View.GONE);
                            /*mPageAdapter.setmSearchString(mSearchString);
                            mCurrentPage = mViewPager.getCurrentItem();
                            mViewPager.setAdapter(mPageAdapter);
                            mViewPager.setCurrentItem(mSessionDaysList.size()+1);*/
                            PolyvKeyBoardUtils.closeKeybord(et_search, getActivity());
                            mViewPager.setVisibility(View.GONE);
                            fl_search.setVisibility(View.VISIBLE);
                            getSearchVideoData("-1", true);
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
        //修改输入法按钮
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int countA) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //直接进行搜索
                mSearchString = s.toString();
                if (!TextUtils.isEmpty(mSearchString)) {
                    try {
                        mSearchString = URLEncoder.encode(mSearchString, Constants.ENCODING_UTF8);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessageDelayed(EDIT_OK, 200);
                } else {
                    mSearchString = "";
                    videoList.clear();
                    mVideoAdapter.notifyDataSetChanged();
                }
            }
        });
        loadLocalDate();
        return view;
    }

    //此handler用于监听editText输入完成 和Runnable配合使用 输入完成刷新搜索
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == EDIT_OK) {
                hideShurufa();
                ll_date_select.setVisibility(View.GONE);
                PolyvKeyBoardUtils.closeKeybord(et_search, getActivity());
                mViewPager.setVisibility(View.GONE);
                fl_search.setVisibility(View.VISIBLE);
                getSearchVideoData("-1", true);
            }
            return false;
        }
    });

    //无网络的时候加载本地数据
    private void loadLocalDate() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            String json = cacheManager.getString(CACHE_COLLEGE_TITLE);
            if (!TextUtils.isEmpty(json)) {
                Gson gson = new Gson();
                mSessionDaysList.clear();
                mSessionIDsList.clear();
                CollegeTitleListBean bean = gson.fromJson(json, new TypeToken<CollegeTitleListBean>() {
                }.getType());
                for (int i = 0; i < bean.getItemArray().size(); i++) {
                    mSessionDaysList.add(bean.getItemArray().get(i).getItemName());
                    mSessionIDsList.add(bean.getItemArray().get(i).getItemId());
                }
                //获取title成功后操作
                getSessionDays();
                mPageAdapter = new CollegeListFragmentAdapter(getChildFragmentManager(), mSessionDaysList, mSessionIDsList);
                mViewPager.setScrollble(false);
                mViewPager.setAdapter(mPageAdapter);
                mViewPager.setOffscreenPageLimit(3);
                for (int i = 0; i < mSessionDaysList.size(); i++) {
                    if (TimeUtils.getCurrentTimeMD().equals(mSessionDaysList.get(i))) {
                        mCurrentPage = i;
                    }
                }
                mViewPager.setCurrentItem(mCurrentPage, false);
                ToastUtils.showToast(getString(R.string.connect_network));
            }
        } else {
            getTitleList();
        }
    }

    private void getSessionDays() {
        final ArrayList<LCardView> listCard = new ArrayList<>();
        final ArrayList<TextView> listText = new ArrayList<>();
        final int length = mSessionDaysList.size();

        for (int i = 0; i < length; i++) {
            final View view;
            if (getActivity() == null) {
                return;
            }
            if (i == 0) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.my_centertextview_first, null);
            } else {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.my_centertextview_sec, null);
            }
            final LinearLayout ll_normal = view.findViewById(R.id.ll_normal);
            final LinearLayout ll_card = view.findViewById(R.id.ll_card);
            final LCardView cardView = view.findViewById(R.id.card_view);
            TextView tv_line = view.findViewById(R.id.tv_line);
            final TextView textView = view.findViewById(R.id.tv_chose_time);
            final TextView normalView = view.findViewById(R.id.tv_normal_time);
            listCard.add(cardView);
            listText.add(normalView);
            int h_size = DensityUtil.getScreenSize(getActivity())[0];
            FrameLayout.LayoutParams params;
            FrameLayout.LayoutParams params2;
            FrameLayout.LayoutParams params3;
            if (length >= 4) {
                params = new FrameLayout.LayoutParams(h_size / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
                params3 = new FrameLayout.LayoutParams(h_size / 4, ViewGroup.LayoutParams.MATCH_PARENT);
                params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 1));
            } else {
                params = new FrameLayout.LayoutParams(h_size / length, ViewGroup.LayoutParams.WRAP_CONTENT);
                params3 = new FrameLayout.LayoutParams(h_size / length, ViewGroup.LayoutParams.MATCH_PARENT);
                params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 1));
            }
            params2.gravity = Gravity.BOTTOM;
            tv_line.setLayoutParams(params2);
            ll_normal.setLayoutParams(params3);
            ll_card.setLayoutParams(params);
            textView.setTextSize(14);
            normalView.setTextSize(14);
            textView.setText(mSessionDaysList.get(i));
            normalView.setText(mSessionDaysList.get(i));
            normalView.setTag(i);
            normalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemView) {
                    for (int i = 0; i < length; i++) {
                        listCard.get(i).setVisibility(View.GONE);
                        listText.get(i).setVisibility(View.VISIBLE);
                        if (i == 0) {
                            FrameLayout frameLayout = (FrameLayout) listText.get(i).getParent();
                            frameLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    if ((int) itemView.getTag() == 0) {
                        FrameLayout frameLayout = (FrameLayout) itemView.getParent();
                        frameLayout.setVisibility(View.GONE);
                    }
                    cardView.setVisibility(View.VISIBLE);
                    itemView.setVisibility(View.GONE);
                    mViewPager.setCurrentItem((int) normalView.getTag());
                }
            });
            ll_date_select.addView(view);
        }
        listText.get(0).performClick();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isBackView) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.VISIBLE);
        MobclickAgent.onPageStart(Constants.ACTIVITY_COLLEGE);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.VISIBLE);
        MobclickAgent.onPageEnd(Constants.ACTIVITY_COLLEGE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
    }

    //获取首页火速上线等列表
    private void getTitleList() {
        CHYHttpClientUsage.getInstanse().doGetCollegeTitle(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                CollegeTitleListBean bean = gson.fromJson(response.toString(), new TypeToken<CollegeTitleListBean>() {
                }.getType());
                if ("1".equals(bean.getState())) {
                    cacheManager.saveString(CACHE_COLLEGE_TITLE, response.toString());
                    for (int i = 0; i < bean.getItemArray().size(); i++) {
                        mSessionDaysList.add(bean.getItemArray().get(i).getItemName());
                        mSessionIDsList.add(bean.getItemArray().get(i).getItemId());
                    }
                } else {
                    ToastUtils.showToast("状态码错误");
                    return;
                }
                //获取title成功后操作
                getSessionDays();
                mPageAdapter = new CollegeListFragmentAdapter(getChildFragmentManager(), mSessionDaysList, mSessionIDsList);
                mViewPager.setScrollble(false);
                mViewPager.setAdapter(mPageAdapter);
                mViewPager.setOffscreenPageLimit(3);
                for (int i = 0; i < mSessionDaysList.size(); i++) {
                    if (TimeUtils.getCurrentTimeMD().equals(mSessionDaysList.get(i))) {
                        mCurrentPage = i;
                    }
                }
                mViewPager.setCurrentItem(mCurrentPage, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }

    //对加载搜索界面处理
    private LinearLayout ll_tips;
    private XRecyclerView xRecyclerView;
    private List<FastOnLineBean.VideoArrayBean> videoList;
    //是否有更多数据： 1：代表还有 0：代表已经没有了
    private String mIsMore;
    private CollegeViewGridViewAdapter mVideoAdapter;
    private String mLastId;
    private String mSearchString;

    @Override
    public void onRefresh() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            xRecyclerView.refreshComplete();
            ToastUtils.showToast(getString(R.string.connect_network));
            return;
        }
        getSearchVideoData("-1", false);
    }

    @Override
    public void onLoadMore() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            xRecyclerView.loadMoreComplete();
            ToastUtils.showToast(getString(R.string.connect_network));
            return;
        }
        getSearchVideoData(mLastId, false);
    }

    private void initSearchView(View view) {
        fl_search = view.findViewById(R.id.fl_search);
        xRecyclerView = view.findViewById(R.id.no_gv_list);
        ll_tips = view.findViewById(R.id.ll_tips);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        xRecyclerView.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.app_normal_margin);
        xRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setLoadingListener(this);
        videoList = new ArrayList<>();
        mVideoAdapter = new CollegeViewGridViewAdapter(videoList, getActivity(), this);
        xRecyclerView.setAdapter(mVideoAdapter);
    }

    /**
     * 获取搜索的数据
     *
     * @param lastId
     */
    private void getSearchVideoData(final String lastId, final boolean isNew) {
        if (TextUtils.isEmpty(mSearchString)) {
            return;
        }
        CHYHttpClientUsage.getInstanse().doGetSearchCollegeTitle(mSearchString, lastId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                FastOnLineBean bean = new Gson().fromJson(response.toString(), new TypeToken<FastOnLineBean>() {
                }.getType());
                if (bean.getVideoArray().size() != 0) {
                    //刷新进来
                    if ("-1".equals(lastId)) {
                        videoList.clear();
                        xRecyclerView.refreshComplete();
                        xRecyclerView.setLoadingMoreEnabled(true);
                        //加载更多进来
                    } else {
                        xRecyclerView.loadMoreComplete();
                        if ("0".equals(mIsMore)) {
                            ToastUtils.showToast(getString(R.string.incongress_send_no_more_data));
                            xRecyclerView.setLoadingMoreEnabled(false);
                            return;
                        }
                    }
                    videoList.addAll(bean.getVideoArray());
                    ll_tips.setVisibility(View.GONE);
                    mVideoAdapter.notifyDataSetChanged();
                    mLastId = String.valueOf(bean.getVideoArray().get(bean.getVideoArray().size() - 1).getDataId());
                    mIsMore = bean.getIsNextPage();
                } else {
                    if (isNew) {
                        videoList.clear();
                        mVideoAdapter.notifyDataSetChanged();
                    }
                    if ("0".equals(bean.getIsNextPage()) && videoList.size() != 0 && !isNew) {
                        xRecyclerView.loadMoreComplete();
                        ToastUtils.showToast(getString(R.string.incongress_send_no_more_data));
                        xRecyclerView.setLoadingMoreEnabled(false);
                        return;
                    }
                    xRecyclerView.refreshComplete();
                    xRecyclerView.loadMoreComplete();
                    ll_tips.setVisibility(View.VISIBLE);
                    return;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    @Override
    public void itemClick(int position) {
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
            bundle.putSerializable(Constants.VIDEO_DETIAL_BEAN, videoList.get(position));
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        } else if (videoList.get(position).getVideoType() == 1) {
            Intent intent = new Intent(getActivity(), PolyvVideoPlayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.VIDEO_DETIAL_BEAN, videoList.get(position));
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }
}
