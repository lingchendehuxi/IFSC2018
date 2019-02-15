package com.android.incongress.cd.conference.widget.blws;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.incongress.cd.conference.utils.PicUtils;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.vo.PolyvVideoVO;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class PolyvPlayerAudioCoverView extends FrameLayout {
    /**
     * 播放音频时的封面图
     */
    private ImageView iv_audio_cover = null;
    private ImageView iv_audio_cover_m = null;
    private FrameLayout fl_cover = null;
    private ObjectAnimator animator;
    private float currentValue;
    private String currentMode;
    private Context context;

    public PolyvPlayerAudioCoverView(@NonNull Context context) {
        this(context, null);
        this.context = context;
    }

    public PolyvPlayerAudioCoverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolyvPlayerAudioCoverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.polyv_player_audio_cover, this);
        iv_audio_cover = (ImageView) findViewById(R.id.iv_audio_cover);
        iv_audio_cover_m = (ImageView) findViewById(R.id.iv_audio_cover_m);
        fl_cover = (FrameLayout) findViewById(R.id.fl_cover);
    }

    private void showCover(PolyvVideoView videoView, ImageView imageView, boolean isRotateView) {
        imageView.setImageBitmap(null);
        imageView.setVisibility(View.VISIBLE);
        PolyvVideoVO videoVO = videoView.getVideo();
        if (videoVO != null)
            if (!videoView.isLocalPlay()) {
                PicUtils.loadImageUrl(context, videoVO.getFirstImage(), imageView);
                /*ImageLoader.getInstance().displayImage(videoVO.getFirstImage(), imageView
                        , new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                                .showImageForEmptyUri(isRotateView ? R.drawable.polyv_rotate_cover_default : R.drawable.polyv_bg_cover_default)//显示默认的图片
                                .showImageOnFail(isRotateView ? R.drawable.polyv_rotate_cover_default : R.drawable.polyv_bg_cover_default).build()
                        , new PolyvAnimateFirstDisplayListener());*/
            } else {
                imageView.setImageResource(isRotateView ? R.drawable.polyv_rotate_cover_default : R.drawable.polyv_bg_cover_default);//显示默认的图片
            }
        else
            imageView.setImageResource(isRotateView ? R.drawable.polyv_rotate_cover_default : R.drawable.polyv_bg_cover_default);//显示默认的图片
    }

    public void changeModeFitCover(PolyvVideoView videoView, String changedMode) {
        this.currentMode = changedMode;
        if (PolyvVideoVO.MODE_AUDIO.equals(changedMode)) {
            currentValue = 0;
            startAnimation();
            fl_cover.setVisibility(View.VISIBLE);
            showCover(videoView, iv_audio_cover_m, false);
            showCover(videoView, iv_audio_cover, true);
        } else {
            hide();
        }
    }

    public void hide() {
        if (animator != null)
            animator.cancel();
        fl_cover.setVisibility(View.GONE);
        iv_audio_cover.setVisibility(View.GONE);
        iv_audio_cover_m.setVisibility(View.GONE);
    }

    public void stopAnimation() {
        if (PolyvVideoVO.MODE_AUDIO.equals(currentMode)) {
            if (animator != null)
                animator.cancel();
        }
    }

    public void startAnimation() {
        stopAnimation();
        if (PolyvVideoVO.MODE_AUDIO.equals(currentMode)) {
            animator = ObjectAnimator.ofFloat(fl_cover, "rotation", currentValue - 360, currentValue);
            animator.setDuration(15000);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentValue = (float) animation.getAnimatedValue();
                }
            });
            animator.start();
        }
    }
}