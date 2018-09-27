package com.android.incongress.cd.conference;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.File;


public class GestureImageViewActivity extends FragmentActivity{
    private String filepath;
    private PhotoView mImageView;
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_imageview);
		mImageView= (PhotoView)findViewById(R.id.photoview);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);

		filepath=getIntent().getStringExtra("filepath");
		Glide.with(this).load(new File(filepath)).listener(new RequestListener<File, GlideDrawable>() {
			@Override
			public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
				return false;
			}

			@Override
			public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
				mProgressBar.setVisibility(View.GONE);
				return false;
			}
		}).into(mImageView);
	}
}
