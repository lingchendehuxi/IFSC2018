package com.android.incongress.cd.conference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by liuheng on 2015/8/19.
 */
public class ScenicXiuPicsViewpagerActivity extends Activity {
    private static final String BUNDLE_PICS = "bundle_pics";
    private static final String BUNDLE_POSITION = "bundle_position";

    private ViewPager mPager;
    private String[] mPics;
    private int mPosition;
    private TextView mTvPageInfo;
    private ImageViewPagerAdapter mAdapter;

    public static final void startViewPagerActivity(Context ctx, String[] pics, int position) {
        Intent intent = new Intent();
        intent.setClass(ctx, ScenicXiuPicsViewpagerActivity.class);
        intent.putExtra(BUNDLE_PICS, pics);
        intent.putExtra(BUNDLE_POSITION, position);
        ctx.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPics = getIntent().getStringArrayExtra(BUNDLE_PICS);
        mPosition = getIntent().getIntExtra(BUNDLE_POSITION, 0);

        setContentView(R.layout.activity_schedule_viewpager);

        mPager = (ViewPager) findViewById(R.id.pager);
        mTvPageInfo = (TextView) findViewById(R.id.tv_page_info);


        mAdapter = new ImageViewPagerAdapter();
        mPager.setAdapter(mAdapter);

        mPager.setCurrentItem(mPosition);
        mTvPageInfo.setText((mPosition+1)+"/" +mPics.length);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTvPageInfo.setText((position+1)+"/" +mPics.length);
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class ImageViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPics.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ScenicXiuPicsViewpagerActivity.this).inflate(R.layout.item_view_pager_image, null);

            PhotoView photoView = (PhotoView) view.findViewById(R.id.photoview);
            photoView.enable();

            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final ProgressBar pb = (ProgressBar) view.findViewById(R.id.progress);
            String url = mPics[position];
            if(url.contains("https:")) {
                url = url.replaceFirst("s","");
            }
            Glide.with(ScenicXiuPicsViewpagerActivity.this).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    pb.setVisibility(View.GONE);
                    return false;
                }
            }).into(photoView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
