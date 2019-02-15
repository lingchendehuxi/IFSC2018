package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.BookDetailBean;
import com.android.incongress.cd.conference.beans.CollegeVideoBean;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/18.
 * 关注的会议列表[meeting]
 */
public class CollegeCourseBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BookDetailBean.MeetingArrayBean> mBookList;
    private List<BookCoursePlayBean.VideoArrayBean> mVideoList;
    private Context mContext;
    private int mType;
    private OnItemClick onItemClick;

    public CollegeCourseBookAdapter(List<BookDetailBean.MeetingArrayBean> mBookList, List<BookCoursePlayBean.VideoArrayBean> mVideoList, Context mContext, int Type, OnItemClick onItemClick) {
        this.mBookList = mBookList;
        this.mVideoList = mVideoList;
        this.mContext = mContext;
        this.mType = Type;
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemCount() {
        if (mVideoList.size() == 0) {
            return mBookList.size();
        } else {
            return mVideoList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mType == 100) {
            final BookDetailBean.MeetingArrayBean bean = mBookList.get(position);
            String[] strings = bean.getTopic().split("#@#");
            if (AppApplication.systemLanguage == 1) {
                ((MBookViewHolder) holder).tv_title.setText(strings[0]);
            } else {
                ((MBookViewHolder) holder).tv_title.setText(strings[1]);
            }
            if("0".equals(bean.getYuyue())){
                ((MBookViewHolder) holder).tv_book.setVisibility(View.VISIBLE);
                ((MBookViewHolder) holder).tv_booked.setVisibility(View.GONE);
            }else {
                ((MBookViewHolder) holder).tv_booked.setVisibility(View.VISIBLE);
                ((MBookViewHolder) holder).tv_book.setVisibility(View.GONE);
            }
            ((MBookViewHolder) holder).ll_play.setVisibility(View.GONE);
            ((MBookViewHolder) holder).tv_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClicking(bean.getTopic(), mType, position);
                }
            });

            ((MBookViewHolder) holder).tv_time.setText(bean.getTimeStart());
        } else {
            final BookCoursePlayBean.VideoArrayBean bean = mVideoList.get(position);
            StringUtils.setCommaTextShow(((MBookViewHolder) holder).tv_title,bean.getTitle());
            ((MBookViewHolder) holder).tv_book.setVisibility(View.GONE);
            ((MBookViewHolder) holder).ll_play.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClicking(bean.getTitle(), mType, position);
                }
            });

            ((MBookViewHolder) holder).tv_time.setText(bean.getCreateTime());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_course_book, null);
        MBookViewHolder myViewHolder = new MBookViewHolder(view);
        return myViewHolder;
    }

    class MBookViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_time, tv_book,tv_booked;
        LinearLayout ll_play;

        public MBookViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_time = view.findViewById(R.id.tv_time);
            tv_book = view.findViewById(R.id.tv_book);
            tv_booked = view.findViewById(R.id.tv_booked);
            ll_play = view.findViewById(R.id.ll_play);
        }
    }

    public interface OnItemClick {
        void onItemClicking(String topic, int type, int position);
    }
}
