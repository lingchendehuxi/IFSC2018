package com.android.incongress.cd.conference;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.utils.LanguageUtil;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.blws.PolyvErrorMessageUtils;
import com.android.incongress.cd.conference.widget.blws.PolyvPlayerMediaController;
import com.android.incongress.cd.conference.widget.blws.PolyvPlayerPreviewView;
import com.android.incongress.cd.conference.widget.blws.PolyvScreenUtils;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

public class PolyvVideoPlayDetailActivity extends AppCompatActivity {
    private FastOnLineBean.VideoArrayBean videoBean;
    private BookCoursePlayBean.VideoArrayBean videoPlayBean;
    private ImageView title_back, title_share;
    private CircleImageView cir_head;
    private TextView author, sec_author, title_address, content;
    private RelativeLayout view_layout;
    private PolyvVideoView polyv_video_view;
    private PolyvPlayerMediaController polyv_player_media_controller;
    private ProgressBar loading_progress;
    private LinearLayout videoErrorLayout;
    private TextView videoErrorContent;
    private TextView videoErrorRetry;
    private PolyvPlayerPreviewView firstStartView;
    private RelativeLayout rl_video_top;
    private int dataId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置状态栏透明
        //StatusBarUtil.setTranslucentStatus(this);
        LanguageUtil.setLanguage(this, AppApplication.systemLanguage);
        setContentView(R.layout.activity_polyv_video_play_detail);
        videoBean = (FastOnLineBean.VideoArrayBean) getIntent().getSerializableExtra(Constants.VIDEO_DETIAL_BEAN);
        videoPlayBean = (BookCoursePlayBean.VideoArrayBean) getIntent().getSerializableExtra("video_play_bean");
        title_back = findViewById(R.id.title_back);
        title_share = findViewById(R.id.title_share);
        cir_head = findViewById(R.id.cir_head);
        author = findViewById(R.id.author);
        sec_author = findViewById(R.id.sec_author);
        title_address = findViewById(R.id.title_address);
        content = findViewById(R.id.content);
        rl_video_top = findViewById(R.id.rl_video_top);
        view_layout = findViewById(R.id.view_layout);
        polyv_video_view = findViewById(R.id.polyv_video_view);
        polyv_player_media_controller = findViewById(R.id.polyv_player_media_controller);
        loading_progress = findViewById(R.id.loading_progress);
        firstStartView = findViewById(R.id.polyv_player_first_start_view);
        videoErrorLayout = findViewById(R.id.video_error_layout);
        videoErrorContent = findViewById(R.id.video_error_content);
        videoErrorRetry = findViewById(R.id.video_error_retry);
        title_share.setVisibility(Constants.COLLEGE_HOME_SHARE?View.VISIBLE:View.INVISIBLE);
        title_back.setVisibility(View.VISIBLE);
        initViewsAction();
    }


    private void initViewsAction() {
        if (videoBean != null) {
            if (!TextUtils.isEmpty(videoBean.getVideoId())) {
                setPolyvConfig(videoBean.getVideoId());
            }
            dataId = videoBean.getDataId();
            StringUtils.setTextShow(author, videoBean.getSpeakerName());
            StringUtils.setTextShow(sec_author, videoBean.getRoleName());
            StringUtils.setTextShow(title_address, videoBean.getClassesName());
            if (!TextUtils.isEmpty(videoBean.getTitle())) {
                String title = videoBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    content.setText(title.substring(0,splitLength));
                    polyv_player_media_controller.tv_title.setText(title.substring(0,splitLength));
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength-1,title.length()));
                        polyv_player_media_controller.tv_title.setText(title.substring(splitLength-1,title.length()));
                    }else {
                        content.setText("");
                        polyv_player_media_controller.tv_title.setText("");
                    }
                }
            }
            if (!TextUtils.isEmpty(videoBean.getSpeakerImg())) {
                PicUtils.loadCircleImage(PolyvVideoPlayDetailActivity.this, videoBean.getSpeakerImg(), cir_head);
            } else {
                cir_head.setImageResource(R.drawable.professor_default);
            }
        } else if (videoPlayBean != null) {
            if (!TextUtils.isEmpty(videoPlayBean.getVideoId())) {
                setPolyvConfig(videoPlayBean.getVideoId());
            }
            dataId = videoPlayBean.getDataId();
            StringUtils.setTextShow(author, videoPlayBean.getSpeakerName());
            StringUtils.setTextShow(sec_author, videoPlayBean.getRoleName());
            StringUtils.setTextShow(title_address, videoPlayBean.getClassesName());
            author.setText(videoPlayBean.getAuthor());
            if (!TextUtils.isEmpty(videoPlayBean.getTitle())) {
                String title = videoPlayBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    content.setText(title.substring(0,splitLength));
                    polyv_player_media_controller.tv_title.setText(title.substring(0,splitLength));
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength-1,title.length()));
                        polyv_player_media_controller.tv_title.setText(title.substring(splitLength-1,title.length()));
                    }else {
                        content.setText("");
                        polyv_player_media_controller.tv_title.setText("");
                    }
                }
            }
            if (!TextUtils.isEmpty(videoPlayBean.getSpeakerImg())) {
                PicUtils.loadCircleImage(PolyvVideoPlayDetailActivity.this, videoPlayBean.getSpeakerImg(), cir_head);
            } else {
                cir_head.setImageResource(R.drawable.professor_default);
            }
        }
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享
                ShareUtils.shareTextWithUrl(PolyvVideoPlayDetailActivity.this, content.getText().toString(), "CIT学院",
                        Constants.CIT_SHARE_URI + dataId + "&conId=" + Constants.getConId() +"&fromWhere=" + Constants.getFromWhere()+ "&isShare=1", null);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (polyv_video_view != null) {
            polyv_video_view.pause();
        }
        MobclickAgent.onPageEnd(Constants.ACTIVITY_COLLEGE_PLAY_BLWS);
    }

    //保利威视视频配置
    private void setPolyvConfig(final String videoUrl) {
        polyv_player_media_controller.setFullScreen(true);
        polyv_player_media_controller.iv_share.setVisibility(View.GONE);
        polyv_player_media_controller.iv_dmswitch.setVisibility(View.GONE);
        polyv_player_media_controller.iv_danmu.setVisibility(View.GONE);
        polyv_player_media_controller.iv_screens.setVisibility(View.GONE);
        polyv_player_media_controller.ll_subtitle.setVisibility(View.GONE);
        polyv_player_media_controller.ll_subtitle_b.setVisibility(View.GONE);
        polyv_video_view.setMediaController(polyv_player_media_controller);
        polyv_video_view.setOpenPreload(true, 2);
        polyv_video_view.setAutoContinue(true);

        Rect rect = new Rect();
        polyv_video_view.getLocalVisibleRect(rect);
        int videoheight3 = polyv_video_view.getHeight();
        Log.e("videoTest", "videoheight3:" + videoheight3 + "===" + "rect.top:" + rect.top + "===" + "rect.bottom:" + rect.bottom);
        //以下是播放异常反馈
        polyv_video_view.setOnErrorListener(new IPolyvOnErrorListener2() {
            @Override
            public boolean onError() {
                String message = "当前视频无法播放，请尝试切换网络重新播放或者向管理员反馈(error code " + PolyvPlayErrorReason.VIDEO_ERROR + ")";
                showErrorView(videoErrorLayout, videoErrorContent, message);
                Toast.makeText(PolyvVideoPlayDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                polyv_video_view.setOnVideoPlayErrorListener(new IPolyvOnVideoPlayErrorListener2() {
                    @Override
                    public boolean onVideoPlayError(@PolyvPlayErrorReason.PlayErrorReason int playErrorReason) {
                        String message = PolyvErrorMessageUtils.getPlayErrorMessage(playErrorReason);
                        message += "(error code " + playErrorReason + ")";
                        showErrorView(videoErrorLayout, videoErrorContent, message);
                        return true;
                    }
                });
                polyv_video_view.setOnVideoStatusListener(new IPolyvOnVideoStatusListener() {
                    @Override
                    public void onStatus(int status) {
                        if (status < 60) {
                            Toast.makeText(PolyvVideoPlayDetailActivity.this, "状态错误 " + status, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            }
        });
        videoErrorRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //初始化视频工具
                polyv_player_media_controller.initConfig(view_layout);
                polyv_video_view.setPlayerBufferingIndicator(loading_progress);
                PolyvScreenUtils.generateHeight16_9(PolyvVideoPlayDetailActivity.this);
                polyv_video_view.pause();
                play(polyv_video_view, polyv_player_media_controller, loading_progress, firstStartView, videoUrl, 0, true, false);
            }
        });
        if (polyv_video_view.isPlaying()) {
            return;
        }
        if (rect.top == 0 && rect.bottom == videoheight3) {
            polyv_player_media_controller.initConfig(view_layout);
            polyv_video_view.setPlayerBufferingIndicator(loading_progress);
            PolyvScreenUtils.generateHeight16_9(PolyvVideoPlayDetailActivity.this);
            polyv_video_view.pause();
            play(polyv_video_view, polyv_player_media_controller, loading_progress, firstStartView, videoUrl, 0, true, false);
            return;
        }
    }

    /**
     * 播放视频
     *
     * @param vid             视频id
     * @param bitrate         码率（清晰度）
     * @param startNow        是否现在开始播放视频
     * @param isMustFromLocal 是否必须从本地（本地缓存的视频）播放
     */
    private void play(final PolyvVideoView videoView, PolyvPlayerMediaController mediaController, ProgressBar loadingProgress, PolyvPlayerPreviewView firstStartView, final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        if (TextUtils.isEmpty(vid)) return;

        videoView.release();
        mediaController.hide();
        loadingProgress.setVisibility(View.GONE);
        firstStartView.hide();

        if (startNow) {
            //调用setVid方法视频会自动播放
            videoView.setVid(vid, bitrate, isMustFromLocal);
        } else {
            //视频不播放，先显示一张缩略图
            firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {

                @Override
                public void onClickStart() {
                    /**
                     * 调用setVid方法视频会自动播放
                     * 如果是有学员登陆的播放，可以在登陆的时候通过
                     * {@link com.easefun.polyvsdk.PolyvSDKClient.getinstance().setViewerId()}设置学员id
                     * 或者调用{@link videoView.setVidWithStudentId}传入学员id进行播放
                     */

                    videoView.setVidWithStudentId(vid, bitrate, isMustFromLocal, "123");
                }
            });

            firstStartView.show(vid);
        }
    }

    //显示视频播放错误信息
    private void showErrorView(LinearLayout videoErrorLayout, TextView videoErrorContent, String message) {
        videoErrorLayout.setVisibility(View.VISIBLE);
        videoErrorContent.setText(message);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rl_video_top.setVisibility(View.GONE);
            polyv_player_media_controller.iv_land.performClick();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rl_video_top.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (polyv_video_view != null) {
            polyv_video_view.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.ACTIVITY_COLLEGE_PLAY_BLWS);
    }
}
