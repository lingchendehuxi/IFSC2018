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
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.fragments.NewDynamicHomeFragment;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/21.
 * 我收到的提问
 */
public class ReceiveProfessorQuestionActionFragment extends BaseFragment {
    public static final String INTENT_QUESTION = "question";
    public static final String QUESTION_POSITION = "question_position";
    public static final int QUESTION_TYPE = 100;
    private List<SceneShowArrayBean> mSceneShowQuestionBeans;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MyQuestionFragmentAdapter mAdapter;
    private int currentPosition ;

    public ReceiveProfessorQuestionActionFragment() {
    }

    public static ReceiveProfessorQuestionActionFragment getInstance() {
        ReceiveProfessorQuestionActionFragment fragment = new ReceiveProfessorQuestionActionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myquestion, null);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        mSceneShowQuestionBeans = new ArrayList<>();
        mAdapter = new MyQuestionFragmentAdapter(getActivity(), mSceneShowQuestionBeans);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .paintProvider(mAdapter)
                .visibilityProvider(mAdapter)
                .marginProvider(mAdapter)
                .build());

        mAdapter.setOnItemClickListener(new MyQuestionFragmentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SceneShowArrayBean question,int position) {
                if (question.getIsHuiFu() == 1) {
                    ToastUtils.showToast("已经回复过了");
                    return;
                }
                currentPosition = position;
                Intent intent = new Intent();
                intent.setClass(getActivity(), AnswerQuestionActivity.class);
                intent.putExtra(INTENT_QUESTION, question);
                intent.putExtra(QUESTION_POSITION,position);
                startActivityForResult(intent,QUESTION_TYPE);
            }
        });
        goSecretary();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
    }

    /**
     * 专家秘书
     */
    private void goSecretary() {
        CHYHttpClientUsage.getInstanse().doGetSceneShowQuestions(Constants.getConId() + "", AppApplication.facultyId + "", "-1", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler("gbk") {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());
                try {
                    int state = response.getInt("state");
                    if (state != 0) {
                        mSceneShowQuestionBeans.clear();
                        JSONArray jsonArray = response.getJSONArray("sceneShowArray");
                        Gson gson = new Gson();
                        List<SceneShowArrayBean> mBeans = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SceneShowArrayBean>>() {
                        }.getType());
                        mSceneShowQuestionBeans.addAll(mBeans);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == QUESTION_TYPE&&mAdapter!=null){
            mSceneShowQuestionBeans.get(currentPosition).setIsHuiFu(1);
            mAdapter.notifyDataSetChanged();
        }
    }
}
