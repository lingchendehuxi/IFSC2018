package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.InvitationBean;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by 13008 on 2019/4/21.
 */

public class InvitationFragment extends BaseFragment{
    private static final String BUNDLE_TIME = "order_title";
    private static final String BUNDLE_TYPE = "order_type";
    private int mType;
    @BindView(R.id.iv_invitation)
    ImageView iv_invitation;
    public static InvitationFragment getInstance(String title, int type) {
        InvitationFragment fragment = new InvitationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, title);
        bundle.putInt(BUNDLE_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invitation_fragment, null);
        ButterKnife.bind(this,view);
        mType = getArguments().getInt(BUNDLE_TYPE);
        getSatelliteArray();
        return view;
    }
    //获取卫星会信息
    private void getSatelliteArray(){
        CHYHttpClientUsage.getInstanse().doGetExhibitorByListInfo("50",mType, "-1", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(JSONCatch.parseInt("resultState",response) == 1){
                    List<InvitationBean> HotListBean = new Gson().fromJson(JSONCatch.parseJsonarray("result", response).toString(), new TypeToken<List<InvitationBean>>() {
                    }.getType());
                    if(HotListBean.size()>0){
                        PicUtils.loadImageUrl(getActivity(),HotListBean.get(0).getImg(),iv_invitation);
                        final String title = HotListBean.get(0).getTitle();
                        final String url = HotListBean.get(0).getUrl();
                        iv_invitation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!TextUtils.isEmpty(url)){
                                    CollegeActivity.startCitCollegeActivity(getActivity(),title , url);
                                }
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }
}
