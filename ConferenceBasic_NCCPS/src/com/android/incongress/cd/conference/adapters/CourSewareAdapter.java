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
public class CourSewareAdapter extends RecyclerView.Adapter<CourSewareAdapter.ViewHolder> {


    private List<CoursewareBean> mCourseBeanList = new ArrayList<CoursewareBean>();
    CourSewareAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;
    private int width;

    public CourSewareAdapter(List<CoursewareBean> beans, Context context, int width) {
        this.mCourseBeanList = beans;
        this.mContext = context;
        this.width = width;
    }

    @Override
    public CourSewareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.item_courseware, parent, false);
        return new CourSewareAdapter.ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(CourSewareAdapter.ViewHolder holder, int position) {
        CoursewareBean bean = mCourseBeanList.get(position);
        String author = bean.getAuthor();
        StringUtils.setCommaTextShow(holder.title, bean.getTitle());
        if (author.contains("#@#")) {
            if (AppApplication.systemLanguage == 1) {
                author = "作者：" + author.split("#@#")[0];
            } else {
                author = "Author：" + author.split("#@#")[1];
            }
        } else {
            author = "作者：" + bean.getAuthor();
        }
        holder.author.setText(author);
        Glide.with(mContext).load(bean.getFirstPic()).transform(new GlideRoundTransform(mContext, 5)).into(holder.img);
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
        TextView title, author;
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.courseware_title);
            author = (TextView) view.findViewById(R.id.courseware_author);
            img = (ImageView) view.findViewById(R.id.courseware_img);
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

    public void SetOnItemClickListener(final CourSewareAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}