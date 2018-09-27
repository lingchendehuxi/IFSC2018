package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/1/13.
 */
public class ScenicXiuGridAdapter extends BaseAdapter {
    private String[] mPics;
    private Context mContext;

    public ScenicXiuGridAdapter(String[] pics, Context mContext) {
        this.mPics = pics;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mPics.length;
    }

    @Override
    public Object getItem(int position) {
        return mPics[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_scenic_xiu, null, false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.scenic_pic);

        String url = mPics[position];
        if(url.contains("https:"))
            url = url.replaceFirst("s","");
        Glide.with(mContext).load(url).placeholder(R.drawable.default_load_bg).into(photoView);
        photoView.disenable();
        return view;
    }
}
