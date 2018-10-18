package com.android.incongress.cd.conference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.BaseFragment.MainCallBack;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.PosterBean;
import com.android.incongress.cd.conference.beans.TitleEntry;
import com.android.incongress.cd.conference.beans.UserInfoBean;
import com.android.incongress.cd.conference.beans.UserInfoEnBean;
import com.android.incongress.cd.conference.fragments.DynamicHomeFragment;
import com.android.incongress.cd.conference.fragments.NewDynamicHomeFragment;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleDetailActionFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.fragments.message_station.MessageStationActionFragment;
import com.android.incongress.cd.conference.fragments.scenic_xiu.ScenicXiuFragment;
import com.android.incongress.cd.conference.fragments.wall_poster.PosterImageFragment;
import com.android.incongress.cd.conference.model.Ad;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.services.AdService;
import com.android.incongress.cd.conference.utils.ConvertUtil;
import com.android.incongress.cd.conference.widget.zxing.activity.CodeUtils;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.ExampleUtil;
import com.android.incongress.cd.conference.utils.FileUtils;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

import static android.view.View.inflate;
import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_STATIC;

/**
 * 主页容器，主要用来布置上方广告，下方广告，以及中间区域，从splash页面启动
 * 以及相应的广告页面
 */
public class HomeActivity extends BaseActivity implements OnClickListener, MainCallBack {

    private ImageView mBackButton, mHomeGuide;
    private Button mHomeButton;
    private TextView mTitleView; //标题,跳过
    private LinearLayout mCoustomRightView, mCustomTitleView, mBackButtonPanel, mHomeButtonPanel;//自定义view，返回按钮，小房子按钮
    private RelativeLayout mTitleContainer;//整个titleBar，就是上方操作栏
    private BottomNavigationBar mNavigationBar;
    private BadgeItem mBadgeItem;

    public Stack<TitleEntry> getmTitleEntries() {
        return mTitleEntries;
    }

    public void setmTitleEntries(Stack<TitleEntry> mTitleEntries) {
        this.mTitleEntries = mTitleEntries;
    }

    //当前所处位置
    private int mCurrentPosition = 0;
    private int mCurrentFirstPosition = 1; //0--ChooseConferenceFragmetn，1--DynamicHomeActionFragment

//  private ChooseConferenceFragment mChooseConferenceFragment;
  public NewDynamicHomeFragment mDynamicHomeFragment;
    private ScenicXiuFragment mShowFragment;
    private PersonCenterFragment mMeFragment;
    private MessageStationActionFragment mMessageStationFragment;

    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;

    protected List<Ad> mAdList;
    private long timeOut = 0;

    /**
     * Jpush 推送数据，判断是否在主页
     **/
    public static boolean isForeground = false;

    private static final int MSG_DISMISS_AD = 0X0001;
    private static final int MSG_UPDATE_INFO = 0X0002;

    public static final int REQUEST_SCANE = 0X0003;
    public static final int REQUEST_VIEW = 0X0004;

    private String mUpdateMsg = "";

    private Stack<TitleEntry> mTitleEntries = new Stack<TitleEntry>();
    private SharedPreferences preferences;
    private IWXAPI api;

    private static boolean stopTimer = false;

    //本地的icon.txt地址
    private String mIconFilePath = "";
    //当前的页面位置
    private int mCurrentHomePosition = DYNAMIC_POSITION;
    private static final int DYNAMIC_POSITION = 1; //动态首页
    private static final int STATIC_POSITION = 2;   //静态首页

    protected Handler mPdHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_AD:
                    hideAdAndSkip();
                    break;
                case MSG_UPDATE_INFO:
                    if (!StringUtils.isEmpty(mUpdateMsg)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle(getString(R.string.dialog_tips)).setMessage(mUpdateMsg).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setCancelable(false).show();
                    }
                    break;
            }
        }
    };

    public void performBackClick() {
        mBackButton.performClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        initData();
        initViews();
        initEvents();
        initJpush();
        if (AppApplication.isUserLogIn()) {
            initRefreshUser();
        }

        EventBus.getDefault().register(this);
        Uri uri=getIntent().getData();
        if(uri!=null){
            try {
                String url = uri.getQueryParameter("url");
                String title = URLDecoder.decode(uri.getQueryParameter("title"), "UTF-8");
                int type = Integer.parseInt(uri.getQueryParameter("isShare"));
                CollegeActivity.startCitCollegeActivity(HomeActivity.this, title,url,type);
                Log.e("GYW",url+"---"+title+"---"+type);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        startService(new Intent(getApplicationContext(), AdService.class));

        mFragmentManager = getSupportFragmentManager();
//        mHomeFragment = new HomeFragment();
//        mChooseConferenceFragment = new ChooseConferenceFragment();
//        mDynamicHomeFragment = new DynamicHomeFragment();

        //检测本地是否有动态布局的包，有的话加载动态首页，没有则加载本地页面
        mIconFilePath = AppApplication.instance().getSDPath() + Constants.FILESDIR + "/icon.txt";

        if(FileUtils.isFileExist(mIconFilePath)) {
            mDynamicHomeFragment = new NewDynamicHomeFragment();
            mCurrentFragment = mDynamicHomeFragment;
            addFragment(mDynamicHomeFragment,true);
            mCurrentHomePosition = DYNAMIC_POSITION;
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Constants.PREFERENCE_DB_VERSION, 0);
            editor.commit();
            startActivity(new Intent(this,SplashActivity.class));
            finish();
        }

//        if(Constants.HAS_COMPAS) {
//            mCurrentFragment = mChooseConferenceFragment;
//            addFragment(mChooseConferenceFragment, true);
//            setTitleEntry(false, false, false, null, R.string.app_name, true, true, false, true);
//        }else {
//            mCurrentFragment = mDynamicHomeFragment;
//            addFragment(mDynamicHomeFragment, true);
//            setTitleEntry(false, false, false, null, R.string.app_name, true, true, false, true);
//        }

        setTitleEntry(true, false, false, null, R.string.app_name, true, true, false, true,null,true);

        //判断是否从推送跳入，需要直接打开webview
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String url = bundle.getString("url");
                String title = bundle.getString("title");
                long notificationId = bundle.getLong("notificationId");
                if (!StringUtils.isEmpty(url))
                    WebViewContainerActivity.startWebViewContainerActivity(HomeActivity.this, url, title);
                JPushInterface.removeLocalNotification(HomeActivity.this, notificationId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //显示数据包的更新信息
        if (getIntent().getBooleanExtra("isNeedShowMsg", false)) {
            CHYHttpClientUsage.getInstanse().doUpdateInfo(AppApplication.conId + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        mUpdateMsg = response.getString("showMsg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPdHandler.sendEmptyMessage(MSG_UPDATE_INFO);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }

        if (AppApplication.userId != -1) {
            String token = SharePreferenceUtils.getUser(Constants.USER_RONG_TOKEN);

            if (TextUtils.isEmpty(token)) {
                CHYHttpClientUsage.getInstanse().doGetToken(AppApplication.userId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        LogUtils.println("response:" + response);
                        try {
                            int state = response.getInt("state");
                            if (state == 1) {
                                String tokenRes = response.getString("token");
                                if (!TextUtils.isEmpty(tokenRes)) {
                                    SharePreferenceUtils.saveUserString(Constants.USER_RONG_TOKEN, tokenRes);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        LogUtils.println("response:" + errorResponse);
                    }
                });
            }
        }

        //注册登录和退出登录的广播

    }

    /*
     * 根据用户id更新用户信息
     */
    private void initRefreshUser() {
        String mobile = SharePreferenceUtils.getUser(Constants.USER_MOBILE);
        String trueName = SharePreferenceUtils.getUser(Constants.USER_NAME);
        try {
            trueName = URLEncoder.encode(trueName, Constants.ENCODING_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String lan = "";
        if(AppApplication.systemLanguage == 1){
            lan = "cn";
            if(!mobile.equals("")){
                CHYHttpClientUsage.getInstanse().doGetMobileUserInfoByMobile(mobile,trueName,lan,AppApplication.conId+"",Constants.PROJECT_NAME, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            int state = response.getInt("state");
                            if (state == 1) {
                                ParseUser.saveUserInfo(response.toString());
                                /*Gson gson = new Gson();
                                UserInfoBean user = gson.fromJson(response.toString(), UserInfoBean.class);
                                AppApplication.setSPStringValue(Constants.USER_NAME, user.getName());
                                AppApplication.setSPStringValue(Constants.USER_IMG, user.getImg());
                                AppApplication.setSPStringValue(Constants.USER_MOBILE, user.getMobilePhone());
                                AppApplication.setSPIntegerValue(Constants.USER_ID, user.getUserId());
                                AppApplication.setSPIntegerValue(Constants.USER_TYPE, user.getUserType());
                                AppApplication.setSPIntegerValue(Constants.USER_FACULTYID, user.getFacultyId());
                                AppApplication.setSPBooleanValue(Constants.USER_IS_LOGIN, true);
                                AppApplication.userId = user.getUserId();
                                AppApplication.username = user.getName();
                                AppApplication.userType = user.getUserType();
                                AppApplication.facultyId = user.getFacultyId();*/
                                if(mMeFragment!=null){
                                    mMeFragment.showLoginResult();
                                }
                            } else {
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else{
            lan = "en";
            if(!mobile.equals("")){
                CHYHttpClientUsage.getInstanse().doGetEmailUserInfoByMobile(mobile,trueName,lan,AppApplication.conId+"",Constants.PROJECT_NAME, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            int state = response.getInt("state");
                            if (state == 1) {
                                ParseUser.saveUserInfo(response.toString());
                                if(mMeFragment!=null){
                                    mMeFragment.showLoginResult();
                                }
                            } else {
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        getDialog();
    }

    @Override
    protected void initViewsAction() {
    }
    private void getDialog() {
        int version = ConvertUtil.convertToInt(SharePreferenceUtils.getApp("dialogversion"),0);
        final int count = ConvertUtil.convertToInt(SharePreferenceUtils.getApp("dialogcount"),0);

        CHYHttpClientUsage.getInstanse().doGetAlertAd(version, new JsonHttpResponseHandler("gbk") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int state = response.getInt("state");
                    int openState = response.getInt("openState");
                    if(openState == 1){
                        if(state == 1){
                            int count = response.getInt("count")-1;
                            ParseUser.saveDataInfo(response.toString());
                            Intent intent = new Intent(HomeActivity.this,DialogActivity.class);
                            intent.putExtra("imgUrl",response.getString("picUrl"));
                            intent.putExtra("time",response.getInt("time"));
                            intent.putExtra("linkUrl",response.getString("linkUrl"));
                            intent.putExtra("alertAdId",response.getInt("alertAdId"));
                            intent.putExtra("alertAdName",response.getString("alertAdName"));
                            startActivity(intent);
                        }else{
                            if(count != 0 && state != 3){
                                int counttow = ConvertUtil.convertToInt(SharePreferenceUtils.getApp("dialogcount"),0)-1;
                                SharePreferenceUtils.saveAppString("dialogcount",counttow+"");
                                Intent intent = new Intent(HomeActivity.this,DialogActivity.class);
                                intent.putExtra("imgUrl",SharePreferenceUtils.getApp("dialogimgUrl"));
                                intent.putExtra("time",SharePreferenceUtils.getApp("dialogtime"));
                                intent.putExtra("linkUrl",SharePreferenceUtils.getApp("dialoglinkUrl"));
                                intent.putExtra("alertAdId",SharePreferenceUtils.getApp("dialogalertAdId"));
                                intent.putExtra("alertAdName",SharePreferenceUtils.getApp("dialogalertAdName"));
                                startActivity(intent);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 将JPush的registerId发送给服务端，方便服务端进行推送
     */
    private void initJpush() {
        final String registrationID = JPushInterface.getRegistrationID(this);
        CHYHttpClientUsage.getInstanse().doSendToken(AppApplication.conId + "", Constants.TYPE_ANDROID, registrationID,AppApplication.userId+"", new JsonHttpResponseHandler("gbk") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void initEvents() {
        mBackButton.setOnClickListener(this);
        mHomeButton.setOnClickListener(this);
        mBackButtonPanel.setOnClickListener(this);
        mHomeButtonPanel.setOnClickListener(this);
    }

    private void initViews() {

        mBackButton = (ImageView) findViewById(R.id.title_back);
        mBackButtonPanel = (LinearLayout) findViewById(R.id.title_back_panel);

        mHomeButton = (Button) findViewById(R.id.title_home);
        mHomeButtonPanel = (LinearLayout) findViewById(R.id.title_home_panel);

        mTitleView = (TextView) findViewById(R.id.title_text);
        mTitleView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/zd.TTF"));
        mCoustomRightView = (LinearLayout) findViewById(R.id.title_right_custom_view);
        mCustomTitleView = (LinearLayout) findViewById(R.id.title_center_custom_view);
        mTitleContainer = (RelativeLayout) findViewById(R.id.title_container);

        mHomeGuide = (ImageView) findViewById(R.id.home_guide);

        mNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

//        mDynamicHomeFragment = new DynamicHomeFragment();
        View scenicTitle = CommonUtils.initView(HomeActivity.this, R.layout.scenic_xiu_title);
        LinearLayout mlayout = (LinearLayout) scenicTitle.findViewById(R.id.ll_senic_xiu_title);
        mShowFragment = new ScenicXiuFragment();
        mShowFragment.setScenicXiuTitle(mlayout);
        mMeFragment = new PersonCenterFragment();
        mNavigationBar.setBackgroundStyle(BACKGROUND_STYLE_STATIC);
        mNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mNavigationBar.setBarBackgroundColor(R.color.theme_bg);
        mNavigationBar.setActiveColor(R.color.white);
        mNavigationBar.setInActiveColor(R.color.theme_icon);
        mBadgeItem = new BadgeItem()
                .setBorderWidth(12)
                .setBackgroundColor(Color.RED)
                .setText("  ")
                .setHideOnSelect(false);
        mNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bottom_home, R.string.bottom_home))
                .addItem(new BottomNavigationItem(R.drawable.bottom_boke, R.string.bottom_broadcast))
                .addItem(new BottomNavigationItem(R.drawable.bottom_message, R.string.bottom_message).setBadgeItem(mBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.bottom_me, R.string.bottom_me))
                .initialise();

        mNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position != mCurrentPosition) {
                    switch (position) {
                        case 0:
                            mHomeGuide.setVisibility(View.GONE);
//                            if(Constants.HAS_COMPAS) {
//                                if (mDynamicHomeFragment == null)
//                                    mDynamicHomeFragment = new DynamicHomeFragment();
//                                if (mCurrentFirstPosition == 0) {
//                                    mCurrentFirstPosition = 0;
//                                    switchContent(mChooseConferenceFragment);
//                                } else {
//                                    mCurrentFirstPosition = 1;
//                                    switchContent(mDynamicHomeFragment);
//                                }
//                            }else {
//                                if (mDynamicHomeFragment == null)
//                                    mDynamicHomeFragment = new DynamicHomeFragment();
//
//                                switchContent(mDynamicHomeFragment);
//                            }
                            switchContent(mDynamicHomeFragment);
                            break;
                        case 1:
                            MobclickAgent.onEvent(HomeActivity.this, Constants.EVENT_ID_BROADCAST);
                            if (mShowFragment == null) {
                                View scenicTitle = CommonUtils.initView(HomeActivity.this, R.layout.scenic_xiu_title);
                                LinearLayout mlayout = (LinearLayout) scenicTitle.findViewById(R.id.ll_senic_xiu_title);
                                mShowFragment = new ScenicXiuFragment();
                                mShowFragment.setScenicXiuTitle(mlayout);
                            }
                            switchContent(mShowFragment);
                            break;
                        case 2:
                            mBadgeItem.hide();
                            mHomeGuide.setVisibility(View.GONE);
                            if(mMessageStationFragment == null) {
                                mMessageStationFragment = new MessageStationActionFragment();
                            }
                            switchContent(mMessageStationFragment);
                            break;
                        case 3:
                            mHomeGuide.setVisibility(View.GONE);
                            MobclickAgent.onEvent(HomeActivity.this, Constants.EVENT_ID_PERSON);
                            if (mMeFragment == null) {
                                mMeFragment = new PersonCenterFragment();
                            }
                            switchContent(mMeFragment);
                            break;
                    }
                    mCurrentPosition = position;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
//                if(Constants.HAS_COMPAS) {
//                    if (position == 0 && mCurrentFirstPosition == 1) {
//                        mCurrentFirstPosition = 0;
//                        switchContent(mChooseConferenceFragment);
//                    }
//                }
            }
        });
        if(!AppApplication.instance().NetWorkIsOpen()){
            mBadgeItem.hide();
        }
        mHomeGuide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeGuide.setVisibility(View.GONE);
            }
        });
    }

    public void addFragment(BaseFragment fragment, boolean save) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment.setCallBack(this);
        transaction.replace(R.id.maincontainer, fragment);
        if (save) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    public void addFragment(BaseFragment oldfragment, BaseFragment newfragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        newfragment.setCallBack(this);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.hide(oldfragment);
        transaction.add(R.id.maincontainer, newfragment);
        transaction.show(newfragment);
        transaction.addToBackStack(newfragment.getClass().getSimpleName());
        transaction.commit();
    }

    /***
     * 设置Title Bar
     *
     * @param title
     */
    public void setTitleBar(TitleEntry title) {
        //返回按钮是否显示
        if (title.isShowBack()) {
            mBackButton.setVisibility(View.VISIBLE);
            mBackButton.setClickable(true);
            mBackButtonPanel.setVisibility(View.VISIBLE);
            mBackButtonPanel.setClickable(true);
        } else {
            mBackButton.setVisibility(View.INVISIBLE);
            mBackButton.setClickable(false);
            mBackButtonPanel.setVisibility(View.INVISIBLE);
            mBackButtonPanel.setClickable(false);
        }

        //标题是否显示
        if (title.isShowHome()) {
            mHomeButton.setVisibility(View.VISIBLE);
            mHomeButtonPanel.setVisibility(View.VISIBLE);
            mHomeButtonPanel.setClickable(true);
            mHomeButton.setClickable(true);
        } else {
            mHomeButton.setVisibility(View.INVISIBLE);
            mHomeButtonPanel.setVisibility(View.INVISIBLE);
            mHomeButtonPanel.setClickable(false);
            mHomeButton.setClickable(false);
        }

        //自定义view是否显示
        if (title.isShowCoustomView()) {
            mHomeButton.setVisibility(View.INVISIBLE);
            mCoustomRightView.removeAllViews();
            mCoustomRightView.setVisibility(View.VISIBLE);
            if (title.getCoustomView() != null) {
                if (title.getCoustomView().getParent() == null) {
                    mCoustomRightView.addView(title.getCoustomView());
                }
            }
        } else {
            mCoustomRightView.removeAllViews();
            mCoustomRightView.setVisibility(View.INVISIBLE);
            if (mTitleEntries.size() >= 3) {
                mHomeButtonPanel.setVisibility(View.VISIBLE);
                mHomeButton.setVisibility(View.VISIBLE);
                mHomeButtonPanel.setClickable(true);
                mHomeButton.setClickable(true);
            }
        }

        if (title.isShowCustomTitleView()) {
            mTitleView.setVisibility(View.GONE);
            mCustomTitleView.removeAllViews();
            mCustomTitleView.setVisibility(View.VISIBLE);
            if (title.getCustomTitleView() != null)
                mCustomTitleView.addView(title.getCustomTitleView());
        } else {
            mTitleView.setVisibility(View.VISIBLE);
            mCustomTitleView.removeAllViews();
            mCustomTitleView.setVisibility(View.INVISIBLE);
        }

        if (title.getTitle() == 0) {
            mTitleView.setText(title.getTitleString());
        } else {
            mTitleView.setText(title.getTitle());
        }

        if (title.isShowtitlebar()) {
            mTitleContainer.setVisibility(View.VISIBLE);
        } else {
            mTitleContainer.setVisibility(View.GONE);
        }

        //是否显示底部的操作栏
        if (title.isShowNavigationBottom()) {
            mNavigationBar.setVisibility(View.VISIBLE);
        } else {
            mNavigationBar.setVisibility(View.GONE);
        }
    }

    /**
     * @param showBack        返回键是否显示
     * @param showHome        小房子是否显示
     * @param showCoustomView 自定义右上方是否显示
     * @param coustomView     自定义右上方UI
     * @param title           标题
     * @param adTop           上方广告
     * @param adBottom        下方广告
     * @param showtitlebar    是否显示上方操作栏
     */
    public void setTitleEntry(boolean showBack, boolean showHome,
                              boolean showCoustomView, View coustomView, int title,
                              boolean adTop, boolean adBottom, boolean showtitlebar, boolean isNavBottomShow) {
        TitleEntry titleEntry = new TitleEntry();
        if (coustomView != null)
            titleEntry.setCoustomView(coustomView);
        titleEntry.setShowBack(showBack);
        titleEntry.setShowHome(showHome);
        titleEntry.setShowCoustomView(showCoustomView);
        titleEntry.setTitle(title);
        titleEntry.setAdTop(adTop);
        titleEntry.setAdBottom(adBottom);
        titleEntry.setShowtitlebar(showtitlebar);
        titleEntry.setShowNavigationBottom(isNavBottomShow);

        mTitleEntries.push(titleEntry);
    }

    /**
     * 添加定制TitleView
     */
    public void setTitleEntry(boolean showBack, boolean showHome,
                              boolean showCoustomView, View coustomView, int title,
                              boolean adTop, boolean adBottom, boolean showtitlebar, boolean showCustomTitleView, View customTitleView, boolean isNavBottomShow) {
        TitleEntry titleEntry = new TitleEntry();
        if (coustomView != null)
            titleEntry.setCoustomView(coustomView);
        if (customTitleView != null)
            titleEntry.setCustomTitleView(customTitleView);
        titleEntry.setShowBack(showBack);
        titleEntry.setShowHome(showHome);
        titleEntry.setShowCoustomView(showCoustomView);
        titleEntry.setShowCustomTitleView(showCustomTitleView);
        titleEntry.setTitle(title);
        titleEntry.setAdTop(adTop);
        titleEntry.setAdBottom(adBottom);
        titleEntry.setShowtitlebar(showtitlebar);
        titleEntry.setShowNavigationBottom(isNavBottomShow);
        mTitleEntries.push(titleEntry);
    }

    public void setTitleEntry(boolean showBack, boolean showHome,
                              boolean showCoustomView, View coustomView, String title,
                              boolean adTop, boolean adBottom, boolean showtitlebar, boolean isNavBottomShow) {
        TitleEntry titleEntry = new TitleEntry();
        titleEntry.setCoustomView(coustomView);
        titleEntry.setShowBack(showBack);
        titleEntry.setShowHome(showHome);
        titleEntry.setShowCoustomView(showCoustomView);
        titleEntry.setTitleString(title);
        titleEntry.setAdTop(adTop);
        titleEntry.setAdBottom(adBottom);
        titleEntry.setShowtitlebar(showtitlebar);
        titleEntry.setShowNavigationBottom(isNavBottomShow);
        mTitleEntries.push(titleEntry);
        setTitleBar(titleEntry);
    }



    @Override
    public void onClick(View v) {
        FragmentManager manager = getSupportFragmentManager();
        switch (v.getId()) {
            //返回控制
            case R.id.title_back:
            case R.id.title_back_panel:
                hideShurufa();
                manager.popBackStackImmediate();
                mTitleEntries.pop();
                setTitleBar(mTitleEntries.peek());
//              mPreFragment.setUserVisibleHint(false);
                break;
        }
    }

    /**
     * 隐藏广告和跳过
     */
    private void hideAdAndSkip() {
        mNavigationBar.show();
        mTitleEntries.get(mTitleEntries.size() - 1).setShowNavigationBottom(true);

        //显示引导
        if(SharePreferenceUtils.getSPBooleanValueF(Constants.SHOW_HOME_BACK_GUIDE)) {
            mHomeGuide.setVisibility(View.VISIBLE);
            SharePreferenceUtils.setSPBooleanValue(Constants.SHOW_HOME_BACK_GUIDE, false);
        }
    }

    /**
     * 弹出退出框
     */
    private void showexitdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_tips)).setMessage(R.string.conference_exit_app).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService(new Intent(getApplicationContext(), AdService.class));
                System.exit(100);
            }
        }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            manager.executePendingTransactions();

            if (manager.getBackStackEntryCount() == 1) {
                showexitdialog();
            } else {
                hideShurufa();
                manager.popBackStackImmediate();
                mTitleEntries.pop();
                setTitleBar(mTitleEntries.peek());
                //TODO 这边有时候会发生异常
//                if (mPreFragment != null) {
//                    mPreFragment.setUserVisibleHint(true);
//                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void perfromBackPressTitleEntry() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        mTitleEntries.pop();
        setTitleBar(mTitleEntries.peek());
//        if (mPreFragment != null) {
//            mPreFragment.setUserVisibleHint(true);
//        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), AdService.class));
        android.os.Debug.stopMethodTracing();
        unregisterReceiver(mMessageReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreateViewFinish() {
        setTitleBar(mTitleEntries.peek());
    }

    public void setTitleVisiable(int visiable) {
        mTitleContainer.setVisibility(visiable);
    }

    // 初始化数据
    private void initData() {
        AppApplication.adList = ConferenceDbUtils.getAllAds();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        AppApplication.instance().setDisPlayMetrics(dm);
        mAdList = AppApplication.adList;

        AppApplication.TOKEN_IMEI = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();

        if (AppApplication.instance().NetWorkIsOpen()) {
            init();
            registerMessageReceiver();
        }

        api = WXAPIFactory.createWXAPI(this, Constants.WXKey);
        api.registerApp(Constants.WXKey);

        ((AppApplication) getApplication()).setApi(api);
    }


    @Override
    protected void onResume() {
        isForeground = true;
        if(AppApplication.isUserLogIn()){
            initRefreshUser();
        }
        getHomeNums();
        super.onResume();
        MobclickAgent.onResume(this);
        Intent intent = getIntent();
        if(intent != null&&intent.getIntExtra("detail_for",0)==11){
            setIntent(null);
            SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
            detail.setArguments(intent.getIntExtra("tartgetPosition",0),(ArrayList)intent.getSerializableExtra("mAllSessionsList"));
            View moreView = CommonUtils.initView(this, R.layout.titlebar_session_detail_more);
            detail.setRightListener(moreView);
            addFragment(detail,detail);
            setTitleEntry(true, false, true, moreView, R.string.meeting_schedule_detail_title, false, false, true, false);
        }
    }
    /**
     * 获取数据
     */
    private void getHomeNums() {
        CHYHttpClientUsage.getInstanse().doGetLookCount(AppApplication.conId + "", AppApplication.userId + "", AppApplication.userType + "", AppApplication.TOKEN_IMEI, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int type = response.getInt("tokenMessageCount");
                    if(type != 0){
                        mBadgeItem.show();
                    }else{
                        mBadgeItem.hide();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.android.incongress.cd.conference.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();

        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(LoginActivity.LOGIN_ACTION);
        filter.addAction(LoginActivity.LOGOUT_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String url = bundle.getString("H5URL");
                String title = bundle.getString("H5TITLE");
                String share = bundle.getString("H5SHARE");
                long notificationId = bundle.getLong("notificationId");
                if (!StringUtils.isEmpty(url)) {
                    if (url.contains("?")) {
                        url = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                    } else {
                        url = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                    }
                    int type = 3;
                    if (share.equals("1")) {
                        type = 1;
                    } else {
                        type = 3;
                    }
                    CollegeActivity.startCitCollegeActivity(HomeActivity.this, title, url,type);
                }
                JPushInterface.removeLocalNotification(HomeActivity.this, notificationId);
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    /**
     * 注册JPush广播的接收器
     */
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MESSAGE_RECEIVED_ACTION.equals(action)) {

                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }

                String content = intent.getStringExtra(KEY_MESSAGE);
                String urlJson = intent.getStringExtra(KEY_EXTRAS);
                JSONObject url = null;
                String trueUrlJson = null;
                String trueShareJson = null;
                String trueTitleJson = null;
                try {
                    if(!StringUtils.isEmpty(urlJson)){
                        url = new JSONObject(urlJson);
                        if(urlJson.indexOf("H5URL")!=-1){
                            trueUrlJson = url.getString("H5URL").replace("\\\\", "");
                        }
                        if(urlJson.indexOf("H5SHARE")!=-1){
                            trueShareJson = url.getString("H5SHARE");
                        }
                        if(urlJson.indexOf("H5TITLE")!=-1){
                            trueTitleJson = url.getString("H5TITLE");
                        }
                    }
                    if (trueUrlJson == null) {
                        showPushInfo(HomeActivity.this, content,"", "","");
                    } else {
                        showPushInfo(HomeActivity.this,content ,trueTitleJson, trueUrlJson,trueShareJson);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(action.equals(LoginActivity.LOGIN_ACTION)) {
                //登录成功
                if(mDynamicHomeFragment != null && Constants.IS_SECRETARY_SHOW) {
                    //显示专家秘书
                    mDynamicHomeFragment.showSecretaryView();
                }
            }else if(action.equals(LoginActivity.LOGOUT_ACTION)) {
                //退出登录
                if(mDynamicHomeFragment != null) {
                    //隐藏专家秘书
                    mDynamicHomeFragment.hideSecretaryView();
                }
            }
        }
    }

    /**
     * 弹出退出框
     */
    private void showPushInfo(final Context context,final String message, final String title, final String url,final String share) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.push_tips)).setMessage(message).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JPushInterface.clearAllNotifications(AppApplication.getContext());
                String webUrl = url;
                if (!StringUtils.isEmpty(webUrl)){
                    if(webUrl.contains("?")) {
                        webUrl = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                    }else {
                        webUrl = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                    }
                    int type = 3;
                    if(share != null){
                        if(!share.equals("1")){
                            type = 3;
                        }else{
                            type = 1;
                        }
                    }
                    CollegeActivity.startCitCollegeActivity(HomeActivity.this, title, webUrl,type);
                }
            }
        }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false);
        dialog = builder.show();
    }

    private AlertDialog dialog;
    public void switchContent(Fragment to) {
        if (mCurrentFragment != to) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mCurrentFragment).add(R.id.maincontainer, to).commit();
            } else {
                transaction.hide(mCurrentFragment).show(to).commit();
            }
            mCurrentFragment = to;
        }
    }

    public void setFirstPosition(int position) {
        mCurrentFirstPosition = position;
    }

    /**
     *
     */
    public static class UpdateConferenceEvent {
        public int conId;
        private boolean isExist;
        private boolean isNeedUpdate;

        public UpdateConferenceEvent(int conId, boolean isExist, boolean isNeedUpdate) {
            this.conId = conId;
            this.isExist = isExist;
            this.isNeedUpdate = isNeedUpdate;
        }
    }

    /**
     * 收到更新通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateConferenceEvent(UpdateConferenceEvent event) {
//        if(mChooseConferenceFragment != null) {
//            mChooseConferenceFragment.updateConferenceData();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_SCANE) {
                Bundle extras = data.getExtras();
                if(extras != null) {
                    String posterJson = extras.getString(CodeUtils.RESULT_STRING);

                    //壁报类型
                    if(posterJson.contains("{\"posterId\":")) {
                        try {
                            JSONObject obj = new JSONObject(posterJson);
                            String posterId = obj.getString("posterId");

                            CHYHttpClientUsage.getInstanse().doGetPosterByID(AppApplication.conId, posterId, AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler("gbk"){
                                @Override
                                public void onStart() {
                                    super.onStart();
                                    showProgressBar("正在获取壁报信息");
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    dismissProgressBar();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    ToastUtils.showShorToast("获取失败");
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    LogUtils.println("response:" + response);
                                    Gson gson = new Gson();
                                    PosterBean posterBean = gson.fromJson(response.toString(), PosterBean.class);

                                    if(posterBean.getState() == 1) {
                                        DZBBBean dzbb = new DZBBBean(posterBean.getPosterId(),posterBean.getPosterCode(), posterBean.getConField(), posterBean.getTitle(), posterBean.getAuthor(), posterBean.getPosterPicUrl(),posterBean.getMaxCount(),posterBean.getDisCount(),posterBean.getIsJingxuan());
                                        Intent intent = new Intent();
                                        intent.setClass(HomeActivity.this, PosterImageFragment.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("bean",dzbb);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else {
                                        ToastUtils.showShorToast("未找到该电子壁报，可能已被删除");
                                    }
                                }
                            });

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(posterJson.contains("http://") || posterJson.contains("https://")){

                        if(posterJson.contains("?")) {
                            posterJson = posterJson + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                        }else {
                            posterJson = posterJson + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                        }
                        CollegeActivity.startCitCollegeActivity(HomeActivity.this, "", posterJson);
                    }else {
                        showDialog(posterJson,null,null, false);
                    }
                }
            }else if(requestCode == PersonCenterFragment.REQUEST_LOGIN){
                mMeFragment.showLoginResult();
            } else{
                Bundle extras = data.getExtras();
                if(extras != null) {
                    String result = extras.getString(CodeUtils.RESULT_STRING);

                    Pattern pattern = Pattern
                            .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");

                    if(pattern.matcher(result).matches()) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri url = Uri.parse(result);
                        intent.setData(url);
                        startActivity(intent);
                    }else {
                        ToastUtils.showShorToast(result);
                    }

                }
            }
        }
    }
}
