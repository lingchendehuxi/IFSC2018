package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.widget.flow_layout.FlowLayout;
import com.android.incongress.cd.conference.widget.flow_layout.TagAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/7.
 */
public class RoomTagAdapter extends TagAdapter<Class> {
    private List<Class> mBeans;
    private Context mContext;

    public RoomTagAdapter(Context ctx, List<Class> datas) {
        super(datas);
        this.mBeans = datas;
        this.mContext = ctx;
    }

    @Override
    public void setSelectedList(int... pos) {
        super.setSelectedList(pos);
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public Class getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public View getView(FlowLayout parent, int position, Class speakerBean) {
        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.room_tag_layout, parent, false);

        if (mBeans != null && mBeans.size() > 0) {
            if (AppApplication.systemLanguage == 1) {
                tv.setText(speakerBean.getClassesCode());
            } else {
                tv.setText(speakerBean.getClassCodeEn());
            }
        }
        return tv;
    }
}
