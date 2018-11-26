package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.incongress.cd.conference.model.Meeting;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2018/3/9.
 */

public class MeetingYYAdapter extends BaseAdapter
{
    private List<Meeting> mList;
    private Context mContext;
    private boolean[] checks;
    public MeetingYYAdapter(List<Meeting> mList, Context mContext)
    {
        this.mList = mList;
        this.mContext = mContext;
        checks = new boolean[mList.size()];
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        MeetingViewHolder holder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_meetingyy, null);
            holder = new MeetingViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.meeting_title);
            holder.cb = (CheckBox) convertView.findViewById(R.id.meeting_cb);
            convertView.setTag(holder);
        }
        else
        {
            holder = (MeetingViewHolder)convertView.getTag();
        }
        final Meeting bean = mList.get(position);

        holder.title.setText(bean.getTopic());
        final int pos  = position; //pos必须声明为final
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                checks[pos] = isChecked;
            }});
        holder.cb.setChecked(checks[pos]);
        return convertView;
    }
    public static class MeetingViewHolder
    {
        public TextView title;
        public CheckBox cb;
    }
}



