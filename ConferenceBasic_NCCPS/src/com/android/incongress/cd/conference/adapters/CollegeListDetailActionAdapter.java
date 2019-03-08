package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.CollegeListDetailBean;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jacky on 2016/1/8.
 */
public class CollegeListDetailActionAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private List<CollegeListDetailBean.ClassArrayBean.SessionArrayBean> mSessions;
    private List<CollegeListDetailBean.ClassArrayBean> mClassBean;
    private LayoutInflater mInflater;
    private List<Meeting> mMeetingBeanList = new ArrayList<>();

    //1）sectionIndices数组用来存放每一轮分组的第一个item的位置。
    //2）sectionHeaders数组用来存放每一个分组要展现的数据，
    private int[] mSectionIndices;
    private String[] mSectionHeaders;
    private CollegeOnItemOnclicking onItemOnclicking;
    private int mType;

    public CollegeListDetailActionAdapter(Context ctx, List<CollegeListDetailBean.ClassArrayBean.SessionArrayBean> session, List<CollegeListDetailBean.ClassArrayBean> mClassBean,CollegeOnItemOnclicking onItemOnclicking,int mType) {
        this.mSessions = session;
        this.mInflater = LayoutInflater.from(ctx);
        this.mClassBean = mClassBean;
        this.mType = mType;
        this.onItemOnclicking = onItemOnclicking;
        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
    }

    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    /**
     * sectionIndices数组用来存放每一轮分组的第一个item的位置。
     * @return
     */
    private int[] getSectionIndices() {
        List<Integer> secionIndices = new ArrayList<Integer>();
        int classId = Integer.parseInt(mSessions.get(0).getClassId());
        secionIndices.add(0);

        for(int i=1; i< mSessions.size(); i++) {
            int tempClassId = Integer.parseInt(mSessions.get(i).getClassId());

            if(classId != tempClassId) {
                classId = tempClassId;
                secionIndices.add(i);
            }
        }

        int[] secions = new int[secionIndices.size()];

        for(int i=0; i<secionIndices.size(); i++) {
            secions[i] = secionIndices.get(i);
        }
        return secions;
    }

    /**
     * header中存放的数据
     * @return
     */
    private String[] getSectionHeaders(){
        String[] sectionHeader = new String[mSectionIndices.length];
        for(int i=0; i< mSectionIndices.length; i++) {
            sectionHeader[i] = mSessions.get(mSectionIndices[i]).getClassId();
        }

        return sectionHeader;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.item_headerview_college_list, parent, false);
            holder.tvClassRoom = convertView.findViewById(R.id.tv_schedule_list_location);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        for(int i=0; i<mClassBean.size(); i++) {
            if(mSessions.get(position).getClassId().equals(mClassBean.get(i).getClassId())) {
                String[] strings = mClassBean.get(i).getClassName().split("#@#");
                if(AppApplication.systemLanguage == 1) {
                    holder.tvClassRoom.setText(strings[0]);
                }else {
                    holder.tvClassRoom.setText(strings[1]);
                }
                break;
            }
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return Integer.parseInt(mSessions.get(position).getClassId());
    }

    @Override
    public int getCount() {
        return mSessions.size();
    }

    @Override
    public Object getItem(int position) {
        return mSessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_single_college_list, parent, false);
            holder.tvMeetingName = convertView.findViewById(R.id.schedule_list_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvMeetingName.setTag(position);
        holder.tvMeetingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemOnclicking.onItemOnclick(mSessions.get((int)view.getTag()).getSessionId(),mType);
            }
        });
        String[] strings = mSessions.get(position).getSessionName().split("#@#");
        if (AppApplication.systemLanguage == 1) {
            holder.tvMeetingName.setText(strings[0]);
        } else {
            holder.tvMeetingName.setText(strings[1]);
        }
        return convertView;
    }

    class HeaderViewHolder {
        TextView tvClassRoom;
    }

    class ViewHolder {
        TextView tvMeetingName;
    }
    public interface CollegeOnItemOnclicking{
        void onItemOnclick(String sessionId,int mType);
    }

}
