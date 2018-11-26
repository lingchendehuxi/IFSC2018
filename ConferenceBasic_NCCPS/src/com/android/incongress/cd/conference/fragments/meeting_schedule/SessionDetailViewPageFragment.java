package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.adapters.MeetingYYAdapter;
import com.android.incongress.cd.conference.adapters.SessionDetailViewPagerAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Note;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.ScrollControlViewpager;
import com.android.incongress.cd.conference.widget.popup.NewIconChoosePopupWindow;
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
 * Created by Jacky on 2015/12/14.
 * 会议详情 目前有一个viewpager和页码标识
 */
public class SessionDetailViewPageFragment extends BaseFragment {
    private ScrollControlViewpager mViewPager;

    private RelativeLayout mSessionDetail;
    private View mRightView;
    private SessionDetailViewPagerAdapter mAdapter;
    private CharSequence[] Titles;
    private TextView mMeetingYY;
    private LinearLayout mMeetingLayout;
    private NewIconChoosePopupWindow mIconChoosePopupWindow;

    public SessionDetailViewPageFragment() {
    }

    private List<Session> mSessionBeanList = new ArrayList<>();

    //分享记笔记弹窗
    private PopupWindow mShareNotePopup;
    private SoundPool sPool;
    //总的页数
    private int mNumTabs = 0;
    public static int mPosition = 0;

    //页码信息
    private TextView mTvPageInfo;

    private List<SessionDetailPageFragment> mSessionDetailFragments = new ArrayList<>();

    public void setArguments(int position, List<Session> sessionBeanList) {
        this.mSessionBeanList = sessionBeanList;
        this.mPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_detail, null, false);
        mSessionDetail = (RelativeLayout) view.findViewById(R.id.session_detail_layout);
        mViewPager = (ScrollControlViewpager) view.findViewById(R.id.session_pager);
        mTvPageInfo = (TextView) view.findViewById(R.id.tv_page_info);
        mMeetingYY = (TextView) view.findViewById(R.id.meeting_yy);
        mMeetingLayout = (LinearLayout) view.findViewById(R.id.meeting_layout);
        mMeetingLayout.setVisibility(Constants.SCHEDULE_BOOK ? View.VISIBLE : View.GONE);
        mNumTabs = mSessionBeanList.size();
        Titles = new CharSequence[mSessionBeanList.size()];

        for (int i = 0; i < mSessionBeanList.size(); i++) {
            Titles[i] = mSessionBeanList.get(i).getSessionName();
        }

        for (int i = 0; i < mSessionBeanList.size(); i++) {
            Session session = mSessionBeanList.get(i);

            SessionDetailPageFragment fragment = SessionDetailPageFragment.getInstance(session.getSessionGroupId());
            fragment.setViewPager(mViewPager);
            mSessionDetailFragments.add(fragment);
        }
        sPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        final int music = sPool.load(getActivity(), R.raw.fy, 1);
        mAdapter = new SessionDetailViewPagerAdapter(getChildFragmentManager(), mSessionDetailFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mTvPageInfo.setText((mPosition + 1) + "/" + mNumTabs);
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvPageInfo.setText((1 + position) + "/" + mNumTabs);
                mPosition = position;
                sPool.play(music, 1, 1, 0, 0, 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    mMeetingLayout.setVisibility(View.GONE);
                } else {
                    mMeetingLayout.setVisibility(View.GONE);
                }
            }
        });

        mMeetingYY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppApplication.isUserLogIn()) {
                    newInitPopupWindow();
                    mIconChoosePopupWindow.showAtLocation(mSessionDetail, Gravity.BOTTOM, 0, 0);
                    lightOff();
                } else {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
                }
            }
        });
        return view;
    }

    private void newInitPopupWindow() {
        final List<Meeting> meetings = ConferenceDbUtils.getMeetingBySessionGroupId(mSessionBeanList.get(mPosition).getSessionGroupId() + "");
        final List<String> meetingTitles = new ArrayList<>();
        mIconChoosePopupWindow = new NewIconChoosePopupWindow(getActivity(), meetings);
        mIconChoosePopupWindow.setAnimationStyle(R.style.icon_popup_window);
        mIconChoosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        ListView listView = (ListView) mIconChoosePopupWindow.getContentView().findViewById(R.id.meeting_yy_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingYYAdapter.MeetingViewHolder viewHolder = (MeetingYYAdapter.MeetingViewHolder) view.getTag();
                if (viewHolder.cb.isChecked()) {
                    meetingTitles.remove(meetings.get(position).getTopic() + "#@#" + meetings.get(position).getTopicEn());
                    viewHolder.cb.setChecked(false);
                } else {
                    meetingTitles.add(meetings.get(position).getTopic() + "#@#" + meetings.get(position).getTopicEn());
                    viewHolder.cb.setChecked(true);
                }
            }
        });
        mIconChoosePopupWindow.getContentView().findViewById(R.id.meeting_yy_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingTitles.size() > 0) {
                    for (int i = 0; i < meetingTitles.size(); i++) {
                        try {
                            String title = URLEncoder.encode(meetingTitles.get(i), "UTF-8");
                            CHYHttpClientUsage.getInstanse().doCoursewareReservation(title, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    ToastUtils.showRoundRectToast(getActivity(), R.layout.view_toast_custom);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mIconChoosePopupWindow.dismiss();
            }
        });
        mIconChoosePopupWindow.getContentView().findViewById(R.id.meeting_yy_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIconChoosePopupWindow.dismiss();
            }
        });
    }

    private void initPopupWindow() {
        final View pview = getActivity().getLayoutInflater().inflate(R.layout.share_note_background, null, false);

        mShareNotePopup = new PopupWindow(pview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mShareNotePopup.setOutsideTouchable(true);
        mShareNotePopup.setBackgroundDrawable(new BitmapDrawable());

        final TextView shareView = (TextView) pview.findViewById(R.id.tv_share);
        shareView.setVisibility(Constants.SCHEDULE_SHARE ? View.VISIBLE : View.GONE);
        TextView noteView = (TextView) pview.findViewById(R.id.tv_make_note);

        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareNotePopup.dismiss();
                String shareTitle = "";
                if (AppApplication.systemLanguage == 1) {
                    shareTitle = mSessionBeanList.get(mPosition).getSessionName();
                } else {
                    shareTitle = mSessionBeanList.get(mPosition).getSessionNameEN();
                }

                //http://app.incongress.cn/chyWebApp/assetsCit/session_detail.jsp?sessionId=38105&conId=194&isShare=1

                //ShareUtils.shareTextWithUrl(getActivity(), shareTitle, "", Constants.APP_DOWNLOAD_SITE, null);
                ShareUtils.shareTextWithUrl(getActivity(), shareTitle, "会议日程",
                        "http://app.incongress.cn/chyWebApp/assetsCsc/session_detail.jsp?sessionId=" + mSessionBeanList.get(mPosition).getSessionGroupId() + "&conId=" + Constants.conId + "&isShare=1", null);
                //252
            }
        });

        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNoted;
                Note note = ConferenceDbUtils.getNoteBySessionId(mSessionBeanList.get(mPosition).getSessionGroupId() + "");

                if (note != null) {
                    isNoted = true;
                } else {
                    isNoted = false;
                }

                if (isNoted) {
                    //已经记录过一次笔记，那么就是type_update
                    View view = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                    ((TextView) view).setText(R.string.save);
                    MeetingNoteEditorActionFragment fragment = MeetingNoteEditorActionFragment.getInstance(MeetingNoteEditorActionFragment.TYPE_UPDATE, mSessionBeanList.get(mPosition).getSessionGroupId());
                    fragment.setRightView(view);
                    action(fragment, R.string.meeting_schedule_note, view, false, false, false);
                    mShareNotePopup.dismiss();
                } else {
                    //没有笔记，那么是第一次进入笔记本是type_new
                    View view = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                    ((TextView) view).setText(R.string.save);
                    MeetingNoteEditorActionFragment fragment = MeetingNoteEditorActionFragment.getInstance(MeetingNoteEditorActionFragment.TYPE_NEW, mSessionBeanList.get(mPosition).getSessionGroupId());
                    fragment.setRightView(view);
                    action(fragment, R.string.meeting_schedule_note, view, false, false, false);
                    mShareNotePopup.dismiss();
                }
            }
        });
    }

    public void setRightListener(View view) {
        mRightView = view;
        mRightView.findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareNotePopup == null)
                    initPopupWindow();

                if (mShareNotePopup != null && mShareNotePopup.isShowing()) {
                    mShareNotePopup.dismiss();
                } else {
                    mShareNotePopup.setAnimationStyle(R.style.popupwindow_anim_alpha);
                    mShareNotePopup.showAtLocation(mViewPager, Gravity.RIGHT | Gravity.TOP, 30, DensityUtil.dip2px(getActivity(), 76));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SESSIONDETAIL);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SESSIONDETAIL);
    }
}
