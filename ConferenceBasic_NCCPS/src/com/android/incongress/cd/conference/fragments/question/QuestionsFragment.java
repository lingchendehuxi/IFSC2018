package com.android.incongress.cd.conference.fragments.question;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.QuestionsAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.wall_poster.PosterFragment;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Jacky on 2016/12/22.
 * 提问页面
 */

public class QuestionsFragment extends BaseFragment implements View.OnClickListener{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private QuestionsAdapter mAdapter;
    private TextView mTvQuestion;

    private int mCurrentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTvQuestion = (TextView) view.findViewById(R.id.tv_question);

        mAdapter = new QuestionsAdapter(getChildFragmentManager(), getActivity(),0,0);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentPosition = tab.getPosition();
                mViewPager.setCurrentItem(mCurrentPosition, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        mTvQuestion.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int targetId = v.getId();
        if(targetId == R.id.tv_question) {
            if(mCurrentPosition == 0) {
                View view = CommonUtils.initView(getActivity(), R.layout.title_right_image);
                ((ImageView)view).setImageResource(R.drawable.room_down);
                QuestionByMeetingOrPoster questionByMeetingOrPoster = new QuestionByMeetingOrPoster();
                questionByMeetingOrPoster.setRightView(view);
                action(questionByMeetingOrPoster, R.string.make_question, view, false, false, false);
            }else {
                PosterFragment fragment = new PosterFragment();
                View rightView = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                TextView tvRight = (TextView)rightView.findViewById(R.id.tv_right);
                tvRight.setText(R.string.scan_right_tips);

                Drawable drawable = getActivity().getResources().getDrawable(R.drawable.scane_scane);
                drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvRight.setCompoundDrawables(null,null,drawable,null);
                action(fragment, R.string.home_wallpaper,rightView, false, false, false);
            }
        }
    }

    public void setRightViewListener(View view) {
        if(view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShorToast("我要进行文字搜索");
                }
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_QUESTIONLIST);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_QUESTIONLIST);
    }
}
