package com.android.incongress.cd.conference.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Map;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

public class TipsClassesAdapter extends BaseAdapter {

    private List<Map> datasource = new ArrayList<>();

    public TipsClassesAdapter() {
        datasource.addAll(ConferenceDbUtils.getAllMaps());
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
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.tips_classes_list_item);
            holder = new Holder();
            IncongressTextView titleView = (IncongressTextView) convertView.findViewById(R.id.tip_classes_name);
            holder.titleView = titleView;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Map bean=datasource.get(position);

        String mapName = "";
        if(bean.getMapRemark().contains("#@#")){
            if (AppApplication.systemLanguage == 1) {
                mapName = bean.getMapRemark().split("#@#")[0];
            }else{
                mapName = bean.getMapRemark().split("#@#")[1];
            }
        }else{
            mapName = bean.getMapRemark();
        }
        holder.titleView.setText(mapName);
        return convertView;
    }

    private class Holder {
    	IncongressTextView titleView;
    }
}
