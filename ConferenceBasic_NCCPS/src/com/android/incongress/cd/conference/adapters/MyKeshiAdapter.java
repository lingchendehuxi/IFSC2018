package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MyFieldBean;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

public class MyKeshiAdapter extends RecyclerView.Adapter<TextViewHolder> implements View.OnClickListener{
    //所有科室集合
    private static final String[] KESHI = {"重症医学科","呼吸内科","麻醉疼痛科","急诊科","内科", "大内科",
            "中西医结合科","乳腺外科", "介入科","儿科", "全科医学","内分泌科","医学影像科", "外科",
            "妇产科", "小儿综合内科","小儿综合外科", "小儿血液科","小儿骨科", "放疗科", "普通外科",
            "核医学科","检验科","泌尿外科","消化内科", "物理治疗与康复科", "病理科","皮肤性病科",
            "神经外科","精神心理科","老年医学科","耳鼻咽喉头颈科","肝胆外科","营养科", "肿瘤科",
            "胸外科", "血液内科","血液外科", "超声科", "骨科","中医科", "其他"};
    private Context mContext;
    /** 当前选择的科室    **/
    private String mCurrentKeshi;

    public MyKeshiAdapter(Context context) {
        this.mContext = context;
        mCurrentKeshi = SharePreferenceUtils.getAppString(Constants.MY_KESHI);
    }

    public void setCurrentKeshi(String keshi) {
        this.mCurrentKeshi = keshi;
        notifyDataSetChanged();
        SharePreferenceUtils.saveAppString(Constants.MY_KESHI, mCurrentKeshi);
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_field, parent, false);
        view.setOnClickListener(this);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TextViewHolder holder, final int position) {
        holder.textView.setText(KESHI[position]);
        if(!TextUtils.isEmpty(mCurrentKeshi) && KESHI[position].equals(mCurrentKeshi)) {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.theme_color));
        }else {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.field_text_color));
        }

        holder.itemView.setTag(KESHI[position]);
    }

    @Override
    public int getItemCount() {
        return KESHI.length;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String bean);
    }
}