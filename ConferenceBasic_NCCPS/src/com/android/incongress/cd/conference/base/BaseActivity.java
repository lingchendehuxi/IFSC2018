package com.android.incongress.cd.conference.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.model.Ad;
import com.android.incongress.cd.conference.utils.ActivityUtils;
import com.android.incongress.cd.conference.utils.LanguageUtil;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends FragmentActivity {
    /** 基类sharePreference **/

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        LanguageUtil.setLanguage(this,AppApplication.systemLanguage);
        setContentView();
        ButterKnife.bind(this);
        initViewsAction();

    }
    /*@Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = res.getConfiguration();
        config.fontScale = 1.25f; //1 设置正常字体大小的倍数
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }*/
    protected abstract void setContentView();

    protected abstract void initViewsAction();

    /**
     * 使用String方式
     * 显示一个信息对话框，包含是否可取消，默认点击外部消除
     */
    protected void showDialog(String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, positiveListener)
                .setNegativeButton(R.string.negative_button, negativeListener).setCancelable(cancelable)
                .setMessage(message).show();
    }

    /**
     * 使用R.String方式
     * 显示一个信息对话框，包含是否可取消，默认点击外部消除
     */
    protected void showDialog(int messageId, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener,boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, positiveListener)
                .setNegativeButton(R.string.negative_button, negativeListener).setCancelable(cancelable)
                .setMessage(messageId).show();
    }

    /**
     * 显示一个信息对话框,只有确定按钮
     */
    protected void showOnlyOneDialog(String messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, null)
                .setMessage(messageId).show();
    }


    //页面销毁时，移除所有的activity
    @Override
	protected void onDestroy() {
		super.onDestroy();
        ActivityUtils.removeActivity(this);
	}

    protected ProgressDialog mProgressDialog;
    //默认进度条显示进度条，不可消除
    protected void showProgressBar(String msg) {
        mProgressDialog = ProgressDialog.show(BaseActivity.this, null,msg);
    }
    //默认进度条消失
    protected void dismissProgressBar() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * TitleBar相关的东西
     */
    TextView mTvTitle;
    ImageView mIvBack;
    /**
     * 初始化状态栏  按返回键直接finish
     */
    protected void initTitleBar(String title) {
        mIvBack = (ImageView) findViewById(R.id.title_back);
        mTvTitle = (TextView) findViewById(R.id.title_text);

        mTvTitle.setText(title);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //隐藏输入法
    public void hideShurufa() {
        View v = getCurrentFocus();
        if (v != null) {
            if (((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS)) {
                return;
            }
        }
    }
    //弹出键盘或取消键盘
    public boolean toggleShurufa() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        return manager.isActive();
    }
}
