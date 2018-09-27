package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.incongress.cd.conference.fragments.socail.SocailFriendFragment;
import com.android.incongress.cd.conference.fragments.socail.SocailMessageFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/10/12.
 * 我的参会全界面的适配器
 */

public class SocialPageAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private static final int TAG_MESSAGE = 0;
    private static final int TAG_FRIEND = 1;
    private static final int[] SOCIAL_TITLE_ID = {R.string.social_message_title, R.string.social_friend_title};

    public SocialPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == TAG_MESSAGE) {
            return new SocailMessageFragment();
        }else {
            return new SocailFriendFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(SOCIAL_TITLE_ID[position]);
    }
}
