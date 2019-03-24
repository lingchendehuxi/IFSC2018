package com.android.incongress.cd.conference.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.EsmoListBean;
import com.android.incongress.cd.conference.beans.EsmosBean;
import com.android.incongress.cd.conference.beans.LiveInfoBean;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.PinyinConverter;
import com.google.gson.Gson;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/7/25.
 * 通过新的方式创建数据库并加入数据
 */
//public abstract void demo(String s);
public class ConferenceDb {
    private static final String CONFERENCES_TXT = "conferences.txt";
    private static final String SESSION_TXT = "session2.0.txt";
    private static final String MEETING_TXT = "meeting2.0.txt";
    private static final String SPEAKER_TXT = "speaker2.0.txt";
    private static final String CLASSES_TXT = "classes2.0.txt";
    private static final String CONFIELD_TXT = "conField.txt";
    private static final String EXHIBITORS_TXT = "exhibitorsNew.txt";
    private static final String TIPS_TXT = "tips.txt";
    private static final String AD_TXT = "ad.txt";
    private static final String CONFERENCE_MAP_TXT = "conferencesMap.txt";
    private static final String ROLE_TXT = "role.txt";
    private static final String TIME_TXT = "time.txt";
    //新加入esmo相关的txt文件
    private static final String ESMO_TXT = "esmo.txt";
    private static final String CONPASs_TXT = "conpassAd.txt";

    private static final int DELETE_DATA = 0;
    private static final int CONFERENCES = 1;
    private static final int SESSION = 2;
    private static final int MEETING = 3;
    private static final int CONFIELD = 4;
    private static final int CLASSES = 5;
    private static final int SPEAKER = 6;
    private static final int EXHIBITORS = 7;
    private static final int TIPS = 8;
    private static final int AD = 9;
    private static final int CONFERENCE_MAP = 10;
    private static final int ROLE = 11;
    private static final int TIME = 12;

    public static void createEsmoDB(String path) {
        LitePal.deleteAll(EsmosBean.class);

        FileInputStream is;
        File file;

        //esmo.txt 会议
        try {
            file = new File(path + ESMO_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                EsmoListBean esmoList = parseEsmo(is);
                List<EsmosBean> esmos = esmoList.getEsmos();

                if (esmos != null && esmos.size() > 0) {
                    for (int i = 0; i < esmos.size(); i++) {
                        esmos.get(i).save();
                    }
                }
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ConpassAd.txt
        try {
            file = new File(path + CONPASs_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<ConpassAd> conpassAds = parseConpassAd(is);

                for (int i = 0; i < conpassAds.size(); i++) {
                    conpassAds.get(i).save();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建数据库并加入新的数据
     *
     * @param path
     */
    public static void createDB(String path, int postion, OnUpdateInfoListener listener) {
        Connector.getDatabase();
        switch (postion) {
            case DELETE_DATA:
                //删除数据库
                try {
                    LitePal.deleteAll(Ad.class);
                    LitePal.deleteAll(Class.class);
                    LitePal.deleteAll(Conferences.class);
                    LitePal.deleteAll(Confield.class);
                    LitePal.deleteAll(Exhibitor.class);
                    LitePal.deleteAll(ExhibitorActivity.class); // 并没有被调用存值
                    LitePal.deleteAll(Map.class);
                    LitePal.deleteAll(Meeting.class);
                    LitePal.deleteAll(Role.class);
                    LitePal.deleteAll(Session.class);
                    LitePal.deleteAll(Speaker.class);
                    LitePal.deleteAll(Tips.class);
                    LitePal.deleteAll(TimeBean.class);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    postion = CONFERENCES;
                    SharePreferenceUtils.saveAppInt("postion", postion);
                }

            case CONFERENCES:
                FileInputStream is = null;
                File file = null;

                //conferences.txt 会议
                try {
                    file = new File(path + CONFERENCES_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        Conferences conferences = parseConference(is);
                        conferences.save();
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_schedule);
                postion = SESSION;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case SESSION:
                //sessionNew.txt 会议
                try {
                    file = new File(path + SESSION_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Session> sessions = parseSession(is);
                        is.close();

                        String sessionCH = "";
                        String sessionEN = "";

                        for (Session session : sessions) {
                            String sessionName[] = session.getSessionName().split("#@#");
                            if (sessionName.length == 1) {
                                sessionCH = sessionEN = sessionName[0];
                            } else if (sessionName.length == 2) {
                                sessionCH = sessionName[0];
                                sessionEN = sessionName[1];
                            }
                            session.setSessionName(sessionCH);
                            session.setSessionNameEN(sessionEN);
                            session.save();
                            //更新sessionEnName和sessionName
                        }
//                file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_schedule);
                postion = MEETING;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case MEETING:
                // meetingNew.txt 会议演讲
                try {
                    file = new File(path + MEETING_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Meeting> meetings = parseMeeting(is);
                        is.close();

                        String topicCH = "";
                        String topicEN = "";
                        for (Meeting temp : meetings) {
                            String topic[] = temp.getTopic().split("#@#");
                            if (topic.length == 1) {
                                topicCH = topicEN = topic[0];
                            } else if (topic.length == 2) {
                                topicCH = topic[0];
                                topicEN = topic[1];
                            }
                            temp.setTopic(topicCH);
                            temp.setTopicEn(topicEN);
                            boolean b = temp.save();
                            Log.d("myTest", "save: "+b);
                        }
//                file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_schedule);
                postion = CONFIELD;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case CONFIELD:
                // conField.txt 领域
                try {
                    file = new File(path + CONFIELD_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Confield> confields = parseConField(is);
                        is.close();

                        for (Confield temp : confields) {
                            temp.save();
                        }

                        //file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_onference);
                postion = CLASSES;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case CLASSES:
                // classes.txt 会议室
                try {
                    file = new File(path + CLASSES_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Class> classes = parseClasses(is);
                        is.close();

                        String chinese = "";
                        String english = "";

                        for (Class temp : classes) {
                            String[] names = temp.getClassesCode().split("#@#");
                            if (names.length == 1) {
                                chinese = english = names[0];
                            } else if (names.length == 2) {
                                chinese = names[0];
                                english = names[1];
                            }
                            temp.setClassesCode(chinese);
                            temp.setClassCodeEn(english);
                            temp.save();
                        }
//              file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_onference);
                postion = SPEAKER;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case SPEAKER:
                // speaker.txt 演讲者
                try {
                    file = new File(path + SPEAKER_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Speaker> speakers = parseSpeakers(is);
                        is.close();

                        for (Speaker speaker : speakers) {
                            speaker.setSpeakerName(speaker.getSpeakerName());
                            if ("".equals(speaker.getEnName())) {
                                speaker.setEnName(speaker.getSpeakerName());
                            } else {
                                speaker.setEnName(speaker.getEnName());
                            }
                            /*String speakernamepingyin = PinyinConverter.getPinyin(speaker.getSpeakerName().trim().replace("\n", ""));
                            speaker.setSpeakerNamePingyin(speakernamepingyin);
                            String s = speakernamepingyin.charAt(0) + "";
                            speaker.setFirstLetter(speakernamepingyin.charAt(0) + "");*/
                            speaker.save();
                        }
//              file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_onference);
                postion = EXHIBITORS;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case EXHIBITORS:
                //exhibitorsNew.txt 参展商
                try {
                    file = new File(path + EXHIBITORS_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Exhibitor> exhibitors = parseExhibitors(is);
                        is.close();
                        String chinese = "";
                        String english = "";

                        for (Exhibitor temp : exhibitors) {
                            String names[] = temp.getTitle().split("#@#");

                            if (names.length == 1) {
                                chinese = english = names[0];
                            } else if (names.length == 2) {
                                chinese = names[0];
                                english = names[1];
                            }
                            temp.setTitle(chinese);
                            temp.setTitleEn(english);

                            String info[] = temp.getInfo().split("#@#");
                            if (info.length == 1) {
                                chinese = english = info[0];
                            } else if (info.length == 2) {
                                chinese = info[0];
                                english = info[1];
                            }
                            temp.setInfo(chinese);
                            temp.setInfoEn(english);

                            String address[] = temp.getAddress().split("#@#");
                            if (address.length == 1) {
                                chinese = english = address[0];
                            } else if (address.length == 2) {
                                chinese = address[0];
                                english = address[1];
                            }
                            temp.setAddress(chinese);
                            temp.setAddressEn(english);

                            temp.save();
                        }
//              file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_speaker);
                postion = TIPS;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case TIPS:
                //tips.txt 基本信息
                try {
                    file = new File(path + TIPS_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Tips> tips = parseTipss(is);
                        is.close();

                        for (Tips temp : tips) {
                            temp.save();
                        }
//                file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_speaker);
                postion = AD;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case AD:
                //ad.txt 基本信息
                try {
                    file = new File(path + AD_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Ad> ads = parseAds(is);
                        is.close();
                        for (Ad temp : ads) {
                            temp.save();
                        }

//              file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_speaker);
                postion = CONFERENCE_MAP;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case CONFERENCE_MAP:
                //conferencesMap.txt 地图
                try {
                    file = new File(path + CONFERENCE_MAP_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Map> maps = parseMaps(is);
                        is.close();

                        for (Map map :
                                maps) {
                            map.save();
                        }
//                file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_exhibitor);
                postion = ROLE;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case ROLE:
                //TODO role.txt 身份表
                try {
                    file = new File(path + ROLE_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        List<Role> roles = parseRole(is);
                        is.close();
                        for (Role temp :
                                roles) {
                            temp.save();
                        }
//                file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_exhibitor);
                postion = TIME;
                SharePreferenceUtils.saveAppInt("postion", postion);
            case TIME:

                //TODO time.text 会议时间表
                try {
                    file = new File(path + TIME_TXT);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        TimeBean timeBean = parseTime(is);
                        is.close();
                        timeBean.save();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listener.onMeetingStart(R.string.splash_exhibitor);
                SharePreferenceUtils.saveAppInt("postion", postion);
                //此处更新闹钟
                updateClock();
        }
    }

    public static void updateClock() {
        List<Alert> list = ConferenceDbUtils.getAllAlert();
        if (list == null) {
            return;
        }
        for (Alert alert : list) {
            if (alert.getRelativeid().length() != 0) {
                ConferenceDbUtils.getAlertByTitle(alert);
            }
        }
    }

    public static void createDB(String path, boolean isUpdate) {
        if (isUpdate) {
            //删除数据库

            LitePal.deleteAll(Ad.class);
            LitePal.deleteAll(Class.class);
            LitePal.deleteAll(Conferences.class);
            LitePal.deleteAll(Confield.class);
            LitePal.deleteAll(Exhibitor.class);
            LitePal.deleteAll(ExhibitorActivity.class);
            LitePal.deleteAll(Map.class);
            LitePal.deleteAll(Meeting.class);
            LitePal.deleteAll(Role.class);
            LitePal.deleteAll(Session.class);
            LitePal.deleteAll(Speaker.class);
            LitePal.deleteAll(Tips.class);
            LitePal.deleteAll(TimeBean.class);
        }


        FileInputStream is = null;
        File file = null;

        //conferences.txt 会议
        try {
            file = new File(path + CONFERENCES_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                Conferences conferences = parseConference(is);
                boolean b = conferences.save();
                Log.d("myTest", "createDB: "+b);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //sessionNew.txt 会议
        try {
            file = new File(path + SESSION_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Session> sessions = parseSession(is);
                is.close();

                String sessionCH = "";
                String sessionEN = "";

                for (Session session : sessions) {
                    String sessionName[] = session.getSessionName().split("#@#");
                    if (sessionName.length == 1) {
                        sessionCH = sessionEN = sessionName[0];
                    } else if (sessionName.length == 2) {
                        sessionCH = sessionName[0];
                        sessionEN = sessionName[1];
                    }
                    session.setSessionName(sessionCH);
                    session.setSessionNameEN(sessionEN);
                    session.save();
                    //更新sessionEnName和sessionName
                }
//                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // meetingNew.txt 会议演讲
        try {
            file = new File(path + MEETING_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Meeting> meetings = parseMeeting(is);
                is.close();

                String topicCH = "";
                String topicEN = "";
                for (Meeting temp : meetings) {
                    String topic[] = temp.getTopic().split("#@#");
                    if (topic.length == 1) {
                        topicCH = topicEN = topic[0];
                    } else if (topic.length == 2) {
                        topicCH = topic[0];
                        topicEN = topic[1];
                    }
                    temp.setTopic(topicCH);
                    temp.setTopicEn(topicEN);
                    temp.save();
                }
//                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // conField.txt 领域
        try {
            file = new File(path + CONFIELD_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Confield> confields = parseConField(is);
                is.close();

                for (Confield temp : confields) {
                    temp.save();
                }

                //file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // classes.txt 会议室
        try {
            file = new File(path + CLASSES_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Class> classes = parseClasses(is);
                is.close();

                String chinese = "";
                String english = "";

                for (Class temp : classes) {
                    String[] names = temp.getClassesCode().split("#@#");
                    if (names.length == 1) {
                        chinese = english = names[0];
                    } else if (names.length == 2) {
                        chinese = names[0];
                        english = names[1];
                    }
                    temp.setClassesCode(chinese);
                    temp.setClassCodeEn(english);
                    temp.save();
                }
//              file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // speaker.txt 演讲者
        try {
            file = new File(path + SPEAKER_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Speaker> speakers = parseSpeakers(is);
                is.close();

                for (Speaker speaker : speakers) {
                    speaker.setSpeakerName(speaker.getSpeakerName());
                    if ("".equals(speaker.getEnName())) {
                        speaker.setEnName(speaker.getSpeakerName());
                    } else {
                        speaker.setEnName(speaker.getEnName());
                    }
                    speaker.save();
                }
//              file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //exhibitorsNew.txt 参展商
        try {
            file = new File(path + EXHIBITORS_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Exhibitor> exhibitors = parseExhibitors(is);
                is.close();
                String chinese = "";
                String english = "";

                for (Exhibitor temp : exhibitors) {
                    String names[] = temp.getTitle().split("#@#");

                    if (names.length == 1) {
                        chinese = english = names[0];
                    } else if (names.length == 2) {
                        chinese = names[0];
                        english = names[1];
                    }
                    temp.setTitle(chinese);
                    temp.setTitleEn(english);

                    String info[] = temp.getInfo().split("#@#");
                    if (info.length == 1) {
                        chinese = english = info[0];
                    } else if (info.length == 2) {
                        chinese = info[0];
                        english = info[1];
                    }
                    temp.setInfo(chinese);
                    temp.setInfoEn(english);

                    String address[] = temp.getAddress().split("#@#");
                    if (address.length == 1) {
                        chinese = english = address[0];
                    } else if (address.length == 2) {
                        chinese = address[0];
                        english = address[1];
                    }
                    temp.setAddress(chinese);
                    temp.setAddressEn(english);

                    temp.save();
                }
//              file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //tips.txt 基本信息
        try {
            file = new File(path + TIPS_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Tips> tips = parseTipss(is);
                is.close();

                for (Tips temp : tips) {
                    temp.save();
                }
//                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ad.txt 基本信息
        try {
            file = new File(path + AD_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Ad> ads = parseAds(is);
                is.close();
                for (Ad temp : ads) {
                    temp.save();
                }

//              file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //conferencesMap.txt 地图
        try {
            file = new File(path + CONFERENCE_MAP_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Map> maps = parseMaps(is);
                is.close();

                for (Map map :
                        maps) {
                    map.save();
                }
//                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO role.txt 身份表
        try {
            file = new File(path + ROLE_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                List<Role> roles = parseRole(is);
                is.close();
                for (Role temp :
                        roles) {
                    temp.save();
                }
//                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO time.text 会议时间表
        try {
            file = new File(path + TIME_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                TimeBean timeBean = parseTime(is);
                is.close();
                timeBean.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 工具集合
    private static Conferences parseConference(InputStream is) {
        String conference = readJsonString(is);
        Gson gson = new Gson();
        Conferences conferences = gson.fromJson(conference, Conferences.class);
        return conferences;
    }

    private static EsmoListBean parseEsmo(InputStream is) {
        String str = readJsonString(is);
        Gson gson = new Gson();
        EsmoListBean esmoList = gson.fromJson(str, EsmoListBean.class);

        return esmoList;
    }

    /**
     * 解析Conpass的广告数据
     *
     * @param is
     * @return
     */
    private static List<ConpassAd> parseConpassAd(InputStream is) {
        List<ConpassAd> mAdas = null;
        String str = readJsonString(is);
        try {
            JSONArray array = new JSONArray(str);
            if (array != null && array.length() > 0) {
                ConpassAd tempAd = null;
                mAdas = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject temp = (JSONObject) array.get(i);
                    tempAd = new ConpassAd();
                    tempAd.setAdUrl(temp.getString("adUrl"));
                    tempAd.setGotoUrl(temp.getString("gotoUrl"));
                    mAdas.add(tempAd);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mAdas;
    }


    private static List<Session> parseSession(InputStream is) {
        String sessionStr = readJsonString(is);
        List<Session> sessions = new ArrayList<>();

        if (!TextUtils.isEmpty(sessionStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(sessionStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Session session = gson.fromJson(jsonObject.toString(), Session.class);
                    sessions.add(session);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return sessions;
    }

    private static List<Meeting> parseMeeting(InputStream is) {
        String meetingStr = readJsonString(is);
        List<Meeting> meetings = new ArrayList<>();

        if (!TextUtils.isEmpty(meetingStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(meetingStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Meeting meeting = gson.fromJson(jsonObject.toString(), Meeting.class);
                    meetings.add(meeting);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return meetings;
    }

    private static List<Confield> parseConField(InputStream is) {
        String confieldStr = readJsonString(is);
        List<Confield> confields = new ArrayList<>();

        if (!TextUtils.isEmpty(confieldStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(confieldStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Confield confield = gson.fromJson(jsonObject.toString(), Confield.class);
                    confields.add(confield);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return confields;
    }

    private static List<Class> parseClasses(InputStream is) {
        String classStr = readJsonString(is);
        List<Class> classes = new ArrayList<>();

        if (!TextUtils.isEmpty(classStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(classStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Class classObj = gson.fromJson(jsonObject.toString(), Class.class);
                    classes.add(classObj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    private static List<Speaker> parseSpeakers(InputStream is) {
        String speakerStr = readJsonString(is);
        List<Speaker> speakers = new ArrayList<>();

        if (!TextUtils.isEmpty(speakerStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(speakerStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Speaker speaker = gson.fromJson(jsonObject.toString(), Speaker.class);
                    speakers.add(speaker);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return speakers;
    }

    private static List<Exhibitor> parseExhibitors(InputStream is) {
        String exhibitorStr = readJsonString(is);
        List<Exhibitor> exhibitors = new ArrayList<>();

        if (!TextUtils.isEmpty(exhibitorStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(exhibitorStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Exhibitor speaker = gson.fromJson(jsonObject.toString(), Exhibitor.class);
                    exhibitors.add(speaker);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return exhibitors;
    }

    private static List<Tips> parseTipss(InputStream is) {
        String tipsStr = readJsonString(is);
        List<Tips> tips = new ArrayList<>();
        if (!TextUtils.isEmpty(tipsStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(tipsStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Tips tip = gson.fromJson(jsonObject.toString(), Tips.class);
                    tips.add(tip);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tips;
    }

    private static List<Ad> parseAds(InputStream is) {
        String adStr = readJsonString(is);
        List<Ad> ads = new ArrayList<>();

        if (!TextUtils.isEmpty(adStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(adStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Ad ad = gson.fromJson(jsonObject.toString(), Ad.class);
                    ads.add(ad);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ads;
    }

    private static List<Map> parseMaps(InputStream is) {
        String mapStr = readJsonString(is);
        List<Map> maps = new ArrayList<>();

        if (!TextUtils.isEmpty(mapStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(mapStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Map map = gson.fromJson(jsonObject.toString(), Map.class);
                    maps.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return maps;
    }

    private static List<Role> parseRole(InputStream is) {
        String roleStr = readJsonString(is);
        List<Role> roles = new ArrayList<>();

        if (!TextUtils.isEmpty(roleStr)) {
            try {
                Gson gson = new Gson();
                JSONArray arr = new JSONArray(roleStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Role role = gson.fromJson(jsonObject.toString(), Role.class);
                    roles.add(role);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return roles;
    }

    private static TimeBean parseTime(InputStream is) {
        TimeBean time = new TimeBean();
        String timeStr = readJsonString(is);

        if (!TextUtils.isEmpty(timeStr)) {
            Gson gson = new Gson();
            time = gson.fromJson(timeStr, TimeBean.class);
        }

        return time;
    }

    private static String readJsonString(InputStream is) {
        String jsonStr = "";
        try {
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            jsonStr = EncodingUtils.getString(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
    //删除数据库所有存储的类
    public static void deleteAllClass(){
        try {
            LitePal.deleteAll(Ad.class);
            LitePal.deleteAll(Class.class);
            LitePal.deleteAll(Conferences.class);
            LitePal.deleteAll(Confield.class);
            LitePal.deleteAll(Exhibitor.class);
            LitePal.deleteAll(ExhibitorActivity.class); // 并没有被调用存值
            LitePal.deleteAll(Map.class);
            LitePal.deleteAll(Meeting.class);
            LitePal.deleteAll(Role.class);
            LitePal.deleteAll(Session.class);
            LitePal.deleteAll(Speaker.class);
            LitePal.deleteAll(Tips.class);
            LitePal.deleteAll(TimeBean.class);
            LitePal.deleteAll(LiveForOrderInfo.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnUpdateInfoListener mInfoListener;

    public interface OnUpdateInfoListener {
        void onMeetingStart(int resId);
    }

}
