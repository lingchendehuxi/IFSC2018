package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.MeetingScheduleAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.NewDynamicHomeFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.ActivityUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.TimeUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.ScrollControlViewpager;
import com.flyco.tablayout.SlidingTabLayout;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 会议日程模块
 *
 * @author Jacky_Chen
 * @time 2014年11月25日
 */
public class MeetingScheduleViewPageFragment extends BaseFragment {

    private MeetingScheduleAdapter adapter;
    private LinearLayout ll_tabLayout;
    private ScrollControlViewpager mViewpager;
    private CharSequence Titles[];
    private List<Fragment> fragmentList = new ArrayList<>();

    private List<String> mSessionDaysList = new ArrayList<>();
    private List<Class> mRoomList = new ArrayList<>();
    private ProgressDialog mDialog;
    private TextView mTvTips;
    private ImageView close;
    private int mCurrentPage = 0;

    public MeetingScheduleViewPageFragment getInstance() {
        MeetingScheduleViewPageFragment meetingSchedule = new MeetingScheduleViewPageFragment();
        return meetingSchedule;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_meeting_schedule, null, false);
        ll_tabLayout = view.findViewById(R.id.ll_tabLayout);
        mViewpager = (ScrollControlViewpager) view.findViewById(R.id.scroll_pager);
        close = (ImageView) view.findViewById(R.id.close);
        mViewpager.setCanScroll(false);
        mViewpager.setOffscreenPageLimit(4);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);
        mTvTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0f);
                alphaAnimation.setDuration(300);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mTvTips.setVisibility(View.GONE);
                        SharePreferenceUtils.saveAppBoolean(Constants.LOOK_SCHEDULE_TIPS, true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mTvTips.startAnimation(alphaAnimation);
            }
        });

        if(SharePreferenceUtils.getAppBoolean(Constants.LOOK_SCHEDULE_TIPS,false)) {
            mTvTips.setVisibility(View.GONE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            HomeActivity activity = (HomeActivity) getActivity();
            FragmentManager manager = activity.getSupportFragmentManager();
            @Override
            public void onClick(View v) {
                manager.popBackStackImmediate();
                activity.getmTitleEntries().pop();
                activity.setTitleBar(activity.getmTitleEntries().peek());
            }
        });

        new MyAsyncTask().execute();
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        /**
         * 这里的Integer参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
         * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
         * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
         */
        @Override
        protected String doInBackground(Integer... params) {
            mSessionDaysList.clear();
            List<Session> allSession = ConferenceDbUtils.getAllSession();
            for (int i = 0; i < allSession.size(); i++) {
                Session session = allSession.get(i);
                if(mSessionDaysList.size()>0) {
                    if(!(mSessionDaysList.get(mSessionDaysList.size()-1).equals(session.getSessionDay()))) {
                        mSessionDaysList.add(session.getSessionDay());
                    }
                }else {
                    mSessionDaysList.add(session.getSessionDay());
                }
            }

            Titles = new CharSequence[mSessionDaysList.size()];
            for (int i = 0; i < mSessionDaysList.size(); i++) {
                Titles[i] = mSessionDaysList.get(i);
                if(TimeUtils.getCurrentTimeMD().equals(mSessionDaysList.get(i))){
                    mCurrentPage = i;
                }
            }

            mRoomList.addAll(ConferenceDbUtils.getAllClasses());//获取会议室
            return "";
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();
            final List<TextView> list = new ArrayList<>();
            for(int i =0;i<Titles.length;i++){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.schedule_table_item,null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((DensityUtil.getScreenSize(getActivity())[0]-DensityUtil.dip2px(getActivity(),50f))/4, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout ll_tabItem = view.findViewById(R.id.ll_tabItem);
                final TextView textView = view.findViewById(R.id.tv_title);
                String[] strings = Titles[i].toString().split("-");
                String title;
                if(strings.length!=3){
                    ToastUtils.showToast("时间格式错误");
                    return;
                }else {
                    title =  Integer.valueOf(strings[1])+"月\n"+Integer.valueOf(strings[2])+"日";
                }
                textView.setText(title);
                textView.setTag(i);
                ll_tabItem.setLayoutParams(params);
                list.add(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int j = 0;j<list.size();j++){
                            list.get(j).setTextColor(getResources().getColor(R.color.white));
                            list.get(j).setBackground(getResources().getDrawable(R.drawable.bg_schedule_unselected));
                        }
                        textView.setTextColor(getResources().getColor(R.color.black));
                        textView.setBackground(getResources().getDrawable(R.drawable.bg_schedule_selected));
                        mViewpager.setCurrentItem((int)view.getTag());
                    }
                });
                ll_tabLayout.addView(view);
            }
            for(int i = 0;i<mSessionDaysList.size();i++){
                MeetingScheduleDetailActionFragment fragment = MeetingScheduleDetailActionFragment.getSingleScheduleFragmetn(Titles[i].toString(), (ArrayList<String>) mSessionDaysList);
                fragmentList.add(fragment);
            }
            adapter = new MeetingScheduleAdapter(getChildFragmentManager(), fragmentList);
            mViewpager.setAdapter(adapter);
            list.get(mCurrentPage).setTextColor(getResources().getColor(R.color.black));
            list.get(mCurrentPage).setBackground(getResources().getDrawable(R.drawable.bg_schedule_selected));
            mViewpager.setCurrentItem(mCurrentPage);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), null, "loading");
        }
    }

    public void setRightListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!= null) {
                    ((HomeActivity)getActivity()).performBackClick();
                }
            }
        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MeetingScheduleListActionFragment listFragment = new MeetingScheduleListActionFragment();
//                ImageView view = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
//                if(AppApplication.systemLanguage == 1) {
//                    view.setImageResource(R.drawable.schedule_switch_cn);
//                }else {
//                    view.setImageResource(R.drawable.schedule_switch);
//                }
//                listFragment.setRightListener(view);
//                action(listFragment, R.string.home_schedule, view, false, false, false);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MEETINGSCHEDULECALENDAR);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MEETINGSCHEDULECALENDAR);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            getActivity().getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
