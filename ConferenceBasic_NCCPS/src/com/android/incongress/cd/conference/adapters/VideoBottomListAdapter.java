package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.CoursewareBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.transformer.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2017/7/5.
 */
public class VideoBottomListAdapter extends BaseAdapter {


    private List<BookCoursePlayBean.VideoArrayBean> mVideoList;
    VideoBottomListAdapter.OnItemClickListener mItemClickListener;
    private Context mContext;

    public VideoBottomListAdapter(List<BookCoursePlayBean.VideoArrayBean> mVideoList, Context context, OnItemClickListener mItemClickListener) {
        this.mVideoList = mVideoList;
        this.mContext = context;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.video_play_item, null);
            holder.iv_preview = view.findViewById(R.id.iv_preview);
            holder.tv_preview = view.findViewById(R.id.tv_preview);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BookCoursePlayBean.VideoArrayBean bean = mVideoList.get(position);
        if (bean.isSelected()) {
            holder.tv_preview.setTextColor(mContext.getResources().getColor(R.color.theme_bg));
        } else {
            holder.tv_preview.setTextColor(mContext.getResources().getColor(R.color.black_login_text));
        }
        PicUtils.loadImageUrl(mContext, bean.getVideoImage(), holder.iv_preview);
        StringUtils.setCommaTextShow(holder.tv_preview, bean.getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(position);
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tv_preview;
        ImageView iv_preview;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void SetOnItemClickListener(final VideoBottomListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}