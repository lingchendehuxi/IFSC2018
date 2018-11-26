package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.BitMapOption;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

public class MeetingGuideRoomMapFragment extends BaseFragment {
    private String filepath;
    private String imgpath = "";
    private PhotoView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gestureimageview, null);
        mImageView = (PhotoView) view.findViewById(R.id.photoview);
        mImageView.enable();
        if("".equals(imgpath)){
            File file = new File(filepath);
            BitMapOption mBitMapOption = new BitMapOption();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filepath, options);
            Bitmap mBitmap = mBitMapOption.getBitmapFromFile(file, options.outWidth, options.outHeight);
            mImageView.setImageBitmap(mBitmap);
        }else{
            PicUtils.loadImageUrl(getContext(),imgpath,mImageView);
        }
        return view;
    }

    public void setFilePath(String filepath) {
        this.filepath = filepath;
    }
    public void setImgPath(String imgPath) {
        this.imgpath = imgPath;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MEETINGMAPINFO);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MEETINGMAPINFO);
    }
}
