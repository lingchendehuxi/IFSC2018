package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.beans.ResourceBeans;
import com.android.incongress.cd.conference.model.Ad;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13008 on 2019/4/10.
 */

public class HomeNormalAdapter extends StaticPagerAdapter {
    private List<Drawable> mCourseBeanList = new ArrayList<>();
    private List<Ad> listString = new ArrayList<>();
    private Context mContext;
    private String totalShow;

    public HomeNormalAdapter(List<Drawable> beans, List<Ad> listString,Context context,String totalShow) {
        this.mCourseBeanList = beans;
        this.mContext = context;
        this.listString = listString;
        this.totalShow = totalShow;
    }


    @Override
    public View getView(ViewGroup container, int position) {
        if(mCourseBeanList == null){
            final Ad adBean= listString.get(position);
            ImageView view = new ImageView(container.getContext());
            PicUtils.loadHomeImageFile(mContext, new File(AppApplication.instance().getSDPath() + Constants.FILESDIR+totalShow+adBean.getAdImage()), view);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = adBean.getAdLink().trim();
                    if (link != null && !link.equals("")) {
                        CollegeActivity.startCitCollegeActivity(mContext, "", link);
                    }
                }
            });
            return view;
        }else {
            final Drawable drawable= mCourseBeanList.get(position);
            ImageView view = new ImageView(container.getContext());
            view.setImageDrawable(drawable);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return view;
        }
    }

    @Override
    public int getCount() {
        if(mCourseBeanList == null){
            return listString.size();
        }else {
            return mCourseBeanList.size() ;
        }
    }

}
