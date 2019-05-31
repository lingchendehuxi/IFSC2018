package com.android.incongress.cd.conference.fragments.scenic_xiu;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.NoScrollGridViewLocalPathAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.widget.IconChoosePopupWindow;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.photo.galleryfinal.FunctionConfig;
import com.android.incongress.cd.conference.widget.photo.galleryfinal.GalleryFinal;
import com.android.incongress.cd.conference.widget.photo.galleryfinal.model.PhotoInfo;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 * 发帖页面
 */
public class MakePostActionFragment extends BaseFragment implements GalleryFinal.OnHanlderResultCallback{
    private EditText mEtPostContent;
    private IconChoosePopupWindow mPopupChoosePhotos;
//    private MyGridView mGvPics;
    private RecyclerView mRcvUploadPhotos;
    private NoScrollGridViewLocalPathAdapter mGridAdapter;

    private ImageView mIvChoosePhoto;
    private View mMakePost;
    private ProgressDialog mProgressDialog;

    private ArrayList<String> mPhotosPath = new ArrayList<String>();

    //当前传送的图片顺序
    private int mCurrentPostImg = 0;

    private Uri fileUri;
    //发布的现场秀id
    private String mPostScenicShowId = "-1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenicxiu_makepost, null);

        mEtPostContent = (EditText) view.findViewById(R.id.et_post_content);
//        mGvPics = (MyGridView) view.findViewById(R.id.gv_pic);
        mRcvUploadPhotos = (RecyclerView) view.findViewById(R.id.rcv_photos);
        mIvChoosePhoto = (ImageView) view.findViewById(R.id.iv_add_pic);

        mIvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow();
                mPopupChoosePhotos.showAtLocation(mEtPostContent, Gravity.BOTTOM, 0, 0);
                lightOff();

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtPostContent.getWindowToken(),0);
            }
        });

        mRcvUploadPhotos.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mGridAdapter = new NoScrollGridViewLocalPathAdapter(getActivity(), mPhotosPath);
        mRcvUploadPhotos.setAdapter(mGridAdapter);
        mRcvUploadPhotos.setItemAnimator(new DefaultItemAnimator());
        mRcvUploadPhotos.setAdapter(mGridAdapter);

        mGridAdapter.setDeleteClickListener(new NoScrollGridViewLocalPathAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, String path) {
                mGridAdapter.removeData(Integer.parseInt((String) view.getTag()));

                if(mPhotosPath.size() == 9) {
                    mIvChoosePhoto.setVisibility(View.GONE);
                }else{
                    mIvChoosePhoto.setVisibility(View.VISIBLE);
                }
            }
        });

        MobclickAgent.onEvent(getActivity(), Constants.EVENT_ID_MAKE_POST);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MAKEPOST);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MAKEPOST);
    }

    public void setRightView(View view) {
        mMakePost = view;
        mMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotosPath.size() != 0 || !StringUtils.isEmpty(mEtPostContent.getText().toString().trim())) {
                    //先传图片，后穿文字
                    doCreateImge();
                } else {
                    Toast.makeText(getActivity(),R.string.scenic_content,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

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
                if (mPhotosPath.size() < 9) {
                    FunctionConfig config = new FunctionConfig.Builder().setMutiSelectMaxSize(9 - mPhotosPath.size()).setFilter(mPhotosPath).build();
                    GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, MakePostActionFragment.this);
                } else {
                    ToastUtils.showToast(getString(R.string.post_photo_more_than_max));
                }
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPhotosPath.size() < 9) {
                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, MakePostActionFragment.this);
                } else {
                    ToastUtils.showToast(getString(R.string.post_photo_more_than_max));
                }
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupChoosePhotos.dismiss();
            }
        });
    }

    //递归上传图片
    private void doCreateImge() {
        if (mPhotosPath.size() == 0) {
            doCreateContent();
        } else {
            //图片进行压缩
            String filePhth = "";
            try {
                filePhth = PicUtils.saveFile(PicUtils.getSmallBitmap(mPhotosPath.get(mCurrentPostImg)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            CHYHttpClientUsage.getInstanse().doCreateSceneShowImg(Constants.getConId() + "", AppApplication.userId + "", AppApplication.userType + "",mPostScenicShowId, new File(filePhth), new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    if (mCurrentPostImg == 0)
                        mProgressDialog = ProgressDialog.show(getActivity(), null, "正在发布...");
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
                        mPostScenicShowId = response.getInt("sceneShowId") + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (mCurrentPostImg < mPhotosPath.size() - 1) {
                        mCurrentPostImg++;
                        doCreateImge();
                    } else {
                        doCreateContent();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    /**
     * 发送文字内容
     */
    private void doCreateContent()   {
        String content = mEtPostContent.getText().toString().trim();
        int conId = Constants.getConId();
        int userId = AppApplication.userId;
        int userType = AppApplication.userType;
        try {
            CHYHttpClientUsage.getInstanse().doCreateSceneShowTxt(conId + "", userId + "", userType + "", mPostScenicShowId, URLEncoder.encode(content, Constants.ENCODING_UTF8), new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (mPhotosPath.size() == 0)
                        mProgressDialog = ProgressDialog.show(getActivity(), null, "正在发布...");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mPostScenicShowId = "-1";

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEtPostContent.getWindowToken(), 0);

                        performback();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    MyLogger.jLog().i(responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    ToastUtils.showToast(JSONCatch.parseString("msg",response));
                    MyLogger.jLog().i(response.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if(mPopupChoosePhotos != null && mPopupChoosePhotos.isShowing()) {
            mPopupChoosePhotos.dismiss();
        }

        if (reqeustCode == REQUEST_CODE_GALLERY) {
            for (int i = 0; i < resultList.size(); i++) {
                mPhotosPath.add(resultList.get(i).getPhotoPath());
            }
            mGridAdapter.notifyDataSetChanged();
        } else if (reqeustCode == REQUEST_CODE_CAMERA) {
            mPhotosPath.add(resultList.get(0).getPhotoPath());
            mGridAdapter.notifyDataSetChanged();
        }

        if(mPhotosPath.size() == 9) {
            mIvChoosePhoto.setVisibility(View.GONE);
        }else{
            mIvChoosePhoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        ToastUtils.showToast(getString(R.string.choose_photo_fail));
    }

}

