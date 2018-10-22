package com.android.incongress.cd.conference.fragments.wall_poster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.adapters.DZBBAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.data.JsonParser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.widget.RefreshLayout;
import com.android.incongress.cd.conference.widget.zxing.activity.CaptureActivity;
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

    private RefreshLayout mRefreshLayout;
    private ListView mLvDzbb;

    private EditText mSearchEditText;
    private ImageView mCancelImage;
    private LinearLayout mRlSort;
    private LinearLayout mLlCode;
    private LinearLayout mLlAuthor;
    private LinearLayout mLl_sort;
    private ImageView mIvCode;
    private ImageView mIvAuthor;
    private TextView mNoData;
    private TextView mNetWorkError;

    private boolean IsNetWorkOpen = true;
    private List<DZBBBean> dzbbBeans = new ArrayList<DZBBBean>();
    private List<DZBBBean> allBeans = new ArrayList<DZBBBean>();
    private DZBBAdapter mAdapter;
    private int orderBy = 1; //1按照code排序，0按照Author排序

    private int currentPage = 1;// 当前分页位置
    private String mSearchString = ""; //搜索的信息,非搜索状态下为空

    private static final int MSG_REFRESH = 0;
    private static final int MSG_DONE = 1;
    private static final int MSG_ERROR = 1;
    private static final int MSG_NO_DATA = 2;
    private static final int MSG_TOAST_NO_MORE_DATA = 3;

    /**
     * 判断是否是搜索模式，防止重复加载
     **/
    private boolean isSearchState = false;

    private boolean isSortViewOn = false;

    @Override
    public void onResume() {
        super.onResume();
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
    private TextView mTvTips;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int result = msg.what;

            if (result == MSG_REFRESH) {
                mNoData.setVisibility(View.GONE);
                getDZBBList();
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.refreshComplete();
                if (isSearchState) {
                    isSearchState = !isSearchState;
                }
            } else if (result == MSG_DONE) {
                mNoData.setVisibility(View.GONE);
                mRefreshLayout.loadMoreComplete();
                mAdapter.notifyDataSetChanged();
            } else if (result == MSG_ERROR) {
                mNoData.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "加载数据出错，请稍后重试", Toast.LENGTH_SHORT).show();
                currentPage = 1;
                mSearchString = "";
                dzbbBeans.clear();
                orderBy = 1;
                getDZBBList();
                mAdapter.notifyDataSetChanged();
            } else if (result == MSG_NO_DATA) {
                mNoData.setVisibility(View.VISIBLE);
                mRefreshLayout.setVisibility(View.GONE);
            } else if (result == MSG_TOAST_NO_MORE_DATA) {
                Toast.makeText(AppApplication.getContext(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.electronic_bb, null);

        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        mLvDzbb = (ListView) view.findViewById(R.id.lv_dzbb);

        mSearchEditText = (EditText) view.findViewById(R.id.itv_search_text);
        mCancelImage = (ImageView) view.findViewById(R.id.iv_cancel);
        mRlSort = (LinearLayout) view.findViewById(R.id.rl_sort);
        mLlCode = (LinearLayout) view.findViewById(R.id.ll_bb_code);
        mLlAuthor = (LinearLayout) view.findViewById(R.id.ll_bb_author);
        mIvCode = (ImageView) view.findViewById(R.id.iv_code);
        mIvAuthor = (ImageView) view.findViewById(R.id.iv_author);
        mLl_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
        mNetWorkError = (TextView) view.findViewById(R.id.itv_net_error);
        mNoData = (TextView) view.findViewById(R.id.no_bb_data);

        mTvTips = (TextView) view.findViewById(R.id.tv_tips);

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

        if (SharePreferenceUtils.getAppBooleanF(Constants.LOOK_POSTER_TIPS)) {
            mTvTips.setVisibility(View.GONE);
        }

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                currentPage = 1;
                allBeans.clear();
                dzbbBeans.clear();
                mHandler.sendEmptyMessage(MSG_REFRESH);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new RefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //加载更多
                if (!isSearchState)
                    getDZBBList();
            }
        });

        IsNetWorkOpen = NetWorkUtils.isNetworkConnected(getActivity());

        if (!IsNetWorkOpen) {
            mNetWorkError.setVisibility(View.VISIBLE);
        } else {
            mLlCode.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearChoose();
                    mIvCode.setImageResource(R.drawable.speaker_role_choose);
                    //按照壁报机编号排序 搜索
                    //肯定是先清空原来的数据，然后重新请求数据
                    currentPage = 1;
                    orderBy = 1;
                    dzbbBeans.clear();
                    allBeans.clear();
                    mHandler.sendEmptyMessage(MSG_REFRESH);
                    mRlSort.setVisibility(View.GONE);
                }
            });

            mLlAuthor.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    clearChoose();
                    mIvAuthor.setImageResource(R.drawable.speaker_role_choose);

                    //按照作者编号排序 搜索
                    //肯定是先清空原来的数据，然后重新请求数据
                    currentPage = 1;
                    dzbbBeans.clear();
                    orderBy = 0;
                    allBeans.clear();
                    mHandler.sendEmptyMessage(MSG_REFRESH);
                    mRlSort.setVisibility(View.GONE);
                }
            });

            mLl_sort.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSortViewOn) {
                        mRlSort.setVisibility(View.GONE);
                        isSortViewOn = false;
                    } else {
                        mRlSort.setVisibility(View.VISIBLE);
                        isSortViewOn = true;
                    }

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
                    isSearchState = true;
                    int length = mSearchEditText.getText().length();
                    mNoData.setVisibility(View.GONE);
                    if (length == 0) {
                        dzbbBeans.clear();
                        currentPage = 1;
                        allBeans.clear();
                        mSearchString = "";
                        getDZBBList();
                        return;
                    }

                    if (length > 0) {
                        if (mNoData.VISIBLE == View.VISIBLE)
                            mCancelImage.setVisibility(View.VISIBLE);
                        //直接进行搜索
                        mSearchString = s.toString();
                        try {
                            mSearchString = URLEncoder.encode(mSearchString, Constants.ENCODING_UTF8);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                    dzbbBeans.clear();
                    currentPage = 1;
                    allBeans.clear();
                    getDZBBList();
                }
            });


            mLvDzbb.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                    if (!AppApplication.isUserLogIn()) {
						LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_PROFESSOR, "", "", "", "");
//                        ChooseIdentityActivity.startChooseIdentityActivity(getActivity());
                        return;
                    }

                    DZBBBean bean = (DZBBBean) parent.getAdapter().getItem(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PosterImageFragment.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",bean);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
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
                    dzbbBeans.clear();
                    currentPage = 1;
                    mSearchString = "";
                    allBeans.clear();
                    mHandler.sendEmptyMessage(MSG_REFRESH);
                }
            });

            mAdapter = new DZBBAdapter(getActivity(), allBeans);
            mLvDzbb.setAdapter(mAdapter);

            getDZBBList();
        }

        return view;
    }

    /**
     * 获取电子壁报数据
     */
    private void getDZBBList() {
        CHYHttpClientUsage.getInstanse().doGetWallPoster(AppApplication.conId, currentPage++, mSearchString, orderBy, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.println("response:" + response);
                String str = response.toString();
                dzbbBeans = JsonParser.parseDzbb(str);

                if (dzbbBeans != null && dzbbBeans.size() > 0) {
                    allBeans.addAll(dzbbBeans);
                    mHandler.sendEmptyMessage(MSG_DONE);
                } else {
                    mHandler.sendEmptyMessage(MSG_ERROR);
                }

                if (dzbbBeans != null) {
                    if (dzbbBeans.size() == 0 && allBeans.size() == 0)
                        mHandler.sendEmptyMessage(MSG_NO_DATA);
                    if (allBeans.size() > 0 && dzbbBeans.size() == 0)
                        mHandler.sendEmptyMessage(MSG_TOAST_NO_MORE_DATA);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.println("statusCode:" + statusCode + ",responseString:" + responseString);
            }
        });
    }

    private void clearChoose() {
        mIvCode.setImageResource(R.drawable.speaker_role_not_choose);
        mIvAuthor.setImageResource(R.drawable.speaker_role_not_choose);
    }

    public void setRightView(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), HomeActivity.REQUEST_SCANE);
            }
        });
    }


}
