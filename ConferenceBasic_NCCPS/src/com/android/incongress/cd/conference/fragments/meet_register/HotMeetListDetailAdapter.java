package com.android.incongress.cd.conference.fragments.meet_register;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.HomeMeetBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;


/**
 * Created by Jacky on 2016/1/8.
 */
public class HotMeetListDetailAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private LayoutInflater inflater;

    private List<HomeMeetBean> mPlanDetails;
    private Context mContext;

    public HotMeetListDetailAdapter(Activity context, List<HomeMeetBean> mPlanDetails) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.mPlanDetails = mPlanDetails;

    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    @Override
    public int getPositionForSection(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return mPlanDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlanDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.fragment_hot_meet_context, parent, false);
            holder.iv_img =  convertView.findViewById(R.id.iv_img);
            holder.tv_coming =  convertView.findViewById(R.id.tv_coming);
            holder.tv_title =  convertView.findViewById(R.id.tv_title);
            holder.tv_time =  convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomeMeetBean planDetail = this.mPlanDetails.get(position);
        if (planDetail != null) {
            if(planDetail.isBegin()){
                holder.tv_coming.setVisibility(View.VISIBLE);
            }else {
                holder.tv_coming.setVisibility(View.GONE);
            }
            PicUtils.loadImageUrl(mContext,planDetail.getIconUrl(),holder.iv_img);
            holder.tv_title.setText(planDetail.getConferencesName());
            holder.tv_time.setText(planDetail.getConferencesDays()+"  "+planDetail.getConferencesAddress());
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.fragment_hot_meet_head, parent, false);
            holder.hot_meet_title =  convertView.findViewById(R.id.hot_meet_title);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set proj_plans_header text as first char in name
        if(this.mPlanDetails.get(position).isCurrentMonth()){
            holder.hot_meet_title.setText("当月");
        }else {
            holder.hot_meet_title.setText(this.mPlanDetails.get(position).getMonthString()+"月");
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return this.mPlanDetails.get(position).getMonthString();
    }

    class HeaderViewHolder {
        TextView hot_meet_title;
    }

    class ViewHolder {
        ImageView iv_img;
        TextView tv_coming;
        TextView tv_title;
        TextView tv_time;
    }

}
