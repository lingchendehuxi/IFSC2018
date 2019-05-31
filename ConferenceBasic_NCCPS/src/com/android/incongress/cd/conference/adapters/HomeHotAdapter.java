package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.CoursewareBean;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.transformer.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2017/7/5.
 */
public class HomeHotAdapter extends RecyclerView.Adapter<HomeHotAdapter.ViewHolder> {


    private List<BookCoursePlayBean.VideoArrayBean> mCourseBeanList = new ArrayList<BookCoursePlayBean.VideoArrayBean>();
    private OnItemClickListener mItemClickListener;
    private Context mContext;
    private int width;

    public HomeHotAdapter(List<BookCoursePlayBean.VideoArrayBean> beans, Context context, int width, OnItemClickListener mItemClickListener) {
        this.mCourseBeanList = beans;
        this.mContext = context;
        this.width = width;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public HomeHotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.item_courseware_hot, parent, false);
        return new HomeHotAdapter.ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(HomeHotAdapter.ViewHolder holder, int position) {
        BookCoursePlayBean.VideoArrayBean bean = mCourseBeanList.get(position);
        StringUtils.setCommaTextShow(holder.title, bean.getTitle());
        Glide.with(mContext).load(bean.getVideoImage()).transform(new GlideRoundTransform(mContext, 5)).into(holder.img);
        ViewGroup.LayoutParams lp = holder.img.getLayoutParams();
        lp.height = (int) (width * 0.5625);
        holder.img.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return mCourseBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        TextView title;
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.courseware_title);
            img = view.findViewById(R.id.courseware_img);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onHotItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onHotItemClick(View view, int position);
    }
}