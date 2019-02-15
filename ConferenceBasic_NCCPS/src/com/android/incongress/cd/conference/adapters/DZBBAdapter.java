package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.fragments.wall_poster.PosterImageFragment;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

public class DZBBAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DZBBBean.ArrayBean> mBeans;
    private Context mContext;

    public DZBBAdapter(Context ctx, List<DZBBBean.ArrayBean> beans) {
        this.mContext = ctx;
        this.mBeans = beans;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = null;
        MViewHolder holder;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.item_dzbb);
            holder = new MViewHolder(convertView);
            holder.posterCode = convertView.findViewById(R.id.posterCode);
            holder.conField = convertView.findViewById(R.id.conField);
            holder.title = convertView.findViewById(R.id.title);
            holder.author = convertView.findViewById(R.id.author);
            holder.jingxuan = convertView.findViewById(R.id.jingxuan);
            convertView.setTag(holder);
        } else {
            holder = (MViewHolder) convertView.getTag();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DZBBBean.ArrayBean bean = mBeans.get(position);

        ((MViewHolder) holder).posterCode.setText(bean.getPosterCode());
        ((MViewHolder) holder).conField.setText("均分：  "+bean.getPoint()+"分");
        ((MViewHolder) holder).title.setText(bean.getPosterTitle());
        PicUtils.loadImageUrl(mContext,bean.getUrl(),((MViewHolder) holder).poster_image_view);
        ((MViewHolder) holder).author.setText(mContext.getString(R.string.first_author)+"  "+ bean.getAuthor());
        ((MViewHolder) holder).jingxuan.setText("浏览：  "+bean.getReadCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AppApplication.isUserLogIn()) {
                    LoginActivity.startLoginActivity(mContext, LoginActivity.TYPE_PROFESSOR, "", "", "", "");
//                        ChooseIdentityActivity.startChooseIdentityActivity(getActivity());
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(mContext, PosterImageFragment.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }


    class MViewHolder extends RecyclerView.ViewHolder {
        TextView posterCode;
        TextView conField;
        TextView title;
        TextView author,jingxuan;
        ImageView poster_image_view;

        public MViewHolder(View view) {
            super(view);
            posterCode = view.findViewById(R.id.posterCode);
            conField = view.findViewById(R.id.conField);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            jingxuan = view.findViewById(R.id.jingxuan);
            poster_image_view = view.findViewById(R.id.poster_image_view);
        }
    }

}
