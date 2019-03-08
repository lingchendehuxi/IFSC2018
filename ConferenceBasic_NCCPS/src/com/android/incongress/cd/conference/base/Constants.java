package com.android.incongress.cd.conference.base;

import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * 说明：参会易中大部分的常量都在这里
 *
 * 友盟中统计的事件
 * 会议日程、差日程、我的日程、参会指南、提问、壁报、XXX学院、参展商、消息站、扫一扫、博客（现场秀)、我
 *
 * 分享动作、发帖动作
 *
 */
public class Constants {

    /** 是否处于调试状态，调试状态都是测试地址；非调试状态都是正是地址 **/
    public static final boolean isDebug = false;
    /** 用户融云的token **/
    public static final String USER_RONG_TOKEN = "userRongToken";

    public static final String CLIENT_TYPE = "2";

    public static final String CONFERENCE_ID = "conferenceId";

    //微信ID
    public static final String WX_APPID = "wxd40d6655d0199e18";
    public static final String APP_SECRET = "37325ffce412660d98a02fdd3e7a2617";
    //每个数据列表显示的最多数
    public static final String MAXDATA = "10";

    public static final String EVENT_ID_MEETING_SCHEDULE = "meeting_schedule";
    public static final String EVENT_ID_SEARCH_SCHEDULE = "search_schedule";
    public static final String EVENT_ID_MY_SCHEDULE = "my_schedule";
    public static final String EVENT_ID_MEETING_GUIDE = "meeting_guide";
    public static final String EVENT_ID_SCANE = "scane";
    public static final String EVENT_ID_FACULTY_TRIP = "faculty_trip";
    public static final String EVENT_ID_QUESTION = "question";
    public static final String EVENT_ID_POSTER = "poster";
    public static final String EVENT_ID_COLLEGE = "college";
    public static final String EVENT_ID_EXHIBITOR = "exhibitor";
    public static final String EVENT_ID_MESSAGE =  "message_station";
    public static final String EVENT_ID_BROADCAST =  "broadcast";
    public static final String EVENT_ID_PERSON = "personCenter";
    public static final String EVENT_ID_SHARE = "share";
    public static final String EVENT_ID_MAKE_POST = "make_post";
    public static final String VIDEO_DETIAL_BEAN = "video_data_bean"; //火速上线视频列表
    public static final String BOOK_VIDEO_DETIAL_BEAN = "book_video_data_bean";  //可预约的课件可播放的视频

    public static final String  ACTIVITY_SPLASH = "启动页"; // splashActivity
    public static final String ACTIVITY_CONFERENCES = "选择大会页面"; // conferencesActivity
    public static final String ACTIVITY_ADVERTISE = "广告页";// advertiseActivity
    public static final String ACTIVITY_GUIDE = "引导页"; // guideActivity
    public static final String FRAGMENT_DYNAMICHOME = "动态首页";// dynamicHomeFragment
    public static final String FRAGMENT_SCENICXIU = "播客，现场秀";// scenicXiuFragment
    public static final String FRAGMENT_PERSONCENTER ="个人中心";// personCenterFragment
    public static final String FRAGMENT_MESSAGESTATION = "消息站";// messageStationFragment
    public static final String FRAGMENT_MEETINGSCHEDULELIST = "看日程列表模式";// meetingScheduleListFragment
    public static final String FRAGMENT_COLLEGETEXTLIST = "CIT学院文章列表模式";// CollegeListDetailFragment
    public static final String FRAGMENT_MEETINGSCHEDULECALENDAR = "看日程日历模式";// meetingScheduleCanlendarFragment
    public static final String FRAGMENT_SESSIONDETAIL = "日程详情页";// sessionDetailFragment
    public static final String FRAGMENT_SPEAKERDETAIL = "讲者详情页";// speakerDetailFragment
    public static final String FRAGMENT_LOCATIONMAP ="定位图页面"; // locationMapFragment
    public static final String FRAGMENT_PHOTOALBUM ="照片墙界面";


    public static final String FRAGMENT_SEARCHSCHEDULE = "查日程";// searchScheduleFragment
    public static final String FRAGMENT_SEARCHRESULT = "日程检索结果";// searchResult
    public static final String FRAGMENT_MYSCHEDULE ="我的日程";// myScheduleFragment

    public static final String ACTIVITY_WEBVIEW = "网页";// webviewActivity

    public static final String FRAGMENT_MEETINGGUIDE = "参会指南";// meetingGuideFragment
    public static final String FRAGMENT_MEETINGMAPINFO = "场馆图页面";// meetingMapInfoFragment


    public static final String FRAGMENT_QUESTIONLIST = "提问列表页面";// questionListFragment
    public static final String FRAGMENT_MAKEQUESTION = "提问页面";// makeQuestionFragment

    public static final String FRAGMENT_POSTER = "壁报集"; // posterFragment
    public static final String FRAGMENT_POSTERDETAIL = "壁报详情_标题";// posterDetailFragment

    public static final String ACTIVITY_COLLEGE = "学院页面";// collegeActivity
    public static final String ACTIVITY_COLLEGE_PLAY = "学院页面，播放";// collegeActivity
    public static final String ACTIVITY_COLLEGE_ORDER = "学院页面，预约";// collegeActivity
    public static final String ACTIVITY_COLLEGE_PLAY_BLWS = "学院页面，播放保利威视";// collegeActivity
    public static final String ACTIVITY_COLLEGE_PLAY_BDY = "学院页面，播放百度云";// collegeActivity

    public static final String FRAGMENT_EXHIBITORS = "参展商";// exhibitorsFragment
    public static final String FRAGMENT_EXHIBITORSDETAIL = "参展商详情";// exhibitorDetailFragment

    public static final String ACTIVITY_SCANE = "扫一扫";// scaneActivity

    public static final String FRAGMENT_MAKEPOST = "发帖";// makePostFragment
    /*我的*/
    public static final String MY_ORDER_COURSER = "我的预约";// myMindbooklistfragment


    /**
     * 是否存在compas页面
     */
    public static final boolean HAS_COMPAS = false;

    /****/
    public static final String SHOW_HOME_BACK_GUIDE = "showHomeBackGuide";

    /** loginByCode登录方法中的type参数，type:1,参会者登录；2，普通用户登录 **/
    public static final int LOGIN_TYPE_PATICIPATER = 1;
    public static final int LOGIN_TYPE_NOMAL = 2;

    /** 用户登录名 **/
    public static final String USER_IS_LOGIN = "userLogIn";//用户是否登录
    public static final String USER_NAME = "name";
    public static final String USER_FAMILY_NAME = "familyName";
    public static final String USER_GIVEN_NAME = "givenName";
    public static final String USER_IMG = "img";
    public static final String USER_SEX = "sex";
    /** 用户手机号 **/
    public static final String USER_MOBILE = "userMobile";
    /** 用户登录id **/
    public static final String USER_ID = "userId";
    /** 用户类型 **/
    public static final String USER_TYPE = "userType";
    public static final String USER_FACULTYID = "facultyId";
    public static final String USER_OPENID = "openId";
    public static final String USER_NICK_NAME = "nickName";
    public static final String CONID = "conId";
    public static final String FROMWHERE = "fromWhere";
    /** 会议日程 提醒开关**/
    public static final String LOOK_SCHEDULE_TIPS = "lookScheduleTips";

    /** 查看壁报状态 开关 **/
    public static final String LOOK_POSTER_TIPS = "lookPosterTips";

    /** 我的领域 **/
    public static final String MY_FIELDS = "myFields";

    /** 默认的编译格式 **/
    public static final String ENCODING_UTF8 = "utf-8";

    /** 默认的分页尺寸 **/
    public static final String PAGE_SIZE = "12";

    /** 客户端类型 1 ios 2 android 3 web **/
    public static final String TYPE_ANDROID = "2";

    /** 游客身份 **/
    public static final int TYPE_USER_VISITOR = 0;
    /** 注册用户，未绑定注册码 **/
    public static final int TYPE_USER_REGISTER_NOT_BIND_CODE = 1;
    /** 注册用户，已绑定注册码 **/
    public static final int TYPE_USER_REGISTER_BIND_CODE = 2;
    /** 无敌权限的专家 **/
    public static final int TYPE_USER_FACUTY = 3;

    /** 语言类型 **/
    public static final String LanguageChinese = "cn";
    public static final String LanguageEnglish = "en";

    /** 注册码类型 1注册 2登录**/
    public static final String ConfirmTypeRegister = "1";
    public static final String ConfirmTypeLogin = "2";

    /** 1现场秀 2消息站**/
    public static final String LookTimeScenicXiu = "1";
    public static final String LookTimeMsgStation = "2";

    /** 是否需要引导页 **/
    public static final String NEED_GUIDE = "need_guide2017";
    public static final int NEED_GUIDE_TRUE = 1;
    public static final int NEED_GUIDE_FALSE = 2;

    /** 我的科室 **/
    public static final String MY_KESHI = "my_keshi";

    /**
     * GBK编码
     */
    public static final String ENCODING_GBK = "GBK";

    /** 检测数据库版本 **/
    public static final String PREFERENCE_DB_VERSION = "db_version";
    public static final String DB_frist = "local2";
    /** Compas首页版本号 **/
    public static final String COMPASS_VERSION = "compas_version";

    /** sharepreference中使用 用于获取用户编号) **/
    public static final String PREFERENCE_USER_ID="db_user_id";
    /** sharepreference中使用 用于获取用户姓名 **/
    public static final String PREFERENCE_USER_NAME="db_user_name";
    /** sharepreference中使用 用于获取用户类别(0表明是用户，1表明是游客) **/
    public static final String PREFERENCE_USER_TYPE="db_user_type";

    /** 会议下载位置 后面还要跟上会议id:/conference194/**/
    public static final String FILE_CONFERENCES = "/cd_incongress/";

    /** 数据库名字 **/
    public static final String DB_NAME = "easycondb.db";

    /** 是否需要国际化 字段 **/
    public static final String GUIDE_INTERACTIVE = "guide_interactive";
    public static final String GUIDE_XIU = "guide_xiu";
    public static final String GUIDE_SESSION = "guide_session";

    /** 0-->不关注; 1-->关注 **/
    public static final int NOATTENTION=0;
    public static final int ATTENTION=1;

    /**　中英文内容的特殊分割符　**/
    public static final String ENCHINASPLIT="#@#";

    /**
     * 说明：
     * 1.www开头测试 app和m开头正式
     * 2.app是没有缓存的,m有缓存
     * 3.只有正式的要端口，测试不需要
     * 4.TEST结尾为测试地址，OFFICIAL结尾为正是地址
     */
    //------------------------------------测试地址--------------------------------------//
    private static final String IMAGEPREFIX_TEST="http://www.incongress.cn"; /** 文件和图片下载地址 **/
    private static final String HOST_TEST = "http://www.incongress.cn/Conferences/proxy.do";;/** 服务器地址 **/
    private static final String NEWSPREFIX_TEST="http://114.80.201.49/"; /** 会议之声的下载地址 **/
    private static final String DZBB_TEST = "http://www.incongress.cn/posterAction.do"; /** 电子壁报**/
    private static final String E_CASE_WEBSITE_TEST = "http://incongress.cn/webapp/discussion/CIT2016H5/casesList.html?conId=" + Constants.getConId(); //电子病历

    private static final int CIT_WEBSITE_TEST = R.string.cit_live_url_test; /** CIT 加载地址 **/
    private static final int HD_SESSION_QUESTION_TEST = R.string.hd_question_site_test; /** 现场互动提问地址 **/
    private static final int HD_SESSION_SERVER_TEST = R.string.hd_server_site_test; /** 现场互动调研地址 **/
    private static final int HANDS_ON_SITE_TEST = R.string.hands_on_site_test; /** 实习中心 **/
    private static final int CIT_COLLEGE_TEST = R.string.cit_college_url_test; /** CIT 学院 **/
    private static final int HdSession_QUESTION_LIST_TEST = R.string.hd_question_list_test; /** 提问列表 **/
    private static final  int MEETING_GUIDE_URL_TEST = R.string.meeting_guide_info_test; /** 参会指南 **/

    //------------------------------------正式地址--------------------------------------//
    private static final String IMAGEPREFIX_OFFICIAL="http://app.incongress.cn"; /** 文件和图片下载地址 **/
    private static final String HOST_OFFICIAL = "http://app.incongress.cn/Conferences/proxy.do";/** 服务器地址 **/
    private static final String NEWSPREFIX_OFFICIAL ="http://app.incongress.cn/Conferences"; /** 会议之声的下载地址 **/
    private static final String DZBB_OFFICIAL = "http://m.incongress.cn/posterAction.do"; /** 电子壁报**/
    private static final String E_CASE_WEBSITE_OFFICIAL = "http://m.incongress.cn/webapp/discussion/CIT2016H5/casesList.html?conId=" + Constants.getConId(); /** 电子病例 **/

    private static final int CIT_WEBSITE_OFFICIAL = R.string.cit_live_url_official;  /** CIT 加载地址 **/
    private static final int HD_SESSION_QUESTION_OFFICIAL = R.string.hd_question_site_official; /** 现场互动提问地址 **/
    private static final int HD_SESSION_SERVER_OFFICIAL = R.string.hd_server_site_official; /** 现场互动调研地址 **/
    private static final int HANDS_ON_SITE_OFFICIAL = R.string.hands_on_site_official; /** 实习中心 **/
    private static final int CIT_COLLEGE_OFFICIAL = R.string.cit_college_url_official; /** CIT 学院 **/
    private static final int HdSession_QUESTION_LIST_OFFICIAL = R.string.hd_question_list_official; /** 提问列表 **/
    private static final  int MEETING_GUIDE_URL_OFFICIAL = R.string.meeting_guide_info_officiaol; /** 参会指南 **/
    public static final String BASE_EXAM_TABLE = "/Exam/data?";
    public static final String BASE_LOCAL_LIST = "/sessiongroup.do?";

    public static final String get_HOST() {
        if(isDebug)
            return HOST_TEST;
        else
            return HOST_OFFICIAL;
    }

    public static final String get_NEWSPREFIX() {
        if(isDebug)
            return NEWSPREFIX_TEST;
        else
            return NEWSPREFIX_OFFICIAL;
    }

    public static final int get_CIT_LIVE_URL() {
        if(isDebug)
            return CIT_WEBSITE_TEST;
        else
            return CIT_WEBSITE_OFFICIAL;
    }

    public static final int get_HD_SESSION_QUESTION() {
        if(isDebug) {
            return HD_SESSION_QUESTION_TEST;
        }else {
            return HD_SESSION_QUESTION_OFFICIAL;
        }
    }

    public static final int get_HD_SSSION_SERVER() {
        if(isDebug) {
            return HD_SESSION_SERVER_TEST;
        }else {
            return HD_SESSION_SERVER_OFFICIAL;
        }
    }

    public static final int get_HANDS_ON_SITE() {
        if(isDebug) {
            return HANDS_ON_SITE_TEST;
        }else {
            return HANDS_ON_SITE_OFFICIAL;
        }
    }

    public static final int get_CIT_COLLEGE() {
        if(isDebug) {
            return CIT_COLLEGE_TEST;
        }else {
            return CIT_COLLEGE_OFFICIAL;
        }
    }

    public static final int get_HD_QUESTION_LIST() {
        if(isDebug) {
            return HdSession_QUESTION_LIST_TEST;
        }else {
            return HdSession_QUESTION_LIST_OFFICIAL;
        }
    }


    public static final String get_E_CASE_WEBSITE() {
        if(isDebug)
            return E_CASE_WEBSITE_TEST;
        else
            return E_CASE_WEBSITE_OFFICIAL;
    }

    public static final String get_DZBB() {
        if(isDebug)
            return DZBB_TEST;
        else
            return DZBB_OFFICIAL;
    }

    public static final int get_MEETING_GUIDE() {
        if(isDebug)
            return MEETING_GUIDE_URL_TEST;
        else
            return MEETING_GUIDE_URL_OFFICIAL;
    }

    public static final String getPreUrl() {
        if(isDebug)
            return "http://incongress.cn/";
        else
            return "http://app.incongress.cn/";
    }

    /*反馈地址*/
    public static final String FEEDBACK_URI = "http://weixin.incongress.cn/xhy/xhyHtml5/html/feedback.html";
    /*个人资料修改地址*/
    public static final String MODEFIY_INFO_URI = "http://app.incongress.cn/modelH5/canhuizhuce/updateUser.jsp?";
    /*CIT学院的分享地址*/
    public static final String CIT_SHARE_URI = "http://app.incongress.cn/Exam/data?method=getDataByDataId&dataId=";
    /*所有功能的开关*/

    /** 广告广播 **/
    public static final String ACTION_CHANGE_AD = "android.intent.action.change_ad.basic.cit";
    public static final String ACTION_UPDATE_MEET = "android.intent.action.update_meet";
    public static final String ACTION_COMMENT_UPDATE = "android.intent.action.update_comment.cit";
    /** 首页点击的按钮位置   */
    public static final Boolean HOME_CLICK_POSITION_INNER = false;
    /**　微分享信key **/
    public static final String WXKey = "wx959030923745168b" ;
    /** 项目名称 一定要修改**/
    public static final String APPNAME = "CIT";

    //初始数据库版本
    public static final int PROJECT_ID = 1;     //这个代表一个项目，如 1代表cit 2代表csccm。。。
    public static final int DATA_VERSION = 258;
    public static final int APP_VERSION = 41;
    /** 参会易下载地址(根据产品不同地址不同) **/
    public static final String APP_DOWNLOAD_SITE = "http://app.incongress.cn/cit";
    /** 下载地址 **/
    public static final String DOWNLOADDIR="/cd_incongress_cit/download/";
    /** 解压地址 **/
    public static final String FILESDIR="/cd_incongress_cit/files/";

    //豌豆荚地址
    public static final String MARKAPP_URI = "https://www.wandoujia.com/apps/com.android.incongress.cd.conference";
    public static final String PACKAGE_NAME = "com.android.incongress.cd.conference";
    //conference id
    public static int conId = 44;//44 cit
    //看日程
    //日程分享      //默认为true
    public static final boolean SCHEDULE_SHARE = true;
    //课件预约
    public static final boolean SCHEDULE_BOOK = false;
    //日程提问
    public static final boolean SCHEDULE_ASK = false;
    //讲者信息介绍
    public static final boolean SCHEDULE_SPEAKER_INFO = true;
    //参会指南     分享
    public static final boolean PARTY_GUIDE_SHARE = true;
    //壁报集
    public static final boolean POSTER_LIST_ASK = false;
    //照片墙
    //专家秘书
    public static boolean IS_SECRETARY_SHOW = false;
    //播客   点赞   评论
    public static boolean SCENICXIU_PRISE_COMMENT = true;
    //首页消息和视频如果没有数据，就不会显示
    //分享界面
    //CIT学院的分享
    public static boolean COLLEGE_HOME_SHARE = true;
    //获取conId
    public static final int getConId() {
        return SharePreferenceUtils.getAppInt(Constants.CONID,Constants.conId);
    }
    public static final String getFromWhere() {
        return SharePreferenceUtils.getAppString(Constants.FROMWHERE);
    }
}
