package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.ResourceGuideBeans;
import com.android.incongress.cd.conference.beans.ResourceListArrayBeans;
import com.android.incongress.cd.conference.beans.ResourceSeacrhZNBeans;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 2017/7/20.
 */

public class ResourceListAdapter extends RecyclerView.Adapter<ResourceListAdapter.ViewHolder> {


    private List<ResourceListArrayBeans.JsonArrayBean> mTitleBeanList = new ArrayList<>();
    private List<ResourceGuideBeans.DiaoChaListBean> mDCBeanList = new ArrayList<>();
    private List<ResourceGuideBeans.ZhiNanListBean> mZNBeanList = new ArrayList<>();
    private List<ResourceSeacrhZNBeans.JsonArrayBean> mSearchZNBeanList = new ArrayList<>();
    ResourceListAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;
    private int type,width;
    private String search;
    private boolean other = false;

    public ResourceListAdapter(List<ResourceListArrayBeans.JsonArrayBean> beans, Context context, String search, int type, int width, boolean other) {
        this.mTitleBeanList = beans;
        this.mContext = context;
        this.width = width;
        this.type = type;
        this.other = other;
        if(!other){
            this.search = search;
        }
    }
    public ResourceListAdapter(List<ResourceGuideBeans.DiaoChaListBean> beans, Context context, int width, int type) {
        this.mDCBeanList = beans;
        this.mContext = context;
        this.width = width;
        this.type = type;
    }
    public ResourceListAdapter(List<ResourceGuideBeans.ZhiNanListBean> beans, Context context, int type) {
        this.mZNBeanList = beans;
        this.mContext = context;
        this.type = type;
    }

    public ResourceListAdapter(List<ResourceSeacrhZNBeans.JsonArrayBean> beans, Context context, int type, boolean other) {
        this.mSearchZNBeanList = beans;
        this.mContext = context;
        this.type = type;
        this.other = other;
    }
    @Override
    public ResourceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView;
        if(type == 1){
            sView = mInflater.inflate(R.layout.item_resourcelist, parent, false);
        }else if(type == 2){
            sView = mInflater.inflate(R.layout.item_resource_diaocha, parent, false);
        }else {
            sView = mInflater.inflate(R.layout.item_resource_zhinan, parent, false);
        }
        return new ResourceListAdapter.ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ResourceListAdapter.ViewHolder holder , int position) {
        if(type == 1){
            ResourceListArrayBeans.JsonArrayBean bean = mTitleBeanList.get(position);
            holder.auther.setText("阅读　"+bean.getReadCount());
            holder.name.setText(bean.getAuthor());
            if(other){
                holder.title.setText(bean.getTitle());
            }else{
                holder.title.setText(setKeyWordColor(bean.getTitle(),search));
            }
            Glide.with(mContext).load(bean.getFirstPic()).into(holder.img);
            ViewGroup.LayoutParams lp=holder.img.getLayoutParams();
            lp.height= (int)(width*0.45);
            holder.img.setLayoutParams(lp);
        }else if(type == 2){
            ResourceGuideBeans.DiaoChaListBean bean = mDCBeanList.get(position);
            Glide.with(mContext).load(bean.getImgUrl()).into(holder.img);
            ViewGroup.LayoutParams lp=holder.img.getLayoutParams();
            lp.height= (int)(width*0.34);
            holder.img.setLayoutParams(lp);
        }else {
            if(!other){
                ResourceGuideBeans.ZhiNanListBean bean = mZNBeanList.get(position);
                holder.time.setText(bean.getQici());
                holder.name.setText(bean.getCnSubject());
                holder.title.setText(bean.getTitle());
                holder.enname.setText(bean.getEnSubject());
                holder.entitle.setText(bean.getEnTitle());
                holder.typefield.setText(bean.getTypeField());
                holder.title.setTextColor(Color.parseColor(bean.getColor()));
                holder.typefield.setTextColor(Color.parseColor(bean.getColor()));
                holder.typebg.setBackgroundColor(Color.parseColor(bean.getColor()));
            }else{
                ResourceSeacrhZNBeans.JsonArrayBean bean = mSearchZNBeanList.get(position);
                holder.time.setText(bean.getQici());
                holder.name.setText(bean.getCnSubject());
                holder.title.setText(bean.getTitle());
                holder.enname.setText(bean.getEnSubject());
                holder.entitle.setText(bean.getEnTitle());
                holder.typefield.setText(bean.getTypeField());
                holder.title.setTextColor(Color.parseColor(bean.getColor()));
                holder.typefield.setTextColor(Color.parseColor(bean.getColor()));
                holder.typebg.setBackgroundColor(Color.parseColor(bean.getColor()));
            }
        }


    }
    /**
     * 设置搜索关键字高亮
     * @param content 原文本内容
     * @param keyword 关键字
     */
    private SpannableString setKeyWordColor(String content, String keyword){
        SpannableString s = new SpannableString(content);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()){
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red)),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }
    @Override
    public int getItemCount() {
        if(type == 1){
            return mTitleBeanList.size();
        }else if(type == 2){
            return mDCBeanList.size();
        }else{
            if(!other){
                return mZNBeanList.size();
            }else{
                return mSearchZNBeanList.size();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout typebg;
        TextView title,name,auther;
        TextView entitle,enname,typefield,time;
        ImageView img;
        public ViewHolder(View view) {
            super(view);
            if(type == 1){
                title = (TextView) view.findViewById(R.id.resource_listitem_title);
                name = (TextView) view.findViewById(R.id.resource_listitem_zz);
                auther = (TextView) view.findViewById(R.id.resource_listitem_auther);
                img = (ImageView) view.findViewById(R.id.resource_listitem_img);
            }else if(type == 2){
                img = (ImageView) view.findViewById(R.id.resource_dc);
            }else{
                typebg = (RelativeLayout) view.findViewById(R.id.resource_zntb);
                title = (TextView) view.findViewById(R.id.resource_title);
                name = (TextView) view.findViewById(R.id.resource_cnsubject);
                time = (TextView) view.findViewById(R.id.resource_qici);
                entitle = (TextView) view.findViewById(R.id.resource_entitle);
                enname = (TextView) view.findViewById(R.id.resource_ensubject);
                typefield = (TextView) view.findViewById(R.id.resource_type);
            }
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

    public void SetOnItemClickListener(final ResourceListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
