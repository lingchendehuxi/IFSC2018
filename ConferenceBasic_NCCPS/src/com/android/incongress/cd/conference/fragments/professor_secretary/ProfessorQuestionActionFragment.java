package com.android.incongress.cd.conference.fragments.professor_secretary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.adapters.MyQuestionFragmentAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 * 我收到的提问
 */
public class ProfessorQuestionActionFragment extends BaseFragment {
    private int currentPosition;
    private List<SceneShowArrayBean> mSceneShowQuestionBeans;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MyQuestionFragmentAdapter mAdapter;

    private static final String QUESTION_LIST = "questions";

    public ProfessorQuestionActionFragment() {
    }

    public static ProfessorQuestionActionFragment getInstance(List<SceneShowArrayBean> sceneShowArrayBeans) {
        ProfessorQuestionActionFragment fragment = new ProfessorQuestionActionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(QUESTION_LIST, (Serializable) sceneShowArrayBeans);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSceneShowQuestionBeans = (List<SceneShowArrayBean>) getArguments().getSerializable(QUESTION_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myquestion, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MyQuestionFragmentAdapter(getActivity(), mSceneShowQuestionBeans);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .paintProvider(mAdapter)
                .visibilityProvider(mAdapter)
                .marginProvider(mAdapter)
                .build());

        mAdapter.setOnItemClickListener(new MyQuestionFragmentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SceneShowArrayBean question, int position) {
                if (question.getIsHuiFu() == 1) {
                    ToastUtils.showToast("已经回复过了");
                    return;
                }
                currentPosition = position;
                Intent intent = new Intent();
                intent.setClass(getActivity(), AnswerQuestionActivity.class);
                intent.putExtra(ReceiveProfessorQuestionActionFragment.INTENT_QUESTION, question);
                intent.putExtra(ReceiveProfessorQuestionActionFragment.QUESTION_POSITION, position);
                startActivityForResult(intent, ReceiveProfessorQuestionActionFragment.QUESTION_TYPE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ReceiveProfessorQuestionActionFragment.QUESTION_TYPE && mAdapter != null) {
            mSceneShowQuestionBeans.get(currentPosition).setIsHuiFu(1);
            mAdapter.notifyDataSetChanged();
        }
    }
}
