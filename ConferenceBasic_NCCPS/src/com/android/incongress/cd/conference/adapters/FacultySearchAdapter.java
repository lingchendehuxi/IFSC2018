package com.android.incongress.cd.conference.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

public class FacultySearchAdapter extends BaseAdapter {

    private List<Speaker> datasource = new ArrayList<>();

    public FacultySearchAdapter() {
    }

    public List<Speaker> getDatasource() {
        return  datasource;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.item_faculty_search);
            holder = new Holder();
            holder.facultyName = (TextView) convertView.findViewById(R.id.tv_faculty_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Speaker bean = datasource.get(position);

        if(AppApplication.systemLanguage == 1) {
            holder.facultyName.setText(bean.getSpeakerName());
        }else {
            holder.facultyName.setText(bean.getEnName());
        }

        return convertView;
    }


    private class Holder {
        TextView facultyName;
    }
    public void search(String sessionname){
        if(AppApplication.systemLanguage == 1) {
            datasource = ConferenceDbUtils.getSpeakerBySpeakerName(sessionname, false);
        }else {
            datasource = ConferenceDbUtils.getSpeakerBySpeakerName(sessionname, true);
        }

        notifyDataSetChanged();
    }
    public void empty(){
        datasource = new ArrayList<>();
        notifyDataSetChanged();
    }
    /**
     * 判断是否显示无数据提示	
     * @return
     */
    public boolean isNoDataShow() {
    	if(datasource == null)
    		return true;
    	if(datasource.size() == 0) {
    		return true;
    	}
    	return false;
    }
}
