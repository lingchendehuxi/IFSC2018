package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.IncongressBean;
import com.android.incongress.cd.conference.beans.MyFieldBean;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.client.methods.HttpOptions;
import cz.msebera.android.httpclient.util.TextUtils;

public class MyFieldAdapter extends RecyclerView.Adapter<TextViewHolder> implements View.OnClickListener{
    //所有领域集合
    private static final String[] FIELDS = {"食管癌",
            "肝胆胰肿瘤",
            "胃肠肿瘤",
            "头颈部肿瘤",
            "呼吸系统肿瘤",
            "乳腺肿瘤",
            "妇科肿瘤",
            "血液淋巴系统肿瘤",
            "泌尿生殖系统肿瘤",
            "姑息治疗和中医药",
            "转化性研究",
            "骨与软组织肉瘤",
            "黑色素瘤及皮肤肿瘤",
            "放疗",
            "其他"};
    private List<MyFieldBean> mMyFields;
    private Context mContext;

    public MyFieldAdapter(Context context) {
        this.mContext = context;
        mMyFields = new ArrayList<>();
        MyFieldBean bean;

        for (int i = 0; i < FIELDS.length; i++) {
            bean = new MyFieldBean();
            bean.setFieldName(FIELDS[i]);
            if (ConferenceDbUtils.getMyFieldState(FIELDS[i]) == 1) {
                bean.setFieldState(1);
            } else {
                bean.setFieldState(0);
            }

            mMyFields.add(bean);
        }
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_field, parent, false);
        view.setOnClickListener(this);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TextViewHolder holder, final int position) {
        final MyFieldBean bean = mMyFields.get(position);
        holder.textView.setText(bean.getFieldName());
        if (bean.isFieldState() == 1) {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.theme_color));
        } else {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.field_text_color));
        }
        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemCount() {
        return FIELDS.length;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (MyFieldBean) v.getTag());
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, MyFieldBean bean);
    }
}