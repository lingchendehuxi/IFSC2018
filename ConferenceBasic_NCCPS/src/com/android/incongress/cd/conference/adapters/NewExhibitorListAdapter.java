package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.ExhibitorListInfoBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/29.
 */
public class NewExhibitorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ExhibitorListInfoBean.ResultBean> mBeans;
    private LayoutInflater mInflater;

    public NewExhibitorListAdapter(Context mContext, List<ExhibitorListInfoBean.ResultBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == 0) {
            view = mInflater.inflate(R.layout.new_exhibitor_list_item, parent, false);
            holder = new ViewHolder(view);
        } else {
            view = mInflater.inflate(R.layout.new_exhibitor_list_item_empty, parent, false);
            holder = new ViewHolder2(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ExhibitorListInfoBean.ResultBean bean = mBeans.get(position);
        if (holder.getItemViewType() == 0) {
            ((ViewHolder) holder).tv_conner.setText(bean.getLocation());
            StringUtils.setTextShow(((ViewHolder) holder).tv_context, bean.getContent());
            StringUtils.setTextShow(((ViewHolder) holder).tv_title,bean.getTitle());
            if (TextUtils.isEmpty(bean.getLogoImg())) {
                ((ViewHolder) holder).iv_img.setVisibility(View.GONE);
            } else {
                PicUtils.loadImageUrl(mContext, bean.getLogoImg(), ((ViewHolder) holder).iv_img);
            }
        } else {
            if (TextUtils.isEmpty(bean.getTopImg())) {
                ((ViewHolder2) holder).iv_img.setVisibility(View.GONE);
            } else {
                PicUtils.loadImageUrl(mContext, bean.getTopImg(), ((ViewHolder2) holder).iv_img);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mBeans.get(position).getType() == 3){
            return 0;
        }else {
            return 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_conner, tv_title, tv_context;
        ImageView iv_img;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_conner = itemView.findViewById(R.id.tv_conner);
            tv_context = itemView.findViewById(R.id.tv_context);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        ImageView iv_img;

        public ViewHolder2(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

