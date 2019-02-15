package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.SearchRoomBean;
import com.android.incongress.cd.conference.model.Class;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/3/7.
 */
public class SearchRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Class> mClasses;
    private int mCurrentChoosePosition = 0;

    private static final int VIEW_TYPE_ALL = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    public SearchRoomAdapter(Context ctx, List<Class> classes) {
        this.mContext = ctx;
        this.mInflater = LayoutInflater.from(ctx);
        this.mClasses = classes;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ALL) {
            View view = mInflater.inflate(R.layout.item_search_all_room, null);
            view.setOnClickListener(this);
            SearchAllRoomHolder holder = new SearchAllRoomHolder(view);
            return holder;
        }else {
            View view = mInflater.inflate(R.layout.item_search_room, null);
            view.setOnClickListener(this);
            SearchRoomHolder holder = new SearchRoomHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            holder.itemView.setTag(null);
            if(mCurrentChoosePosition == 0) {
                ((SearchAllRoomHolder)holder).tvAllRoom.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                ((SearchAllRoomHolder)holder).tvAllRoom.setBackground(mContext.getResources().getDrawable(R.drawable.bg_segment_selected));
            }else {
                ((SearchAllRoomHolder)holder).tvAllRoom.setTextColor(mContext.getResources().getColor(R.color.search_normal_color));
                ((SearchAllRoomHolder)holder).tvAllRoom.setBackground(mContext.getResources().getDrawable(R.drawable.bg_segment_unselected));
            }
        }else {
            Class bean = mClasses.get(position-1);

            SearchRoomBean roomBean = new SearchRoomBean();
            roomBean.setPosition(position);
            roomBean.setRoomId(bean.getClassesId());

            if(AppApplication.systemLanguage == 1) {
                ((SearchRoomHolder)holder).tvRoom.setText(bean.getClassesCode());
                roomBean.setRoomeName(bean.getClassesCode());
            }else{
                ((SearchRoomHolder) holder).tvRoom.setText(bean.getClassCodeEn());
                roomBean.setRoomeName(bean.getClassCodeEn());
            }
            holder.itemView.setTag(roomBean);
//            ((SearchRoomHolder)holder).tvRoom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 42)));
            ((SearchRoomHolder)holder).tvRoom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((SearchRoomHolder)holder).tvRoom.setTextColor(mContext.getResources().getColor(R.color.search_normal_color));
            ((SearchRoomHolder)holder).tvRoom.setBackground(mContext.getResources().getDrawable(R.drawable.bg_segment_unselected));

            if (mCurrentChoosePosition == position) {
                ((SearchRoomHolder)holder).tvRoom.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                ((SearchRoomHolder)holder).tvRoom.setBackground((mContext.getResources().getDrawable(R.drawable.bg_segment_selected)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mClasses.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            SearchRoomBean bean = (SearchRoomBean) v.getTag();
            if(bean != null) {
                mCurrentChoosePosition = bean.getPosition();
                mOnItemClickListener.doOnItemClick(v, bean);
            }else {
                mCurrentChoosePosition = 0;
                mOnItemClickListener.doOnItemClick(v, bean);
            }
            notifyDataSetChanged();
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void doOnItemClick(View v, SearchRoomBean data);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return VIEW_TYPE_ALL;
        }else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class SearchRoomHolder extends RecyclerView.ViewHolder {
       private TextView tvRoom;
       public SearchRoomHolder(View itemView) {
           super(itemView);
           tvRoom = (TextView) itemView.findViewById(R.id.tv_room);
       }
   }

    class SearchAllRoomHolder extends RecyclerView.ViewHolder {
        private TextView tvAllRoom;

        public SearchAllRoomHolder(View itemView) {
            super(itemView);
            this.tvAllRoom = (TextView) itemView.findViewById(R.id.tv_all_room);
        }
    }

    public void resetSearch() {
        mCurrentChoosePosition = 0;
        notifyDataSetChanged();
    }
}
