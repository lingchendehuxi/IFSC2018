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
import com.android.incongress.cd.conference.beans.MessageBean;
import com.android.incongress.cd.conference.beans.MyOrderCourse;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.slip_delete.SwipeMenuLayout;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/29.
 */
public class MindBookListAdapter extends RecyclerView.Adapter<MindBookListAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<MyOrderCourse.ArrayBean> mBeans;
    private LayoutInflater mInflater;
    private DeleteItemOnClick deleteItemOnClick;

    public MindBookListAdapter(DeleteItemOnClick deleteItemOnClick,Context mContext, List<MyOrderCourse.ArrayBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
        this.mInflater = LayoutInflater.from(mContext);
        this.deleteItemOnClick = deleteItemOnClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        View view = mInflater.inflate(R.layout.item_mind_book_item, parent, false);
        view.setOnClickListener(this);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyOrderCourse.ArrayBean bean = mBeans.get(position);
        StringUtils.setTextShow(holder.tv_title,bean.getTopic());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(bean);
        if(bean.isShow()){
            holder.swipe_menu.smoothExpand();
        }else {
            holder.swipe_menu.smoothClose();
        }
        if(bean.getType() == 1){
            holder.tv_time.setText(bean.getTime());
            holder.ll_sign.setVisibility(View.VISIBLE);
            holder.tv_type.setText(mContext.getString(R.string.question_meeting));
        }else if(bean.getType() == 3){
            holder.tv_time.setText(bean.getMonthDay()+"  "+bean.getTime());
            holder.ll_sign.setVisibility(View.GONE);
        }else if(bean.getType() == 4){
            holder.tv_time.setText(bean.getTime());
            holder.ll_sign.setVisibility(View.GONE);
        }
        holder.iv_delete.setTag(position);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemOnClick.deleteItem((int)(view.getTag()));
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (MessageBean) v.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_sign;
        TextView tv_time, tv_title,tv_type;
        SwipeMenuLayout swipe_menu;
        ImageView iv_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            swipe_menu = itemView.findViewById(R.id.swipe_menu);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            ll_sign = itemView.findViewById(R.id.ll_sign);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            tv_type = itemView.findViewById(R.id.tv_type);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, MessageBean data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public interface DeleteItemOnClick{
        void deleteItem(int position);
    }
}

