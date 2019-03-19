package com.android.incongress.cd.conference.fragments.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.AskQuestionAdapter;
import com.android.incongress.cd.conference.adapters.MyMeetingQuestionsAdapter;
import com.android.incongress.cd.conference.adapters.QuestionsSquarAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AskPupBean;
import com.android.incongress.cd.conference.beans.AskQuestionListBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.beans.MyMeetingQuestion;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListActionFragment;
import com.android.incongress.cd.conference.fragments.wall_poster.PosterFragment;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.popup.QuestionPopupWindow;
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
 * Created by Jacky on 2016/12/22.
 */

public class QuestionSquarFragment extends BaseFragment implements View.OnClickListener {
    private XRecyclerView mRVQuestion;
    private View emptyView;
    private TextView fawen;
    private MyMeetingQuestion mAllQuestions;
    private QuestionsSquarAdapter mQuestionAdapter;
    private QuestionPopupWindow mQuestionPopupWindow;
    private int currentPosition;
    List<AskPupBean> listBean = new ArrayList<>();
    private LinearLayout ll_question;
    private List<MyMeetingQuestion.SceneShowArray2Bean> myList = new ArrayList<>();
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.fragment_squar_question, container, false);
        mRVQuestion = view.findViewById(R.id.rv_question);
        fawen = view.findViewById(R.id.tv_question);
        emptyView = view.findViewById(R.id.tv_tips);
        ll_question = view.findViewById(R.id.ll_question);

        fawen.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRVQuestion.setLayoutManager(layoutManager);

        mRVQuestion.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRVQuestion.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRVQuestion.setArrowImageView(R.drawable.iconfont_downgrey);
        mQuestionAdapter = new QuestionsSquarAdapter(getActivity(), myList);
        mRVQuestion.setAdapter(mQuestionAdapter);
        mRVQuestion.setLoadingMoreEnabled(false);
        mRVQuestion.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshQuestionData();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mRVQuestion.setRefreshing(true);
        return view;
    }

    /**
     * 只有下拉刷新
     */
    private void refreshQuestionData() {
        CHYHttpClientUsage.getInstanse().doGetQuestionsBySessionV2(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    myList.clear();
                    mAllQuestions = new Gson().fromJson(response.toString(), new TypeToken<MyMeetingQuestion>() {
                    }.getType());
                    myList.addAll(mAllQuestions.getSceneShowArray2()) ;
                    mQuestionAdapter.notifyDataSetChanged();
                    if(myList.size()<=0){
                        mRVQuestion.setEmptyView(emptyView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mRVQuestion.refreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_question:
                if (listBean.size() == 0) {
                    AskPupBean bean = new AskPupBean();
                    bean.setFirName("日程提问");
                    bean.setTopic("对大会日程进行提问，即将前往日程模块");
                    AskPupBean bean2 = new AskPupBean();
                    bean2.setFirName("壁报提问");
                    bean2.setTopic("对壁报内容进行提问，即将前往日程模块");
                    listBean.add(bean);
                    listBean.add(bean2);
                }
                ll_question.setVisibility(View.GONE);
                InitQuestionPopupWindow(listBean);
                break;

        }
    }

    private void InitQuestionPopupWindow(final List<AskPupBean> list) {
        if (list.size() == 0) {
            ToastUtils.showToast("暂无数据");
            return;
        }
        mQuestionPopupWindow = new QuestionPopupWindow(getActivity());
        mQuestionPopupWindow.setAnimationStyle(R.style.icon_popup_window_no_down);
        mQuestionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ll_question.setVisibility(View.VISIBLE);
                lightOn();
            }
        });
        ListView listView = mQuestionPopupWindow.getContentView().findViewById(R.id.list_scroll);
        final AskQuestionAdapter askQuestionAdapter = new AskQuestionAdapter(list, getActivity());
        listView.setAdapter(askQuestionAdapter);
        int totalHeight = 0;
        for (int i = 0; i < askQuestionAdapter.getCount(); i++) {
            View listItem = askQuestionAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }
        if (totalHeight > (DensityUtil.getScreenSize(getActivity())[1] * 0.35)) {
            totalHeight = (int) (DensityUtil.getScreenSize(getActivity())[1] * 0.35);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButton radioButton = view.findViewById(R.id.question_rb);
                askQuestionAdapter.clearCheck();
                radioButton.setChecked(true);
                currentPosition = position+10;
            }
        });
        mQuestionPopupWindow.getContentView().findViewById(R.id.rb_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPosition == 10){
                    MeetingScheduleListActionFragment listFragment = new MeetingScheduleListActionFragment();
                    ImageView ImageView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
                    action(listFragment, ImageView);
                    mQuestionPopupWindow.dismiss();
                }else if(currentPosition == 11){
                    View scane = CommonUtils.initView(getContext(), R.layout.title_right_image);
                    ((ImageView) scane).setImageResource(R.drawable.scane_scane);
                    PosterFragment post = new PosterFragment();
                    action(post, getString(R.string.home_wallpaper), scane, false, false, false);
                    mQuestionPopupWindow.dismiss();
                }else{
                    ToastUtils.showToast("请先选择提问模块");
                }
            }
        });
        mQuestionPopupWindow.getContentView().findViewById(R.id.rb_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 0;
                mQuestionPopupWindow.dismiss();
            }
        });
        mQuestionPopupWindow.showAtLocation(fawen, Gravity.TOP, 0, (int) (DensityUtil.getScreenSize(getActivity())[1] * 0.4));
        lightOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        MobclickAgent.onPageStart(Constants.FRAGMENT_QUESTIONLIST);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_QUESTIONLIST);
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
