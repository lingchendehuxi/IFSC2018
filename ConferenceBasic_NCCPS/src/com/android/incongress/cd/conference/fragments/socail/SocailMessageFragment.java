package com.android.incongress.cd.conference.fragments.socail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/10/12.
 * 社交消息
 */

public class SocailMessageFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_message, container, false);
        return view;
    }
}
