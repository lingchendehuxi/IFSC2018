package com.android.incongress.cd.conference;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.VideoBottomListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.LanguageUtil;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.android.incongress.cd.conference.widget.ListViewForFix;
import com.android.incongress.cd.conference.widget.blws.PolyvErrorMessageUtils;
import com.android.incongress.cd.conference.widget.blws.PolyvPlayerMediaController;
import com.android.incongress.cd.conference.widget.blws.PolyvPlayerPreviewView;
import com.android.incongress.cd.conference.widget.blws.PolyvScreenUtils;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PolyvVideoPlayDetailActivity extends AppCompatActivity implements VideoBottomListAdapter.OnItemClickListener{
    private FastOnLineBean.VideoArrayBean videoBean;
    private BookCoursePlayBean.VideoArrayBean videoPlayBean;
    @BindView(R.id.title_back_polyv)
    ImageView title_back_polyv;
    @BindView(R.id.title_share_polyv)
    ImageView title_share_polyv;
    @BindView(R.id.cir_head)
    CircleImageView cir_head;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.sec_author)
    TextView sec_author;
    @BindView(R.id.title_address)
    TextView title_address;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.video_peopel_number)
    TextView video_peopel_number;
    @BindView(R.id.polyv_player_media_controller)
    PolyvPlayerMediaController polyv_player_media_controller;
    @BindView(R.id.loading_progress)
    ProgressBar loading_progress;
    @BindView(R.id.polyv_player_first_start_view)
    PolyvPlayerPreviewView firstStartView;
    @BindView(R.id.video_error_layout)
    LinearLayout videoErrorLayout;
    @BindView(R.id.video_error_content)
    TextView videoErrorContent;
    @BindView(R.id.video_error_retry)
    TextView videoErrorRetry;
    @BindView(R.id.polyv_video_view)
    PolyvVideoView polyv_video_view;
    @BindView(R.id.view_layout)
    RelativeLayout view_layout;
    @BindView(R.id.rl_video_top)
    RelativeLayout rl_video_top;
    @BindView(R.id.xr_video)
    ListViewForFix xRecyclerView;
    private List<BookCoursePlayBean.VideoArrayBean> listVideo;
    private VideoBottomListAdapter listAdapter;
    private int dataId;
    private int mCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //设置状态栏透明
        //StatusBarUtil.setTranslucentStatus(this);
        LanguageUtil.setLanguage(this, AppApplication.systemLanguage);
        setContentView(R.layout.activity_polyv_video_play_detail);
        ButterKnife.bind(this);
        videoBean = (FastOnLineBean.VideoArrayBean) getIntent().getSerializableExtra(Constants.VIDEO_DETIAL_BEAN);
        videoPlayBean = (BookCoursePlayBean.VideoArrayBean) getIntent().getSerializableExtra("video_play_bean");
        listVideo = (List<BookCoursePlayBean.VideoArrayBean>) getIntent().getSerializableExtra("video_list");
        mCurrentPosition = getIntent().getIntExtra("chose_position",0);
        if(listVideo!=null&&listVideo.size()>0){
            xRecyclerView.setVisibility(View.VISIBLE);
            xRecyclerView.setFocusable(false);
            listVideo.get(mCurrentPosition).setSelected(true);
            listAdapter = new VideoBottomListAdapter(listVideo,this,this);
            xRecyclerView.setAdapter(listAdapter);
        }else {
            xRecyclerView.setVisibility(View.GONE);
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.width = DensityUtil.getScreenSize(this)[0];
        params.height = DensityUtil.getScreenSize(this)[0]*9/16;
        view_layout.setLayoutParams(params);
        title_share_polyv.setVisibility(Constants.COLLEGE_HOME_SHARE?View.VISIBLE:View.INVISIBLE);
        initViewsAction();
    }
    @OnClick(R.id.title_back_polyv)
    void clickFinish(){ finish(); }
    @OnClick(R.id.title_share_polyv)
    void clickShare(){ ShareUtils.shareTextWithUrl(PolyvVideoPlayDetailActivity.this, content.getText().toString(), Constants.COLLEGE_SHARE_TITLE,
                Constants.CIT_SHARE_URI + dataId + "&conId=" + Constants.getConId() +"&fromWhere=" + Constants.getFromWhere()+ "&isShare=1", null); }


    private void initViewsAction() {
        if (videoBean != null) {
            if (!TextUtils.isEmpty(videoBean.getVideoId())) {
                setPolyvConfig(videoBean.getVideoId());
            }
            dataId = videoBean.getDataId();
            getLookNumber(dataId);
            StringUtils.setTextShow(author, videoBean.getSpeakerName());
            StringUtils.setTextShow(sec_author, videoBean.getRoleName());
            StringUtils.setTextShow(title_address, videoBean.getClassesName());
            if (!TextUtils.isEmpty(videoBean.getTitle())) {
                String title = videoBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    if(splitLength == -1){
                        content.setText(title);
                        polyv_player_media_controller.tv_title.setText(title);
                    }else {
                        content.setText(title.substring(0,splitLength));
                        polyv_player_media_controller.tv_title.setText(title.substring(0,splitLength));
                    }
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength+1,title.length()));
                        polyv_player_media_controller.tv_title.setText(title.substring(splitLength+1,title.length()));
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
            getLookNumber(dataId);
            StringUtils.setTextShow(author, videoPlayBean.getSpeakerName());
            StringUtils.setTextShow(sec_author, videoPlayBean.getRoleName());
            StringUtils.setTextShow(title_address, videoPlayBean.getClassesName());
            author.setText(videoPlayBean.getAuthor());
            if (!TextUtils.isEmpty(videoPlayBean.getTitle())) {
                String title = videoPlayBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    if(splitLength == -1){
                        content.setText(title);
                        polyv_player_media_controller.tv_title.setText(title);
                    }else {
                        content.setText(title.substring(0,splitLength));
                        polyv_player_media_controller.tv_title.setText(title.substring(0,splitLength));
                    }
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength+1,title.length()));
                        polyv_player_media_controller.tv_title.setText(title.substring(splitLength+1,title.length()));
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
    }
    //获取课件查看人数
    private void getLookNumber(int dataId){
        CHYHttpClientUsage.getInstanse().uploadVideoPlayNumber(dataId,new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                video_peopel_number.setText(getString(R.string.read_count, JSONCatch.parseInt("readCount",response)));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (polyv_video_view != null) {
            polyv_video_view.pause();
        }
        MobclickAgent.onPageEnd(Constants.FRAGMENT_COLLEGEFRAGMENT_DETIAL);
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
        MobclickAgent.onPageStart(Constants.FRAGMENT_COLLEGEFRAGMENT_DETIAL);
    }
    @Override
    public void onItemClick(int position) {
        if(mCurrentPosition == position){
            return;
        }
        mCurrentPosition = position;
        for(int i = 0;i<listVideo.size();i++){
            listVideo.get(i).setSelected(false);
        }
        listVideo.get(position).setSelected(true);
        listAdapter.notifyDataSetChanged();
        videoPlayBean = listVideo.get(position);
        if (polyv_video_view != null) {
            polyv_video_view.pause();
            polyv_video_view.release();
        }
        initViewsAction();
    }
}
