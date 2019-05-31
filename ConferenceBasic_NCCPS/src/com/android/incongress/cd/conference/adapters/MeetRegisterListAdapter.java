package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.MeetRegisterBean;
import com.android.incongress.cd.conference.fragments.meet_register.MeetRegisterFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by 13008 on 2019/5/17.
 */

public class MeetRegisterListAdapter extends RecyclerView.Adapter<MeetRegisterListAdapter.ViewHolder>{
    private Context mContext;
    private List<MeetRegisterBean> mBeans;
    private LayoutInflater mInflater;
    private MeetRegisterOnClick meetRegisterOnClick;

    public MeetRegisterListAdapter(Context mContext, List<MeetRegisterBean> mBeans,MeetRegisterOnClick meetRegisterOnClick) {
        this.mContext = mContext;
        this.mBeans = mBeans;
        this.mInflater = LayoutInflater.from(mContext);
        this.meetRegisterOnClick =meetRegisterOnClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_meet_register_item, parent, false);
        ;
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_item.setText(mBeans.get(position).getConName());
        if (mBeans.get(position).isChecked()) {
            holder.tv_item.setBackground(mContext.getResources().getDrawable(R.drawable.bg_segment_selected));
            holder.tv_item.setTextColor(mContext.getResources().getColor(R.color.theme_color));
        } else {
            holder.tv_item.setBackground(mContext.getResources().getDrawable(R.drawable.bg_segment_meet_unselect));
            holder.tv_item.setTextColor(mContext.getResources().getColor(R.color.unselect_color));
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meetRegisterOnClick.onItemOnclick((int)view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item);
        }
    }
    public interface MeetRegisterOnClick {
        void onItemOnclick(int position);
    }
}
