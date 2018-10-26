package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Exhibitor;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

public class ExhibitorListAdapter extends BaseAdapter {

    private List<Exhibitor> mExhibitors = new ArrayList<>();
    private Context mContext;

    public ExhibitorListAdapter(Context mContext) {
        mExhibitors.addAll(ConferenceDbUtils.getAllExhibitors());
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mExhibitors.size();
    }

    @Override
    public Object getItem(int position) {
        return mExhibitors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.exhibitor_item_view, parent, false);
            holder = new Holder();
            holder.logoView =  (ImageView) convertView.findViewById(R.id.exhibitor_logo);
            holder.nameView =(TextView) convertView.findViewById(R.id.exhibitor_name);
            holder.locationView =  (TextView) convertView.findViewById(R.id.exhibitor_location);
            holder.infoView =  (TextView) convertView.findViewById(R.id.exhibitor_info);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

      Exhibitor bean = mExhibitors.get(position);

        if (bean.getLogo() == null || bean.getLogo().equals("")) {
            holder.logoView.setImageResource(R.drawable.default_load_bg);
        } else {
            PicUtils.loadImageUrl(mContext,Constants.getPreUrl()+bean.getLogo(),holder.logoView);
        }

        if (AppApplication.systemLanguage == 1) {
            holder.nameView.setText(bean.getTitle());
        } else if (AppApplication.systemLanguage == 2) {
            if (bean.getTitleEn() != null && !"".equals(bean.getTitleEn())) {
                holder.nameView.setText(bean.getTitleEn());
            } else {
                holder.nameView.setText(bean.getTitle());
            }
        }

        holder.locationView.setText(AppApplication
                .getContext()
                .getResources()
                .getString(R.string.exhibitor_detail_zanwei,
                        bean.getLocation()));

        if (bean.getInfo() == null || bean.getInfo().equals("")) {
            holder.infoView.setVisibility(View.GONE);
        } else {
            if (AppApplication.systemLanguage == 1) {
                holder.infoView.setText(bean.getInfo());
            } else if (AppApplication.systemLanguage == 2) {
                if (bean.getInfoEn() != null && !"".equals(bean.getInfoEn())) {
                    holder.infoView.setText(bean.getInfoEn());
                } else {
                    holder.infoView.setText(bean.getInfo());
                }
            }
            holder.infoView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class Holder {
        ImageView logoView;
        TextView nameView;
        TextView locationView;
        TextView infoView;
    }
}
