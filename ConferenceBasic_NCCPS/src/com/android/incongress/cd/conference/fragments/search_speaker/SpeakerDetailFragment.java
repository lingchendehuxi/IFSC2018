package com.android.incongress.cd.conference.fragments.search_speaker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.SpeakerDetailAdapter;
import com.android.incongress.cd.conference.adapters.SpeakerDetailDayAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersListView;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/7.
 * 讲者详情页面
 */
public class SpeakerDetailFragment extends BaseFragment {
    private StickyListHeadersListView mStickLVSpeaker;
    private TextView mTvSpeakerName, mTvSpeakerInfo, mTvSpeakerWoke, mTvSpeakerDay;
    private SpeakerDetailAdapter mSpeakerAdapter;
    private SpeakerDetailDayAdapter mSpeakerDayAdapter;
    private List<MeetingBean_new> mMeetings;
    private List<Role> mRoleList = new ArrayList<>();
    private ImageView mIvSpeaker;

    private String mSpeakerName, mSpeakerRoles, mTrueRoleNames = "", mSpeakerNameEn, mImg;
    private boolean mIsFromSessionDetai = false;

    private int mSpeakerId;
    private View mHeaderView;
    private View mEmptyView;

    public SpeakerDetailFragment() {
    }

    public void setArguments(int speakerId, String speakerName, String speakerEnName, String speakerRoles, String img, List<MeetingBean_new> meetings, boolean isFromSessionDetai) {
        this.mSpeakerId = speakerId;
        this.mSpeakerName = speakerName;
        this.mSpeakerRoles = speakerRoles;
        this.mMeetings = meetings;
        this.mSpeakerNameEn = speakerEnName;
        this.mIsFromSessionDetai = isFromSessionDetai;
        this.mImg = img;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_detail_new, container, false);
        mStickLVSpeaker = (StickyListHeadersListView) view.findViewById(R.id.slhlv_speaker);
        mEmptyView = view.findViewById(R.id.tv_empty);

        mStickLVSpeaker.setEmptyView(mEmptyView);
        mStickLVSpeaker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mIsFromSessionDetai && position != 0) {
                    int sessionGroupId = mMeetings.get(position - 1).getSessionGroupId();

                    SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                    detail.setArguments(getSessionPosition(sessionGroupId), mSessionList);
                    action(detail, R.string.meeting_schedule_detail_title, false, false, false);
                }
            }
        });

        mHeaderView = inflater.inflate(R.layout.header_view_speaker_detail, null);
        mStickLVSpeaker.addHeaderView(mHeaderView);
        mTvSpeakerName = (TextView) mHeaderView.findViewById(R.id.tv_speaker_name);
        mTvSpeakerInfo = (TextView) mHeaderView.findViewById(R.id.tv_speaker_info);
        mTvSpeakerInfo.setVisibility(Constants.SCHEDULE_SPEAKER_INFO ? View.VISIBLE : View.GONE);
        mTvSpeakerDay = (TextView) mHeaderView.findViewById(R.id.tv_speaker_detail_day);
        mTvSpeakerWoke = (TextView) mHeaderView.findViewById(R.id.tv_speaker_detail_woke);
        mIvSpeaker = (ImageView) mHeaderView.findViewById(R.id.iv_speaker);

        if (mMeetings.size() != 0) {
            new MyAsyncTask().execute();
        } else {
            ToastUtils.showShorToast(getString(R.string.faculty_no_assignments));

        }

        mTvSpeakerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewContainerActivity.startWebViewContainerActivity(getActivity(), getString(R.string.secretary_info_url, Constants.getConId() + "", mSpeakerId + "", AppApplication.getSystemLanuageCode()), mSpeakerName);
            }
        });

        if (!StringUtils.isEmpty(mImg))
            PicUtils.loadImageUrl(getContext(), mImg, mIvSpeaker);

        return view;
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (AppApplication.systemLanguage == 1) {
                mTvSpeakerName.setText(mSpeakerName);
            } else {
                mTvSpeakerName.setText(mSpeakerNameEn);
            }

            mStickLVSpeaker.setAdapter(mSpeakerDayAdapter);
            mTvSpeakerDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*mStickLVSpeaker.removeAllViews();
                    mStickLVSpeaker.setEmptyView(mEmptyView);
                    mStickLVSpeaker.addHeaderView(mHeaderView);*/
                    mTvSpeakerDay.setTextColor(getResources().getColor(R.color.white));
                    mTvSpeakerDay.setBackgroundDrawable(getResources().getDrawable(R.drawable.detail_yes));
                    mTvSpeakerWoke.setTextColor(getResources().getColor(R.color.gray));
                    mTvSpeakerWoke.setBackgroundDrawable(getResources().getDrawable(R.drawable.detail_no));
                    mSpeakerDayAdapter = new SpeakerDetailDayAdapter(getActivity(), mMeetings, mIsFromSessionDetai);
                    mStickLVSpeaker.setAdapter(mSpeakerDayAdapter);
                }
            });
            mTvSpeakerWoke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*mStickLVSpeaker.removeAllViews();
                    mStickLVSpeaker.setEmptyView(mEmptyView);
                    mStickLVSpeaker.addHeaderView(mHeaderView);*/
                    mTvSpeakerWoke.setTextColor(getResources().getColor(R.color.white));
                    mTvSpeakerWoke.setBackgroundDrawable(getResources().getDrawable(R.drawable.detail_yes));
                    mTvSpeakerDay.setTextColor(getResources().getColor(R.color.gray));
                    mTvSpeakerDay.setBackgroundDrawable(getResources().getDrawable(R.drawable.detail_no));
                    mSpeakerAdapter = new SpeakerDetailAdapter(getActivity(), mMeetings, mIsFromSessionDetai);
                    mStickLVSpeaker.setAdapter(mSpeakerAdapter);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            mSpeakerDayAdapter = new SpeakerDetailDayAdapter(getActivity(), mMeetings, mIsFromSessionDetai);

            mSessionList.addAll(ConferenceDbUtils.getAllSession());
            mRoleList.addAll(ConferenceDbUtils.getAllRoles());
            return null;
        }
    }

    public void setRightView(View view) {
        view.setVisibility(View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShorToast("这里要去掉这个提问功能");
//                if (AppApplication.userType== Constants.TYPE_USER_VISITOR) {
////                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "" , "");
//                    ChooseIdentityActivity.startChooseIdentityActivity(getActivity());
//                    return;
//                }
//                MakeQuestionFragment fragment = null;
//                if (AppApplication.systemLanguage == 1) {
//                    fragment = MakeQuestionFragment.getInstance(mSpeakerName, mSpeakerId, "");
//                } else {
//                    fragment = MakeQuestionFragment.getInstance(mSpeakerNameEn, mSpeakerId, "");
//                }
//                View question = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
//                fragment.setRightListener(question);
//                action(fragment, getString(R.string.ask_sb, mSpeakerName), question, false, false, false);
            }
        });
    }

    private List<Session> mSessionList = new ArrayList<>();

    /**
     * 获取Meeting在session中的位置
     *
     * @param sessionGroupId
     * @return
     */
    private int getSessionPosition(int sessionGroupId) {
        for (int i = 0; i < mSessionList.size(); i++) {
            Session bean = mSessionList.get(i);
            if (bean.getSessionGroupId() == sessionGroupId) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SPEAKERDETAIL);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SPEAKERDETAIL);
    }
}
