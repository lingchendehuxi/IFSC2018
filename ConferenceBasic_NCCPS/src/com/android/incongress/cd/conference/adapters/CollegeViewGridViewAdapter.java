package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.CollegeVideoBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/18.
 * 关注的会议列表[meeting]
 */
public class CollegeViewGridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FastOnLineBean.VideoArrayBean> mVideoList;
    private Context mContext;
    private VideoItemClick videoItemClick;

    public CollegeViewGridViewAdapter(List<FastOnLineBean.VideoArrayBean> mVideoList, Context mContext,VideoItemClick videoItemClick) {
        this.mVideoList = mVideoList;
        this.mContext = mContext;
        this.videoItemClick = videoItemClick;
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        FastOnLineBean.VideoArrayBean bean = mVideoList.get(position);
        StringUtils.setTextShow(((MyViewHolder) holder).video_show_time,bean.getOpenTime());
        StringUtils.setCommaTextShow(((MyViewHolder) holder).video_title,bean.getTitle());
        StringUtils.setTextShow(((MyViewHolder) holder).video_time,bean.getSpeakerName().replaceAll(","," "));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.height = ((DensityUtil.getScreenSize((HomeActivity)mContext)[0]-DensityUtil.dip2px(mContext,46))*2)/8;
        ((MyViewHolder) holder).iv_background.setLayoutParams(layoutParams);
        PicUtils.loadImageUrl(mContext,bean.getVideoImage(),((MyViewHolder) holder).iv_background);
        ((MyViewHolder) holder).ll_video_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoItemClick.itemClick(position);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_list, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_background;
        TextView video_show_time, video_title, video_time;
        LinearLayout ll_video_item;

        public MyViewHolder(View view) {
            super(view);
            video_show_time = view.findViewById(R.id.video_show_time);
            video_title = view.findViewById(R.id.video_title);
            video_time = view.findViewById(R.id.video_time);
            iv_background = view.findViewById(R.id.iv_background);
            ll_video_item = view.findViewById(R.id.ll_video_item);
        }
    }
    public interface VideoItemClick{
        void itemClick(int position);
    }
}
