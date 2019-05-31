package com.android.incongress.cd.conference.fragments.live;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.beans.HomeLiveBean;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by 13008 on 2019/4/12.
 */

public class HomeLiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HomeLiveBean> mBeans;
    private Context mContext;
    private static int IS_BEGIN = 0;
    private static int NO_BEGIN = 1;
    private itemOnclick itemOnclick;
    private showMoreOnclick showMoreOnclick;
    private int mFoot=1;
    private final static int ITEM_FOOT=2;

    public HomeLiveAdapter(Context ctx, List<HomeLiveBean> beans, itemOnclick itemOnclick,showMoreOnclick showMoreOnclick) {
        this.mContext = ctx;
        this.mBeans = beans;
        this.itemOnclick = itemOnclick;
        this.showMoreOnclick = showMoreOnclick;
    }

    @Override
    public int getItemCount() {
        return mBeans.size()+mFoot;
    }

    @Override
    public int getItemViewType(int position) {
        if (mFoot!=0&&position>=mBeans.size()){
            return ITEM_FOOT;
        }
        if (DateUtil.isStart(mBeans.get(position).getSessionStartTime())) {
            return IS_BEGIN;
        } else {
            return NO_BEGIN;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView;
        if (viewType == NO_BEGIN) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_live_order_video, parent, false);
            return new MViewHolder(convertView);
        } else if(viewType == IS_BEGIN){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_live_play_video, parent, false);
            return new MViewHolder(convertView);
        }else if (viewType==ITEM_FOOT){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_new_home_show_more, parent, false);
            return new FooterViewHolder(convertView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(position<mBeans.size()){
            final HomeLiveBean bean = mBeans.get(position);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (DensityUtil.getScreenSize((HomeActivity)mContext)[0]*0.9), (int) (DensityUtil.getScreenSize((HomeActivity)mContext)[0]*0.9*0.56));
            holder.itemView.setLayoutParams(params);
            if (holder.getItemViewType() == NO_BEGIN) {
                if (TextUtils.isEmpty(bean.getSessionStartTime())) {
                    ToastUtils.showToast("直播时间格式错误，请联系指导员");
                } else {
                    Long[] strings = DateUtil.getRemainsTime(bean.getSessionStartTime());
                    if(timeIsBegin(strings)){
                        ((MViewHolder) holder).ll_time.setVisibility(View.GONE);
                        ((MViewHolder) holder).tv_begin_show.setVisibility(View.VISIBLE);
                    }else {
                        ((MViewHolder) holder).ll_time.setVisibility(View.VISIBLE);
                        ((MViewHolder) holder).tv_begin_show.setVisibility(View.GONE);
                        ((MViewHolder) holder).tv_day.setText(longOverString(strings[0]));
                        ((MViewHolder) holder).tv_hour.setText(longOverString(strings[1]));
                        ((MViewHolder) holder).tv_minute.setText(longOverString(strings[2]));
                    }
                }
                ((MViewHolder) holder).tv_person.setText(StringUtils.getNeedString(mBeans.get(position).getClassName()));
            }
            StringUtils.setTextShow(((MViewHolder) holder).tv_title, bean.getSessionGroupName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.getItemViewType() == IS_BEGIN){
                        itemOnclick.onItemClick(position);
                    }
                }
            });
        }else {
            ((FooterViewHolder)holder).tv_show_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMoreOnclick.onShowMoreClick();
                }
            });
        }
    }
    private boolean timeIsBegin(Long[] strings){
        for (Long string:strings){
            if(string != 0){
               return false;
            }
        }
        return true;
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_person, tv_day, tv_hour, tv_minute,tv_begin_show;
        LinearLayout ll_person, ll_time;

        public MViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_person = view.findViewById(R.id.tv_person);
            tv_day = view.findViewById(R.id.tv_day);
            tv_hour = view.findViewById(R.id.tv_hour);
            tv_minute = view.findViewById(R.id.tv_minute);
            tv_begin_show = view.findViewById(R.id.tv_begin_show);
            ll_person = view.findViewById(R.id.ll_person);
            ll_time = view.findViewById(R.id.ll_time);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tv_show_more;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tv_show_more = itemView.findViewById(R.id.tv_show_more);
        }
    }

    private String longOverString(Long date) {
        if (date == 0) {
            return String.valueOf(date) + "0";
        } else {
            return String.valueOf(date);
        }
    }

    public interface itemOnclick {
        void onItemClick(int position);
    }
    public interface showMoreOnclick {
        void onShowMoreClick();
    }
}
