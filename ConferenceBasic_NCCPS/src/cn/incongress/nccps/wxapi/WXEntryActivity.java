package cn.incongress.nccps.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.JSONUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.weixin.callback.WXCallbackActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.client.ClientProtocolException;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler{
    protected static final int RETURN_OPENID_ACCESSTOKEN = 0;// 返回openid，accessToken消息码
    protected static final int RETURN_NICKNAME_UID = 1; // 返回昵称，uid消息码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppApplication.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AppApplication.wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        // LogManager.show(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:"
        // + resp.errStr, 1);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
                    ToastUtils.showShorToast("分享成功");
                    this.finish();
                    break;
                } else {
                    String code = ((SendAuth.Resp) resp).code;
                    // HttpUtil.showToast(getApplicationContext(), "登陆成功");
                    getResult(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
                    ToastUtils.showShorToast("分享取消");
                    this.finish();
                    break;
                } else {
                    ToastUtils.showShorToast("登录取消");
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
                    ToastUtils.showShorToast("分享拒绝");
                    this.finish();
                    break;
                } else {
                    ToastUtils.showShorToast("登录拒绝");
                }
                break;

        }
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code
     *            请求码
     */
    private void getResult(final String code) {
        new Thread() {// 开启工作线程进行网络请求
            public void run() {
                String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + Constants.WX_APPID
                        + "&secret="
                        + Constants.APP_SECRET
                        + "&code="
                        + code
                        + "&grant_type=authorization_code";
                try {
                    JSONObject jsonObject = JSONUtils
                            .initSSLWithHttpClinet(path);// 请求https连接并得到json结果
                    if (null != jsonObject) {
                        String openid = jsonObject.getString("openid")
                                .toString().trim();
                        String access_token = jsonObject
                                .getString("access_token").toString().trim();
                        Log.d("sgqTest", "openid = " + openid);
                        Log.d("sgqTest", "access_token = " + access_token);

                        Message msg = handler.obtainMessage();
                        msg.what = RETURN_OPENID_ACCESSTOKEN;
                        Bundle bundle = new Bundle();
                        bundle.putString("openid", openid);
                        bundle.putString("access_token", access_token);
                        msg.obj = bundle;
                        handler.sendMessage(msg);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            };
        }.start();
    }

    /**
     * 获取用户唯一标识
     *
     * @param openId
     * @param accessToken
     */
    private void getUID(final String openId, final String accessToken) {
        new Thread() {
            @Override
            public void run() {
                String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                        + accessToken + "&openid=" + openId;
                JSONObject jsonObject = null;
                try {
                    jsonObject = JSONUtils.initSSLWithHttpClinet(path);
                    String nickname = jsonObject.getString("nickname");
                    String unionid = jsonObject.getString("unionid");
                    int sex = jsonObject.getInt("sex");
                    String headimgurl = jsonObject.getString("headimgurl");
                    Log.d("sgqTest", "nickname = " + nickname);
                    Log.d("sgqTest", "unionid = " + unionid);

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_NICKNAME_UID;
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname", nickname);
                    bundle.putString("unionid", unionid);
                    bundle.putInt("sex", sex);
                    bundle.putString("headimgurl", headimgurl);
                    msg.obj = bundle;
                    handler.sendMessage(msg);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    private String openId;
    private String nickname;
    private int sex;
    private String headimgurl;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RETURN_OPENID_ACCESSTOKEN:
                    Bundle bundle1 = (Bundle) msg.obj;
                    String accessToken = bundle1.getString("access_token");
                    openId = bundle1.getString("openid");
                    Log.e("TAG", "openId =" + openId);
                    getUID(openId, accessToken);
                    break;

                case RETURN_NICKNAME_UID:
                    Bundle bundle2 = (Bundle) msg.obj;
                    nickname = bundle2.getString("nickname");
                    String uid = bundle2.getString("unionid");
                    Log.e("TAG", bundle2.toString());
                    sex = bundle2.getInt("sex");
                    headimgurl = bundle2.getString("headimgurl");
                    //调用微信登录接口
                    //////
                    /*
                     * textView.setText("uid:" + uid); loginBtn.setText("昵称：" +
                     * nickname);
                     */
                    break;

                default:
                    break;
            }
        };
    };


}
