package com.android.incongress.cd.conference.fragments.photo_album;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2017/1/20.
 */

public class PhotoAlbumGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhotoAlbumDetailBean.PhotoWallArrayBean>  mBeans;
    private LayoutInflater mInflater;
    public PhotoAlbumGridAdapter(Context context, List<PhotoAlbumDetailBean.PhotoWallArrayBean> beans){
        this.mContext = context;
        this.mBeans = beans;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(mBeans != null && mBeans.size() > 0)
            return mBeans.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PhotoHolder holder;
        final PhotoAlbumDetailBean.PhotoWallArrayBean photoWallArrayBean = mBeans.get(position);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_album_photo_detail,parent, false);
            holder = new PhotoHolder();
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
            holder.tvLaudCount = (TextView) convertView.findViewById(R.id.tv_praise_count);
            holder.ivLaud = (ImageView) convertView.findViewById(R.id.iv_praise);
            holder.ivLayout = (LinearLayout) convertView.findViewById(R.id.iv_layout);
            convertView.setTag(holder);
        }else {
            holder = (PhotoHolder) convertView.getTag();
        }

        String url = photoWallArrayBean.getImageUrl();
        if(url.contains("https:")) {
            url = url.replaceFirst("s","");
        }
        PicUtils.loadImageUrl(mContext,url,holder.ivPhoto);
        holder.tvLaudCount.setText(photoWallArrayBean.getLaudCount()+"");

        if(photoWallArrayBean.getIsLaud() == 1) {
            holder.ivLaud.setImageResource(R.drawable.photo_praised);
        }else {
            holder.ivLaud.setImageResource(R.drawable.photo_praise);
        }

        holder.ivLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppApplication.userType <1) {
                    LoginActivity.startLoginActivity(mContext, LoginActivity.TYPE_NORMAL, "", "", "", "");
                    return;
                }

                if(photoWallArrayBean.getIsLaud() == 1) {
                    ToastUtils.showToast("已经点赞完成");
                    return;
                }

                CHYHttpClientUsage.getInstanse().doPhotoWallLaud(AppApplication.userId, AppApplication.userType,photoWallArrayBean.getPhotoWallId(), Constants.getConId() + "", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("laud response:", response.toString());
                        try {
                            int state = response.getInt("state");
                            if(state == 1) {
                                holder.tvLaudCount.setText(response.getInt("laudCount") + "");
                                holder.ivLaud.setImageResource(R.drawable.photo_praised);
                                photoWallArrayBean.setIsLaud(1);
                            }else {
                                holder.ivLaud.setImageResource(R.drawable.photo_praise);
                                ToastUtils.showToast("点赞失败，请重新尝试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        return convertView;
    }

    class PhotoHolder {
        ImageView ivPhoto;
        ImageView ivLaud;
        TextView tvLaudCount;
        LinearLayout ivLayout;
    }
}
