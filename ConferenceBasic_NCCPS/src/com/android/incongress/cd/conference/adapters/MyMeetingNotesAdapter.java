package com.android.incongress.cd.conference.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Note;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

public class MyMeetingNotesAdapter extends BaseAdapter {

    private List<Note> datasource = new ArrayList<>();

    public MyMeetingNotesAdapter(List<Note> notes) {
        if(notes != null)
            datasource.addAll(notes);
    }

    public List<Note> getDatas() {
        return datasource;
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
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.mycenter_note_child);
            holder = new Holder();
            TextView titleView = (TextView) convertView.findViewById(R.id.mycenter_note_child_title);
            TextView timeView = (TextView) convertView.findViewById(R.id.mycenter_note_child_time);
            TextView classview = (TextView) convertView.findViewById(R.id.mycenter_note_child_room);
            holder.titleView = titleView;
            holder.timeView = timeView;
            holder.classView = classview;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Note bean = datasource.get(position);
        holder.titleView.setText(bean.getTitle());
        holder.timeView.setText(CommonUtils.formatTimeYueRi(bean.getDate()) + " " + bean.getStart() + "-" + bean.getEnd());
        holder.classView.setText(bean.getRoom());

        return convertView;
    }


    private class Holder {
        TextView titleView;
        TextView timeView;
        TextView classView;
    }

    /**
     * 删除一条笔记
     * @param position
     */
    public void deleteNote(int position) {
        Note note = datasource.get(position);
        if(ConferenceDbUtils.deleteOneNote(note) > 0) {
            datasource.remove(note);
            notifyDataSetChanged();
        }
    }

}
