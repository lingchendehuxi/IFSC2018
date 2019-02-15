package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/18.
 * 关注的会议列表[meeting]
 */
public class LiveVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FastOnLineBean.VideoArrayBean> mVideoList;
    private Context mContext;

    public LiveVideoAdapter(List<FastOnLineBean.VideoArrayBean> mVideoList, Context mContext) {
        this.mVideoList = mVideoList;
        this.mContext = mContext;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FastOnLineBean.VideoArrayBean bean = mVideoList.get(position);
        ((MyViewHolder) holder).video_show_time.setText(bean.getOpenTime());
        String[]  strings = bean.getTitle().split(",");
        if(AppApplication.systemLanguage == 1){
            ((MyViewHolder) holder).video_title.setText(strings[0]);
        }else {
            ((MyViewHolder) holder).video_title.setText(strings[1]);
        }
        ((MyViewHolder) holder).video_time.setText(bean.getTime());
        PicUtils.loadImageUrl(mContext,bean.getVideoImage(),((MyViewHolder) holder).iv_background);
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

        public MyViewHolder(View view) {
            super(view);
            video_show_time = view.findViewById(R.id.video_show_time);
            video_title = view.findViewById(R.id.video_title);
            video_time = view.findViewById(R.id.video_time);
            iv_background = view.findViewById(R.id.iv_background);
        }
    }
}
