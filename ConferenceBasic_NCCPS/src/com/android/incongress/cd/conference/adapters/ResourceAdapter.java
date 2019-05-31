package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.ResourceBeans;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/7/5.
 */
public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {


    private List<BookCoursePlayBean.VideoArrayBean> mNewsBeanList = new ArrayList<>();
    private List<BookCoursePlayBean.VideoArrayBean> mHotBeanList = new ArrayList<>();
    private List<ResourceBeans.ResourcesBottomArrayBean> mEsmoBeanList = new ArrayList<>();
    private List<ResourceBeans.KjArrayBean> mKJBeanList = new ArrayList<>();
    ResourceAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;
    private int type, width;
    private int that;


    public ResourceAdapter(List<BookCoursePlayBean.VideoArrayBean> beans, Context context, int type, int width, int that, String my) {
        this.mNewsBeanList = beans;
        this.mContext = context;
        this.width = width;
        this.type = type;
        this.that = that;
    }

    public ResourceAdapter(List<BookCoursePlayBean.VideoArrayBean> beans, Context context, int type, int width, int that) {
        this.mHotBeanList = beans;
        this.mContext = context;
        this.type = type;
        this.width = width;
        this.that = that;
    }

    public ResourceAdapter(List<ResourceBeans.ResourcesBottomArrayBean> beans, Context context, int type) {
        this.mEsmoBeanList = beans;
        this.mContext = context;
        this.type = type;
    }

    public ResourceAdapter(List<ResourceBeans.KjArrayBean> beans, Context context, int type, int width, int that, boolean my) {
        this.mKJBeanList = beans;
        this.mContext = context;
        this.width = width;
        this.type = type;
        this.that = that;
    }

    @Override
    public ResourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView;
        if (type == 2) {
            sView = mInflater.inflate(R.layout.item_rescource, parent, false);
        } else {
            sView = mInflater.inflate(R.layout.item_esmo_resource, parent, false);
        }
        return new ResourceAdapter.ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ResourceAdapter.ViewHolder holder, int position) {
        if (type == 2) {
            if (that == 1) {
                BookCoursePlayBean.VideoArrayBean bean = mNewsBeanList.get(position);
                StringUtils.setCommaTextShow(holder.title,bean.getTitle());
                PicUtils.loadImageUrl(mContext,bean.getVideoImage(),holder.img);
            } else if (that == 2) {
                BookCoursePlayBean.VideoArrayBean bean = mHotBeanList.get(position);
                StringUtils.setCommaTextShow(holder.title,bean.getTitle());
                PicUtils.loadImageUrl(mContext,bean.getVideoImage(),holder.img);
            } else if (that == 3) {
                ResourceBeans.KjArrayBean bean = mKJBeanList.get(position);
                StringUtils.setCommaTextShow(holder.title,bean.getTitle());
                PicUtils.loadImageUrl(mContext,bean.getFirstPic(),holder.img);
            }
            ViewGroup.LayoutParams lp = holder.img.getLayoutParams();
            lp.height = (int) (width / 1.77);
            holder.img.setLayoutParams(lp);
        } else {
            ResourceBeans.ResourcesBottomArrayBean bean = mEsmoBeanList.get(position);
            holder.remark.setText(bean.getResourceInfo());
            StringUtils.setCommaTextShow(holder.title,bean.getResourceName());
            PicUtils.loadImageUrl(mContext,bean.getImgUrl(),holder.img);
        }
    }

    @Override
    public int getItemCount() {
        if (type == 2) {
            if (that == 1) {
                return mNewsBeanList.size();
            } else if (that == 2) {
                return mHotBeanList.size();
            } else {
                return mKJBeanList.size();
            }
        } else {
            return mEsmoBeanList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, remark;
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            if (type == 2) {
                title = view.findViewById(R.id.resource_text);
                img = view.findViewById(R.id.resource_img);
            } else {
                title = view.findViewById(R.id.tv_title);
                remark = view.findViewById(R.id.tv_remark);
                img = view.findViewById(R.id.iv_logo);
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

    public void SetOnItemClickListener(final ResourceAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}