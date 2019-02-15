package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommentArrayBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.transformer.CircleTransform;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


/**
 * Created by Jacky on 2016/3/30.
 */
public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentArrayBean> mCommentListBean;
    private LayoutInflater mLayoutInflater;

    private OnItemClickListener mOnItemClickListner;

    public OnItemClickListener getmOnItemClickListner() {
        return mOnItemClickListner;
    }

    public void setmOnItemClickListner(OnItemClickListener mOnItemClickListner) {
        this.mOnItemClickListner = mOnItemClickListner;
    }

    public CommentAdapter(Context context, List<CommentArrayBean> beans) {
        this.mCommentListBean = beans;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public Object getItem(int i) {
        return mCommentListBean.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        if (mCommentListBean != null && mCommentListBean.size() > 0) {
            return mCommentListBean.size();
        } else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (mCommentListBean == null || mCommentListBean.size() == 0) {
            view = mLayoutInflater.inflate(R.layout.item_empty_view, viewGroup, false);
            TextView tvEmpty = view.findViewById(R.id.tv_empty_tips);
            tvEmpty.setText("快来发表评论，抢占沙发！");
        } else {
            CommentViewHolder holder;
            if (view == null) {
                view = mLayoutInflater.inflate(R.layout.item_vvtalk_comment, viewGroup, false);
                holder = new CommentViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (CommentViewHolder) view.getTag();
            }
            final CommentArrayBean data = mCommentListBean.get(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListner != null) {
                        mOnItemClickListner.onItemClick(view, data);
                    }
                }
            });
            try {
                if (!TextUtils.isEmpty(data.getUserImg())) {
                    PicUtils.loadCircleImage(mContext, replaceHttps(data.getUserImg()), holder.civCommentIcon);
                } else {
                    holder.civCommentIcon.setImageResource(R.drawable.professor_default);
                }
                String time = URLDecoder.decode(data.getTimeShow(), Constants.ENCODING_UTF8);
                holder.tvCommentTime.setText(URLDecoder.decode(time, Constants.ENCODING_UTF8));
                String name = URLDecoder.decode(data.getUserName(), Constants.ENCODING_UTF8);
                holder.tvCommentName.setText(URLDecoder.decode(name, Constants.ENCODING_UTF8));
                String content = URLDecoder.decode(data.getContent(), Constants.ENCODING_UTF8);
                content = content.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                content = URLDecoder.decode(content, Constants.ENCODING_UTF8);
                String parentName = URLDecoder.decode(data.getParentName(), Constants.ENCODING_UTF8);
                parentName = URLDecoder.decode(parentName, Constants.ENCODING_UTF8);
                if (!TextUtils.isEmpty(data.getParentName())) {
                    holder.tvCommentContent.setText("@" + parentName + "    " + content);
                } else {
                    holder.tvCommentContent.setText(content);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                holder.tvCommentContent.setText("数据解析错误");
            }

        }
        return view;
    }

    private String replaceHttps(String url) {
        if (url.contains("https"))
            return url.replace("https", "http");
        else
            return url;
    }

    class CommentViewHolder {
        ImageView civCommentIcon;
        TextView tvCommentName;
        TextView tvCommentTime;
        TextView tvCommentContent;

        public CommentViewHolder(View itemView) {
            this.civCommentIcon = itemView.findViewById(R.id.civ_commentPublisher);
            this.tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
            this.tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
            this.tvCommentName = itemView.findViewById(R.id.tv_comment_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, CommentArrayBean commentListBean);
    }
}
