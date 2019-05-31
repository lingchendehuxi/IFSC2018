package com.android.incongress.cd.conference.fragments.scenic_xiu;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.adapters.ScenicXiuAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.SceneShowTopBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.fragments.DynamicHomeFragment;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.ui.login.view.LoginActivity;
import com.android.incongress.cd.conference.utils.CacheManager;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.blws.PolyvErrorMessageUtils;
import com.android.incongress.cd.conference.widget.blws.PolyvPlayerMediaController;
import com.android.incongress.cd.conference.widget.blws.PolyvPlayerPreviewView;
import com.android.incongress.cd.conference.widget.blws.PolyvScreenUtils;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/13.
 * <p/>
 * 现场秀模块
 */
public class ScenicXiuFragment extends BaseFragment implements View.OnClickListener, ScenicXiuAdapter.NewsAndActivitysListener, ScenicXiuAdapter.praiseCommentListener,XRecyclerView.LoadingListener {
    private XRecyclerView mRecyclerView;
    private ScenicXiuAdapter mAdapter;
    private View mScenicXiuTitle;
    private String mLastId;
    //是否有更多数据： 1：代表还有 0：代表已经没有了
    private String mIsMore;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    public static final String BROAD_SCENIC_XIU_ID = "sceneXiuId";
    public static final String BROAD_POSITION = "position";
    public static final String BROAD_COMMENT_ID = "commentId";
    public static final String BROAD_PARENT_NAME = "parentName";
    public static final String BROAD_PARENT_ID = "parentId";
    private static final String CACHE_PATH = "scenic_xiu_fragment";
    private static final String CACHE_PATH_List = "scenic_xiu_fragment_list";

    private CommentClickReceiver mCommentReceiver;
    private ArrayList<ScenicXiuBean> mDatas = new ArrayList<>();

    private SceneShowTopBean mTopBean = new SceneShowTopBean();
    private ImageView mIvFirst, mIvSecond;
    private ImageView mIvMakepost, mTitleBack;
    private TextView mTitleText;

    private boolean first = true;
    //视频播放器集合
    private List<PolyvVideoView> listVideo;
    //缓存
    private CacheManager cacheManager;

    public ScenicXiuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.fragment_scenic_xiu, null);
        mTitleBack = view.findViewById(R.id.title_back);
        mTitleBack.setVisibility(View.GONE);
        mTitleText = view.findViewById(R.id.title_text);
        mTitleText.setText(R.string.bottom_broadcast);

        mRecyclerView = view.findViewById(R.id.recyclerview);

        mIvMakepost = (ImageView) view.findViewById(R.id.iv_make_post);
        listVideo = new ArrayList<>();
        cacheManager = CacheManager.getInstance().open(CACHE_PATH_List, 1);

        mIvMakepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppApplication.isUserLogIn()) {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
                    return;
                }

                MakePostActionFragment fragment = new MakePostActionFragment();
                View postView = LayoutInflater.from(getActivity()).inflate(R.layout.include_title_make_post, null);
                fragment.setRightView(postView);
                action(fragment, R.string.create_post, postView, false, false, false);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        View header = inflater.inflate(R.layout.scenic_top, container, false);
        mIvFirst = (ImageView) header.findViewById(R.id.iv_news_notification);
        mIvSecond = (ImageView) header.findViewById(R.id.iv_exhibitors_activity);
        mIvFirst.setOnClickListener(this);
        mIvSecond.setOnClickListener(this);

        //配置加载缓存数据
        mAdapter = new ScenicXiuAdapter(mDatas, this, this, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(this);
        loadLocalDate();

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
        //配置滑动播放视频
        mlayoutManager = mRecyclerView.getLayoutManager();
        handleVideo(mRecyclerView);
        return view;
    }

    //滑动视频处理
    public RecyclerView.LayoutManager mlayoutManager;
    private int firstVisibleItem, lastVisibleItem, visibleCount;

    private void handleVideo(XRecyclerView xRecyclerView) {
        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                switch (scrollState) {
                    //停止的时候播放视频
                    case 0:
                        if(NetWorkUtils.NETWORK_TYPE_WIFI.equals(NetWorkUtils.getNetworkTypeName(getActivity()))){
                            autoPlayVideo();
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mlayoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mlayoutManager;
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    visibleCount = lastVisibleItem - firstVisibleItem;
                }
            }
        });

    }

    //视频播放处理
    private void autoPlayVideo() {
        for (int i = 0; i < visibleCount; i++) {
            if (mDatas.size() != 0 && firstVisibleItem + i < mDatas.size() - 1 && mDatas.get(firstVisibleItem + 1) != null && mDatas.get(firstVisibleItem + i).getType() == 3) {
                View mView = mlayoutManager.findViewByPosition(firstVisibleItem + i + 1);

                if (mView != null && mView.findViewById(R.id.polyv_video_view) != null) {
                    final PolyvVideoView videoView = mView.findViewById(R.id.polyv_video_view);
                    final RelativeLayout viewLayout = mView.findViewById(R.id.view_layout);
                    final PolyvPlayerMediaController mediaController = mView.findViewById(R.id.polyv_player_media_controller);
                    final ProgressBar loading_progress = mView.findViewById(R.id.loading_progress);
                    final PolyvPlayerPreviewView firstStartView = mView.findViewById(R.id.polyv_player_first_start_view);
                    final LinearLayout videoErrorLayout = mView.findViewById(R.id.video_error_layout);
                    final TextView videoErrorContent = mView.findViewById(R.id.video_error_content);
                    final TextView videoErrorRetry = mView.findViewById(R.id.video_error_retry);
                    mediaController.setFullScreen(false);
                    videoView.setMediaController(mediaController);
                    videoView.setOpenPreload(true, 2);
                    videoView.setAutoContinue(true);
                    listVideo.add(videoView);

                    Rect rect = new Rect();
                    videoView.getLocalVisibleRect(rect);
                    int videoheight3 = videoView.getHeight();
                    Log.e("videoTest", "i=" + i + "===" + "videoheight3:" + videoheight3 + "===" + "rect.top:" + rect.top + "===" + "rect.bottom:" + rect.bottom);
                    //以下是播放异常反馈
                    videoView.setOnErrorListener(new IPolyvOnErrorListener2() {
                        @Override
                        public boolean onError() {
                            String message = "当前视频无法播放，请尝试切换网络重新播放或者向管理员反馈(error code " + PolyvPlayErrorReason.VIDEO_ERROR + ")";
                            showErrorView(videoErrorLayout, videoErrorContent, message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            videoView.setOnVideoPlayErrorListener(new IPolyvOnVideoPlayErrorListener2() {
                                @Override
                                public boolean onVideoPlayError(@PolyvPlayErrorReason.PlayErrorReason int playErrorReason) {
                                    String message = PolyvErrorMessageUtils.getPlayErrorMessage(playErrorReason);
                                    message += "(error code " + playErrorReason + ")";
                                    showErrorView(videoErrorLayout, videoErrorContent, message);
                                    return true;
                                }
                            });
                            videoView.setOnVideoStatusListener(new IPolyvOnVideoStatusListener() {
                                @Override
                                public void onStatus(int status) {
                                    if (status < 60) {
                                        Toast.makeText(getActivity(), "状态错误 " + status, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            return true;
                        }
                    });
                    videoErrorRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //初始化视频工具
                            mediaController.initConfig(viewLayout);
                            videoView.setPlayerBufferingIndicator(loading_progress);
                            PolyvScreenUtils.generateHeight16_9(getActivity());
                            pauseListPlay();
                            play(videoView, mediaController, loading_progress, firstStartView, "23d81808464ddb6d0422ed37cc89aa5a_2", 0, true, false);
                        }
                    });
                    if (videoView.isPlaying()) {
                        return;
                    }
                    if (rect.top == 0 && rect.bottom == videoheight3) {
                        mediaController.initConfig(viewLayout);
                        videoView.setPlayerBufferingIndicator(loading_progress);
                        PolyvScreenUtils.generateHeight16_9(getActivity());
                        pauseListPlay();
                        play(videoView, mediaController, loading_progress, firstStartView, "23d81808464ddb6d0422ed37cc89aa5a_2", 0, true, false);
                        return;
                    }

                }
            }

        }
        Log.e("videoTest", "======================releaseAllVideos=====================");
        pauseListPlay();
    }

    /**
     * 播放视频
     *
     * @param vid             视频id
     * @param bitrate         码率（清晰度）
     * @param startNow        是否现在开始播放视频
     * @param isMustFromLocal 是否必须从本地（本地缓存的视频）播放
     */
    public void play(final PolyvVideoView videoView, PolyvPlayerMediaController mediaController, ProgressBar loadingProgress, PolyvPlayerPreviewView firstStartView, final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        if (TextUtils.isEmpty(vid)) return;

        videoView.release();
        mediaController.hide();
        loadingProgress.setVisibility(View.GONE);
        firstStartView.hide();

        if (startNow) {
            //调用setVid方法视频会自动播放
            videoView.setVid(vid, bitrate, isMustFromLocal);
        } else {
            //视频不播放，先显示一张缩略图
            firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {

                @Override
                public void onClickStart() {
                    /**
                     * 调用setVid方法视频会自动播放
                     * 如果是有学员登陆的播放，可以在登陆的时候通过
                     * {@link com.easefun.polyvsdk.PolyvSDKClient.getinstance().setViewerId()}设置学员id
                     * 或者调用{@link videoView.setVidWithStudentId}传入学员id进行播放
                     */

                    videoView.setVidWithStudentId(vid, bitrate, isMustFromLocal, "123");
                }
            });

            firstStartView.show(vid);
        }
    }

    //暂停所有的播放器
    private void pauseListPlay() {
        for (int i = 0; i < listVideo.size(); i++) {
            if (listVideo.get(i) != null && listVideo.get(i).isPlaying()) {
                listVideo.get(i).pause();
            }
        }
    }

    //显示视频播放错误信息
    private void showErrorView(LinearLayout videoErrorLayout, TextView videoErrorContent, String message) {
        videoErrorLayout.setVisibility(View.VISIBLE);
        videoErrorContent.setText(message);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    mIvMakepost.setVisibility(View.VISIBLE);
                    first = false;
                    startHeartBeat();
                    break;
            }
            return false;
        }
    }) ;

    /**
     * 开始心跳
     */
    private void startHeartBeat() {
        Animator anim = AnimatorInflater.loadAnimator(getActivity(),R.animator.head_shake);
        anim.setTarget(mIvMakepost);
        anim.start();
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

                if (!isLogin) {
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
     * @param lastId
     */
    private void getDownData(final String lastId) {
        CHYHttpClientUsage.getInstanse().doGetSceneShowDown(Constants.getConId() + "", lastId, AppApplication.userId + "", AppApplication.userType + "", new JsonHttpResponseHandler("gbk") {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String jsonArray = JSONCatch.parseJsonarray("sceneShowArray", response).toString();
                cacheManager.saveString(CACHE_PATH_List, jsonArray);
                ArrayList<ScenicXiuBean> tempList = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<ScenicXiuBean>>() {
                }.getType());
                if (tempList.size() != 0) {
                    //刷新进来
                    if ("-1".equals(lastId)) {
                        mDatas.clear();
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                        //加载更多进来
                    } else {
                        mRecyclerView.loadMoreComplete();
                        if ("0".equals(mIsMore)) {
                            ToastUtils.showToast(getString(R.string.incongress_send_no_more_data));
                            mRecyclerView.setLoadingMoreEnabled(false);
                            return;
                        }
                    }
                    mDatas.addAll(tempList);
                    mAdapter.notifyDataSetChanged();
                    mLastId = String.valueOf(tempList.get(tempList.size() - 1).getSceneShowId());
                    mIsMore = JSONCatch.parseString("pageState",response);
                } else {
                    if ("0".equals(JSONCatch.parseString("pageState",response)) && mDatas.size() != 0) {
                        mRecyclerView.loadMoreComplete();
                        ToastUtils.showToast(getString(R.string.incongress_send_no_more_data));
                        mRecyclerView.setLoadingMoreEnabled(false);
                        return;
                    }
                    mRecyclerView.refreshComplete();
                    mRecyclerView.loadMoreComplete();
                    return;
                }
                if (first) {
                    handler.sendEmptyMessage(1);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mRecyclerView.reset();
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }
    @Override
    public void onRefresh() {
        if(!NetWorkUtils.isNetworkConnected(getActivity())){
            mRecyclerView.refreshComplete();
            ToastUtils.showToast(getString(R.string.connect_network));
            return;
        }
        getDownData("-1");
    }

    @Override
    public void onLoadMore() {
        if(!NetWorkUtils.isNetworkConnected(getActivity())){
            mRecyclerView.loadMoreComplete();
            ToastUtils.showToast(getString(R.string.connect_network));
            return;
        }
        getDownData(mDatas.get(mDatas.size() - 1).getSceneShowId() + "");
    }

    //无网络的时候加载本地数据
    private void loadLocalDate() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            String json = cacheManager.getString(CACHE_PATH_List);
            if (!TextUtils.isEmpty(json)) {
                Gson gson = new Gson();
                mDatas.clear();
                mDatas = gson.fromJson(json, new TypeToken<ArrayList<ScenicXiuBean>>() {
                }.getType());
                mAdapter = new ScenicXiuAdapter(mDatas, this, this, getActivity());
                mRecyclerView.setAdapter(mAdapter);
                handler.sendEmptyMessage(1);
            }
        }else {
            mRecyclerView.setRefreshing(true);
        }
    }

    public static final String COMMENT_CLICK_RECEIVED_ACTION_NORMAL = "click_action_normal";
    public static final String COMMENT_CLICK_RECEIVED_ACTION_COMMENT = "click_action_comment";
    public static final String GO_TO_LOGIN_FIRST = "go_login";

    @Override
    public void onClick(View v) {
        int targetId = v.getId();
        if (targetId == R.id.iv_news_notification) {
            CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.media_center), mTopBean.getGotoUrl1(), 1);
        } else if (targetId == R.id.iv_exhibitors_activity) {
            CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.industrial_events), mTopBean.getGotoUrl2(), 1);

        }
    }

    @Override
    public void doWhenNewsOrActivityClicked(int type, String url, String title) {
        CollegeActivity.startCitCollegeActivity(getActivity(), title, url, 1);
    }

    @Override
    public void doWhenCommentClicked(ScenicXiuBean bean) {
        ScenicXiuCommentActivity.scenicXiuCommentActivity(getActivity(), bean);
    }

    //评论广播接收器
    class CommentClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_COMMENT_UPDATE.equals(intent.getAction())) {
                mRecyclerView.setRefreshing(true);
            }
        }

    }

    //注册评论广播接收器
    public void registerMessageReceiver() {
        mCommentReceiver = new CommentClickReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_COMMENT_UPDATE);
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
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        MobclickAgent.onPageStart(Constants.FRAGMENT_SCENICXIU);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SCENICXIU);
        pauseListPlay();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        } else {
            pauseListPlay();
        }
    }
}
