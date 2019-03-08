package com.android.incongress.cd.conference.fragments.photo_album;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.ScenicXiuPicsViewpagerActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.IconChoosePopupWindow;
import com.android.incongress.cd.conference.widget.RefreshLayout;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2017/1/20.
 */

public class PhotoAlbumDetailFragment extends BaseFragment implements GalleryFinal.OnHanlderResultCallback{

    private RefreshLayout mRefreshLayout;
    private RelativeLayout mRlAll;
    private GridView mGridView;
    private TextView mTvUpload;
    private int mTypeId;
    private IconChoosePopupWindow mPopupChoosePhotos;
    private TextView mTvTips;

    private List<PhotoAlbumDetailBean.PhotoWallArrayBean> mPhotoAlbumDetailArrayList;

    private ProgressDialog mProgressDialog;
    private ArrayList<String> mPhotosPath = new ArrayList<String>();
    private PhotoAlbumGridAdapter mAdapter;

    private int mLastId = -1;

    private int mType = ALL_AVAILABLE_UPLOAD;//当前的类型
    public static final int ALL_AVAILABLE_UPLOAD = 1;//所有人都允许上传
    public static final int STAFF_AVAILABLE_UPLOAD = 2;//zhiyunxu

    public void setTypeId(int typeId) {
        this.mTypeId = typeId;
    }

    public void setType(int type) {
        this.mType = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_album_detail, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mTvUpload = (TextView) view.findViewById(R.id.tv_upload);
        mRlAll = (RelativeLayout) view.findViewById(R.id.rl_all);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refresh_layout);

        mPhotoAlbumDetailArrayList = new ArrayList<>();

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    mLastId = -1;
                    mPhotoAlbumDetailArrayList.clear();
                    getPhotos(mLastId);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new RefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getPhotos(mLastId);
            }
        });
        mAdapter = new PhotoAlbumGridAdapter(getActivity(), mPhotoAlbumDetailArrayList);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = null;
                for (int i =0 ;i<mPhotoAlbumDetailArrayList.size();i++){
                     if(i == 0){
                         s = mPhotoAlbumDetailArrayList.get(0).getImageUrl();
                     }else{
                         s = s+","+mPhotoAlbumDetailArrayList.get(i).getImageUrl();
                     }
                }
                final String[] strPics = s.split(",");/*
                String picUrl = mPhotoAlbumDetailArrayList.get(position).getImageUrl();
                ScenicXiuPicsViewpagerActivity.startViewPagerActivity(getActivity(),new String[]{picUrl},0);*/
                ScenicXiuPicsViewpagerActivity.startViewPagerActivity(getActivity(), strPics, position);
            }
        });

        mTvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppApplication.userType == 0) {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
                }else{
                    if(mType == ALL_AVAILABLE_UPLOAD) {
                        //发帖
                        openPopupWindow();
                    }else if(mType == STAFF_AVAILABLE_UPLOAD){
                        //只有专家允许
                        if(AppApplication.userType == 4) {
                            //发帖
                            openPopupWindow();
                        }else {
                            ToastUtils.showToast("参会者上传照片，请前往\"参会者空间\"");
                        }
                    }
                }
            }
        });

        getPhotos(mLastId);

        return view;
    }


    private void openPopupWindow() {
        initPopupWindow();
        mPopupChoosePhotos.showAtLocation(mRlAll, Gravity.BOTTOM, 0, 0);
        lightOff();

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mRlAll.getWindowToken(),0);
    }

    private void getPhotos(final int lastId) {
        CHYHttpClientUsage.getInstanse().doGetPhotoWallImgs(AppApplication.userId, AppApplication.userType,Constants.getConId()+"", AppApplication.getSystemLanuageCode(),mTypeId, lastId, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("doGetPhotoWallImgs", response.toString());
                    Gson gson = new Gson();
                    PhotoAlbumDetailBean photoAlbumDetailBean = gson.fromJson(response.toString(), PhotoAlbumDetailBean.class);
                    if (photoAlbumDetailBean != null && photoAlbumDetailBean.getState() == 1) {
                        mPhotoAlbumDetailArrayList.addAll(photoAlbumDetailBean.getPhotoWallArray());
                        mAdapter.notifyDataSetChanged();

                        if (lastId == -1) {
                            mRefreshLayout.refreshComplete();
                            mRefreshLayout.setmIsCanLoad(true);
                        } else {
                            mRefreshLayout.loadMoreComplete();
                        }

                        if (photoAlbumDetailBean.getPageState() == 0)
                            mRefreshLayout.setmIsCanLoad(false);

                        mLastId = mPhotoAlbumDetailArrayList.get(mPhotoAlbumDetailArrayList.size() - 1).getPhotoWallId();
                    }

                    if (mPhotoAlbumDetailArrayList == null || mPhotoAlbumDetailArrayList.size() == 0) {
                        mTvTips.setVisibility(View.VISIBLE);
                    } else {
                        mTvTips.setVisibility(View.GONE);
                    }
                mRefreshLayout.refreshComplete();
                }
        });
    }

    private void initPopupWindow() {
        mPopupChoosePhotos = new IconChoosePopupWindow(getActivity());
        mPopupChoosePhotos.setAnimationStyle(R.style.icon_popup_window);
        mPopupChoosePhotos.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionConfig config = new FunctionConfig.Builder().setMutiSelectMaxSize(1).setFilter(mPhotosPath).build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, PhotoAlbumDetailFragment.this);
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, PhotoAlbumDetailFragment.this);
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupChoosePhotos.dismiss();
            }
        });
    }


    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if(mPopupChoosePhotos != null && mPopupChoosePhotos.isShowing()) {
            mPopupChoosePhotos.dismiss();
        }

        //获取成功那就上传图片
        //图片进行压缩
        String filePhth = "";
        try {
            filePhth = PicUtils.saveFile(PicUtils.getSmallBitmap(resultList.get(0).getPhotoPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CHYHttpClientUsage.getInstanse().doCreatePhotoImage(Constants.getConId()+"", AppApplication.userId +"", AppApplication.userType + "", mTypeId + "", AppApplication.getSystemLanuageCode(),new File(filePhth),
                new JsonHttpResponseHandler(){
                    @Override
                    public void onStart() {
                        super.onStart();
                        mProgressDialog = ProgressDialog.show(getActivity(),null, "loading...",true, false);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if(mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }



                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            int state =response.getInt("state");
                            if(state == 1) {
                                ToastUtils.showToast("上传成功，等待检查");
                            }else {
                                ToastUtils.showToast("上传失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        ToastUtils.showToast(getString(R.string.choose_photo_fail));
    }
}
