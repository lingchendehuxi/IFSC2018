package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.OnlyWebViewActionFragment;
import com.android.incongress.cd.conference.model.Map;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 参会指南
 *
 * @author Jacky_Chen
 * @time 2014年11月25日
 */
public class MeetingGuideFragment extends BaseFragment {
    private static final int[] CONTENT = new int[]{R.string.tips_title_information, R.string.tips_title_classes};

    private TabLayout mTabLayout;
    private ViewPager mViewpager;
    private GuideAdapter mPageAdapter;

    private View mShareView;
    private MeetingInfoFragment mInfoFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tips_listview, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mViewpager = (ViewPager) view.findViewById(R.id.pager);
        mViewpager.setOffscreenPageLimit(2);

        mPageAdapter = new GuideAdapter(getFragmentManager());
        mInfoFragment = new MeetingInfoFragment();

        mInfoFragment.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map bean = (Map) mInfoFragment.getTips_classes_adapter().getItem(position);
                MeetingGuideRoomMapFragment fragment = new MeetingGuideRoomMapFragment();
                String filepath = AppApplication.instance().getSDPath() + Constants.FILESDIR + bean.getMapUrl();
                fragment.setFilePath(filepath);
                String mapName = "";
                if (bean.getMapRemark().contains("#@#")) {
                    if (AppApplication.systemLanguage == 1) {
                        mapName = bean.getMapRemark().split("#@#")[0];
                    } else {
                        mapName = bean.getMapRemark().split("#@#")[1];
                    }
                } else {
                    mapName = bean.getMapRemark();
                }
                action(fragment, mapName, false, false, false);
            }
        });
        mViewpager.setAdapter(mPageAdapter);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewpager);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 把当前显示的position传递出去
                if (position == 0) {
                    mShareView.setVisibility(View.GONE);
                } else {
                    mShareView.setVisibility(View.GONE
                    );
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    class GuideAdapter extends FragmentStatePagerAdapter {
        public GuideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return OnlyWebViewActionFragment.getInstance(getActivity().getString(Constants.get_MEETING_GUIDE(), Constants.getConId(), AppApplication.getSystemLanuageCode()));
            else
                return mInfoFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(CONTENT[position]);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

    /**
     * 分享
     *
     * @param view
     */
    public void setRightView(View view) {
        if (view != null) {
            mShareView = view;
            mShareView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.shareTextWithUrl(getActivity(), getString(R.string.share_info), getString(R.string.party_guide), getActivity().getString(Constants.get_MEETING_GUIDE(), Constants.getConId(), AppApplication.getSystemLanuageCode()), null);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MEETINGGUIDE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MEETINGGUIDE);
    }
}
