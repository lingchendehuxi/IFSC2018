package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.widget.flow_layout.FlowLayout;
import com.android.incongress.cd.conference.widget.flow_layout.TagAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/7.
 */
public class SpeakerTagAdapter extends TagAdapter<Speaker> {
    private List<Speaker> mBeans;
    private List<Role> mRoleBeans;
    private Context mContext;

    public SpeakerTagAdapter(Context ctx, List<Speaker> datas) {
        super(datas);
        mBeans = datas;
        this.mContext = ctx;
    }

    public void setRoleName(List<Role> roleBeans) {
        this.mRoleBeans = roleBeans;
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
    public Speaker getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public View getView(FlowLayout parent, int position, Speaker speakerBean) {
        View view =  LayoutInflater.from(mContext).inflate(R.layout.speaker_layout, parent, false);

        TextView  name =  view.findViewById(R.id.speaker_name);
        ImageView img =  view.findViewById(R.id.speaker_img);
//        if(mRoleBeans!= null && mRoleBeans.size()>0)  {
//            if(AppApplication.systemLanguage == 1) {
////                tv.setText(mRoleBeans.get(position).getName()+":"+speakerBean.getSpeakerName());
//                tv.setText(speakerBean.getSpeakerName());
//            }else {
////                tv.setText(mRoleBeans.get(position).getEnName()+":"+speakerBean.getSpeakerName());
//                tv.setText(speakerBean.getEnName());
//            }
//        }
        if (mBeans != null && mBeans.size() > 0) {
            if (AppApplication.systemLanguage == 1) {
                if(speakerBean.getSpeakerName().length()>3){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    name.setLayoutParams(layoutParams);
                }
                name.setText(speakerBean.getSpeakerName());
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                name.setLayoutParams(layoutParams);
                name.setText(speakerBean.getEnName());
            }
        }
        return view;
    }

    /**
     * 获取讲者列表
     *
     * @return
     */
    public List<Speaker> getSpeakerList() {
        return mBeans;
    }
}
