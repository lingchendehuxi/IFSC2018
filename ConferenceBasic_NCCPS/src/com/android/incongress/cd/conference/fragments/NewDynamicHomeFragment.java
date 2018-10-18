package com.android.incongress.cd.conference.fragments;

import android.animation.ValueAnimator;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.adapters.CourSewareAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.CoursewareBean;
import com.android.incongress.cd.conference.beans.Row;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.fragments.bus_reminder.MeetingBusRemindAllFragment;
import com.android.incongress.cd.conference.fragments.cit_live.CitLiveFragment;
import com.android.incongress.cd.conference.fragments.exhibitor.ExhibitorsActionFragment;
import com.android.incongress.cd.conference.fragments.interactive.HdSessionActionFragment;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.fragments.meeting_guide.MeetingGuideFragment;
import com.android.incongress.cd.conference.fragments.meeting_guide.NewMeetingInfoFragment;
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
import com.android.incongress.cd.conference.utils.ArrayUtils;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.ImageColorChangeUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.zxing.activity.CaptureActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by GG on 2018/1/3.
 */

public class NewDynamicHomeFragment extends BaseFragment implements View.OnClickListener{
    private LinearLayout mLlConstainer,marquee_layout,zk_layout;
    private ViewFlipper mMarqueeView;
    private Row mRow;
    private RecyclerView mRecyclerView;
    private RelativeLayout mRelativeLayout;
    //广告轮播
    protected List<Ad> mAdList;
    private AdReceiver mAdReceiver ;
    //上方广告抽取
    private ImageView mIvADTop,mTopADImg;
    private String mIconFilePath;

    private ImageView zk;
    //专家秘书模块需要
    private List<ActivityBean> mAllActivitys = new ArrayList<>();
    private List<ActivityBean> mValidActivitys = new ArrayList<>();
    private List<SceneShowArrayBean> mScenceShowBeans = new ArrayList<>();
    private int mTaskNum, mQuestionNum;
    //我的专家秘书
    private TextView mTvSecretaryTime, mTvSecretaryRoom, mTvSecretaryTask, mTvSecretarySessionName;
    private LinearLayout mSecretaryView,courseware_text;

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
    private static final int VENUEPICTURE = 24; //场馆图
    private static final int HANDLER_NUMS = 0x0001;




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
            Glide.with(getActivity()).load(file).into(imageview);
            int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams lp=imageview.getLayoutParams();
            lp.height= (int) (width*0.17);
            imageview.setLayoutParams(lp);
        }
    }

    private List<CoursewareBean> coursewareBeanList = new ArrayList<>();
    private CourSewareAdapter mCourSewareAdapter;
    private List<String> textList = new ArrayList<>();
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_dynamic_home_fragment, null);
         mRelativeLayout = (RelativeLayout) view.findViewById(R.id.animatorlayout);
        mLlConstainer = (LinearLayout) view.findViewById(R.id.ll_container);
        mIvADTop = (ImageView) view.findViewById(R.id.ad_top);
        mTopADImg = (ImageView) view.findViewById(R.id.layout_ad_top);
        zk = (ImageView) view.findViewById(R.id.zk_button);
         zk_layout = (LinearLayout) view.findViewById(R.id.zk_layout);
         mRecyclerView = (RecyclerView) view.findViewById(R.id.courseware_recycler);
         marquee_layout = (LinearLayout) view.findViewById(R.id.marquee_layout);
         courseware_text = (LinearLayout) view.findViewById(R.id.courseware_layout);
        mMarqueeView = (ViewFlipper) view.findViewById(R.id.viewflipper);

         mSecretaryView = (LinearLayout) view.findViewById(R.id.ll_secretary);
         mTvSecretaryTime = (TextView) view.findViewById(R.id.tv_secretary_time);
         mTvSecretaryRoom = (TextView) view.findViewById(R.id.tv_secretary_room);
         mTvSecretaryTask = (TextView) view.findViewById(R.id.tv_secretary_task);
         mTvSecretarySessionName = (TextView) view.findViewById(R.id.tv_secretary_session_name);

//       mIconFilePath = AppApplication.instance().getSDPath() + Constants.FILE_CONFERENCES + "incongress" + AppApplication.conId + "/icon.txt";
        mIconFilePath = AppApplication.instance().getSDPath() + Constants.FILESDIR + "/icon.txt";

        mAdList = AppApplication.adList;
        ScaleAnimation sa = (ScaleAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
        LayoutAnimationController lac = new LayoutAnimationController(sa, 0);
        mLlConstainer.setLayoutAnimation(lac);
         mRecyclerView.setNestedScrollingEnabled(false);
         mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
         mRecyclerView.setItemAnimator(new DefaultItemAnimator());
         CHYHttpClientUsage.getInstanse().doGetContentStream(new JsonHttpResponseHandler("gbk") {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                 super.onSuccess(statusCode, headers, response);
                 try {
                     if(response.length()>0){
                         for(int i = 0 ; i<response.length();i++){
                             String text = response.getJSONObject(i).getString("title");
                             textList.add(text);
                             mMarqueeView.addView(getTextViewTitle(text));
                         }
                         marquee_layout.setVisibility(View.VISIBLE);
                     }else{
                         marquee_layout.setVisibility(View.GONE);
                     }

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         });
         CHYHttpClientUsage.getInstanse().doGetCoursewareStream(new JsonHttpResponseHandler("gbk") {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                 super.onSuccess(statusCode, headers, response);
                 try {
                     for(int i = 0 ; i<response.length();i++){
                         CoursewareBean bean = new CoursewareBean();
                         bean.setAuthor(response.getJSONObject(i).getString("author"));
                         bean.setDataId(response.getJSONObject(i).getInt("dataId"));
                         bean.setFirstPic(response.getJSONObject(i).getString("firstPic"));
                         bean.setTitle(response.getJSONObject(i).getString("title"));
                         bean.setUrl(response.getJSONObject(i).getString("url"));
                         coursewareBeanList.add(bean);
                     }
                     if(coursewareBeanList.size()>0){
                         courseware_text.setVisibility(View.VISIBLE);
                         mCourSewareAdapter.notifyDataSetChanged();
                     }else{
                         courseware_text.setVisibility(View.GONE);
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }
         });
        getHomeIconInfo();

        getHomeAssistant();

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
         zk_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(z){
                    z=!z;
                    getAgainHomeIcon(z);
                    zk.setImageResource(R.drawable.zk);
                }else{
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mLlConstainer.setLayoutParams(lp);
                    z=!z;
                    getAgainHomeIcon(z);
                    zk.setImageResource(R.drawable.sq);
                }
            }
        });
         mMarqueeView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.e("GYW",mMarqueeView.getDisplayedChild()+"");
             }
         });
         mCourSewareAdapter = new CourSewareAdapter(coursewareBeanList,getActivity(),getActivity().getWindowManager().getDefaultDisplay().getWidth()/2);
         mRecyclerView.setAdapter(mCourSewareAdapter);

         mCourSewareAdapter.SetOnItemClickListener(new CourSewareAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(View view, int position) {
                 CoursewareBean bean = coursewareBeanList.get(position);
                 String url = bean.getUrl();
                 if(url.contains("?")) {
                     url = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode()+"&fromWhere="+ Constants.PROJECT_NAME;
                 }else {
                     url = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode()+"&fromWhere="+ Constants.PROJECT_NAME;
                 }
                 CollegeActivity.startCitCollegeActivity(getActivity(), bean.getTitle(), url);
             }
         });

         initReceiver();
         if(!AppApplication.isUserLogIn()){
             hideSecretaryView();
         }
        return view;
    }
    private TextView getTextViewTitle(String title) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.home_xxview, null);
        textView.setText(title);
        return textView;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            mMarqueeView.stopFlipping();
            mMarqueeView.removeAllViews();
        }else{
            for (int i = 0;i<textList.size();i++){
                mMarqueeView.addView(getTextViewTitle(textList.get(i)));
            }
            mMarqueeView.startFlipping();
        }
   /* private int index = 0;
    index = mMarqueeView.getDisplayedChild();
    mMarqueeView.setDisplayedChild(index);*/
    }


    private void getHomeAssistant() {
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
        if (AppApplication.facultyId != -1 && Constants.IS_SECRETARY_SHOW) {
            mSecretaryView.setVisibility(View.VISIBLE);
        } else {
            mSecretaryView.setVisibility(View.VISIBLE);
        }
    }

    private boolean z = false;
    private void getAgainHomeIcon(boolean zk) {
         mLlConstainer.removeAllViews();
        if(zk){
            for (int i = 1;i<mRow.getRows().size();i++) {
                Row.RowsBean bean = mRow.getRows().get(i);
                LinearLayout linearLayout =new LinearLayout(getActivity());;
                //linearLayout = getHorizontalLinearLayout(true);
                if(i == mRow.getRows().size()-1){
                    linearLayout = getHorizontalLinearLayout(false);
                }else{
                    linearLayout = getHorizontalLinearLayout(true);
                }
                for (int j = 0; j < bean.getObj().size(); j++) {
                    Row.RowsBean.ObjBean rowBean = bean.getObj().get(j);
                    LinearLayout innerLinearLayout = getInnerLinearLayout(Float.parseFloat(rowBean.getWidth().replace("%", "")));
                    innerLinearLayout.addView(addTextAndImage(rowBean, Float.parseFloat(rowBean.getWidth().replace("%", "")), innerLinearLayout));
                    innerLinearLayout.setTag(rowBean);
                    innerLinearLayout.setOnClickListener(this);
                    linearLayout.addView(innerLinearLayout);
                }
                mLlConstainer.addView(linearLayout);
            }
        }else{
            for (int i = 1;i<3;i++) {
                Row.RowsBean bean = mRow.getRows().get(i);
                LinearLayout linearLayout ;
                //linearLayout = getHorizontalLinearLayout(true);
                if(i == 2){
                    linearLayout = getHorizontalLinearLayout(false);
                }else{
                    linearLayout = getHorizontalLinearLayout(true);
                }
                for (int j = 0; j < bean.getObj().size(); j++) {
                    Row.RowsBean.ObjBean rowBean = bean.getObj().get(j);
                    LinearLayout innerLinearLayout = getInnerLinearLayout(Float.parseFloat(rowBean.getWidth().replace("%", "")));
                    innerLinearLayout.addView(addTextAndImage(rowBean, Float.parseFloat(rowBean.getWidth().replace("%", "")), innerLinearLayout));
                    innerLinearLayout.setTag(rowBean);
                    innerLinearLayout.setOnClickListener(this);
                    linearLayout.addView(innerLinearLayout);
                }
                mLlConstainer.addView(linearLayout);
            }
        }
    }
    private void getHomeIconInfo() {
        try {
            JSONObject obj = new JSONObject(readFileSdcardFile(mIconFilePath));

            Gson gson = new Gson();
            String iconSort = obj.getString("iconSort");
            mRow = gson.fromJson(iconSort, Row.class);
            int hegit = 0;
            for (int i = 0; i < mRow.getRows().size(); i++) {
                Row.RowsBean bean = mRow.getRows().get(i);
                LinearLayout linearLayout;
                if(i == 2){
                    linearLayout = getHorizontalLinearLayout(false);
                }else{
                    linearLayout = getHorizontalLinearLayout(true);
                }
                //设置首页上方广告
                if (bean.getObj().size() == 1 && bean.getObj().get(0).getIconCode().equals(17 + "")) {
                    final Row.RowsBean.ObjBean picBean = bean.getObj().get(0);
                    Glide.with(getActivity()).load(picBean.getIconUrl()).placeholder(R.drawable.default_load_bg).into(mTopADImg);
                    mTopADImg.setOnClickListener(new View.OnClickListener() {
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
                    String imgSize = picBean.getImgSize();
                    String[] wh = imgSize.split(",");
                    int width =  Integer.parseInt(wh[0]);
                    double height =  Double.parseDouble(wh[1]);
                    double bl = width/height;
                    int widthMax = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                    ViewGroup.LayoutParams lp=mTopADImg.getLayoutParams();
                    lp.height= (int) (widthMax/bl);
                    mTopADImg.setLayoutParams(lp);
                } else {
                    if(i!= 0){
                        if(i==3){
                            break;
                        }
                        for (int j = 0; j < bean.getObj().size(); j++) {
                            Row.RowsBean.ObjBean rowBean = bean.getObj().get(j);
                            LinearLayout innerLinearLayout = getInnerLinearLayout(Float.parseFloat(rowBean.getWidth().replace("%", "")));
                            innerLinearLayout.addView(addTextAndImage(rowBean, Float.parseFloat(rowBean.getWidth().replace("%", "")), innerLinearLayout));
                            innerLinearLayout.setTag(rowBean);
                            innerLinearLayout.setOnClickListener(this);
                            linearLayout.addView(innerLinearLayout);
                        }
                        mLlConstainer.addView(linearLayout);
                    }
                }
            }
            mHandler.sendEmptyMessageDelayed(1,1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private LinearLayout getInnerLinearLayout(float weight) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        if(weight == 50) {
            weight = (float)(weight + 2.4);
        }
        lp.weight = weight;
        int spMarginBottom = DensityUtil.dip2px(getActivity(), 3.5f);
        lp.setMargins(spMarginBottom, 0, spMarginBottom, 0);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        return linearLayout;
    }
    /**
     * @return
     */
    private LinearLayout getHorizontalLinearLayout(boolean have) {
        LinearLayout linearLayout = new LinearLayout(getActivity());

        int spWidth = DensityUtil.dip2px(getActivity(), 110);
        int spMarginBottom = DensityUtil.dip2px(getActivity(), 6);
        int spPadding = DensityUtil.dip2px(getActivity(), 5);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spWidth);
        if(have){
            lp.setMargins(0, 0, 0, spMarginBottom);
        }
        linearLayout.setLayoutParams(lp);
        //linearLayout.setPadding(spPadding, 0, spPadding, 0);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));

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
                //AppApplication.applyFont(getActivity(), view, "fonts/cg.TTF");
                if (AppApplication.systemLanguage == 1) {
                    tvName.setText(bean.getIconName());
                } else {
                    tvName.setText(bean.getIconEnName());
                }
                tvName.setTextColor(Color.parseColor(bean.getIconFontColor()));
                if (TextUtils.isEmpty(bean.getIconUrl())) {
                    ivLogo.setImageResource(iconDefaultId);
                    //ivLogo.setImageDrawable(ImageColorChangeUtils.changeIconColor(getActivity(), iconDefaultId, Color.parseColor(bean.getIconFontColor())));
                } else {
                    Glide.with(getActivity()).load(bean.getIconUrl()).placeholder(R.drawable.default_load_bg).into(ivLogo);
                }
                return view;
            } else {
                //纵向布局
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.merge_vertical_text_image, parentView, false);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                ImageView ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
                TextView tvMsgNum = (TextView) view.findViewById(R.id.tv_msg_nums);
                ImageView ivInteractive = (ImageView) view.findViewById(R.id.iv_hd_session_on);

                //AppApplication.applyFont(getActivity(), view, "fonts/cg.TTF");
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
                    Glide.with(getActivity()).load(bean.getIconUrl()).placeholder(R.drawable.default_load_bg).into(ivLogo);
                }
                return view;
            }
        } else {
            ToastUtils.showShorToast("iconCode解析出错...");
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
            case VENUEPICTURE:
                return R.drawable.home_icon_venuepic;

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
                if(url.contains("?")) {
                    url = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode()+"&fromWhere="+ Constants.PROJECT_NAME+"&conId="+ AppApplication.conId;
                }else {
                    url = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode()+"&fromWhere="+ Constants.PROJECT_NAME+"&conId="+ AppApplication.conId;
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
                        if(AppApplication.systemLanguage == 1){
                            goLookSchedule(rowBean.getIconName());
                        }else{
                            goLookSchedule(rowBean.getIconEnName());
                        }
                        break;
                    case SEARCH:
                        //差日程
                        if(AppApplication.systemLanguage == 1){
                            goSearchSchedule(rowBean.getIconName());
                        }else{
                            goSearchSchedule(rowBean.getIconEnName());
                        }
                        break;
                    case MY_AGENDA:
                        //我的日程
                        if(AppApplication.systemLanguage == 1){
                            goMySchedule(rowBean.getIconName());
                        }else{
                            goMySchedule(rowBean.getIconEnName());
                        }
                        break;
                    case INFO_RELEASE:
//                        Intent social = new Intent(getActivity(), SocialActivity.class);
//                        startActivity(social);
                        //现场秀
                        if(AppApplication.systemLanguage == 1){
                            goScenicXiu(rowBean.getIconName());
                        }else{
                            goScenicXiu(rowBean.getIconEnName());
                        }
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
                        if(AppApplication.systemLanguage == 1){
                            goGuide(rowBean.getIconName());
                        }else{
                            goGuide(rowBean.getIconEnName());
                        }
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
                        if(AppApplication.systemLanguage == 1){
                            goExhibitor(rowBean.getIconName());
                        }else{
                            goExhibitor(rowBean.getIconEnName());
                        }
                        break;
                    case PERSONAL:
                        //我
                        goPersonCenter();
                        break;
                    case FACULTY_INDEX:
                        //讲者检索
                        if(AppApplication.systemLanguage == 1){
                            goSpeakerSearch(rowBean.getIconName());
                        }else{
                            goSpeakerSearch(rowBean.getIconEnName());
                        }
                        break;
                    case INTERACTIVE:
                        if(AppApplication.systemLanguage == 1){
                            goQuestionAndA(rowBean.getIconName());
                        }else{
                            goQuestionAndA(rowBean.getIconEnName());
                        }
                        break;
                    case POSTER:
                        if(AppApplication.systemLanguage == 1){
                            goPost(rowBean.getIconName());
                        }else{
                            goPost(rowBean.getIconEnName());
                        }
                        break;
                    case FACULTY:
                        goSecretary();
                        break;
                    case PICTURE:
                        if (!TextUtils.isEmpty(rowBean.getNewUrl())) {
                            if (AppApplication.systemLanguage == 1) {
                                CollegeActivity.startCitCollegeActivity(getActivity(),  rowBean.getIconName(), rowBean.getNewUrl());
                            } else {
                                CollegeActivity.startCitCollegeActivity(getActivity(), rowBean.getIconEnName(), rowBean.getNewUrl());
                            }
                        }
                        break;
                    case QUESTION:
                        if(AppApplication.systemLanguage == 1){
                            goQuestions(rowBean.getIconName());
                        }else{
                            goQuestions(rowBean.getIconEnName());
                        }
                        break;
                    case SCANE:
                        goScane();
                        break;
                    case NOW:
                        if(AppApplication.systemLanguage == 1){
                            goNow(rowBean.getIconName());
                        }else{
                            goNow(rowBean.getIconEnName());
                        }
                        break;
                    case NEXT:
                        if(AppApplication.systemLanguage == 1){
                            goNext(rowBean.getIconName());
                        }else{
                            goNext(rowBean.getIconEnName());
                        }
                        break;
                    case BUS:
                        if(AppApplication.systemLanguage == 1){
                            goBus(rowBean.getIconName());
                        }else{
                            goBus(rowBean.getIconEnName());
                        }
                        break;
                    case PHOTOWALL:
                        if(AppApplication.systemLanguage == 1){
                            goPhotoAlbum(rowBean.getIconName());
                        }else{
                            goPhotoAlbum(rowBean.getIconEnName());
                        }
                        break;
                    case VENUEPICTURE:
                        if(AppApplication.systemLanguage == 1){
                            goVenuepicture(rowBean.getIconName());
                        }else{
                            goVenuepicture(rowBean.getIconEnName());
                        }
                        break;
                    default:
                        ToastUtils.showShorToast("未找到该模块，请尝试更新数据包");
                        break;
                }
            }
        }

    }
    //=======================================================================模块跳转=======================================================================

    /**
     * 看日程
     */
    private void goLookSchedule(String title) {
        MeetingScheduleListActionFragment listFragment = new MeetingScheduleListActionFragment();
        ImageView view = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
        if (AppApplication.systemLanguage == 1) {
            view.setImageResource(R.drawable.schedule_switch_cn);
        } else {
            view.setImageResource(R.drawable.schedule_switch);
        }

        listFragment.setRightListener(view);
        action(listFragment, title, view, false, false, false);


    }

    /**
     * 查日程(包含讲者检索)
     */
    private void goSearchSchedule(String title) {
        ImageView searchView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
        searchView.setImageResource(R.drawable.search);
        NewSearchScheduleActionFragment searchFragment = new NewSearchScheduleActionFragment();
        searchFragment.setRightView(searchView);
        /*View titleView = CommonUtils.initView(getActivity(), R.layout.title_segment);
        searchFragment.setCenterView(titleView);*/
        //goQuestions("提问");
        action(searchFragment, title, searchView, false, false, false);
    }

    /**
     * 我的日程
     */
    private void goMySchedule(String title) {
        TextView my_schedule = (TextView) CommonUtils.initView(getActivity(), R.layout.title_right_textview);
        my_schedule.setText(R.string.my_schedule_edit);
        MyScheduleActionFragment schedule = new MyScheduleActionFragment();
        schedule.setRightView(my_schedule);
        action(schedule, title, my_schedule, false, false, false);
    }

    /**
     * 现场秀
     */
    private void goScenicXiu(String title) {
        View scenicTitle = CommonUtils.initView(this.getActivity(), R.layout.scenic_xiu_title);
        LinearLayout mlayout = (LinearLayout) scenicTitle.findViewById(R.id.ll_senic_xiu_title);
        ScenicXiuFragment xiu = new ScenicXiuFragment();
        xiu.setScenicXiuTitle(mlayout);
        action(xiu, title, scenicTitle, false, false, false);
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
    private void goNow(String title) {
        NowFragment now = new NowFragment();
        action(now, title, false, false, false);
    }

    /**
     * 即将进行
     */
    private void goNext(String title) {
        NextFragment next = new NextFragment();
        action(next, title, false, false, false);
    }
    /**
     * 专家秘书
     */
    private void goSecretary() {
        CHYHttpClientUsage.getInstanse().doGetSceneShowQuestions(AppApplication.conId + "", AppApplication.userId + "", "-1", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler("gbk") {
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
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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

            }
        });
        new TaskAsyncTask().execute();
    }

    /**
     * 参会指南
     */
    private void goGuide(String title) {
        if(AppApplication.instance().NetWorkIsOpen()){
            ImageView searchView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
            searchView.setImageResource(R.drawable.icon_share);
            //searchView.setVisibility(View.GONE);
            OnlyWebViewActionFragment fragment = OnlyWebViewActionFragment.getInstance(getActivity().getString(Constants.get_MEETING_GUIDE(), AppApplication.conId, AppApplication.getSystemLanuageCode()));
            fragment.setRightView(searchView);
            action(fragment, R.string.home_meetingguide, searchView,false, false, false);
        }else{
            Toast.makeText(getActivity(),R.string.nowifi,Toast.LENGTH_SHORT).show();
        }
    }
    private void goVenuepicture(String title){
        action(new NewMeetingInfoFragment(), title , false, false, false);
    }

    /**
     * 现场互动
     */
    private void goQuestionAndA(String title) {
        if(AppApplication.isUserLogIn()){
            HdSessionActionFragment hdFragment = new HdSessionActionFragment();
            View hdTtile = CommonUtils.initView(this.getActivity(), R.layout.title_hdsession);
            hdFragment.setRightListener(hdTtile);
            action(hdFragment, title, hdTtile, false, false, false);
        }else{
            LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
        }
    }

    /**
     * 提问模块
     */
    private void goQuestions(String title) {
        if(AppApplication.isUserLogIn()) {
            action(new MeetingQuestionFragment(), title, false, false, false);
        }else {
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
                getString(Constants.get_HANDS_ON_SITE(), AppApplication.conId, lan2, AppApplication.userId, AppApplication.userType));

    }

    /**
     * 壁报
     */
    private void goPost(String title) {
        View scane = CommonUtils.initView(this.getActivity(), R.layout.title_right_image);
        ((ImageView) scane).setImageResource(R.drawable.scane_scane);
        PosterFragment post = new PosterFragment();
        post.setRightView(scane);
        action(post, title, scane, false, false, false);
    }

    /**
     * 参展商
     */
    private void goExhibitor(String title) {
        action(new ExhibitorsActionFragment(), title, false, false, false);
    }
    /**
     *照片墙
     */
    private void goPhotoAlbum(String title) {
        action(new PhotoAlbumFragment(), title, false,false, false);
    }
    /**
     * 班车
     */
    private void goBus(String title) {
        action(new MeetingBusRemindAllFragment(),  title, false, false, false);
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
        CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.home_cit_college), getString(Constants.get_CIT_COLLEGE(), AppApplication.conId, AppApplication.getSystemLanuageCode(), AppApplication.userId, AppApplication.userType));
    }

    /**
     * 讲者检索
     */
    private void goSpeakerSearch(String title) {
        action(SpeakerSearchFragment.getInstance(SpeakerSearchFragment.TYPE_FROM_HOME), title, false, false, false);
    }

    /**
     * 扫一扫
     */
    private void goScane() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        getActivity().startActivityForResult(intent, HomeActivity.REQUEST_SCANE);
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
                ToastUtils.showShorToast(R.string.secretary_module_not_available);
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
    private int mSPLayoutHeight = 0;
    private int mZKLayoutHeight = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                        z=!z;
                        getAgainHomeIcon(z);
                        zk.setImageResource(R.drawable.sq);
                    mZKLayoutHeight = mLlConstainer.getHeight();
                    mHandler.sendEmptyMessageDelayed(3,1500);
                    mHandler.sendEmptyMessageDelayed(2,3000);
                    break;
                case 2:
                        z=!z;
                        getAgainHomeIcon(z);
                        zk.setImageResource(R.drawable.zk);
                    break;
                case 3:
                    int mLayoutHeight = mLlConstainer.getHeight();
                    Log.d("sgqTest", "mLayoutHeight: "+mLayoutHeight);
                    ValueAnimator valueAnimator = createDropAnimator(mLlConstainer, mLayoutHeight, mZKLayoutHeight);
                    valueAnimator.setDuration(1500);
                    valueAnimator.start();
                    break;
            }
        }
    };
    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
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
