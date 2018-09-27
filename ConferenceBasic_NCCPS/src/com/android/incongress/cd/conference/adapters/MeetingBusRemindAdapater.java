package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.BusInfo;
import com.android.incongress.cd.conference.utils.AlarmUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Admin on 2017/5/18.
 */

public class MeetingBusRemindAdapater extends RecyclerView.Adapter<MeetingBusRemindAdapater.ItemViewHolder> {
    private List<BusInfo.DateArrayBean.BusArrayBean> mBusInfos;
    private Context mContext;

    private BusRemindClickListener mBusRemindListener;

    /**
     * 设置Item点击监听  自定义 让子类实现
     *
     * @param listener
     */
    public void setOnItemClickListener(BusRemindClickListener listener) {
        this.mBusRemindListener = listener;
    }

    public MeetingBusRemindAdapater(Context c, List<BusInfo.DateArrayBean.BusArrayBean> beans) {
        this.mContext = c;
        if (beans != null)
            mBusInfos = beans;
    }

    @Override
    public int getItemCount() {
        return mBusInfos.size();
    }

    @Override
    public void onBindViewHolder(final MeetingBusRemindAdapater.ItemViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final BusInfo.DateArrayBean.BusArrayBean busArrayBean = mBusInfos.get(position);
            holder.busFrom.setText(busArrayBean.getBusFrom());
            holder.busTo.setText(busArrayBean.getBusTo());
            holder.busStartTime.setText(busArrayBean.getBusTime());
            holder.busEndTime.setText(busArrayBean.getBackTime());

            if (busArrayBean.getIsVip() == 1) {
                holder.ivVip.setVisibility(View.VISIBLE);
            } else {
                holder.ivVip.setVisibility(View.INVISIBLE);
            }
            if(busArrayBean.getBackTime().equals("")){
                holder.backRemind.setVisibility(View.INVISIBLE);
                holder.hc.setVisibility(View.INVISIBLE);
                holder.isBus.setImageResource(R.drawable.shuttlebus_circulation);
            }else{
                holder.backRemind.setVisibility(View.VISIBLE);
                holder.hc.setVisibility(View.VISIBLE);
                holder.isBus.setImageResource(R.drawable.shuttlebus_circulation_loop);
            }
            holder.startRemind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBusRemindListener.onStartRemindListener(v, busArrayBean,position);
                }
            });

            holder.backRemind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBusRemindListener.onBackRemindListener(v, busArrayBean, position);
                }
            });
            if(AlarmUtils.findBusRemindByBusInfoIdAndTime(busArrayBean.getBusInfoId(),0)) {
                //已定闹钟
                holder.startRemind.setBackgroundResource(R.drawable.bus_remind_pressed);
                holder.startRemind.setText(R.string.bus_tx_qx);
                busArrayBean.setStartNotify(true);
            }else {
                holder.startRemind.setBackgroundResource(R.drawable.bus_selected);
                holder.startRemind.setText(R.string.bus_tx_sz);
                busArrayBean.setStartNotify(false);
            }
            if(AlarmUtils.findBusRemindByBusInfoIdAndTime(busArrayBean.getBusInfoId(),1)) {
                //已定闹钟
                holder.backRemind.setBackgroundResource(R.drawable.bus_remind_pressed);
                holder.backRemind.setText(R.string.bus_tx_qx);
                busArrayBean.setEndNotify(true);
            }else {
                holder.backRemind.setBackgroundResource(R.drawable.bus_selected);
                holder.backRemind.setText(R.string.bus_tx_fc);
                busArrayBean.setEndNotify(false);
            }
        }
    }
    @Override
    public MeetingBusRemindAdapater.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_reminder_new, parent, false);
        return new ItemViewHolder(view);
    }


    /**
     * ItemViewHolder 实现点击事件的接口
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView busFrom, busTo, busStartTime, busEndTime, startRemind, backRemind,hc;
        ImageView ivVip,isBus;

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

    public interface BusRemindClickListener {
        void onStartRemindListener(View view, BusInfo.DateArrayBean.BusArrayBean busArrayBean, int position);
        void onBackRemindListener(View view, BusInfo.DateArrayBean.BusArrayBean busArrayBean, int position);
    }

    
    
}
