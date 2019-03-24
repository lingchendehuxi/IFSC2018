package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.BusRemindBean;
import com.android.incongress.cd.conference.model.BusInfo;
import com.android.incongress.cd.conference.utils.AlarmUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Admin on 2017/5/18.
 */

public class MeetingBusRemindMyAdapater extends RecyclerView.Adapter<MeetingBusRemindMyAdapater.ItemViewHolder> {
    private List<BusRemindBean> mBusInfos;
    private Context mContext;

    public MeetingBusRemindMyAdapater(Context c, List<BusRemindBean> beans) {
        this.mContext = c;
        if (beans != null)
            mBusInfos = beans;
    }

    @Override
    public int getItemCount() {
        return mBusInfos.size();
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final BusRemindBean busArrayBean = mBusInfos.get(position);
        holder.busFrom.setText(busArrayBean.getBusFrom());
        holder.busTo.setText(busArrayBean.getBusTo());
        holder.busStartTime.setText(busArrayBean.getBusTime());
        holder.busEndTime.setText(busArrayBean.getBackTime());

        if (busArrayBean.getIsVip() == 1) {
            holder.ivVip.setVisibility(View.VISIBLE);
        } else {
            holder.ivVip.setVisibility(View.INVISIBLE);
        }
        holder.backRemind.setVisibility(View.GONE);
        holder.hc.setVisibility(View.GONE);
        holder.isBus.setImageResource(R.drawable.shuttlebus_circulation);
        holder.startRemind.setVisibility(View.GONE);

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_reminder_new, parent, false);
        return new ItemViewHolder(view);
    }


    /**
     * ItemViewHolder 实现点击事件的接口
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView busFrom, busTo, busStartTime, busEndTime, startRemind, backRemind, hc;
        ImageView ivVip, isBus;

        /**
         * @param view
         */
        public ItemViewHolder(View view) {
            super(view);
            isBus = (ImageView) view.findViewById(R.id.bus_img);
            busFrom = (TextView) view.findViewById(R.id.tv_from);
            hc = (TextView) view.findViewById(R.id.hc_text);
            busTo = (TextView) view.findViewById(R.id.tv_to);
            busStartTime = (TextView) view.findViewById(R.id.tv_start_time);
            busEndTime = (TextView) view.findViewById(R.id.tv_end_time);
            ivVip = (ImageView) view.findViewById(R.id.iv_vip);
            startRemind = (TextView) view.findViewById(R.id.tv_start_remind);
            backRemind = (TextView) view.findViewById(R.id.tv_back_remind);
        }

    }


}
