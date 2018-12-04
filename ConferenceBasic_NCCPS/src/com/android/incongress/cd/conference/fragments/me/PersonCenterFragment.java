package com.android.incongress.cd.conference.fragments.me;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MeGridListBean;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Note;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.CacheUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.transformer.CircleTransform;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.android.incongress.cd.conference.widget.IconChoosePopupWindow;
import com.android.incongress.cd.conference.widget.NoScrollGridView;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Jacky on 2016/1/28.
 * 模块：我
 * Jacky Chen
 */
public class PersonCenterFragment extends BaseFragment implements View.OnClickListener, GalleryFinal.OnHanlderResultCallback {

    public static final int REQUEST_LOGIN = 0x0001;
    private RelativeLayout mMeetingAlertPanel, mContackPanel, mSharePanel, mHelpPanel, mRlMyField, mRlMyKeshi, mRlSettingsCache;
    private TextView mTvCacheSize;
    private TextView username, welcomeInfo;
    private int mNoteCount, mTieZiCount;
    private CircleImageView mCivHeadIcon;
    private NoScrollGridView mGridView;
    private ArrayList<MeGridListBean> meGridListBeans;
    private boolean currentState = false;
    //中间图片和文字
    private Drawable[] imgList;
    private String[] strList;

    private static final int HANDLE_TIEZI_COUNT = 0x0001;
    private static final int HANDLE_NOTE_COUNT = 0x0002;

    //缓存文件
    private String mCacheFilePath = "";

    /**
     * 页面是否处于打开状态
     **/
    private boolean mIsOpen = true;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_IDENTIFY_CODE = 1002;

    //头像上传相关
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int ALBUM_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int UPLOAD_IMGURL_SUCCESS = 3;
    public static final String EXTRA_FROM_ME = "fromMe";

    /**
     * 头像上传后的地址
     **/
    private String mUploadFilePath = "";
    private IconChoosePopupWindow mIconChoosePopupWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mIsOpen == false) {
                return;
            }

            int target = msg.what;
            /*if (target == HANDLE_TIEZI_COUNT) {
                if (mTieZiCount > 0) {
                    mBtTieZi.setText(getString(R.string.mymeeting_tiezi) + " " + mTieZiCount);
                }else {
                    mBtTieZi.setText(getString(R.string.mymeeting_tiezi));
                }
            }else if(target == HANDLE_NOTE_COUNT) {
                if (mNoteCount > 0) {
                    mBtNote.setText(getString(R.string.mymeeting_note) + " " + mNoteCount);
                }else {
                    mBtNote.setText(getString(R.string.mymeeting_note));
                }
            }else*/
            if (target == UPLOAD_IMGURL_SUCCESS) {
                SharePreferenceUtils.saveUserString(Constants.USER_IMG, mUploadFilePath);

                if (mUploadFilePath.contains("https:"))
                    mUploadFilePath = mUploadFilePath.replaceFirst("s", "");
                Glide.with(getActivity()).load(mUploadFilePath).transform(new CircleTransform(getActivity())).into(mCivHeadIcon);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mycenter_warmning_panel:
                action(new MyMeetingWarmning(), R.string.mymeeting_warning, false, false, false);
                break;
            case R.id.settings_contact_panel:
                SettingContactActionFragment contact = new SettingContactActionFragment();
                action(contact, R.string.settings_contact, false, false, false);
                break;
            case R.id.settings_share_panel:
                ShareUtils.shareTextWithUrl(getActivity(), Constants.APPNAME, getString(R.string.settings_share_wxtitle), Constants.APP_DOWNLOAD_SITE, null);
                /*SettingsShare share = new SettingsShare();
                action(share, R.string.settings_share_title, false, false, false);*/
                break;
            case R.id.settings_help_panel:
                /*SettingsHelper help = new SettingsHelper ();
                View view = CommonUtils.initView(this.getActivity(), R.layout.hysqhome_shuoliangju_nav_rightbtn);
                TextView mText = (TextView) view.findViewById(R.id.hysq_jiangliangju_titlebar_send);
                help.setView(mText);
                action(help, R.string.settings_help_title, view, false, false, false);*/
                String url = "http://weixin.incongress.cn/xhy/xhyHtml5/html/feedback.html";
                url = url + "?userId=" + AppApplication.userId + "&project=" + Constants.PROJECT_NAME + "&lan=" + AppApplication.getSystemLanuageCode();

                CollegeActivity.startCitCollegeActivity(getContext(), getActivity().getResources().getString(R.string.settings_help_title), url);
                break;
            /*case R.id.bt_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_LOGIN);
                break;*/
            /*case R.id.bt_login_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_tips).setMessage(R.string.login_out_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginOut();
                    }
                }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).show();
                break;*/
            case R.id.civ_me:
                if (AppApplication.isUserLogIn()) {
                    initPopupWindow();
                    //mIconChoosePopupWindow.showAtLocation(mLlPersonInfo, Gravity.BOTTOM, 0, 0);
                    lightOff();
                } else {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
//                    ChooseIdentityActivity.startChooseIdentityActivity(getActivity());
                }
                break;
            /*case R.id.iv_scane:
                getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), HomeActivity.REQUEST_SCANE);
                break;*/
            /*case R.id.rl_my_field:
                Intent fieldIntent = new Intent(getActivity(), ChooseFieldActivity.class);
                fieldIntent.putExtra(EXTRA_FROM_ME, true);
                startActivity(fieldIntent);
                break;
            case R.id.rl_my_keshi:
                Intent keshiIntent = new Intent(new Intent(getActivity(), ChooseKeShiActivity.class));
                keshiIntent.putExtra(EXTRA_FROM_ME, true);
                startActivity(keshiIntent);
            case R.id.settings_cache:
                showDialog("确定清理缓存吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CacheUtils.deleteFolderFile(mCacheFilePath,true);
                        mTvCacheSize.setText("0.0");
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }, false);
                break;*/
            default:
                break;
        }
    }

    private void initEvents() {
        mMeetingAlertPanel.setOnClickListener(this);
        mContackPanel.setOnClickListener(this);
        mSharePanel.setOnClickListener(this);
        mHelpPanel.setOnClickListener(this);
        mCivHeadIcon.setOnClickListener(this);
        /*mRlMyField.setOnClickListener(this);
        mRlMyKeshi.setOnClickListener(this);
        mRlSettingsCache.setOnClickListener(this);*/
        getNoteCount();
    }

    /**
     * 查询需要显示的笔记
     */
    private void getNoteCount() {
        List<Note> allNotes = ConferenceDbUtils.getAllNotes();
        mNoteCount = allNotes.size();
        mHandler.sendEmptyMessage(HANDLE_NOTE_COUNT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),false);
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        // mRlMyField = (RelativeLayout) view.findViewById(R.id.rl_my_field);
        //mRlMyKeshi = (RelativeLayout) view.findViewById(R.id.rl_my_keshi);
        mCivHeadIcon =  view.findViewById(R.id.civ_me);
        mMeetingAlertPanel =  view.findViewById(R.id.mycenter_warmning_panel);
        mContackPanel = (RelativeLayout) view.findViewById(R.id.settings_contact_panel);
        mSharePanel = (RelativeLayout) view.findViewById(R.id.settings_share_panel);
        mHelpPanel = (RelativeLayout) view.findViewById(R.id.settings_help_panel);
        username = (TextView) view.findViewById(R.id.tv_name);
        welcomeInfo = (TextView) view.findViewById(R.id.tv_welcome);
        mGridView = view.findViewById(R.id.gv_list);
        meGridListBeans = new ArrayList<>();
        imgList = new Drawable[]{getResources().getDrawable(R.drawable.remind),getResources().getDrawable(R.drawable.quiz),getResources().getDrawable(R.drawable.post),getResources().getDrawable(R.drawable.note)};
        strList = new String[]{getString(R.string.me_remind),getString(R.string.me_quiz),getString(R.string.me_post),getString(R.string.me_note)};
        for(int i = 0;i<4;i++){
            MeGridListBean bean = new MeGridListBean();
            bean.setTestImg("");
            bean.setTestTiltle("测试"+i);
            meGridListBeans.add(bean);
        }
        mGridView.setAdapter(new GridListAdapter(getActivity(),meGridListBeans));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        if (AppApplication.isUserLogIn()) {
                            action(HistoryPostActionFragment.getInstance(), R.string.mymeeting_tiezi, false, false, false);
                        } else {
                            LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
//                    ChooseIdentityActivity.startChooseIdentityActivity(getActivity());
                        }
                        break;
                    case 3:
                        NoteManageActionFragment noteManager = new NoteManageActionFragment();
                        action(noteManager, R.string.mymeeting_note, false, false, false);
                        break;
                }
            }
        });

        String cacheSize = "";
        mCacheFilePath = AppApplication.instance().getSDPath() + Constants.DOWNLOADDIR;

        try {
            cacheSize = CacheUtils.getCacheSize(new File(mCacheFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mTvCacheSize.setText(cacheSize);

        initEvents();
        refreshInfo();
        return view;
    }


    /**
     * 退出登录
     */
    private void loginOut() {
        refreshInfo();
        ParseUser.clearUserInfo(getActivity());
        /*AppApplication.setSPStringValue(Constants.USER_NAME, StringUtils.EMPTY_STR);
        AppApplication.setSPIntegerValue(Constants.USER_ID, -1);
        AppApplication.setSPIntegerValue(Constants.USER_TYPE, Constants.TYPE_USER_VISITOR);
        AppApplication.setSPStringValue(Constants.USER_IMG, StringUtils.EMPTY_STR);*/

        /*AppApplication.setSPBooleanValue(Constants.USER_IS_LOGIN, false);
        AppApplication.userType = Constants.TYPE_USER_VISITOR;
        AppApplication.userId = -1;
        AppApplication.username = "";*/

        /*Intent loginIntent = new Intent();
        loginIntent.setAction(LoginActivity.LOGOUT_ACTION);
        getActivity().sendBroadcast(loginIntent);*/

//        queryCount();

//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        intent.putExtra("fromSplash", true);
//        startActivity(intent);
//        getActivity().finish();
    }


    private void queryCount() {
        //查询我的发帖
        CHYHttpClientUsage.getInstanse().doGetSceneShowByUser(Constants.conId + "", "-1", AppApplication.userId + "", AppApplication.userType + "", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    mTieZiCount = response.getInt("sceneShowCount");
                    mHandler.sendEmptyMessage(HANDLE_TIEZI_COUNT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getNoteCount();
    }


    private void initPopupWindow() {
        mIconChoosePopupWindow = new IconChoosePopupWindow(getActivity());
        mIconChoosePopupWindow.setAnimationStyle(R.style.icon_popup_window);
        mIconChoosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionConfig config = new FunctionConfig.Builder().setMutiSelectMaxSize(1).build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, PersonCenterFragment.this);
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, PersonCenterFragment.this);
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIconChoosePopupWindow.dismiss();
            }
        });
    }


    private ProgressDialog mProgressDialog;


    private void doUploadFile(String userId, String userType, File uploadFile) {

        try {
            CHYHttpClientUsage.getInstanse().doCreateUserImg(userId, userType, uploadFile, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    mProgressDialog = ProgressDialog.show(getActivity(), null, "loading...");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    MyLogger.jLog().i("onSuccess" + response.toString());
                    try {
                        int state = response.getInt("state");
                        if (state == 1) {
                            mUploadFilePath = response.getString("imgUrl");
                            mHandler.sendEmptyMessage(UPLOAD_IMGURL_SUCCESS);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if (mIconChoosePopupWindow != null && mIconChoosePopupWindow.isShowing())
            mIconChoosePopupWindow.dismiss();

        String photoPath = "";
        if (reqeustCode == REQUEST_CODE_GALLERY) {
            photoPath = resultList.get(0).getPhotoPath();
        } else if (reqeustCode == REQUEST_CODE_CAMERA) {
            photoPath = resultList.get(0).getPhotoPath();
        }

        //图片进行压缩
        try {
            mUploadFilePath = PicUtils.saveFile(PicUtils.getSmallBitmap(photoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //上传
        doUploadFile(AppApplication.userId + "", AppApplication.userType + "", new File(mUploadFilePath));
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        ToastUtils.showShorToast(getString(R.string.choose_photo_fail));
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsOpen = true;
        queryCount();

        MobclickAgent.onPageStart(Constants.FRAGMENT_PERSONCENTER);
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsOpen = false;

        MobclickAgent.onPageEnd(Constants.FRAGMENT_PERSONCENTER);
    }

    //刷新用户信息
    public void refreshInfo() {
        if (SharePreferenceUtils.getUserBoolean(Constants.USER_IS_LOGIN, false)) {
            username.setText(SharePreferenceUtils.getUser(Constants.USER_NAME));
            welcomeInfo.setText(getString(R.string.mymeeting_welcome_sb, SharePreferenceUtils.getUser(Constants.USER_NAME)));
            Glide.with(getActivity()).load(SharePreferenceUtils.getUser(Constants.USER_IMG)).placeholder(R.drawable.professor_default).transform(new CircleTransform(getActivity())).into(mCivHeadIcon);
        }else {
            mCivHeadIcon.setImageResource(R.drawable.professor_default);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(),false);
        }
    }
    class GridListAdapter extends BaseAdapter{
        ArrayList<MeGridListBean> listBeans;
        public Context context;
        public LayoutInflater layoutInflater;
        public GridListAdapter (Context context,ArrayList<MeGridListBean> listBeans){
            this.context = context;
            this.listBeans = listBeans;
            layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return listBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return listBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            MyHold myHold;
            if(convertView == null){
                view = layoutInflater.inflate(R.layout.fragment_me_gv_list,null);
                myHold = new MyHold();
                myHold.iv_gv_list = view.findViewById(R.id.iv_gv_list);
                myHold.tv_gv_list = view.findViewById(R.id.tv_gv_list);
                myHold.remind_red = view.findViewById(R.id.remind_red);
                view.setTag(myHold);
            }else {
                view = convertView;
                myHold = (MyHold) view.getTag();
            }
            myHold.iv_gv_list.setImageDrawable(imgList[position]);
            myHold.tv_gv_list.setText(strList[position]);
            if(position == 0){
                myHold.remind_red.setVisibility(View.VISIBLE);
            }
            //PicUtils.loadImageUrl(context,listBeans.get(position).testImg,myHold.iv_gv_list);
            //myHold.tv_gv_list.setText(listBeans.get(position).getTestTiltle());
            return view;
        }
        class MyHold {
            ImageView iv_gv_list;
            TextView tv_gv_list,remind_red;
        }
    }

}
