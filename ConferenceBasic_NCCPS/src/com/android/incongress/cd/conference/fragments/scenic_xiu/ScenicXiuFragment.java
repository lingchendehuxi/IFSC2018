package com.android.incongress.cd.conference.fragments.scenic_xiu;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.ScenicXiuAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommentArrayBean;
import com.android.incongress.cd.conference.beans.SceneShowTopBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.fragments.DynamicHomeFragment;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/13.
 * <p/>
 * 现场秀模块
 */
public class ScenicXiuFragment extends BaseFragment implements View.OnClickListener, ScenicXiuAdapter.NewsAndActivitysListener {
    private XRecyclerView mRecyclerView;
    private ScenicXiuAdapter mAdapter;

    private View mScenicXiuTitle;

    private RelativeLayout mRlCommentArea;

    private ProgressDialog mProgressDialog;
    private String mLastDataId = "-1";

    public static final String BROAD_SCENIC_XIU_ID = "sceneXiuId";
    public static final String BROAD_POSITION = "position";
    public static final String BROAD_COMMENT_ID = "commentId";
    public static final String BROAD_PARENT_NAME = "parentName";
    public static final String BROAD_PARENT_ID = "parentId";

    private CommentClickReceiver mCommentReceiver;
    private Button mBtSend;

    private EditText mEtComment;

    private LinkedList<ScenicXiuBean> mDatas = new LinkedList<>();

    private SceneShowTopBean mTopBean = new SceneShowTopBean();
    private ImageView mIvFirst,mIvSecond;
    private ImageView mIvMakepost;

    private boolean first = true;
    public ScenicXiuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),false);
        View view = inflater.inflate(R.layout.fragment_scenic_xiu, null);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);

        mRlCommentArea = (RelativeLayout) view.findViewById(R.id.rl_comment_area);
        mEtComment = (EditText) view.findViewById(R.id.et_make_comment);
        mBtSend = (Button) view.findViewById(R.id.bt_send_comment);
        mIvMakepost = (ImageView) view.findViewById(R.id.iv_make_post);

        mIvMakepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!AppApplication.isUserLogIn()) {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "" , "");
                    return;
                }

                MakePostActionFragment fragment = new MakePostActionFragment();
                View postView = LayoutInflater.from(getActivity()).inflate(R.layout.include_title_make_post, null);
                fragment.setRightView(postView);
                action(fragment, R.string.create_post, postView, false, false, false);
            }
        });
        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtComment.getText().toString().trim();
                try {
                    content = URLEncoder.encode(content, Constants.ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(content)) {
                    ToastUtils.showShorToast(getString(R.string.comment_no_empty));
                } else {
                    CHYHttpClientUsage.getInstanse().doSceneShowComment(mScenicXiuId + "", AppApplication.userId + "", AppApplication.userType + "", content, mCommentId + "", new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            mProgressDialog = ProgressDialog.show(getActivity(), null, "loading...");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            MyLogger.jLog().i(response.toString());
                            try {
                                int state = response.getInt("state");
                                if (state == 1) {
                                    Gson gson = new Gson();
                                    List<CommentArrayBean> comments = gson.fromJson(response.getString("commentArray"), new TypeToken<List<CommentArrayBean>>() {}.getType());
                                    int commentCount = response.getInt("commentCount");
                                    for (int i = 0; i < comments.size(); i++) {
                                        CommentArrayBean bean = comments.get(i);
                                        String name = URLDecoder.decode(bean.getUserName(), Constants.ENCODING_UTF8);
                                        bean.setUserName(URLDecoder.decode(name, Constants.ENCODING_UTF8));
                                        if (bean.getParentId() != -1) {
                                            String parentName = URLDecoder.decode(bean.getParentName(), Constants.ENCODING_UTF8);
                                            bean.setParentName(URLDecoder.decode(parentName, Constants.ENCODING_UTF8));
                                        }
                                    }

                                    if (comments != null && comments.size() > 0) {
                                        mDatas.get(mPosition).setCommentArray(comments);
                                        mDatas.get(mPosition).setCommentCount(commentCount);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            if (mProgressDialog != null && mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                                mRlCommentArea.setVisibility(View.GONE);
                                toggleShurufa();
                                mEtComment.setHint(StringUtils.EMPTY_STR);
                                mEtComment.setText(StringUtils.EMPTY_STR);
                        }
                    });
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mAdapter = new ScenicXiuAdapter(mDatas, this, getActivity());

        View header = inflater.inflate(R.layout.scenic_top, container , false);
        mIvFirst = (ImageView) header.findViewById(R.id.iv_news_notification);
        mIvSecond = (ImageView) header.findViewById(R.id.iv_exhibitors_activity);
        mIvFirst.setOnClickListener(this);
        mIvSecond.setOnClickListener(this);

        //mRecyclerView.addHeaderView(header);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mLastDataId = "-1";
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                getDownData(mLastDataId);
            }

            @Override
            public void onLoadMore() {
                mLastDataId = mDatas.get(mDatas.size() - 1).getSceneShowId() + "";
                getDownData(mLastDataId);
            }
        });

        mRecyclerView.setRefreshing(true);
        mRlCommentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRlCommentArea.setVisibility(View.GONE);
                mEtComment.setHint(R.string.schedule_comment_sth);
                ((HomeActivity) getActivity()).toggleShurufa();
            }
        });

        /**滑动隐藏发帖按钮 停止滑动显示按钮
         * 开始*/
        final int[] mScrollThreshold = new int[1];
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mScrollThreshold[0] = newState;
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mIvMakepost.setVisibility(View.VISIBLE);
                    Animation animation= AnimationUtils.loadAnimation(getActivity(), R.anim.addpost);
                    mIvMakepost.startAnimation(animation);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold[0];
                if (isSignificantDelta) {
                    mIvMakepost.setVisibility(View.GONE);
                }
            }
        }); /*结束*/

        //注册广播接收器
        registerMessageReceiver();

        //更新查看时间
        CHYHttpClientUsage.getInstanse().doCreateUserLooked(AppApplication.userId + "", AppApplication.userType + "", AppApplication.TOKEN_IMEI, Constants.LookTimeScenicXiu, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                sendMessageStationBroadcast();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

//      showGuideInfo();
        return view;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mIvMakepost.setVisibility(View.VISIBLE);
                    first = false;
                    startHeartBeat();
                    break;
            }
        }
    };
    private class HeatbeatThread extends Thread {
        int anInt = 0;
        boolean go = true;
        public void run() {
            while (go) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (anInt < 5) {
                            playHeartbeatAnimation();
                            anInt++;
                        }else{
                            go = false;
                            heartbeatThread.interrupt();
                        }
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private Thread heartbeatThread;
    /**
     * 开始心跳
     */
    private void startHeartBeat() {
        if (heartbeatThread == null) {
            heartbeatThread = new HeatbeatThread();
        }
        if (!heartbeatThread.isAlive()) {
            heartbeatThread.start();
        }
    }
    private void playHeartbeatAnimation() {
        AnimationSet swellAnimationSet = new AnimationSet(true);
        swellAnimationSet.addAnimation(new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f));
        swellAnimationSet.addAnimation(new AlphaAnimation(1.0f, 0.3f));
        swellAnimationSet.setDuration(500);
        swellAnimationSet.setInterpolator(new AccelerateInterpolator());
        swellAnimationSet.setFillAfter(true);
        swellAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet shrinkAnimationSet = new AnimationSet(true);
                shrinkAnimationSet.addAnimation(new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                shrinkAnimationSet.addAnimation(new AlphaAnimation(0.3f, 1.0f));
                shrinkAnimationSet.setDuration(1000);
                shrinkAnimationSet.setInterpolator(new DecelerateInterpolator());
                shrinkAnimationSet.setFillAfter(false);
                mIvMakepost.startAnimation(shrinkAnimationSet);// 动画结束时重新开始，实现心跳的View
            }
        });
        mIvMakepost.startAnimation(swellAnimationSet);
    }

    /**
     * 通知更新首页信息
     */
    private void sendMessageStationBroadcast() {
        Intent intent = new Intent();
        intent.setAction(DynamicHomeFragment.INTENT_MESSAGE_STATION);
        getActivity().sendBroadcast(intent);
    }


    /**
     * 显示指示页
     */
    private void showGuideInfo() {
        if (!"1".equals(SharePreferenceUtils.getAppString(Constants.GUIDE_XIU))) {
            getActivity().findViewById(R.id.home_guide).setVisibility(View.VISIBLE);
            ((ImageView) getActivity().findViewById(R.id.home_guide)).setImageResource(R.drawable.show_guide);
            ((ImageView) getActivity().findViewById(R.id.home_guide)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().findViewById(R.id.home_guide).setVisibility(View.GONE);
                    SharePreferenceUtils.saveAppString(Constants.GUIDE_XIU, "1");
                }
            });
        }
    }

    //设置标题栏的点击事件
    public void setScenicXiuTitle(View view) {
        mScenicXiuTitle = view;

        ImageView askQuestion = (ImageView) mScenicXiuTitle.findViewById(R.id.iv_ask_professor);
        ImageView makePost = (ImageView) mScenicXiuTitle.findViewById(R.id.iv_make_post);

        askQuestion.setVisibility(View.GONE);
//        askQuestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AppApplication.instance().getmUserType() == Constants.TYPE_USER_VISITOR) {
////                  LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "" , "");
//                    ChooseIdentityActivity.startChooseIdentityActivity(getActivity());
//                    return;
//                }
//
//                SpeakerSearchActionFragment speaker = SpeakerSearchActionFragment.getInstance(SpeakerSearchActionFragment.TYPE_FROM_SCENIC_XIU);
//                action(speaker, getString(R.string.scenic_xiu_speaker_list), false, false, false);
//            }
//        });

        makePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = AppApplication.isUserLogIn();

                if(!isLogin) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, PersonCenterFragment.REQUEST_LOGIN);
                    return;
                }

                MakePostActionFragment fragment = new MakePostActionFragment();
                View postView = LayoutInflater.from(getActivity()).inflate(R.layout.include_title_make_post, null);
                fragment.setRightView(postView);
                action(fragment, R.string.create_post, postView, false, false, false);
            }
        });
    }

    /**
     * 获取下方现场秀列表
     *
     * @param lastSceneShowId
     */
    private void getDownData(final String lastSceneShowId) {
        CHYHttpClientUsage.getInstanse().doGetSceneShowDown(Constants.conId + "", lastSceneShowId, AppApplication.userId + "", AppApplication.userType + "", new JsonHttpResponseHandler("gbk") {
            @Override
            public void onFinish() {
                super.onFinish();
                if(mRecyclerView != null) {
                    mRecyclerView.refreshComplete();
                    mRecyclerView.loadMoreComplete();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int state = response.getInt("state");
                    if (state == 1) {
                        if (lastSceneShowId.equals("-1")) {
                            Gson gson = new Gson();
                            String jsonArray = "";

                            try {
                                jsonArray = response.getJSONArray("sceneShowArray").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mDatas.addAll((Collection<? extends ScenicXiuBean>) gson.fromJson(jsonArray, new TypeToken<LinkedList<ScenicXiuBean>>() { }.getType()));
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.refreshComplete();
                            if(first){
                                handler.sendEmptyMessage(1);
                            }
                        } else {
                            Gson gson = new Gson();
                            String jsonArray = "";
                            try {
                                jsonArray = response.getJSONArray("sceneShowArray").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mDatas.addAll((LinkedList<ScenicXiuBean>) gson.fromJson(jsonArray, new TypeToken<LinkedList<ScenicXiuBean>>() {
                            }.getType()));
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.loadMoreComplete();
                        }

                        int pageState = response.getInt("pageState");
                        if(pageState == 0) {
//                            mRecyclerView.setNoMore(true);
                            mRecyclerView.loadMoreComplete();
                        }

                        mTopBean.setImgUrl1(response.getString("imgUrl1"));
                        mTopBean.setImgUrl2(response.getString("imgUrl2"));
                        mTopBean.setGotoUrl1(response.getString("gotoUrl1"));
                        mTopBean.setGotoUrl2(response.getString("gotoUrl2"));

                        String urlFirst = mTopBean.getImgUrl1();
                        String urlSecond = mTopBean.getImgUrl2();
                        if(urlFirst.contains("https:"))
                            urlFirst = urlFirst.replaceFirst("s","");

                        if(urlSecond.contains("https:"))
                            urlSecond = urlSecond.replaceFirst("s","");

                        PicUtils.loadImageUrl(getContext(),urlFirst,mIvFirst);
                        PicUtils.loadImageUrl(getContext(),urlSecond,mIvSecond);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final String COMMENT_CLICK_RECEIVED_ACTION_NORMAL = "click_action_normal";
    public static final String COMMENT_CLICK_RECEIVED_ACTION_COMMENT = "click_action_comment";
    public static final String GO_TO_LOGIN_FIRST = "go_login";
    private int mScenicXiuId = 0;
    private int mPosition = 0;
    private int mCommentId = 0;
    private String mParentName = "";
    private String mUserId = "";

    @Override
    public void onClick(View v) {
        int targetId = v.getId();
        if (targetId == R.id.iv_news_notification) {
            CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.media_center),mTopBean.getGotoUrl1(),1);
        } else if (targetId == R.id.iv_exhibitors_activity) {
            CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.industrial_events),mTopBean.getGotoUrl2(),1);

        }
    }

    @Override
    public void doWhenNewsOrActivityClicked(int type, String url, String title) {
        CollegeActivity.startCitCollegeActivity(getActivity(), title,url,1);
    }

    //评论广播接收器
    class CommentClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mScenicXiuId = 0;
            mPosition = 0;
            mCommentId = 0;
            mParentName = "";
            mUserId = "";

            if (GO_TO_LOGIN_FIRST.equals(intent.getAction())) {
                LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL,"","", "" , "");
//                ChooseIdentityActivity.startChooseIdentityActivity(getActivity());
                return;
            }
            mScenicXiuId = intent.getIntExtra(BROAD_SCENIC_XIU_ID, 0);
            mPosition = intent.getIntExtra(BROAD_POSITION, 0);
            mCommentId = intent.getIntExtra(BROAD_COMMENT_ID, -1);
            mParentName = intent.getStringExtra(BROAD_PARENT_NAME);
            mUserId = intent.getStringExtra(BROAD_PARENT_ID);

            if ((AppApplication.userId + "").equals(mUserId)) {
                return;
            }

            mRlCommentArea.setVisibility(View.VISIBLE);
            toggleShurufa();
            mEtComment.requestFocus();

            if (COMMENT_CLICK_RECEIVED_ACTION_NORMAL.equals(intent.getAction())) {

            } else if (COMMENT_CLICK_RECEIVED_ACTION_COMMENT.equals(intent.getAction())) {
                mEtComment.setHint("@" + mParentName);
            }
        }
    }

    //注册评论广播接收器
    public void registerMessageReceiver() {
        mCommentReceiver = new CommentClickReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(COMMENT_CLICK_RECEIVED_ACTION_COMMENT);
        filter.addAction(COMMENT_CLICK_RECEIVED_ACTION_NORMAL);
        filter.addAction(GO_TO_LOGIN_FIRST);
        getActivity().registerReceiver(mCommentReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCommentReceiver != null) {
            getActivity().unregisterReceiver(mCommentReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SCENICXIU);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SCENICXIU);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(),false);
        }
    }
}
