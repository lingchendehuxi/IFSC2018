package com.android.incongress.cd.conference.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.fragments.cit_live.CitLiveFragment;
import com.android.incongress.cd.conference.fragments.exhibitor.ExhibitorsActionFragment;
import com.android.incongress.cd.conference.fragments.interactive.HdSessionActionFragment;
import com.android.incongress.cd.conference.fragments.meeting_guide.MeetingGuideFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListActionFragment;
import com.android.incongress.cd.conference.fragments.message_station.MessageStationActionFragment;
import com.android.incongress.cd.conference.fragments.my_schedule.MyScheduleActionFragment;
import com.android.incongress.cd.conference.fragments.now_next.NextFragment;
import com.android.incongress.cd.conference.fragments.now_next.NowFragment;
import com.android.incongress.cd.conference.fragments.professor_secretary.SecretaryActivity;
import com.android.incongress.cd.conference.fragments.question.QuestionsFragment;
import com.android.incongress.cd.conference.fragments.scenic_xiu.ScenicXiuFragment;
import com.android.incongress.cd.conference.fragments.search_schedule.NewSearchScheduleActionFragment;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerSearchFragment;
import com.android.incongress.cd.conference.fragments.wall_poster.PosterFragment;
import com.android.incongress.cd.conference.model.Ad;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.widget.zxing.activity.CaptureActivity;
import com.android.incongress.cd.conference.utils.ArrayUtils;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jacky_Chen
 * @time 2014年11月24日  将home界面中的margin_left和margin_right的距离32去除，界面更好看些。
 * @Time 2014-12-1 Jacky_Chen 添加精彩回顾
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = HomeFragment.class.getSimpleName();
    public static final String INTENT_MESSAGE_STATION = "com.incongress.messasge";

    private static final int HANDLER_NUMS = 0x0001;

    //现场秀消息数，消息站消息数
    private TextView mTvMsgNums;
    private int mMessageCount, mXchdCount;
//    private ImageView mIvHdSessionOnTips;

    //专家秘书模块需要
    private List<ActivityBean> mAllActivitys = new ArrayList<>();
    private List<ActivityBean> mValidActivitys = new ArrayList<>();
    private List<SceneShowArrayBean> mScenceShowBeans = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private int mTaskNum, mQuestionNum;
    private ImageView mIvADTop;

    //广告轮播
    protected List<Ad> mAdList;
    private AdReceiver mAdReceiver;

    private class AdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onAdChanging();
        }
    }

    /**
     * 广告跟换
     */
    private void onAdChanging() {
        if (AppApplication.topNum < 0 || AppApplication.bottomNum < 0) {
            return;
        }

        int top = AppApplication.topNum;
        int bottom = AppApplication.bottomNum;

        String filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR;
        String pathTop = "";
        if (mAdList != null && mAdList.size() > top) {
            pathTop = filespath + mAdList.get(top).getAdImage();
        }

        String pathBottom = "";
        if (mAdList != null && mAdList.size() > bottom) {
            pathBottom = filespath + mAdList.get(bottom).getAdImage();
        }

        File fileTop = new File(pathTop);
        if (fileTop.exists()) {
            PicUtils.loadImageFile(getContext(), new File(fileTop.getAbsolutePath()), mIvADTop);
        }
    }


    private MessageStationBroadCast mMsgBroadcast;

    @Override
    public void onResume() {
        super.onResume();
        getHomeNums();
        MobclickAgent.onResume(getActivity());

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int target = message.what;
            if (target == HANDLER_NUMS) {
//                if (mXchdCount > 0) {
//                    mIvHdSessionOnTips.setVisibility(View.VISIBLE);
//                } else {
//                    mIvHdSessionOnTips.setVisibility(View.GONE);
//                }

                if (mMessageCount > 0) {
                    mTvMsgNums.setVisibility(View.VISIBLE);
                    if (mMessageCount < 99) {
                        mTvMsgNums.setText(mMessageCount + "");
                    } else {
                        mTvMsgNums.setText(99 + "");
                    }
                }
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //上方广告控制
            case R.id.iv_top:
                int top = AppApplication.topNum;
                if (top >= 0) {
                    Ad bean = AppApplication.adList.get(top);
                    String link = bean.getAdLink().trim();
                    if (link != null && !link.equals("")) {
                        WebViewContainerActivity.startWebViewContainerActivity(getActivity(), link, "");
                    }
                }
                break;
            case R.id.ll_watch_schedule:
                goLookSchedule();
//                goNow();
                break;
            case R.id.ll_search_schedule:
                goSearchSchedule();
                break;
            case R.id.ll_my_schedule:
                goMySchedule();
                break;
//            case R.id.rl_qanda:
//                goQuestionAndA();
//                break;
            case R.id.ll_exhibitor:
                goGuide();
                goExhibitor();
                break;
            case R.id.ll_meeting_guide:
                break;
            case R.id.ll_cit_college:
                goCollege();
                break;
            case R.id.ll_message_station:
                goMessageStation();
                break;
//            case R.id.ll_speaker_search:
//                goSpeakerSearch();
//                break;
            case R.id.ll_question:
                goMakeQuestion();
                break;
            case R.id.ll_scane:
                goScane();
                break;
//            case R.id.iv_top_cit:
//                goLive();
//                break;
            case R.id.ll_bb:
                goPost();
                break;
//            case R.id.ll_now:
//                goNow();
//                break;
//            case R.id.ll_next:
//                goNext();
//                break;
//            case R.id.ll_hands_on:
//                goHandsOn();
//                break;
            case R.id.ll_reg_online:
                goRegisterOnline();
                break;
            case R.id.ll_secretary_trip:
                goSecretaryTrip();
                break;
        }
    }


    /**
     * 专家行程
     */
    private void goSecretaryTrip() {
        if (AppApplication.facultyId != -1) {
            MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_FACULTY_TRIP);
            WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                    getString(R.string.secretary_trip, AppApplication.getSystemLanuageCode()), getString(R.string.home_icon_professor_trip));
        } else {
            ToastUtils.showToast(getString(R.string.only_professor_can_use));
        }
    }

    /**
     * 在线注册
     */
    private void goRegisterOnline() {
        if (AppApplication.systemLanguage == 1) {
            WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                    getString(R.string.reg_online_cn), getString(R.string.home_icon_reg_online));
        } else {
            WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                    getString(R.string.reg_online_en), getString(R.string.home_icon_reg_online));
        }
    }


    private void goMakeQuestion() {
        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_QUESTION);
        action(new QuestionsFragment(), R.string.question, false, false, false);
    }

    private void goNow() {
        action(new NowFragment(), R.string.home_now, false, false, false);
    }

    private void goNext() {
        action(new NextFragment(), R.string.home_next, false, false, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_cit2017, container, false);

        mAdList = AppApplication.adList;

        initViews(view);
        initEvents(view);

        registerMeessageBroadcast();
        registerAdBroadcast();


        return view;
    }

    private void registerAdBroadcast() {
        mAdReceiver = new AdReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CHANGE_AD);

        getActivity().registerReceiver(mAdReceiver, filter);
    }


    private void registerMeessageBroadcast() {
        mMsgBroadcast = new MessageStationBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_MESSAGE_STATION);   //为BroadcastReceiver指定action，使之用于接收同action的广播

        getActivity().registerReceiver(mMsgBroadcast, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mMsgBroadcast);
    }

    /**
     * 获取数据
     */
    private void getHomeNums() {
        CHYHttpClientUsage.getInstanse().doGetLookCount(Constants.getConId() + "", AppApplication.userId + "", AppApplication.userType + "", AppApplication.TOKEN_IMEI, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    mMessageCount = response.getInt("tokenMessageCount");
                    mXchdCount = response.getInt("xchdCount");
                } catch (Exception e) {
                    e.printStackTrace();
                    mMessageCount = 0;
                    mXchdCount = 0;
                }
                mHandler.sendEmptyMessage(HANDLER_NUMS);
            }
        });
    }

    private void initEvents(View view) {

        view.findViewById(R.id.ll_watch_schedule).setOnClickListener(this);
        view.findViewById(R.id.ll_search_schedule).setOnClickListener(this);
        view.findViewById(R.id.ll_my_schedule).setOnClickListener(this);

        view.findViewById(R.id.ll_reg_online).setOnClickListener(this);
        view.findViewById(R.id.ll_meeting_guide).setOnClickListener(this);
        view.findViewById(R.id.ll_secretary_trip).setOnClickListener(this);

        view.findViewById(R.id.ll_question).setOnClickListener(this);
        view.findViewById(R.id.ll_bb).setOnClickListener(this);

        view.findViewById(R.id.ll_cit_college).setOnClickListener(this);
        view.findViewById(R.id.ll_exhibitor).setOnClickListener(this);
        view.findViewById(R.id.ll_message_station).setOnClickListener(this);
        view.findViewById(R.id.ll_scane).setOnClickListener(this);

//        view.findViewById(R.id.ll_now).setOnClickListener(this);
//        view.findViewById(R.id.ll_next).setOnClickListener(this);
        mIvADTop.setOnClickListener(this);

        if (AppApplication.systemLanguage == 1) {
            applyFont(getActivity(), view.findViewById(R.id.home_root), "fonts/home_ch.ttf");
        } else {
            applyFont(getActivity(), view.findViewById(R.id.home_root), "fonts/home_en.ttf");
        }
    }

    private void initViews(View view) {
        mIvADTop = (ImageView) view.findViewById(R.id.iv_top);
        mTvMsgNums = (TextView) view.findViewById(R.id.tv_msg_nums);
//      mIvHdSessionOnTips = (ImageView) view.findViewById(R.id.iv_hd_session_on);
    }

    class TaskAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mValidActivitys.clear();
            mAllActivitys.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_PROFESSOR, "", "", "", "");
                return;
            } else if (AppApplication.facultyId == -1) {
                ToastUtils.showToast("您不是主席团成员，无法使用该模块");
                return;
            }

            SecretaryActivity.startSecretaryActivity(getActivity(), mTaskNum, mQuestionNum, mValidActivitys, mScenceShowBeans);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                return null;
            } else if (AppApplication.facultyId == -1) {
                return null;
            }

            mAllActivitys = ConferenceDbUtils.getSessionAndMeetingBySpeakerId(AppApplication.facultyId);

            // 处理过期时间下的活动
            int size = mAllActivitys.size();

            Date date = new Date();
            String current_day = DateUtil.getNowDate(DateUtil.DEFAULT);
            String currentTime = current_day + " 00:00:00";
            Date currentDate = DateUtil.getDate(currentTime, DateUtil.DEFAULT_SECOND);
            long currentSecond = currentDate.getTime();

            for (int i = 0; i < size; i++) {
                String time = mAllActivitys.get(i).getTime() + ":00";
                date = DateUtil.getDate(time, DateUtil.DEFAULT_SECOND);
                long second = date.getTime();
                if (second > currentSecond) {
                    mValidActivitys.add(mAllActivitys.get(i));
                }
            }

            //按照时间排序。
            ArrayUtils.quickSort(mValidActivitys);
            mTaskNum = mValidActivitys.size();
            return null;
        }
    }

    public class MessageStationBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_MESSAGE_STATION)) {
                getHomeNums();
            }
        }
    }

    /**
     * 看日程
     */
    private void goLookSchedule() {
        MeetingScheduleListActionFragment listFragment = new MeetingScheduleListActionFragment();
        ImageView view = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
        if (AppApplication.systemLanguage == 1) {
            view.setImageResource(R.drawable.schedule_switch_cn);
        } else {
            view.setImageResource(R.drawable.schedule_switch);
        }

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_MEETING_SCHEDULE);

        listFragment.setRightListener(view);
        action(listFragment, R.string.home_schedule, view, false, false, false);
    }

    /**
     * 查日程(包含讲者检索)
     */
    private void goSearchSchedule() {
        ImageView searchView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
        searchView.setImageResource(R.drawable.search);
        NewSearchScheduleActionFragment searchFragment = new NewSearchScheduleActionFragment();
        searchFragment.setRightView(searchView);
        View titleView = CommonUtils.initView(getActivity(), R.layout.title_segment);
        searchFragment.setCenterView(titleView);

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_SEARCH_SCHEDULE);
        action(searchFragment, R.string.home_search, searchView, false, false, titleView, false);

//        ImageView searchView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
//        searchView.setImageResource(R.drawable.search);
//        NewSearchScheduleActionFragment searchFragment = new NewSearchScheduleActionFragment();
//        searchFragment.setRightView(searchView);
//        action(searchFragment, getString(R.string.home_search), searchView, false, false, false);
    }

    /**
     * 我的日程
     */
    private void goMySchedule() {
        TextView my_schedule = (TextView) CommonUtils.initView(getActivity(), R.layout.title_right_textview);
        my_schedule.setText(R.string.my_schedule_edit);
        MyScheduleActionFragment schedule = new MyScheduleActionFragment();
        schedule.setRightView(my_schedule);

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_MY_SCHEDULE);
        action(schedule, R.string.home_my_schedule, my_schedule, false, false, false);
    }

    /**
     * 现场秀
     */
    private void goScenicXiu() {
        View scenicTitle = CommonUtils.initView(this.getActivity(), R.layout.scenic_xiu_title);
        LinearLayout mlayout = (LinearLayout) scenicTitle.findViewById(R.id.ll_senic_xiu_title);
        ScenicXiuFragment xiu = new ScenicXiuFragment();
        xiu.setScenicXiuTitle(mlayout);
        action(xiu, R.string.home_scene_xiu, scenicTitle, false, false, true);
    }

    /**
     * 直播
     */
    private void goLive() {
        getActivity().startActivity(new Intent(getActivity(), CitLiveFragment.class));
    }

    /**
     * 消息站
     */
    private void goMessageStation() {

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_MESSAGE);
        action(new MessageStationActionFragment(), R.string.home_messagestation, false, false, false);
    }

    /**
     * 专家秘书
     */
    private void goSecretary() {
        CHYHttpClientUsage.getInstanse().doGetSceneShowQuestions(Constants.getConId() + "", AppApplication.userId + "", "-1", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler("gbk") {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(getActivity(), null, "loading...");
                mQuestionNum = 0;
                mTaskNum = 0;
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());
                try {
                    int state = response.getInt("state");
                    if (state == 0) {
                        mQuestionNum = 0;
                    } else {
                        JSONArray jsonArray = response.getJSONArray("sceneShowArray");
                        Gson gson = new Gson();
                        mScenceShowBeans = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SceneShowArrayBean>>() {
                        }.getType());
                        mQuestionNum = response.getInt("questionCount");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new TaskAsyncTask().execute();
            }
        });
    }

    /**
     * 参会指南
     */
    private void goGuide() {
        MeetingGuideFragment guide = new MeetingGuideFragment();
        View view = CommonUtils.initView(getActivity(), R.layout.title_right_image);

        guide.setRightView(view);

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_MEETING_GUIDE);
        action(guide, R.string.home_meetingguide, view, false, false, true);
    }

    /**
     * 现场互动
     */
    private void goQuestionAndA() {
        HdSessionActionFragment hdFragment = new HdSessionActionFragment();
        View hdTtile = CommonUtils.initView(this.getActivity(), R.layout.title_hdsession);
        hdFragment.setRightListener(hdTtile);
        action(hdFragment, R.string.home_interactive, hdTtile, false, false, false);
    }

    /**
     * 实习中心
     */
    private void goHandsOn() {
        WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                getString(Constants.get_HANDS_ON_SITE(), Constants.getConId(), AppApplication.getSystemLanuageCode(), AppApplication.userId, AppApplication.userType), getString(R.string.home_hands_on));
    }

    /**
     * 壁报
     */
    private void goPost() {
        PosterFragment fragment = new PosterFragment();
        View rightView = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
        TextView tvRight = (TextView) rightView.findViewById(R.id.tv_right);
        tvRight.setText(R.string.scan_right_tips);

        Drawable drawable = getActivity().getResources().getDrawable(R.drawable.scane_scane);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvRight.setCompoundDrawables(null, null, drawable, null);

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_POSTER);
        action(fragment, R.string.home_wallpaper, rightView, false, false, false);
    }

    /**
     * 扫一扫
     */
    private void goScane() {
        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_SCANE);
        getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), HomeActivity.REQUEST_SCANE);
    }

    /**
     * 参展商
     */
    private void goExhibitor() {
        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_EXHIBITOR);
        action(new ExhibitorsActionFragment(), R.string.home_exhibitors, false, true, true);
    }

    /**
     * 学院
     */
    private void goCollege() {

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_COLLEGE);
        CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.home_cit_college), getString(Constants.get_CIT_COLLEGE(), Constants.getConId(), AppApplication.getSystemLanuageCode(), AppApplication.userId, AppApplication.userType));
    }

    /**
     * 讲者检索
     */
    private void goSpeakerSearch() {
        action(SpeakerSearchFragment.getInstance(SpeakerSearchFragment.TYPE_FROM_HOME), R.string.home_speakersearch, false, false, false);
    }

    /**
     * 循环设置字体格式
     *
     * @param context
     * @param root
     * @param fontName
     */
    public static void applyFont(final Context context, final View root, final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        } catch (Exception e) {
            LogUtils.println(String.format("Error occured when trying to apply %s font for %s view", fontName, root));
            e.printStackTrace();
        }
    }
}
