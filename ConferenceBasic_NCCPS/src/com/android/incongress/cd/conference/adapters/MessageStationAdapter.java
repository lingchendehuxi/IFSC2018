package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.MessageBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/29.
 */
public class MessageStationAdapter extends RecyclerView.Adapter<MessageStationAdapter.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<MessageBean> mBeans;
    private LayoutInflater mInflater;

    private static final int TYPE_NO_URL = 1;
    private static final int TYPE_HAVE_URL = 2;

    public MessageStationAdapter(Context mContext, List<MessageBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        View view;
        viewType = TYPE_HAVE_URL;
        if (viewType == TYPE_HAVE_URL) {
            view = mInflater.inflate(R.layout.item_messagestation_with_url,parent, false);
            view.setOnClickListener(this);
            holder = new ViewHolder(view);
        } else {
            view = mInflater.inflate(R.layout.item_mind_book_item, parent, false);
            view.setOnClickListener(this);
            holder = new ViewHolder(view);
        }
        //AppApplication.applyFont(mContext,view,"fonts/SourceHanSans_Light.otf");
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageBean bean = mBeans.get(position);
        String content = bean.getContent();
        int finalPosition = content.indexOf("】");
        boolean isShow = content.startsWith("【");
        if(isShow){
            String string1 = content.substring(1,finalPosition);
            String string2 = content.substring(finalPosition+1,content.length()-1);
            holder.tv_title.setText(string1);
            holder.tvMessage.setText(string2);
        }else {
            holder.tv_title.setVisibility(View.GONE);
            holder.tvMessage.setText(content);
        }
        holder.tvTime.setText(bean.getCreateTime());
        if(TextUtils.isEmpty(bean.getUrl())){
            holder.iv_left.setImageResource(R.drawable.professor_default);
        }else {
            PicUtils.loadCircleImage(mContext,bean.getUrl(),holder.iv_left);
        }
        if(bean.getType() == 2){
            holder.tv_more.setVisibility(View.VISIBLE);
        }else {
            holder.tv_more.setVisibility(View.GONE);
        }
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemViewType(int position) {
        MessageBean bean = mBeans.get(position);
        if (bean.getType() == TYPE_HAVE_URL) {
            return TYPE_HAVE_URL;
        } else {
            return TYPE_NO_URL;
        }
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (MessageBean)v.getTag());
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage,tv_title;
        TextView tvTime,tv_more;
        CircleImageView iv_left;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_left = itemView.findViewById(R.id.iv_left);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            tv_more = itemView.findViewById(R.id.tv_more);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , MessageBean data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
