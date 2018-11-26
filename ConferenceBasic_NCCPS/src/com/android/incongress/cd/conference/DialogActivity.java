package com.android.incongress.cd.conference;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import butterknife.BindView;

public class DialogActivity extends BaseActivity {

    private String imgUrl,linkUrl,adName;
    private int time;

    @BindView(R.id.dialog_img)
    ImageView imageView;
    @BindView(R.id.dialog_black)
    ImageView imageBlack;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_dialog);
    }

    @Override
    protected void initViewsAction() {
        imgUrl = getIntent().getStringExtra("imgUrl");
        linkUrl = getIntent().getStringExtra("linkUrl");
        adName = getIntent().getStringExtra("alertAdName");
        time = getIntent().getIntExtra("time",0);
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        ViewGroup.LayoutParams lp=imageView.getLayoutParams();
        lp.width= (int)(wm.getDefaultDisplay().getWidth()*0.6);
        lp.height = (int)(wm.getDefaultDisplay().getWidth()*0.6/3*4);
        imageView.setLayoutParams(lp);
        PicUtils.loadImageUrl(DialogActivity.this,imgUrl,imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linkUrl.contains("?")) {
                    linkUrl = linkUrl + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                }else {
                    linkUrl = linkUrl + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                }
                CollegeActivity.startCitCollegeActivity(DialogActivity.this,adName, linkUrl);
                handler.sendEmptyMessage(1);
            }
        });
        if(time != 0){
            handler.sendEmptyMessageDelayed(1,time*1000);
        }
        imageBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(1);
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };
}
