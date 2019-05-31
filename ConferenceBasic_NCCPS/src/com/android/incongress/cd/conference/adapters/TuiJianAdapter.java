package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.TuiJianBean;
import com.android.incongress.cd.conference.utils.transformer.CircleTransform;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/7/5.
 */
public class TuiJianAdapter extends RecyclerView.Adapter<TuiJianAdapter.ViewHolder> {


    private List<TuiJianBean> mCourseBeanList = new ArrayList<TuiJianBean>();
    TuiJianAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;
    private int type,width;

    public TuiJianAdapter(List<TuiJianBean> beans, Context context, int width) {
        this.mCourseBeanList = beans;
        this.mContext = context;
        this.type = type;
        this.width = width;
    }

    @Override
    public TuiJianAdapter.ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.item_tuijian, parent, false);
        ViewGroup.LayoutParams lp=sView.getLayoutParams();
        lp.width= (int) (width*0.35);
        lp.height = (int) (lp.width*1.2);
        sView.setLayoutParams(lp);
        return new TuiJianAdapter.ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(TuiJianAdapter.ViewHolder holder , int position) {
        TuiJianBean bean = mCourseBeanList.get(position);
        holder.name.setText(bean.getTrueName());
        holder.yy.setText(bean.getDanwei());
        Glide.with(mContext).load(bean.getImgUrl()).transform(new CircleTransform(mContext)).into(holder.img);
        }

    @Override
    public int getItemCount() {
        return mCourseBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,yy;
        ImageView img;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tj_name);
            yy = (TextView) view.findViewById(R.id.tj_yy);
            img = (ImageView) view.findViewById(R.id.tj_img);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final TuiJianAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}