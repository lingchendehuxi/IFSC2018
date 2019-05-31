package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2017/7/5.
 */
public class CSCOSoundAdapter extends RecyclerView.Adapter<CSCOSoundAdapter.ViewHolder> {


    private List<ScenicXiuBean> mCourseBeanList = new ArrayList<>();
    CSCOSoundAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;

    public CSCOSoundAdapter(List<ScenicXiuBean> beans, Context context) {
        this.mCourseBeanList = beans;
        this.mContext = context;
    }

    @Override
    public CSCOSoundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.item_sound, parent, false);
        return new CSCOSoundAdapter.ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(CSCOSoundAdapter.ViewHolder holder, final int position) {
        ScenicXiuBean bean = mCourseBeanList.get(position);
        holder.title.setText(bean.getTitle());
        holder.time.setText(bean.getTimeShow());
        if (bean.getType() == 1) {
            holder.type.setText("新闻");
            holder.type.setBackgroundColor(mContext.getResources().getColor(R.color.home_theme_color));
        } else {
            holder.type.setText("通知");
            holder.type.setBackgroundColor(mContext.getResources().getColor(R.color.home_theme_color));
        }
        if (bean.getLogoUrl().equals("")) {
            holder.layout.setBackgroundColor(Color.WHITE);
            holder.img.setVisibility(View.GONE);
        } else {
            holder.img.setVisibility(View.VISIBLE);
            PicUtils.loadImageUrl(mContext,bean.getLogoUrl(),holder.img);
            ViewGroup.LayoutParams lp = holder.img.getLayoutParams();
            lp.height = (int) (DensityUtil.getScreenSize((HomeActivity) mContext)[0] * 0.45);
            holder.img.setLayoutParams(lp);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView title, type, time;
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.sound_layout);
            title = view.findViewById(R.id.sound_title);
            type = view.findViewById(R.id.sound_type);
            time = view.findViewById(R.id.sound_time);
            img = view.findViewById(R.id.sound_img);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final CSCOSoundAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}