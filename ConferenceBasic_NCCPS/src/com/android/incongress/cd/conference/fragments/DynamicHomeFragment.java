package com.android.incongress.cd.conference.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.Row;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.fragments.bus_reminder.MeetingBusRemindAllFragment;
import com.android.incongress.cd.conference.fragments.cit_live.CitLiveFragment;
import com.android.incongress.cd.conference.fragments.exhibitor.ExhibitorsActionFragment;
import com.android.incongress.cd.conference.fragments.interactive.HdSessionActionFragment;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.fragments.meeting_guide.MeetingGuideFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListActionFragment;
import com.android.incongress.cd.conference.fragments.message_station.MessageStationActionFragment;
import com.android.incongress.cd.conference.fragments.my_schedule.MyScheduleActionFragment;
import com.android.incongress.cd.conference.fragments.now_next.NextFragment;
import com.android.incongress.cd.conference.fragments.now_next.NowFragment;
import com.android.incongress.cd.conference.fragments.photo_album.PhotoAlbumFragment;
import com.android.incongress.cd.conference.fragments.professor_secretary.SecretaryActivity;
import com.android.incongress.cd.conference.fragments.question.MeetingQuestionFragment;
import com.android.incongress.cd.conference.fragments.scenic_xiu.ScenicXiuFragment;
import com.android.incongress.cd.conference.fragments.search_schedule.NewSearchScheduleActionFragment;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerSearchFragment;
import com.android.incongress.cd.conference.fragments.wall_poster.PosterFragment;
import com.android.incongress.cd.conference.model.Ad;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.ui.login.view.LoginActivity;
import com.android.incongress.cd.conference.utils.ArrayUtils;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.ImageColorChangeUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Jacky on 2016/5/16.
 */
public class DynamicHomeFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mLlConstainer;
    private Row mRow;

    //广告轮播
    protected List<Ad> mAdList;
    private AdReceiver mAdReceiver;
    //专家秘书
    private View mSecretaryView;

    private class AdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppApplication.topNum < 0) {
                return;
            }

            int top = AppApplication.topNum;

//          String filespath = AppApplication.instance().getSDPath() + Constants.FILE_CONFERENCES + "incongress" + AppApplication.conId + "/"; //如果是compas会议的情况下会有这个conId存在
            String filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR;
            String pathTop = "";
            if (mAdList != null && mAdList.size() > top) {
                pathTop = filespath + mAdList.get(top).getAdImage();
            }

            File fileTop = new File(pathTop);
            if (fileTop.exists()) {
                setAdImageView(fileTop.getAbsolutePath(), mIvADTop);
            }
        }
    }

    private void setAdImageView(String filepath, ImageView imageview) {
        File file = new File(filepath);
        if (file != null) {
            PicUtils.loadImageFile(getContext(), file, imageview);
            int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams lp = imageview.getLayoutParams();
            lp.height = (int) (width * 0.17);
            imageview.setLayoutParams(lp);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mAdReceiver);
    }

    /**
     * 初始化接收器
     */
    private void initReceiver() {
        mAdReceiver = new AdReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CHANGE_AD);

        getActivity().registerReceiver(mAdReceiver, filter);
    }

    /**
     * 挑战二维码的code
     */
    public static final int REQUEST_CODE = 111;

    private static final int PROGRAM = 1; //看日程
    private static final int SEARCH = 2;    //查日程
    private static final int MY_AGENDA = 3; //我的日程
    private static final int INFO_RELEASE = 4;  //现场秀
    private static final int LIVE = 5;          //直播
    private static final int MESSAGE = 6;       //消息站
    private static final int FACULTY = 7;       //专家秘书
    private static final int INFOMATION = 8;    //参会指南
    private static final int INTERACTIVE = 9;   //现场互动
    private static final int LEARNING = 10;     //实习中西
    private static final int POSTER = 11;       //壁报
    private static final int DEMAND = 12;       //学院
    private static final int EXHIBITORS = 13;   //参展商
    private static final int PERSONAL = 14;     //我
    private static final int FACULTY_INDEX = 15;    //讲者检索
    private static final int NEWS_CENTER = 16; //新闻中心
    private static final int PICTURE = 17; //图片模块
    private static final int QUESTION = 18; //提问
    private static final int SCANE = 19; //扫一扫
    private static final int NOW = 20;//正在进行
    private static final int NEXT = 21;//即将进行
    private static final int BUS = 22; //班车提醒
    private static final int PHOTOWALL = 23; //照片墙
    private static final int HANDLER_NUMS = 0x0001;

    private TextView mTvMsgCount;//消息站的提示信息
    private TextView mTvScenicShowCount; //现场秀的提示信息
    private ImageView mIvHdSession;//现场互动的标志

    private int mMessageCount, mSceneShowCount, mXchdCount;

    //上方广告抽取
    private ImageView mIvADTop;

    private String mIconFilePath;

    //专家秘书模块需要
    private List<ActivityBean> mAllActivitys = new ArrayList<>();
    private List<ActivityBean> mValidActivitys = new ArrayList<>();
    private List<SceneShowArrayBean> mScenceShowBeans = new ArrayList<>();
    private int mTaskNum, mQuestionNum;

    //我的专家秘书
    private TextView mTvSecretaryTime, mTvSecretaryRoom, mTvSecretaryTask, mTvSecretarySessionName;

    //显示专家秘书
    public void showSecretaryView() {
        if (mSecretaryView != null && AppApplication.facultyId != -1) {
            mSecretaryView.setVisibility(View.VISIBLE);

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

            if (mValidActivitys != null && mValidActivitys.size() > 0) {
                //获取距离当前时间最近的
                ActivityBean activityBean = null;
                for (int k = 0; k < mValidActivitys.size(); k++) {
                    Date date1 = DateUtil.getDate(mValidActivitys.get(k).getTime(), DateUtil.DEFAULT_ENGLISH2);
                    if (date1.getTime() > System.currentTimeMillis()) {
                        activityBean = mValidActivitys.get(k);
                        break;
                    }
                }

                if (activityBean != null) {
                    mTvSecretaryTime.setVisibility(View.VISIBLE);
                    mTvSecretaryRoom.setVisibility(View.VISIBLE);
                    mTvSecretaryTask.setVisibility(View.VISIBLE);
                    if (AppApplication.systemLanguage == 1) {
                        mTvSecretaryTime.setText(activityBean.getDate() + " " + activityBean.getStart_time() + "-" + activityBean.getEnd_time());
                        mTvSecretaryRoom.setText(activityBean.getLocation());
                        mTvSecretaryTask.setText("任务：" + activityBean.getTypeName());
                        mTvSecretarySessionName.setText(activityBean.getActivityName());
                    } else {
                        mTvSecretaryTime.setText(activityBean.getDate() + " " + activityBean.getStart_time() + "-" + activityBean.getEnd_time());
                        mTvSecretaryRoom.setText(activityBean.getLocationEn());
                        mTvSecretaryTask.setText("Task:" + activityBean.getTypeEnName());
                        mTvSecretarySessionName.setText(activityBean.getActivityNameEN());
                    }
                }
            } else {
                //没有任务
                mTvSecretaryTime.setVisibility(View.GONE);
                mTvSecretaryRoom.setVisibility(View.GONE);
                mTvSecretaryTask.setVisibility(View.GONE);
                mTvSecretarySessionName.setText(R.string.secretary_no_task);
            }
        }
    }

    //隐藏专家秘书
    public void hideSecretaryView() {
        if (mSecretaryView != null)
            mSecretaryView.setVisibility(View.GONE);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int target = message.what;
            if (target == HANDLER_NUMS) {
                if (mIvHdSession != null) {
                    if (mXchdCount > 0) {
                        mIvHdSession.setVisibility(View.VISIBLE);
                        if (AppApplication.systemLanguage == 2)
                            mIvHdSession.setImageResource(R.drawable.hdsession_loading_en);
                    } else {
                        mIvHdSession.setVisibility(View.GONE);
                    }
                }

                if (mTvMsgCount != null) {
                    if (mMessageCount > 0) {
                        mTvMsgCount.setVisibility(View.VISIBLE);
                        if (mMessageCount < 99) {
                            mTvMsgCount.setText(mMessageCount + "");
                        } else {
                            mTvMsgCount.setText(99 + "");
                        }
                    } else {
                        mTvMsgCount.setVisibility(View.GONE);
                    }
                }

                if (mTvScenicShowCount != null) {
                    if (mSceneShowCount > 0) {
                        mTvScenicShowCount.setVisibility(View.VISIBLE);
                        if (mSceneShowCount < 99) {
                            mTvScenicShowCount.setText(mSceneShowCount + "");
                        } else {
                            mTvScenicShowCount.setText(99 + "");
                        }
                    } else {
                        mTvScenicShowCount.setVisibility(View.GONE);
                    }
                }
            }
            return false;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_home_fragment, null);
        mLlConstainer = (LinearLayout) view.findViewById(R.id.ll_container);
        mIvADTop = (ImageView) view.findViewById(R.id.ad_top);

//       mIconFilePath = AppApplication.instance().getSDPath() + Constants.FILE_CONFERENCES + "incongress" + AppApplication.conId + "/icon.txt";
        mIconFilePath = AppApplication.instance().getSDPath() + Constants.FILESDIR + "/icon.txt";

        mAdList = AppApplication.adList;

        ScaleAnimation sa = (ScaleAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
        LayoutAnimationController lac = new LayoutAnimationController(sa, 0);
        mLlConstainer.setLayoutAnimation(lac);
        getHomeIconInfo();

        CHYHttpClientUsage.getInstanse().doGetLookCount(Constants.getConId() + "", AppApplication.userId + "", AppApplication.userType + "", AppApplication.TOKEN_IMEI, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    mMessageCount = response.getInt("tokenMessageCount");
                    mSceneShowCount = response.getInt("sceneShowCount");
                    mXchdCount = response.getInt("xchdCount");
                } catch (Exception e) {
                    e.printStackTrace();
                    mMessageCount = 0;
                    mSceneShowCount = 0;
                    mXchdCount = 0;
                }
                mHandler.sendEmptyMessage(HANDLER_NUMS);
            }
        });

        registerMeessageBroadcast();

        mIvADTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int top = AppApplication.topNum;
                if (top >= 0) {
                    Ad bean = AppApplication.adList.get(top);
                    String link = bean.getAdLink().trim();
                    if (link != null && !link.equals("")) {
                        CollegeActivity.startCitCollegeActivity(getActivity(), "", link);
                    }
                }
            }
        });

        initReceiver();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mMsgBroadcast);
    }

    private void getHomeIconInfo() {
        try {
            JSONObject obj = new JSONObject(readFileSdcardFile(mIconFilePath));

            Gson gson = new Gson();
            String iconSort = obj.getString("iconSort");
            mRow = gson.fromJson(iconSort, Row.class);

            for (int i = 0; i < mRow.getRows().size(); i++) {
                Row.RowsBean bean = mRow.getRows().get(i);
                LinearLayout linearLayout = getHorizontalLinearLayout();

                //设置首页上方广告
                if (bean.getObj().size() == 1 && bean.getObj().get(0).getIconCode().equals(PICTURE + "")) {
                    final Row.RowsBean.ObjBean picBean = bean.getObj().get(0);
                    ImageView imageView = new ImageView(getActivity());
                    int spWidth = DensityUtil.dip2px(getActivity(), 110);
                    int spMargin = DensityUtil.px2dip(getActivity(), 2.5f);
                    LinearLayout.LayoutParams picLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    picLp.setMargins(spMargin, 0, spMargin, 0);
                    imageView.setLayoutParams(picLp);
                    PicUtils.loadImageUrl(getContext(), picBean.getIconUrl(), imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(picBean.getNewUrl())) {
                                if (AppApplication.systemLanguage == 1) {
                                    CollegeActivity.startCitCollegeActivity(getActivity(), picBean.getIconName(), picBean.getNewUrl());
                                } else {
                                    CollegeActivity.startCitCollegeActivity(getActivity(), picBean.getIconEnName(), picBean.getNewUrl());
                                }
                            }
                        }
                    });
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    linearLayout.addView(imageView);
                    String imgSize = picBean.getImgSize();
                    String[] wh = imgSize.split(",");
                    int width = Integer.parseInt(wh[0]);
                    int height = Integer.parseInt(wh[1]);
                    double bl = width / height;
                    int widthMax = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                    ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                    lp.height = (int) (widthMax / bl);
                    imageView.setLayoutParams(lp);
                } else {
                    for (int j = 0; j < bean.getObj().size(); j++) {
                        Row.RowsBean.ObjBean rowBean = bean.getObj().get(j);
                        LinearLayout innerLinearLayout = getInnerLinearLayout(Float.parseFloat(rowBean.getWidth().replace("%", "")));
                        //设置内部文字和图片
                        innerLinearLayout.addView(addTextAndImage(rowBean, Float.parseFloat(rowBean.getWidth().replace("%", "")), innerLinearLayout));
                        innerLinearLayout.setTag(rowBean);
                        innerLinearLayout.setOnClickListener(this);
                        linearLayout.addView(innerLinearLayout);
                    }
                }

                mLlConstainer.addView(linearLayout);

                if (i == 0 && bean.getObj().get(0).getIconCode().equals(PICTURE + "")) {
                    //加入专家秘书的模块
                    //将专家秘书放入其中
                    mSecretaryView = LayoutInflater.from(getActivity()).inflate(R.layout.part_secretary, mLlConstainer, false);

                    mTvSecretaryTime = (TextView) mSecretaryView.findViewById(R.id.tv_secretary_time);
                    mTvSecretaryRoom = (TextView) mSecretaryView.findViewById(R.id.tv_secretary_room);
                    mTvSecretaryTask = (TextView) mSecretaryView.findViewById(R.id.tv_secretary_task);
                    mTvSecretarySessionName = (TextView) mSecretaryView.findViewById(R.id.tv_secretary_session_name);

                    mAllActivitys = ConferenceDbUtils.getSessionAndMeetingBySpeakerId(AppApplication.facultyId);
                    // 处理过期时间下的活动
                    int size = mAllActivitys.size();
                    Date date = new Date();
                    String current_day = DateUtil.getNowDate(DateUtil.DEFAULT);
                    String currentTime = current_day + " 00:00:00";
                    Date currentDate = DateUtil.getDate(currentTime, DateUtil.DEFAULT_SECOND);
                    long currentSecond = currentDate.getTime();

                    for (int j = 0; j < size; j++) {
                        String time = mAllActivitys.get(j).getTime() + ":00";
                        date = DateUtil.getDate(time, DateUtil.DEFAULT_SECOND);
                        long second = date.getTime();
                        if (second > currentSecond) {
                            mValidActivitys.add(mAllActivitys.get(j));
                        }
                    }

                    //按照时间排序。
                    ArrayUtils.quickSort(mValidActivitys);

                    if (mValidActivitys != null && mValidActivitys.size() > 0) {
                        //获取距离当前时间最近的
                        ActivityBean activityBean = null;
                        for (int k = 0; k < mValidActivitys.size(); k++) {
                            Date date1 = DateUtil.getDate(mValidActivitys.get(k).getTime(), DateUtil.DEFAULT_ENGLISH2);
                            if (date1.getTime() > System.currentTimeMillis()) {
                                activityBean = mValidActivitys.get(k);
                                break;
                            }
                        }

                        if (activityBean != null) {
                            if (AppApplication.systemLanguage == 1) {
                                mTvSecretaryTime.setText(activityBean.getDate() + " " + activityBean.getStart_time() + "-" + activityBean.getEnd_time());
                                mTvSecretaryRoom.setText(activityBean.getLocation());
                                mTvSecretaryTask.setText("任务：" + activityBean.getTypeName());
                                mTvSecretarySessionName.setText(activityBean.getActivityName());
                            } else {
                                mTvSecretaryTime.setText(activityBean.getDate() + " " + activityBean.getStart_time() + "-" + activityBean.getEnd_time());
                                mTvSecretaryRoom.setText(activityBean.getLocationEn());
                                mTvSecretaryTask.setText("Task:" + activityBean.getTypeEnName());
                                mTvSecretarySessionName.setText(activityBean.getActivityNameEN());
                            }
                        }
                    } else {
                        //没有任务
                        mTvSecretaryTime.setVisibility(View.GONE);
                        mTvSecretaryRoom.setVisibility(View.GONE);
                        mTvSecretaryTask.setVisibility(View.GONE);
                        mTvSecretarySessionName.setText(R.string.secretary_no_task);
                    }

                    mSecretaryView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goSecretary();
                        }
                    });

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 120f));
                    int spMarginBottom = DensityUtil.dip2px(getActivity(), 8);
                    lp.setMargins(spMarginBottom, 0, spMarginBottom, spMarginBottom);
                    mSecretaryView.setLayoutParams(lp);
                    mLlConstainer.addView(mSecretaryView);

                    if (AppApplication.facultyId != -1 && Constants.IS_SECRETARY_SHOW) {
                        mSecretaryView.setVisibility(View.VISIBLE);
                    } else {
                        mSecretaryView.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    private LinearLayout getHorizontalLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(getActivity());

        int spWidth = DensityUtil.dip2px(getActivity(), 110);
        int spMarginBottom = DensityUtil.dip2px(getActivity(), 5);
        int spPadding = DensityUtil.dip2px(getActivity(), 5);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spWidth);
        lp.setMargins(0, 0, 0, spMarginBottom);
        linearLayout.setLayoutParams(lp);
        linearLayout.setPadding(spPadding, 0, spPadding, 0);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));

        return linearLayout;
    }

    private LinearLayout getInnerLinearLayout(float weight) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        if (weight == 50) {
            weight = (float) (weight + 1.5);
        }
        lp.weight = weight;

        int spMarginBottom = DensityUtil.dip2px(getActivity(), 2.5f);

        lp.setMargins(spMarginBottom, 0, spMarginBottom, 0);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        return linearLayout;
    }

    private View addTextAndImage(Row.RowsBean.ObjBean bean, float weight, ViewGroup parentView) {
        int iconCode = 0;
        try {
            iconCode = Integer.parseInt(bean.getIconCode());
        } catch (Exception e) {
            e.printStackTrace();
            iconCode = 0;
        }

        int iconDefaultId = getDefaultIconId(iconCode);
        if (iconCode != 0) {
            if (weight >= 50) {
                //横向布局
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.merge_horizontal_text_image, parentView, false);
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                ImageView ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
                TextView tvMsgNum = (TextView) view.findViewById(R.id.tv_msg_nums);
                ImageView ivInteractive = (ImageView) view.findViewById(R.id.iv_hd_session_on);
                view.setBackgroundColor(Color.parseColor(bean.getIconColor()));

                if (AppApplication.systemLanguage == 1) {
                    tvName.setText(bean.getIconName());
                } else {
                    tvName.setText(bean.getIconEnName());
                }

                tvName.setTextColor(Color.parseColor(bean.getIconFontColor()));

                if (TextUtils.isEmpty(bean.getIconUrl())) {
                    ivLogo.setImageDrawable(ImageColorChangeUtils.changeIconColor(getActivity(), iconDefaultId, Color.parseColor(bean.getIconFontColor())));
                } else {
                    PicUtils.loadImageUrl(getContext(), bean.getIconUrl(), ivLogo);
                }

                setCountNum(bean, tvMsgNum, ivInteractive);
                return view;
            } else {
                //纵向布局
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.merge_vertical_text_image, parentView, false);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                ImageView ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
                TextView tvMsgNum = (TextView) view.findViewById(R.id.tv_msg_nums);
                ImageView ivInteractive = (ImageView) view.findViewById(R.id.iv_hd_session_on);

                if (AppApplication.systemLanguage == 1) {
                    tvName.setText(bean.getIconName());
                } else {
                    tvName.setText(bean.getIconEnName());
                }

                tvName.setTextColor(Color.parseColor(bean.getIconFontColor()));
                view.setBackgroundColor(Color.parseColor(bean.getIconColor()));
                if (TextUtils.isEmpty(bean.getIconUrl())) {
                    ivLogo.setImageResource(iconDefaultId);
                } else {
                    PicUtils.loadImageUrl(getContext(), bean.getIconUrl(), ivLogo);
                }
                setCountNum(bean, tvMsgNum, ivInteractive);
                return view;
            }
        } else {
            ToastUtils.showToast("iconCode解析出错...");
            return null;
        }
    }

    /**
     * 根据iconId去查默认的图片
     *
     * @param iconCode
     */
    private int getDefaultIconId(int iconCode) {
        switch (iconCode) {
            case PROGRAM:
                return R.drawable.watch_schedule;
            case SEARCH:
                return R.drawable.search_schedule;
            case MY_AGENDA:
                return R.drawable.my_schedule;
            case INFO_RELEASE:
                return R.drawable.home_icon_scenicxiu;
            case LIVE:
                return R.drawable.home_icon_live;
            case MESSAGE:
                return R.drawable.home_icon_msg;
            case FACULTY:
                return R.drawable.home_icon_zjms;
            case INFOMATION:
                return R.drawable.home_icon_guide;
            case INTERACTIVE:
                return R.drawable.home_icon_interactive;
            case LEARNING:
                return R.drawable.home_icon_handson;
            case POSTER:
                return R.drawable.home_icon_dzbb;
            case DEMAND:
                return R.drawable.home_icon_cit_college;
            case EXHIBITORS:
                return R.drawable.home_icon_exhibitor;
            case PERSONAL:
                return R.drawable.home_icon_me;
            case FACULTY_INDEX:
                return R.drawable.home_icon_jzjs;
            case NEWS_CENTER:
                return R.drawable.home_icon_news;
            case QUESTION:
                return R.drawable.home_icon_question;
            case SCANE:
                return R.drawable.home_icon_scane;
            case NOW:
                return R.drawable.home_icon_now;
            case NEXT:
                return R.drawable.home_icon_next;
            case BUS:
                return R.drawable.home_icon_reminder;
            case PHOTOWALL:
                return R.drawable.home_icon_photowall;

        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        Row.RowsBean.ObjBean rowBean = (Row.RowsBean.ObjBean) v.getTag();

        if (rowBean != null) {
            if (!TextUtils.isEmpty(rowBean.getNewModel())) {

                //链接中加入userId和lan信息
//                if(rowBean.getIconName().equals("壁报")) {
//                    WebViewContainerActivity.startWebViewContainerActivity(getActivity(), "http://192.168.0.213:8082/EPoster/html/indexCsccmH5.html?1=1" + "&userId=" + AppApplication.userId + "&lan=" + AppApplication.getSystemLanuageCode(), rowBean.getIconName());
//                }else{
//                }
                String url = rowBean.getNewModel();
                if (url.contains("?")) {
                    url = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                } else {
                    url = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                }
                if (AppApplication.systemLanguage == 1) {
                    CollegeActivity.startCitCollegeActivity(getActivity(), rowBean.getIconName(), url);
                } else {
                    CollegeActivity.startCitCollegeActivity(getActivity(), rowBean.getIconEnName(), url);
                }
            } else {
                //根据ID跳转到不同的模块中
                switch (Integer.parseInt(rowBean.getIconCode())) {
                    case PROGRAM:
                        //看日程
                        goLookSchedule();
                        break;
                    case SEARCH:
                        //差日程
                        goSearchSchedule();
                        break;
                    case MY_AGENDA:
                        //我的日程
                        goMySchedule();
                        break;
                    case INFO_RELEASE:
//                        Intent social = new Intent(getActivity(), SocialActivity.class);
//                        startActivity(social);
                        //现场秀
                        goScenicXiu();
                        break;
                    case LIVE:
                        //直播
                        goLive();
                        break;
                    case MESSAGE:
                        //消息站
                        goMessageStation();
                        break;
                    case INFOMATION:
                        //参会指南
                        goGuide();
                        break;
                    case LEARNING:
                        //实习中心
                        goHandsOn();
                        break;
                    case DEMAND:
                        //学院
                        goCollege();
                        break;
                    case EXHIBITORS:
                        //参展商
                        goExhibitor();
                        break;
                    case PERSONAL:
                        //我
                        goPersonCenter();
                        break;
                    case FACULTY_INDEX:
                        //讲者检索
                        goSpeakerSearch();
                        break;
                    case INTERACTIVE:
                        goQuestionAndA();
                        break;
                    case POSTER:
                        goPost();
                        break;
                    case FACULTY:
                        goSecretary();
                        break;
                    case PICTURE:
                        if (!TextUtils.isEmpty(rowBean.getNewUrl())) {
                            if (AppApplication.systemLanguage == 1) {
                                CollegeActivity.startCitCollegeActivity(getActivity(), rowBean.getIconName(), rowBean.getNewUrl());
                            } else {
                                CollegeActivity.startCitCollegeActivity(getActivity(), rowBean.getIconEnName(), rowBean.getNewUrl());
                            }
                        }
                        break;
                    case QUESTION:
                        goQuestions();
                        break;
                    case SCANE:
                        goScane();
                        break;
                    case NOW:
                        goNow();
                        break;
                    case NEXT:
                        goNext();
                        break;
                    case BUS:
                        goBus();
                        break;
                    case PHOTOWALL:
                        goPhotoAlbum();
                        break;
                    default:
                        ToastUtils.showToast("未找到该模块，请尝试更新数据包");
                        break;
                }
            }
        }
    }


    //=======================================================================模块跳转=======================================================================

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
        action(searchFragment, R.string.home_search, searchView, false, false, titleView, false);
    }

    /**
     * 我的日程
     */
    private void goMySchedule() {
        TextView my_schedule = (TextView) CommonUtils.initView(getActivity(), R.layout.title_right_textview);
        my_schedule.setText(R.string.my_schedule_edit);
        MyScheduleActionFragment schedule = new MyScheduleActionFragment();
        schedule.setRightView(my_schedule);
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
        action(xiu, R.string.bottom_broadcast, scenicTitle, false, false, false);
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
        action(new MessageStationActionFragment(), R.string.home_messagestation, false, false, false);
    }

    /**
     * 正在进行
     */
    private void goNow() {
        NowFragment now = new NowFragment();
        action(now, R.string.home_now, false, false, false);
    }

    /**
     * 即将进行
     */
    private void goNext() {
        NextFragment next = new NextFragment();
        action(next, R.string.home_next, false, false, false);
    }

    /**
     * 专家秘书
     */
    private void goSecretary() {
        CHYHttpClientUsage.getInstanse().doGetSceneShowQuestions(Constants.getConId() + "", AppApplication.userId + "", "-1", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler("gbk") {
            @Override
            public void onStart() {
                super.onStart();
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
        ImageView searchView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
        searchView.setImageResource(R.drawable.icon_share);
        MeetingGuideFragment meetingGuideFragment = new MeetingGuideFragment();
        meetingGuideFragment.setRightView(searchView);
        action(meetingGuideFragment, R.string.home_meetingguide, searchView, false, false, false);
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
     * 提问模块
     */
    private void goQuestions() {
        if (AppApplication.isUserLogIn()) {
            action(new MeetingQuestionFragment(), R.string.question, false, false, false);
        } else {
            LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
        }
        // action(new QuestionsFragment(), R.string.question, false, false, false);
    }

    /**
     * 实习中心
     */
    private void goHandsOn() {
        String lan2 = "";
        if (AppApplication.systemLanguage == 1) {
            lan2 = "cn";
        } else {
            lan2 = "en";
        }
        CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.home_hands_on),
                getString(Constants.get_HANDS_ON_SITE(), Constants.getConId(), lan2, AppApplication.userId, AppApplication.userType));

    }

    /**
     * 壁报
     */
    private void goPost() {
        View scane = CommonUtils.initView(this.getActivity(), R.layout.title_right_image);
        //((ImageView) scane).setImageResource(R.drawable.scane_scane);
        PosterFragment post = new PosterFragment();
        //post.setRightView(scane);
        action(post, R.string.home_wallpaper, scane, false, false, false);
    }

    /**
     * 参展商
     */
    private void goExhibitor() {
        action(new ExhibitorsActionFragment(), R.string.home_exhibitors, false, false, false);
    }

    /**
     * 照片墙
     */
    private void goPhotoAlbum() {
        action(new PhotoAlbumFragment(), "照片墙", false, false, false);
    }

    /**
     * 班车
     */
    private void goBus() {
        action(new MeetingBusRemindAllFragment(), "班车提醒", false, false, false);
    }

    /**
     * 我(个人中心)
     */
    private void goPersonCenter() {
        action(new PersonCenterFragment(), R.string.home_me, false, false, true);
    }

    /**
     * 学院
     */
    private void goCollege() {
        CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.home_cit_college), getString(Constants.get_CIT_COLLEGE(), Constants.getConId(), AppApplication.getSystemLanuageCode(), AppApplication.userId, AppApplication.userType));
    }

    /**
     * 讲者检索
     */
    private void goSpeakerSearch() {
        action(SpeakerSearchFragment.getInstance(SpeakerSearchFragment.TYPE_FROM_HOME), R.string.home_speakersearch, false, false, false);
    }

    /**
     * 扫一扫
     */
    private void goScane() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        getActivity().startActivityForResult(intent, HomeActivity.REQUEST_SCANE);
    }

    /**
     * 设置一些提示信息
     */


    private void setCountNum(Row.RowsBean.ObjBean bean, TextView tvMsgNum, ImageView ivInteractive) {
        if (bean.getIconCode().equals(MESSAGE + "")) {
            mTvMsgCount = tvMsgNum;
        }
        if (bean.getIconCode().equals(INFO_RELEASE + "")) {
            mTvScenicShowCount = tvMsgNum;
        }
        if (bean.getIconCode().equals(INTERACTIVE + "")) {
            mIvHdSession = ivInteractive;
        }
    }

    //读SD中的文件
    public String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
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

            if (AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_PROFESSOR, "", "", "", "");
                return;
            } else if (AppApplication.facultyId == -1) {
                ToastUtils.showToast(getString(R.string.secretary_module_not_available));
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

    private MessageStationBroadCast mMsgBroadcast;
    public static final String INTENT_MESSAGE_STATION = "com.incongress.messasge";

    private void registerMeessageBroadcast() {
        mMsgBroadcast = new MessageStationBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_MESSAGE_STATION);   //为BroadcastReceiver指定action，使之用于接收同action的广播

        getActivity().registerReceiver(mMsgBroadcast, intentFilter);
    }

    private class MessageStationBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_MESSAGE_STATION)) {
                getHomeNums();
            }
        }
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
                    mSceneShowCount = response.getInt("sceneShowCount");
                    mXchdCount = response.getInt("xchdCount");
                } catch (Exception e) {
                    e.printStackTrace();
                    mMessageCount = 0;
                    mSceneShowCount = 0;
                    mXchdCount = 0;
                }
                mHandler.sendEmptyMessage(HANDLER_NUMS);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_DYNAMICHOME);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_DYNAMICHOME);
    }
}
