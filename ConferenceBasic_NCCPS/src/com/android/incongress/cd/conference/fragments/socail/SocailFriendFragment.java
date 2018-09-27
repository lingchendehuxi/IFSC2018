package com.android.incongress.cd.conference.fragments.socail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/10/12.
 */

public class SocailFriendFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_friend, container, false);
        return view;
    }
}
