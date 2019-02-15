package com.android.incongress.cd.conference.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.IncongressBean;
import com.android.incongress.cd.conference.model.Ad;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.services.AdService;
import com.android.incongress.cd.conference.utils.CrashHandler;
import com.android.incongress.cd.conference.utils.GlideImageLoader;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.widget.zxing.activity.ZXingLibrary;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.loopj.android.http.AsyncHttpClient;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.List;
import java.util.Locale;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class AppApplication extends LitePalApplication {

    private static AppApplication instance;
    private static Context mContext;

    //EsmoId
    public static String COMPAS_ID = "1";

    //表明类型 在初始化数据时会用
    public static int conType = 2;

    //0表明是游客  1表明是用户，2绑定注册码的用户，, 3是专家
    public static int userType = 0;

    //-1为普通用户 其余均为专家用户
    public static int facultyId = -1;
    //用户id
    public static int userId = -1;

    //用户真实姓名
    public static String username = "";

    //初始化数据的Bean
    public static IncongressBean conBean = new IncongressBean();

    //1代表 中文  2代表英文 其他值  代表随系统变化
    public static int systemLanguage = 3;

    public static Typeface mTypeface = null;

    private static IWXAPI api;

    /**
     * 设备的唯一token
     **/
    public static String TOKEN_IMEI = "";
    /**
     * 上方广告序号
     */
    public static int topNum = -1;

    /**
     * 下方广告序号
     */
    public static int bottomNum = -1;

    /**
     * 广告列表
     */
    public static List<Ad> adList = null;

    private static DisplayMetrics mDisplayMetrics;
    public static AppApplication instance() {
        return instance;
    }
    public static Context getContext() {
        return mContext;
    }
    public static IWXAPI wxApi;

    public static AsyncHttpClient mHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this;

//        OkHttpFinalConfiguration.Builder okhttpBuilder = new OkHttpFinalConfiguration.Builder();
//        OkHttpFinal.getInstance().init(okhttpBuilder.build());

       /* * 微信分享初始化 **/
        PlatformConfig.setWeixin("wxd40d6655d0199e18", "37325ffce412660d98a02fdd3e7a2617");

        /**新浪分享初始化**/
        PlatformConfig.setSinaWeibo("4015025148", "458be4e8e2dbce03700b155aef9c8123","https://api.weibo.com/oauth2/default.html");

        wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APPID, false);
        wxApi.registerApp(Constants.WX_APPID);

        mHttpClient = new AsyncHttpClient();
        mHttpClient.setMaxConnections(10);
        mHttpClient.setMaxRetriesAndTimeout(3, 20000);

        //设置字体不受系统限制
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

//        //设置app语言为英文
//        Resources resources = getContext().getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        Configuration config = resources.getConfiguration();
//        config.locale = Locale.ENGLISH;
//        resources.updateConfiguration(config, dm);

        CrashHandler.getInstance().init(getApplicationContext());

        if (systemLanguage != 1) {
            mTypeface = Typeface.createFromAsset(getAssets(), "Arial.ttf");
        }

        //Jpush初始化操作
        JPushInterface.setDebugMode(Constants.isDebug);
        JPushInterface.init(this);

        CustomPushNotificationBuilder builder = new
                CustomPushNotificationBuilder(getApplicationContext(),
                R.layout.item_notitfication,
                R.id.icon,
                R.id.title,
                R.id.text);
        builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.layoutIconDrawable = R.drawable.ic_launcher;
        JPushInterface.setPushNotificationBuilder(2, builder);

        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(21,41,58))
                .setTitleBarTextColor(Color.WHITE)
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setMutiSelectMaxSize(9)
                .build();
        //配置imageloader
        GlideImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(mContext, imageloader, theme).setFunctionConfig(functionConfig).build();
        //相册配置
        GalleryFinal.init(coreConfig);
        //配置一下zxing的初始化工作
        ZXingLibrary.initDisplayOpinion(this);

        //初始化用户数据
        ParseUser.initUserInfo();
        /*if (StringUtils.isAllNotEmpty(SharePreferenceUtils.getUser(Constants.USER_NAME),
            SharePreferenceUtils.getUser(Constants.USER_ID) , SharePreferenceUtils.getUser(Constants.USER_TYPE))) {
            AppApplication.userId = AppApplication.getSPIntegerValue(Constants.USER_ID);
            AppApplication.userType = AppApplication.getSPIntegerValue(Constants.USER_TYPE);
            AppApplication.username = AppApplication.getSPStringValue(Constants.USER_NAME);
            AppApplication.facultyId = AppApplication.getSPIntegerValue(Constants.USER_FACULTYID);
        }*/
        //不默认统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        //初始化保利威视
        initPolyvCilent();
    }
    public static AppApplication getInstance(){
        if(instance == null){
            instance = new AppApplication();
        }
        return instance;
    }

    /**
     *
     corePoolSize  线程池中核心线程的数量
     maximumPoolSize  线程池中最大线程数量
     keepAliveTime 非核心线程的超时时长，当系统中非核心线程闲置时间超过keepAliveTime之后，则会被回收。如果ThreadPoolExecutor的allowCoreThreadTimeOut属性设置为true，则该参数也表示核心线程的超时时长
     unit 第三个参数的单位，有纳秒、微秒、毫秒、秒、分、时、天等
     workQueue 线程池中的任务队列，该队列主要用来存储已经被提交但是尚未执行的任务。存储在这里的任务是由ThreadPoolExecutor的execute方法提交来的。
     threadFactory  为线程池提供创建新线程的功能，这个我们一般使用默认即可
     handler 拒绝策略，当线程无法执行新任务时（一般是由于线程池中的线程数量已经达到最大数或者线程池关闭导致的），默认情况下，当线程池无法处理新线程时，会抛出一个RejectedExecutionException。

     这个线程池的规则：
     1.execute一个线程之后，如果线程池中的线程数未达到核心线程数，则会立马启用一个核心线程去执行
     2.execute一个线程之后，如果线程池中的线程数已经达到核心线程数，且workQueue未满，则新线程放入workQueue中等待执行
     3.execute一个线程之后，如果线程池中的线程数已经达到核心线程数但未超过非核心线程数，且workQueue已满，则开启一个非核心线程来执行任务
     4.execute一个线程之后，如果线程池中的线程数已经超过非核心线程数，则拒绝执行任务

     ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(128));
     */
//    private static ExecutorService mPoolExecutor;
//
//    public void executeToPool(Runnable runnable) {
//        if(mPoolExecutor == null)
//            mPoolExecutor = Executors.newSingleThreadExecutor();
//        mPoolExecutor.execute(runnable);
//    }

    public String getImei() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    public static AsyncHttpClient getHttpClient() {
        return mHttpClient;
    }

    public boolean NetWorkIsOpen() {
        boolean flag = false;
        //判断网络是否连接
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info != null) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }
    /**
     * 判断用户是否是登录状态
     * @return
     */
    public static boolean isUserLogIn() {
        return SharePreferenceUtils.getUserBoolean(Constants.USER_IS_LOGIN,false) == true;
    }

    public void setDisPlayMetrics(DisplayMetrics mDisplayMetrics) {
        this.mDisplayMetrics = mDisplayMetrics;
    }

    public DisplayMetrics getDisPlayMetrics() {
        return this.mDisplayMetrics;
    }

    public String getVersionName() {
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    this.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {

        }
        return null;
    }

    public String getSDPath() {
        File file = Environment.getExternalStorageDirectory();
        if(file == null) {
            file = mContext.getCacheDir();
        }
        return file.getAbsolutePath();
    }

    public static String getSystemLanuageCode() {
        if (systemLanguage == 1) {
            return "cn";
        } else {
            return "en";
        }
    }

    public static IWXAPI getApi() {
        return api;
    }

    /**
     * 将微信的分享方法集合进这里做一个简单的封装
     *
     * @param urlStr      点击分享后跳转的链接
     * @param thumb       缩略图
     * @param title       分享标题
     * @param description 分享的描述    这里设置为参会易相应的模块
     * @param transaction 分享的时间    req.transaction = String.valueOf(System.currentTimeMillis());
     * @param secene      分享的形式（朋友/朋友圈）
     * @return 是否成功分享
     */
    public static void ShareToWX(String urlStr, Bitmap thumb, String title, String description, String transaction, int secene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = urlStr;   //分享后的链接
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = title;
        msg.description = description;

        //一直都分享失败主要是因为这个分享的图标不能超过32KB，想来想去可能就是这里出的问题。
        msg.setThumbImage(thumb);
        msg.thumbData = null;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = secene;//1代表朋友圈，2代表朋友
        boolean isSuccess = api.sendReq(req);//这个返回值仅仅是调用是否成功的返回值
    }

    public void setApi(IWXAPI api) {
        this.api = api;
    }

    /**
     * 异常退出时候使用
     */
    public void stopService() {
        stopService(new Intent(getApplicationContext(), AdService.class));
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }
    /**
     * 循环设置字体格式
     * @param context
     * @param root
     * @param fontName
     */
    public static void applyFont(final Context context, final View root, final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        } catch (Exception e) {
            LogUtils.println(String.format("Error occured when trying to apply %s font for %s view", fontName, root));
            e.printStackTrace();
        }
    }
    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
    /** 加密秘钥 */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /** 加密向量 */
    private String iv = "2u9gDPKdX6GyQJKU";

    public void initPolyvCilent() {
        //网络方式取得SDK加密串，（推荐）
        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //使用SDK加密串来配置
        client.setConfig("POft5F5piX+CUvwVC99ENyorruGGChHaMuY4es1RvZyxOYYxProEkZf9QMh+fXplYijSAAssN1JECVJoM9oyaiS80VBy1nCW4JLy/4i+ATTcH4cxkzAl7GtUkBbq+DMO+aGDGI6tHVeDQXEak5NwhA==", aeskey, iv, getApplicationContext());
        //初始化SDK设置
        client.initSetting(getApplicationContext());
        //启动Bugly
        client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
    }


}
