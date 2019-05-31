package com.android.incongress.cd.conference;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.VideoBottomListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.LanguageUtil;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.WrapContentLinearLayoutManager;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.android.incongress.cd.conference.widget.ListViewForFix;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.inter.listener.OnVideoBackListener;
import org.yczbj.ycvideoplayerlib.inter.listener.OnVideoControlListener;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class VideoPlayDetailActivity extends AppCompatActivity implements OnVideoBackListener ,OnVideoControlListener ,VideoBottomListAdapter.OnItemClickListener {
    private FastOnLineBean.VideoArrayBean videoBean;
    private BookCoursePlayBean.VideoArrayBean videoPlayBean;
    private List<BookCoursePlayBean.VideoArrayBean> listVideo;
    @BindView(R.id.videoPlay)
    VideoPlayer videoPlay;
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
    @BindView(R.id.xr_video)
    ListViewForFix xRecyclerView;

    private VideoPlayerController mController;
    private VideoBottomListAdapter listAdapter;
    private int dataId;
    private int mCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        LanguageUtil.setLanguage(this, AppApplication.systemLanguage);
        setContentView(R.layout.activity_video_play_detail);
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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoPlay.getLayoutParams();
        params.width = DensityUtil.getScreenSize(this)[0];
        params.height = DensityUtil.getScreenSize(this)[0]*9/16;
        videoPlay.setLayoutParams(params);
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
            getLookNumber(dataId);
            StringUtils.setTextShow(author, videoBean.getSpeakerName().replaceAll(","," "));
            StringUtils.setTextShow(sec_author, videoBean.getRoleName().replaceAll(","," "));
            StringUtils.setTextShow(title_address, videoBean.getClassesName());
            if (!TextUtils.isEmpty(videoBean.getTitle())) {
                String title = videoBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    if(splitLength == -1){
                        content.setText(title);
                        mController.setTitle(title);
                    }else {
                        content.setText(title.substring(0,splitLength));
                        mController.setTitle(title.substring(0,splitLength));
                    }
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength+1,title.length()));
                        mController.setTitle(title.substring(splitLength+1,title.length()));
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
            getLookNumber(dataId);
            StringUtils.setTextShow(author, videoPlayBean.getSpeakerName());
            StringUtils.setTextShow(sec_author, videoPlayBean.getRoleName());
            StringUtils.setTextShow(title_address, videoPlayBean.getClassesName());
            if (!TextUtils.isEmpty(videoPlayBean.getTitle())) {
                String title = videoPlayBean.getTitle();
                int splitLength = title.indexOf(",");
                if (AppApplication.systemLanguage == 1) {
                    if(splitLength == -1){
                        content.setText(title);
                        mController.setTitle(title);
                    }else {
                        content.setText(title.substring(0,splitLength));
                        mController.setTitle(title.substring(0,splitLength));
                    }
                } else {
                    if(splitLength != 0){
                        content.setText(title.substring(splitLength+1,title.length()));
                        mController.setTitle(title.substring(splitLength+1,title.length()));
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
    //获取课件查看人数
    private void getLookNumber(int dataId){
        CHYHttpClientUsage.getInstanse().uploadVideoPlayNumber(dataId,new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                video_peopel_number.setText(getString(R.string.read_count,JSONCatch.parseInt("readCount",response)));
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
        ShareUtils.shareTextWithUrl(VideoPlayDetailActivity.this, content.getText().toString(), Constants.COLLEGE_SHARE_TITLE,
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
        if (videoPlay != null) {
            videoPlay.pause();
            videoPlay.release();
        }
        initViewsAction();
    }
}
