package com.android.incongress.cd.conference;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.LanguageUtil;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.inter.listener.OnVideoBackListener;
import org.yczbj.ycvideoplayerlib.inter.listener.OnVideoControlListener;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

public class VideoPlayDetailActivity extends AppCompatActivity implements OnVideoBackListener ,OnVideoControlListener {
    private FastOnLineBean.VideoArrayBean videoBean;
    private BookCoursePlayBean.VideoArrayBean videoPlayBean;
    private VideoPlayer videoPlay;
    private CircleImageView cir_head;
    private TextView author, sec_author, title_address, content;
    private VideoPlayerController mController;
    private int dataId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        LanguageUtil.setLanguage(this, AppApplication.systemLanguage);
        setContentView(R.layout.activity_video_play_detail);
        videoBean = (FastOnLineBean.VideoArrayBean) getIntent().getSerializableExtra(Constants.VIDEO_DETIAL_BEAN);
        videoPlayBean = (BookCoursePlayBean.VideoArrayBean) getIntent().getSerializableExtra("video_play_bean");
        videoPlay = findViewById(R.id.videoPlay);
        cir_head = findViewById(R.id.cir_head);
        author = findViewById(R.id.author);
        sec_author = findViewById(R.id.sec_author);
        title_address = findViewById(R.id.title_address);
        content = findViewById(R.id.content);
        initViewsAction();
    }


    private void initViewsAction() {
        videoPlay.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_NATIVE); // IjkPlayer or MediaPlayer
        //创建视频控制器
        mController = new VideoPlayerController(this);
        mController.setLoadingType(ConstantKeys.Loading.LOADING_QQ);
        mController.setBackVisible(View.VISIBLE);
        mController.setTopVisibility(true);
        mController.setShareVisibity(Constants.COLLEGE_HOME_SHARE);
        mController.setOnVideoBackListener(this);
        mController.setOnVideoControlListener(this);
        mController.mTop.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 17), DensityUtil.dip2px(this, 10), 0);
        if (videoBean != null) {
            dataId = videoBean.getDataId();
            StringUtils.setTextShow(author, videoBean.getSpeakerName().replaceAll(","," "));
            StringUtils.setTextShow(sec_author, videoBean.getRoleName().replaceAll(","," "));
            StringUtils.setTextShow(title_address, videoBean.getClassesName());
            if (!TextUtils.isEmpty(videoBean.getTitle())) {
                String title = videoBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    content.setText(title.substring(0,splitLength));
                    mController.setTitle(title.substring(0,splitLength));
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength-1,title.length()));
                        mController.setTitle(title.substring(splitLength-1,title.length()));
                    }else {
                        content.setText("");
                        mController.setTitle("");
                    }
                }
            }
            if (!TextUtils.isEmpty(videoBean.getSpeakerImg())) {
                PicUtils.loadCircleImage(VideoPlayDetailActivity.this, videoBean.getSpeakerImg(), cir_head);
            } else {
                cir_head.setImageResource(R.drawable.professor_default);
            }
            PicUtils.loadImageUrl(this, videoBean.getVideoImage(), mController.imageView());
            videoPlay.setController(mController);
            videoPlay.setUp(videoBean.getVideoUrl(), null);
            if (NetWorkUtils.NETWORK_TYPE_WIFI.equals(NetWorkUtils.getNetworkTypeName(this))) {
                videoPlay.start();
            }
        } else if (videoPlayBean != null) {
            dataId = videoPlayBean.getDataId();
            StringUtils.setTextShow(author, videoPlayBean.getSpeakerName());
            StringUtils.setTextShow(sec_author, videoPlayBean.getRoleName());
            StringUtils.setTextShow(title_address, videoPlayBean.getClassesName());
            if (!TextUtils.isEmpty(videoPlayBean.getTitle())) {
                String title = videoPlayBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    content.setText(title.substring(0,splitLength));
                    mController.setTitle(title.substring(0,splitLength));
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength-1,title.length()));
                        mController.setTitle(title.substring(splitLength-1,title.length()));
                    }else {
                        content.setText("");
                        mController.setTitle("");
                    }
                }
            }
            if (!TextUtils.isEmpty(videoPlayBean.getSpeakerImg())) {
                PicUtils.loadCircleImage(VideoPlayDetailActivity.this, videoPlayBean.getSpeakerImg(), cir_head);
            } else {
                cir_head.setImageResource(R.drawable.professor_default);
            }
            PicUtils.loadImageUrl(this, videoPlayBean.getVideoImage(), mController.imageView());
            videoPlay.setController(mController);
            videoPlay.setUp(videoPlayBean.getVideoUrl(), null);
            if (NetWorkUtils.NETWORK_TYPE_WIFI.equals(NetWorkUtils.getNetworkTypeName(this))) {
                videoPlay.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoPlay != null) {
            videoPlay.pause();
        }
        if(videoBean!=null){
            MobclickAgent.onPageEnd(Constants.FRAGMENT_COLLEGEFRAGMENT_DETIAL+StringUtils.getNeedString(videoBean.getTitle()));
        }else if(videoPlayBean!=null){
            MobclickAgent.onPageEnd(Constants.FRAGMENT_COLLEGEFRAGMENT_DETIAL+StringUtils.getNeedString(videoPlayBean.getTitle()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoPlay != null) {
            videoPlay.release();
        }
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onVideoControlClick(int type) {
        //分享
        ShareUtils.shareTextWithUrl(VideoPlayDetailActivity.this, content.getText().toString(), "CIT学院",
                Constants.CIT_SHARE_URI + dataId + "&conId=" + Constants.getConId() +"&fromWhere=" + Constants.getFromWhere()+ "&isShare=1", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(videoBean!=null){
            MobclickAgent.onPageStart(Constants.FRAGMENT_COLLEGEFRAGMENT_DETIAL+StringUtils.getNeedString(videoBean.getTitle()));
        }else if(videoPlayBean!=null){
            MobclickAgent.onPageStart(Constants.FRAGMENT_COLLEGEFRAGMENT_DETIAL+StringUtils.getNeedString(videoPlayBean.getTitle()));
        }
    }
}
