package com.android.incongress.cd.conference.fragments.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.cache.CacheClearUtils;
import com.android.incongress.cd.conference.utils.cache.NotificationsUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.button.ToggleButton;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class SettingFragment extends BaseFragment implements View.OnClickListener {
    RelativeLayout mLogout;
    RelativeLayout mClear;
    TextView memory_of;
    ToggleButton tog_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, null);
        mLogout = view.findViewById(R.id.rl_login_out);
        mClear = view.findViewById(R.id.rl_clear);
        memory_of = view.findViewById(R.id.memory_of);
        tog_button = view.findViewById(R.id.tog_button);
        mLogout.setOnClickListener(this);
        mClear.setOnClickListener(this);
        try {
            memory_of.setText(CacheClearUtils.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (NotificationsUtils.isNotificationEnabled(getActivity())) {
            tog_button.setToggleOn();
        } else {
            tog_button.setToggleOff();
        }

        tog_button.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", AppApplication.getContext().getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);

                    localIntent.setClassName("com.android.settings",
                            "com.android.settings.InstalledAppDetails");

                    localIntent.putExtra("com.android.settings.ApplicationPkgName",
                            AppApplication.getContext().getPackageName());
                }
                startActivity(localIntent);
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_login_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_tips).setMessage(R.string.login_out_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginOut();
                    }
                }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).show();
                break;
            case R.id.rl_clear:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle(R.string.dialog_tips).setMessage(R.string.clear_cache_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CacheClearUtils.clearAllCache(getActivity());
                        ToastUtils.showToast(getString(R.string.clear_success));
                        memory_of.setText("");
                    }
                }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).show();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        umengDeleteOauth(getActivity(),SHARE_MEDIA.WEIXIN);
        ParseUser.clearUserInfo(getActivity());
    }
    /**
     * 友盟取消授权（登入）
     */
    public static void umengDeleteOauth(Activity activity, SHARE_MEDIA share_media_type) {
        UMShareAPI.get(AppApplication.getContext()).deleteOauth(activity, share_media_type, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                //开始授权
                Log.e("onStart", "onStart: ");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //取消授权成功 i=1
                Log.e("onComplete", "onComplete: ");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                //授权出错
                Log.e("onError", "onError: ");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                //取消授权
                Log.e("onCancel", "onCancel: ");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
