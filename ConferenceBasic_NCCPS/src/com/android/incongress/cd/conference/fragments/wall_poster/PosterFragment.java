package com.android.incongress.cd.conference.fragments.wall_poster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.DZBBAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BBFiledTypeBean;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.MyOrderCourse;
import com.android.incongress.cd.conference.data.JsonParser;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListActionFragment;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.WrapContentLinearLayoutManager;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.popup.ChooseBBPopupWindow;
import com.android.incongress.cd.conference.widget.popup.ChooseTimePopupWindow;
import com.android.incongress.cd.conference.widget.zxing.activity.CaptureActivity;
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
 * 电子壁报
 * 测试地址：
 * http://114.80.201.49/posterAction.do?method=getPosterListByConId&count=10&conId=44&pageIndex=1&searchStirng=
 *
 * @author Administrator
 */
public class PosterFragment extends BaseFragment {

    private XRecyclerView mRecyclerView;

    private EditText mSearchEditText;
    private ImageView mCancelImage;
    private LinearLayout mLl_sort,ll_search_part,ll_tips;
    private ImageView mBackTop;
    private TextView mNetWorkError, mNoDate, tv_black_bg;;

    private boolean IsNetWorkOpen = true;
    private List<DZBBBean.ArrayBean> allBeans = new ArrayList<>();
    private DZBBAdapter mAdapter;

    private int currentPage = 0, mDistanceY;// 当前分页位置
    private String mSearchString = ""; //搜索的信息,非搜索状态下为空

    private static final int MSG_REFRESH = 0;
    private static final int MSG_NO_DATA = 2;
    private static final int MSG_TOAST_NO_MORE_DATA = 3;
    private static final int MSG_DONE = 4;
    private static final int EDIT_OK = 104;
    private int toolbarHeight,mCurrentFiled = -1;

    private static float ScreenHeightLPercent = 0.35f;
    private static float ScreenHeightHPercent = 0.45f;
    private float fixHeight;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    @Override
    public void onResume() {
        super.onResume();
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        hideShurufa();
        MobclickAgent.onPageStart(Constants.FRAGMENT_POSTER);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_POSTER);
    }

    /**
     * 提醒
     */
    private TextView mTvTips,tv_type;
    //此handler应用于监听数据加载
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int result = msg.what;

            if (result == MSG_REFRESH) {
                currentPage = 0;
                getDZBBList(true);
                if(mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
            } else if (result == MSG_DONE) {
                mAdapter.notifyDataSetChanged();
            } else if (result == MSG_NO_DATA) {
            } else if (result == MSG_TOAST_NO_MORE_DATA) {
                Toast.makeText(AppApplication.getContext(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
            }
        }
    };
    //此handler用于监听editText输入完成 和Runnable配合使用 输入完成刷新搜索
    private Handler myHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == EDIT_OK) {
                hideShurufa();
                currentPage = 0;
                getDZBBList(true);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.electronic_bb, null);

        mRecyclerView = view.findViewById(R.id.xr_dzbb);
        tv_type = view.findViewById(R.id.tv_type);
        mSearchEditText = view.findViewById(R.id.itv_search_text);
        mCancelImage = view.findViewById(R.id.iv_cancel);
        mLl_sort = view.findViewById(R.id.ll_sort);
        tv_black_bg = view.findViewById(R.id.tv_black_bg);
        mNetWorkError = view.findViewById(R.id.itv_net_error);
        mNoDate = view.findViewById(R.id.no_bb_data);
        ll_tips = view.findViewById(R.id.ll_tips);
        mBackTop = view.findViewById(R.id.iv_back_top);
        rl_layout = view.findViewById(R.id.rl_layout);
        ll_search_part = view.findViewById(R.id.ll_search_part);
        mTvTips = view.findViewById(R.id.tv_tips);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                currentPage = 0;
                mHandler.sendEmptyMessage(MSG_REFRESH);
            }

            @Override
            public void onLoadMore() {
                //加载更多
                getDZBBList(false);
            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (myHandler.hasMessages(EDIT_OK)) {
                    return true;
                } else {
                    hideShurufa();
                    return false;
                }
            }
        });
        mNoDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (myHandler.hasMessages(EDIT_OK)) {
                    return true;
                } else {
                    hideShurufa();
                    return false;
                }
            }
        });

        mRecyclerView.setRefreshing(true);
        mBackTop.setAlpha(0.0f);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                toolbarHeight = DensityUtil.getScreenSize(getActivity())[1];

                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY >= toolbarHeight) {
                    float scale = (float) (mDistanceY - toolbarHeight) / toolbarHeight;
                    float alpha = scale;
                    mBackTop.setAlpha(alpha);
                }else {
                    mBackTop.setAlpha(0.0f);
                }

            }
        });
        mBackTop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mDistanceY < toolbarHeight) {
                    return false;
                } else {
                    mRecyclerView.scrollToPosition(0);
                    mDistanceY = 0;
                    mBackTop.setAlpha(0.0f);
                    return true;
                }
            }
        });

        mTvTips.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(300);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mTvTips.setVisibility(View.GONE);
                        SharePreferenceUtils.saveAppBoolean(Constants.LOOK_POSTER_TIPS, true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mTvTips.startAnimation(alphaAnimation);
            }
        });

        if (SharePreferenceUtils.getAppBoolean(Constants.LOOK_POSTER_TIPS, false)) {
            mTvTips.setVisibility(View.GONE);
        }


        IsNetWorkOpen = NetWorkUtils.isNetworkConnected(getActivity());

        if (!IsNetWorkOpen) {
            mNetWorkError.setVisibility(View.VISIBLE);
        } else {
            mLl_sort.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取壁报类型
                    getDZBBType();
                    /*if (isSortViewOn) {
                        mRlSort.setVisibility(View.GONE);
                        isSortViewOn = false;
                    } else {
                        mRlSort.setVisibility(View.VISIBLE);
                        isSortViewOn = true;
                    }*/

                }
            });

            mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View arg0, boolean focus) {
                    if (focus == true) {
                        toggleShurufa();
                    } else {
                        hideShurufa();
                    }
                }

            });

            mSearchEditText.addTextChangedListener(new TextWatcher() {

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
                    if (mSearchString.length() != 0) {
                        try {
                            mSearchString = URLEncoder.encode(mSearchString, Constants.ENCODING_UTF8);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    myHandler.sendEmptyMessageDelayed(EDIT_OK, 200);
                }
            });

            mCancelImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mSearchEditText.setText("");
                    mSearchEditText.clearFocus();
                    hideShurufa();
                    mCancelImage.setVisibility(View.GONE);
                    //初始化各个搜索条件
                    currentPage = 0;
                    mHandler.sendEmptyMessage(MSG_REFRESH);
                }
            });

            mAdapter = new DZBBAdapter(getActivity(), allBeans);
            mRecyclerView.setAdapter(mAdapter);

        }

        return view;
    }

    /**
     * 获取电子壁报数据
     */
    private void getDZBBList(final boolean isFresh) {
        if(isFresh){
            currentPage = 0;
        }else {
            currentPage++;
        }
        CHYHttpClientUsage.getInstanse().doGetWallPoster(currentPage, mSearchString, mCurrentFiled, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                DZBBBean bean = new Gson().fromJson(response.toString(), new TypeToken<DZBBBean>() {
                }.getType());
                if(isFresh){
                    allBeans.clear();
                }
                if (bean.getArray().size() > 0) {
                    allBeans.addAll(bean.getArray());
                    mNoDate.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessage(MSG_DONE);
                } else if (bean.getArray().size() == 0) {
                    if (allBeans.size() == 0) {
                        mRecyclerView.setVisibility(View.GONE);
                        if(TextUtils.isEmpty(mSearchString)){
                            mNoDate.setVisibility(View.GONE);
                            ll_tips.setVisibility(View.VISIBLE);
                        }else {
                            mNoDate.setVisibility(View.VISIBLE);
                            ll_tips.setVisibility(View.GONE);
                        }
                        mHandler.sendEmptyMessage(MSG_NO_DATA);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mNoDate.setVisibility(View.GONE);
                        ll_tips.setVisibility(View.GONE);
                        mHandler.sendEmptyMessage(MSG_TOAST_NO_MORE_DATA);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.println("statusCode:" + statusCode + ",responseString:" + responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mRecyclerView != null) {
                    mRecyclerView.refreshComplete();
                    mRecyclerView.loadMoreComplete();
                }
            }
        });
    }
    /**
     * 获取电子壁报类型
     */
    private void getDZBBType() {
        CHYHttpClientUsage.getInstanse().doGetWallPosterType(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                newSessionDaysList.clear();
                BBFiledTypeBean bean = new Gson().fromJson(response.toString(), new TypeToken<BBFiledTypeBean>() {
                }.getType());
                newSessionDaysList.addAll(bean.getClassArray());
                if(newSessionDaysList.size() == 0){
                    ToastUtils.showToast("没有数据");
                    return;
                }
                initPopupWindow();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }

        });
    }

    private ChooseBBPopupWindow popupWindow;
    private  ListAdapter listAdapter;
    private RelativeLayout rl_layout;
    private ArrayList<BBFiledTypeBean.ClassArrayBean> newSessionDaysList = new ArrayList<>();
    //创建popupwindow
    private void initPopupWindow(){
        popupWindow = new ChooseBBPopupWindow(getActivity());
        final ListView listView = popupWindow.getmListView();
        listView.setVerticalScrollBarEnabled(false);
        listAdapter = new ListAdapter(getActivity(),newSessionDaysList);
        listView.setAdapter(listAdapter);
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }
        if(DensityUtil.getScreenSize(getActivity())[1]<=1920){
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightLPercent;
        }else {
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightHPercent;
        }
        if (totalHeight > fixHeight) {
            totalHeight = (int) fixHeight;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_black_bg.setVisibility(View.GONE);
                lightOn(rl_layout);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentFiled = newSessionDaysList.get(i).getFieldId();
                listAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
                tv_type.setText(newSessionDaysList.get(i).getFieldName());
                getDZBBList(true);
            }
        });
        tv_black_bg.setVisibility(View.VISIBLE);
        lightOff(tv_black_bg);
        //popupWindow.showAsDropDown(view);
        popupWindow.showAsDropDown(ll_search_part,(int)(DensityUtil.getScreenSize(getActivity())[0]*0.1),0);
    }
    class ListAdapter extends BaseAdapter {
        ArrayList<BBFiledTypeBean.ClassArrayBean> listBeans;
        public Context context;
        public LayoutInflater layoutInflater;
        public ListAdapter (Context context,ArrayList<BBFiledTypeBean.ClassArrayBean> listBeans){
            this.context = context;
            this.listBeans = listBeans;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return listBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PosterFragment.ListAdapter.MyTimeHold myHold;
            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.my_centertextview,null);
                myHold = new PosterFragment.ListAdapter.MyTimeHold();
                myHold.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(myHold);
            }else {
                myHold = (PosterFragment.ListAdapter.MyTimeHold) convertView.getTag();
            }
            myHold.tv_time.setText(listBeans.get(position).getFieldName());
            return convertView;
        }
        class MyTimeHold {
            TextView tv_time;
        }
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
