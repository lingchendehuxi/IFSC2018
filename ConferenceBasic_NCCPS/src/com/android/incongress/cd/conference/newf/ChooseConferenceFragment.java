package com.android.incongress.cd.conference.newf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.ConferencedDownloadDetailActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.ChooseConferenceAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.EsmosBean;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Jacky on 2016/11/21.
 * 选择大会
 */

public class ChooseConferenceFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;

    private ChooseConferenceAdapter mAdapter;
    private List<EsmosBean> mEsmoBeans;
    private View mHeaderView;

    private static final int TAG_OPEN_CONFERENCE = 0x0001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_conference, null);
        mRecyclerView = (XRecyclerView)view.findViewById(R.id.recyclerview);
        mHeaderView = inflater.inflate(R.layout.head_choose_conference, null);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.addHeaderView(mHeaderView);

        mEsmoBeans = ConferenceDbUtils.getEsmoBeans();
        mAdapter = new ChooseConferenceAdapter(getActivity(), mEsmoBeans);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);

        mAdapter.setmOnItemClickListener(new ChooseConferenceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int id) {
                int position = (int) view.getTag();

                EsmosBean esmosBean = mEsmoBeans.get(position);
                if(esmosBean.getType() == 1) {
                    Intent intent = new Intent(getActivity(), ConferencedDownloadDetailActivity.class);
                    intent.putExtra(Constants.CONFERENCE_ID, esmosBean.getDataConferencesId());
                    startActivityForResult(intent,TAG_OPEN_CONFERENCE);
                }else if(esmosBean.getType() == 2) {
                    WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                            esmosBean.getHtmlUrl(),  esmosBean.getConferencesName());
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK) {
            if(requestCode == TAG_OPEN_CONFERENCE) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.switchContent(activity.mDynamicHomeFragment);
//                activity.setFirstPosition(1);
            }
        }
    }

    public void updateConferenceData() {
        mEsmoBeans = ConferenceDbUtils.getEsmoBeans();

        if(mAdapter != null) {
            mAdapter.updateConferenceState(true,mEsmoBeans);
        }
    }

}
