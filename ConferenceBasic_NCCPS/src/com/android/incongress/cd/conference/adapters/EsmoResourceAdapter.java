package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.EsmosBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghejie on 15/11/26.
 */
public class EsmoResourceAdapter extends RecyclerView.Adapter<EsmoResourceAdapter.ViewHolder> implements View.OnClickListener {
    private List<EsmosBean> mEsmoBeans = new ArrayList<>();
    private Context mContext;
    //创建新View，被LayoutManager所调用
    private OnItemClickListener mOnItemClickListener;
    private int mScreenWidth;//屏幕宽度

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public EsmoResourceAdapter(Context context, List<EsmosBean> esmos, int screenWidth) {
        if(esmos!= null && esmos.size()>0) {
            mEsmoBeans.addAll(esmos);
        }

        this.mScreenWidth = screenWidth;
        this.mContext = context;
    }

    public void updateConferenceState(boolean isClear,List<EsmosBean> beans) {
        if(isClear)
            mEsmoBeans.clear();

        mEsmoBeans.addAll(beans);
        notifyDataSetChanged();
    }



    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_esmo_resource, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        EsmosBean bean = mEsmoBeans.get(position);
        viewHolder.itemView.setTag(position);
        viewHolder.title.setText(bean.getConferencesName());
//        viewHolder.remark.setText(bean.getConferencesDays() +" " + bean.getConferencesAddress());
        viewHolder.remark.setText(bean.getResourceInfo());
//        ViewGroup.LayoutParams layoutParams = viewHolder.logo.getLayoutParams();
//        float itemWidth = (mScreenWidth - DensityUtil.dip2px(mContext,24))/2; //宽度 一行两个
//        float itemWidth = mScreenWidth - DensityUtil.dip2px(mContext, 24); //宽度一行一个
//        float scale = ((float)bean.getWidth())/itemWidth;
//        float itemHeight = ((float)bean.getHeight())/scale;
//        layoutParams.width = (int)itemWidth;
//        layoutParams.height = (int)itemHeight;
//        viewHolder.logo.setLayoutParams(layoutParams);
//        Glide.with(mContext).load(bean.getIconUrl()).override(layoutParams.width,layoutParams.height).placeholder(R.drawable.default_load_bg).into(viewHolder.logo);
        PicUtils.loadImageUrl(mContext,bean.getResourceIcon(),viewHolder.logo);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        if(mEsmoBeans!= null && mEsmoBeans.size() >0) {
            return mEsmoBeans.size();
        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,remark;
        public ImageView logo;

        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.tv_title);
            remark = (TextView) view.findViewById(R.id.tv_remark);
            logo = (ImageView) view.findViewById(R.id.iv_logo);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int id);
    }
}
