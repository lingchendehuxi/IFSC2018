package com.android.incongress.cd.conference.fragments.photo_album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.PhotoTypeBean;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2017/1/20.
 */

public class PhotoTypesAdapter extends RecyclerView.Adapter<PhotoTypesAdapter.PhotoViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<PhotoTypeBean.PhotoWallTypeArrayBean> mPhotoWallTypeArrayBeanList;
    private OnPhotoTypeClick mOnPhotoType;

    public PhotoTypesAdapter(Context context, List<PhotoTypeBean.PhotoWallTypeArrayBean> photoWallTypeArrayBeanList, OnPhotoTypeClick onPhotoTypeClick) {
        this.mContext = context;
        this.mPhotoWallTypeArrayBeanList = photoWallTypeArrayBeanList;
        this.mOnPhotoType = onPhotoTypeClick;
    }

    @Override
    public PhotoTypesAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_types,parent, false);
        view.setOnClickListener(this);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoTypesAdapter.PhotoViewHolder holder, int position) {
        PhotoTypeBean.PhotoWallTypeArrayBean photoWallTypeArrayBean = mPhotoWallTypeArrayBeanList.get(position);
        holder.itemView.setTag(photoWallTypeArrayBean);
        if(photoWallTypeArrayBean != null) {
            if(position%2 == 0){
                holder.ivImgLayout.setBackgroundColor(mContext.getResources().getColor(R.color.alpha_color));
                holder.ivPhotoImg.setImageResource(R.drawable.photo_light);
            }else {
                holder.ivImgLayout.setBackgroundColor(mContext.getResources().getColor(R.color.theme_color));
                holder.ivPhotoImg.setImageResource(R.drawable.photo_heavy);
            }
            int id = getDefaultIconId(position);
            holder.ivPhoto.setImageResource(id);
            holder.tvPhotoTitle.setText(photoWallTypeArrayBean.getTypeName());
        }
    }
    private int getDefaultIconId(int iconCode) {
        switch (iconCode) {
            case 0:
                return R.drawable.photo1;
            case 1:
                return R.drawable.photo2;
            case 2:

                return R.drawable.photo3;
            case 3:
                return R.drawable.photo4;
            case 4:
                return R.drawable.photo5;
            case 5:
                return R.drawable.photo6;
            case 6:
                return R.drawable.photo7;
            case 7:
                return R.drawable.photo8;
            case 8:
                return R.drawable.photo9;
            case 9:
                return R.drawable.photo10;
        }
        return 0;
    }
    @Override
    public int getItemCount() {
        if(mPhotoWallTypeArrayBeanList != null && mPhotoWallTypeArrayBeanList.size() > 0)
            return mPhotoWallTypeArrayBeanList.size();
        else
            return 0;
    }

    @Override
    public void onClick(View v) {
        if(mOnPhotoType != null) {
            mOnPhotoType.OnPhotoTypeClickListen((PhotoTypeBean.PhotoWallTypeArrayBean) v.getTag());
        }
    }

    public interface OnPhotoTypeClick{
        void OnPhotoTypeClickListen(PhotoTypeBean.PhotoWallTypeArrayBean photoWallTypeArrayBean);
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ivImgLayout;
        ImageView ivPhotoImg,ivPhoto;
        TextView tvPhotoTitle;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivImgLayout = (LinearLayout) itemView.findViewById(R.id.photo_imglayout);
            ivPhotoImg = (ImageView) itemView.findViewById(R.id.photo_img);
            ivPhoto = (ImageView) itemView.findViewById(R.id.photo_logo);
            tvPhotoTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
