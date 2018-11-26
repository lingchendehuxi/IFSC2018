package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.AllRooms;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Admin on 2018/5/17.
 */

public class AllRoomsAdapter extends RecyclerView.Adapter<AllRoomsAdapter.ViewHolder>{
    private List<AllRooms> mAllRooms;
    OnItemClickListener mItemClickListener;
    private Context mContext;



        public AllRoomsAdapter(Context context, List<AllRooms> beans) {
            this.mAllRooms = beans;
            this.mContext = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
            final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            final View sView;
                sView = mInflater.inflate(R.layout.item_room, parent, false);

            return new ViewHolder(sView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder , int position) {
            AllRooms bean = mAllRooms.get(position);
            if(bean.isSelect()){
                holder.img.setImageResource(R.drawable.room_selected);
                holder.name.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_room_tag_selected));
                holder.name.setTextColor(mContext.getResources().getColor(R.color.alpha_theme_color));
            }else{
                holder.img.setImageResource(R.drawable.room_unselected);
                holder.name.setTextColor(Color.GRAY);
                holder.name.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_room_tag_normal));
            }
            holder.name.setText(bean.getClassesCode());
        }

        @Override
        public int getItemCount() {
            return mAllRooms.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView name;
            ImageView img;
            public ViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.rooms_name);
                img = (ImageView) view.findViewById(R.id.is_select);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, getPosition());
                }
            }

        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}