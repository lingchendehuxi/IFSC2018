package com.android.incongress.cd.conference;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jacky on 2016/8/25.
 */
public class FindbackCCodeSecondActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_mobile)
    EditText mEtMobile;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_find_back_ccode_second);
    }

    @Override
    protected void initViewsAction() {
        initTitleBar(getString(R.string.findback_ccode_title));
    }

    @OnClick(R.id.get_ccode)
    void getCCode() {
        String name = mEtName.getText().toString().trim();
        String mobile = mEtMobile.getText().toString().trim();

        if(StringUtils.isAllNotEmpty(name, mobile)) {
            try {
                name = URLEncoder.encode(name, Constants.ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CHYHttpClientUsage.getInstanse().doFindbackCCode(name,mobile, AppApplication.getSystemLanuageCode(), Constants.getFromWhere(), Constants.getConId() + "",new JsonHttpResponseHandler(Constants.ENCODING_GBK){
                @Override
                public void onStart() {
                    super.onStart();
                    showProgressBar("finding c-code...");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dismissProgressBar();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtils.println("response;===" + response);
                    try {
                        int state = response.getInt("state");
                        if(state == 1) {
                            String msg = response.getString("msg");
                            showOnlyOneDialog(msg);
                        }else {
                            String msg = response.getString("msg");
                            final String mobile = response.getString("msgPhone");

                            showDialog(msg, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!StringUtils.isEmpty(mobile)) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:" + mobile));
                                        startActivity(intent);
                                    }
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //取消
                                }
                            },true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            showOnlyOneDialog(getString(R.string.find_tips_name_and_mobile));
        }
    }
}
