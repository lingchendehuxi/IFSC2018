package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.android.incongress.cd.conference.beans.HotListBean;
import com.android.incongress.cd.conference.beans.SatelliteInfoListBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/29.
 */
public class NewSatelliteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SatelliteInfoListBean.ResultBean> mBeans;
    private LayoutInflater mInflater;

    public NewSatelliteListAdapter(Context mContext, List<SatelliteInfoListBean.ResultBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        view = mInflater.inflate(R.layout.new_satellite_item, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        SatelliteInfoListBean.ResultBean bean = mBeans.get(position);
        StringUtils.setTextShow(((ViewHolder) holder).tv_title,bean.getTitle());
        Drawable drawable=mContext.getResources().getDrawable(R.drawable.time_small);
        Drawable drawable2=mContext.getResources().getDrawable(R.drawable.icon_location);
        drawable.setBounds(0,0,40,40);
        drawable2.setBounds(0,0,40,40);
        ((ViewHolder) holder).tv_time.setCompoundDrawables(drawable,null,null,null);
        ((ViewHolder) holder).tv_location.setCompoundDrawables(drawable2,null,null,null);
        ((ViewHolder) holder).tv_time.setText(bean.getDay()+" "+bean.getStartTime()+" - "+bean.getEndTime());
        StringUtils.setTextShow(((ViewHolder) holder).tv_location," "+bean.getLocation());
        if(TextUtils.isEmpty(bean.getImg())){
            ((ViewHolder) holder).iv_img.setVisibility(View.GONE);
            ((ViewHolder) holder).view_top.setVisibility(View.GONE);
        }else {
            ((ViewHolder) holder).iv_img.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).view_top.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height = DensityUtil.getScreenSize((HomeActivity)mContext)[0]/2;
            ((ViewHolder) holder).iv_img.setLayoutParams(layoutParams);
            PicUtils.loadImageUrl(mContext, bean.getImg(), ((ViewHolder) holder).iv_img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_time,tv_location;
        ImageView iv_img;
        View view_top;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            iv_img = itemView.findViewById(R.id.iv_img);
            view_top = itemView.findViewById(R.id.view_top);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

