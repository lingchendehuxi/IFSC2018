package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.SearchTimeBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/3/7.
 */
public class SearchTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private LayoutInflater mInflater;
    private Context mContext;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_CUSTOM = 2;
    private int mCurrentChoosePosition = 0;

    private int[] mTimes = {R.string.search_morning, R.string.search_midday, R.string.search_afternoon};
    private int[] mDrawables = {R.drawable.search_time_morning, R.drawable.search_time_midday, R.drawable.search_time_afternoon};
    private int[] mDrawablesSelected = {R.drawable.search_time_morning_selected, R.drawable.search_time_midday_selected, R.drawable.search_time_afternoon_selected};
    private int[] mTimesNum = {R.string.search_morning_time, R.string.search_midday_time, R.string.search_afternoon_time};
    private int[] mScreenSize = new int[2];

    private String mCustomTime = "";

    public SearchTimeAdapter(Context ctx, int[] screenSize) {
        this.mInflater = LayoutInflater.from(ctx);
        this.mScreenSize = screenSize;
        this.mContext = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View itemView = mInflater.inflate(R.layout.item_search_fragment, null);
            itemView.setOnClickListener(this);
            SearchViewHolder holder = new SearchViewHolder(itemView);
            return holder;
        } else {
            View itemView = mInflater.inflate(R.layout.item_search_custom_time, null);
            itemView.setOnClickListener(this);
            SearchCustomTimeViewHolder holder = new SearchCustomTimeViewHolder(itemView);
            return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 3   ) {
            return TYPE_CUSTOM;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchTimeBean bean = new SearchTimeBean();
        bean.setPosition(position);
        if (position == 3) {
            ((SearchCustomTimeViewHolder) holder).tvCustomTime.setText(R.string.search_custom_time);
            ((SearchCustomTimeViewHolder) holder).tvCustomTime.setTextColor(mContext.getResources().getColor(R.color.search_normal_color));
            ((SearchCustomTimeViewHolder) holder).tvCustomTime.setBackgroundResource(R.drawable.bg_segment_unselected);
            ((SearchCustomTimeViewHolder) holder).mLlCustomTime.setLayoutParams(new LinearLayout.LayoutParams(mScreenSize[0] / 4, DensityUtil.dip2px(mContext, 100)));
            ((SearchCustomTimeViewHolder) holder).mLlCustomTime.setGravity(Gravity.CENTER);
            bean.setTime("custom");
            ((SearchCustomTimeViewHolder) holder).itemView.setTag(bean);

            if(!StringUtils.isEmpty(mCustomTime)) {
                ((SearchCustomTimeViewHolder) holder).tvCustomTime.setText(mCustomTime);
            }
        } else {
            ((SearchViewHolder) holder).ivTimeLogo.setImageResource(mDrawables[position]);
            ((SearchViewHolder) holder).tvTimeName.setText(mTimes[position]);
            ((SearchViewHolder) holder).tvTimeNum.setText(mTimesNum[position]);
            ((SearchViewHolder) holder).tvTimeNum.setTextColor(mContext.getResources().getColor(R.color.search_normal_color));
            ((SearchViewHolder) holder).tvTimeName.setTextColor(mContext.getResources().getColor(R.color.search_normal_color));
            ((SearchViewHolder) holder).llTimeItem.setLayoutParams(new LinearLayout.LayoutParams(mScreenSize[0] / 4, DensityUtil.dip2px(mContext, 100)));
            bean.setTime(mContext.getString(mTimes[position]));
            ((SearchViewHolder) holder).itemView.setTag(bean);
        }

        if (mCurrentChoosePosition == position) {
            if (position == 3) {
                ((SearchCustomTimeViewHolder) holder).tvCustomTime.setTextColor(mContext.getResources().getColor(R.color.white));
                ((SearchCustomTimeViewHolder) holder).tvCustomTime.setBackgroundResource(R.drawable.bg_segment_selected);
            } else {
                ((SearchViewHolder) holder).ivTimeLogo.setImageResource(mDrawablesSelected[position]);
                ((SearchViewHolder) holder).tvTimeName.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                ((SearchViewHolder) holder).tvTimeNum.setTextColor(mContext.getResources().getColor(R.color.theme_color));
            }
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            SearchTimeBean bean = (SearchTimeBean) v.getTag();
            mCurrentChoosePosition = bean.getPosition();
            mOnItemClickListener.onItemClick(v, bean);
            notifyDataSetChanged();
        }
    }

    /**
     * 早上中午晚上
     */
    class SearchViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llTimeItem;
        TextView tvTimeNum;
        ImageView ivTimeLogo;
        TextView tvTimeName;

        public SearchViewHolder(View itemView) {
            super(itemView);
            tvTimeName = (TextView) itemView.findViewById(R.id.tv_time_name);
            tvTimeNum = (TextView) itemView.findViewById(R.id.tv_time_num);
            ivTimeLogo = (ImageView) itemView.findViewById(R.id.iv_time_logo);
            llTimeItem = (LinearLayout) itemView.findViewById(R.id.ll_time_item);
        }
    }

    /**
     * 自定义的时间区域
     */
    class SearchCustomTimeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomTime;
        private LinearLayout mLlCustomTime;

        public SearchCustomTimeViewHolder(View itemView) {
            super(itemView);
            tvCustomTime = (TextView) itemView.findViewById(R.id.tv_custom_time);
            mLlCustomTime = (LinearLayout) itemView.findViewById(R.id.ll_custom_time);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    //define interface
    public abstract interface OnItemClickListener {
        void onItemClick(View view, SearchTimeBean data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void resetSearch() {
        mCurrentChoosePosition = 0;
        notifyDataSetChanged();
    }

    public void setCustomTime(String customTime) {
        this.mCustomTime = customTime;
        mCurrentChoosePosition = 3;
        notifyDataSetChanged();
    }
}
