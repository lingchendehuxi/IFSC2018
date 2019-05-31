package com.android.incongress.cd.conference.widget.zxing.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Initial the camera
 * <p>
 * 默认的二维码扫描Activity
 */
public class QRCodeCaptureActivity extends BaseActivity {

    private ImageView mIvBack;
    private CaptureFragment captureFragment;

    @Override
    protected void setContentView() {
        setContentView(R.layout.camera);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViewsAction() {
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Log.d("sgqTest", "onAnalyzeSuccess: "+result);
            if(!TextUtils.isEmpty(result) && result.lastIndexOf("#") == result.length()-1&&result.lastIndexOf("#")!=-1){
                String string = result.substring(0,result.lastIndexOf("#"));
                if(StringUtils.isNumeric(string)){
                    uploadQRCode(string);
                    captureFragment.onPause();
                }else {
                    captureFragment.onPause();
                    ToastUtils.showToast("二维码不正确，请联系管理员");
                    captureFragment.onResume();
                }
            }else {
                captureFragment.onPause();
                ToastUtils.showToast("二维码不正确，请联系管理员");
                captureFragment.onResume();
            }

        }

        @Override
        public void onAnalyzeFailed() {
            captureFragment.onPause();
            ToastUtils.showToast("扫描失败，请重试");
            captureFragment.onResume();
        }
    };

    //获取二维码信息
    private void uploadQRCode(String userId) {
        CHYHttpClientUsage.getInstanse().doUploadQRCodeInfo(userId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("sgqTest", "onSuccess: "+response.toString());
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeCaptureActivity.this);
                    builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //对确定按钮的处理
                            captureFragment.onResume();
                        }
                    })
                            .setCancelable(false)
                            .setMessage(response.getString("remark")).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.ACTIVITY_SCANE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.ACTIVITY_SCANE);
    }
}