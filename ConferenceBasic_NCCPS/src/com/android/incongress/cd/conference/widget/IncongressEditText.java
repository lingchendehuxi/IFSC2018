package com.android.incongress.cd.conference.widget;

import com.android.incongress.cd.conference.base.AppApplication;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.EditText;

public class IncongressEditText extends AppCompatEditText {

	public IncongressEditText(Context context) {
		super(context);
		if(AppApplication.mTypeface!=null){
			setTypeface(AppApplication.mTypeface);
		}
	}
	public IncongressEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(AppApplication.mTypeface!=null){
			setTypeface(AppApplication.mTypeface);
		}
	}

}
