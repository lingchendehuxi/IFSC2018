package com.android.incongress.cd.conference.fragments.photo_album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.PhotoTypeBean;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2017/1/20.
 */

public class PhotoAlbumFragment extends BaseFragment implements PhotoTypesAdapter.OnPhotoTypeClick{
    private XRecyclerView mRecyclerView;
    private PhotoTypeBean mPhotoType;
    private PhotoTypesAdapter mPhotoTypeAdapter;
    private List<PhotoTypeBean.PhotoWallTypeArrayBean> mPhotoWallTypeArrayList = new ArrayList<>();
    private ImageView tv_tips;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.fragment_photo_album, container, false);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        mPhotoTypeAdapter = new PhotoTypesAdapter(getActivity(), mPhotoWallTypeArrayList, this);
        tv_tips = (ImageView) view.findViewById(R.id.tv_tips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setAdapter(mPhotoTypeAdapter);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getDatas();
            }

            @Override
            public void onLoadMore() {
            }
        });

        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setRefreshing(true);
        return view;
    }

    private void getDatas() {
        mPhotoWallTypeArrayList.clear();
        mPhotoTypeAdapter.notifyDataSetChanged();
        CHYHttpClientUsage.getInstanse().doGetPhotoWallTypes(Constants.getConId()+"", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler("gbk"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("photoAlbum", response.toString());
                Gson gson = new Gson();
                mPhotoType = gson.fromJson(response.toString(), PhotoTypeBean.class);
                if(mPhotoType.getState() == 1) {
                    tv_tips.setVisibility(View.GONE);
                    mPhotoWallTypeArrayList.addAll(mPhotoType.getPhotoWallTypeArray());
                    mPhotoTypeAdapter.notifyDataSetChanged();
                }else {
                    tv_tips.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(),R.string.photo_tips,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mRecyclerView.refreshComplete();
            }
        });
    }

    @Override
    public void OnPhotoTypeClickListen(PhotoTypeBean.PhotoWallTypeArrayBean photoWallTypeArrayBean) {
        PhotoAlbumDetailFragment detailFragment = new PhotoAlbumDetailFragment();
        detailFragment.setTypeId(photoWallTypeArrayBean.getTypeId());
        detailFragment.setType(photoWallTypeArrayBean.getType());
        action(detailFragment, photoWallTypeArrayBean.getTypeName(),false, false,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        MobclickAgent.onPageStart(Constants.FRAGMENT_PHOTOALBUM);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_PHOTOALBUM);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
