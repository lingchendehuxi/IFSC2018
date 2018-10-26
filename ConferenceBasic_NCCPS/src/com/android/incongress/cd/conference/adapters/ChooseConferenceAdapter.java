package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.EsmosBean;
import com.android.incongress.cd.conference.model.CompasBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghejie on 15/11/26.
 */
public class ChooseConferenceAdapter extends RecyclerView.Adapter<ChooseConferenceAdapter.ViewHolder> implements View.OnClickListener {
    private List<EsmosBean> mEsmoBeans = new ArrayList<>();
    private Context mContext;
    //创建新View，被LayoutManager所调用
    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public ChooseConferenceAdapter(Context context, List<EsmosBean> esmos) {
        if(esmos!= null && esmos.size()>0) {
            mEsmoBeans.addAll(esmos);
        }

        this.mContext = context;
    }

    public void updateConferenceState(boolean isClear,List<EsmosBean> beans) {
        if(isClear)
            mEsmoBeans.clear();

        mEsmoBeans.addAll(beans);
        notifyDataSetChanged();
    }



    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_choose_conference, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        EsmosBean bean = mEsmoBeans.get(position);
        viewHolder.itemView.setTag(position);
        viewHolder.appName.setText(bean.getConferencesName());
        viewHolder.appTimeAndLocation.setText(bean.getConferencesDays() +" " + bean.getConferencesAddress());
        PicUtils.loadImageUrl(mContext,bean.getIconUrl(),viewHolder.appIcon);

        if(bean.getType() == 2 || bean.getIsExist() == 1) {
            viewHolder.ivDownload.setVisibility(View.GONE);
        }else {
            viewHolder.ivDownload.setVisibility(View.VISIBLE);
        }
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        if(mEsmoBeans!= null && mEsmoBeans.size() >0) {
            return mEsmoBeans.size();
        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appName,appTimeAndLocation;
        public ImageView appIcon;
        public ImageView ivDownload;

        public ViewHolder(View view){
            super(view);
            appName = (TextView) view.findViewById(R.id.tv_conference_name);
            appTimeAndLocation = (TextView) view.findViewById(R.id.tv_conference_time_and_location);
            appIcon = (ImageView) view.findViewById(R.id.iv_conference);
            ivDownload = (ImageView) view.findViewById(R.id.iv_download);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view,int id);
    }
}
