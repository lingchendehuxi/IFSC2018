package com.android.incongress.cd.conference.fragments.professor_secretary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.adapters.MyTaskFragmentAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 * 我的学术任务
 *
 * 已徐波为例返回数据
 */
public class ProfessorTaskActionFragment extends BaseFragment {
    private List<ActivityBean> mValidActivitys = new ArrayList<ActivityBean>();
    private RecyclerView mRecyclerView;
    private MyTaskFragmentAdapter mMyTaskFragmentAdapter;
    private LinearLayoutManager mLinearLayoutManger;

    public ProfessorTaskActionFragment(){}

    public static final ProfessorTaskActionFragment getInstance(List<ActivityBean> tasks) {
        ProfessorTaskActionFragment fragment = new ProfessorTaskActionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SecretaryActivity.TASK_LIST, (Serializable) tasks);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mValidActivitys = (List<ActivityBean>) getArguments().getSerializable(SecretaryActivity.TASK_LIST);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mytask, null);
        mLinearLayoutManger = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManger);

        mMyTaskFragmentAdapter = new MyTaskFragmentAdapter(mValidActivitys, getActivity());
        mRecyclerView.setAdapter(mMyTaskFragmentAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .paintProvider(mMyTaskFragmentAdapter)
                .visibilityProvider(mMyTaskFragmentAdapter)
                .marginProvider(mMyTaskFragmentAdapter)
                .build());

        return view;
    }
}
