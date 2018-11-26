package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.incongress.cd.conference.adapters.TipsClassesAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2017/3/16.
 */

public class MeetingInfoFragment extends BaseFragment {
    private TipsClassesAdapter tips_classes_adapter;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(AdapterView.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public TipsClassesAdapter getTips_classes_adapter() {
        return tips_classes_adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchfragment, null);
        ListView listView = (ListView) view.findViewById(R.id.search_session_list);

        tips_classes_adapter = new TipsClassesAdapter();
        listView.setAdapter(tips_classes_adapter);
        listView.setOnItemClickListener(mOnItemClickListener);
        return view;
    }
}
