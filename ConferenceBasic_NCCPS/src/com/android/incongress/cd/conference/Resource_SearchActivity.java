package com.android.incongress.cd.conference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.ResourceListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DownloadPDFBeans;
import com.android.incongress.cd.conference.beans.ResourceListArrayBeans;
import com.android.incongress.cd.conference.beans.ResourceSeacrhZNBeans;
import com.android.incongress.cd.conference.utils.ListDataSave;
import com.android.incongress.cd.conference.widget.ClearEditText;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import cn.finalteam.toolsfinal.io.FileUtils;
import cz.msebera.android.httpclient.Header;

public class Resource_SearchActivity extends AppCompatActivity {

    private LinearLayout mLinaearLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mPgb;
    private ClearEditText mEdit;
    private Button mGoSearch;
    private TextView mResultNull;
    private TextView mBack;
    private TextView mSearchKeJian;
    private TextView mSearchZhiNan;
    private TextView mSearchZhuanJia;
    private int TEXTTYPE = 1;
    private String text;
    private int isVip;
    private ResourceListArrayBeans mKJBeans;
    private ResourceSeacrhZNBeans mZNBeans;
    private ResourceListAdapter mKJAdapter,mZNAdapter;
    private ListDataSave dataSave;
    private List<DownloadPDFBeans> mDownloadList;
    protected ProgressDialog mProgressDialog;
    public static void searchActivity(Context context,int isvip) {
        Intent intent = new Intent();
        intent.setClass(context, Resource_SearchActivity.class);
        intent.putExtra("isVip", isvip);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_search);
        mLinaearLayout = (LinearLayout) findViewById(R.id.choiceLayout);
        mRecyclerView = (RecyclerView)findViewById(R.id.search_result);
        mEdit = (ClearEditText) findViewById(R.id.search_edit);
        mSearchKeJian = (TextView) findViewById(R.id.search_title_text);
        mSearchZhiNan = (TextView) findViewById(R.id.search_author_text);
        mSearchZhuanJia = (TextView) findViewById(R.id.search_expert_text);
        mResultNull = (TextView) findViewById(R.id.search_result_null);
        mBack = (TextView) findViewById(R.id.search_back);
        mGoSearch = (Button) findViewById(R.id.go_search);
        mPgb = (ProgressBar) findViewById(R.id.search_pgb);
        isVip = getIntent().getIntExtra("isVip",0);

        dataSave = new ListDataSave(Resource_SearchActivity.this, "download");
        mDownloadList = dataSave.getDataList("All");

        mSearchKeJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setHint(getBaseContext().getString(R.string.search_title_keyword));
                mSearchZhuanJia.setTextColor(getBaseContext().getResources().getColor(R.color.popup_bg));
                mSearchZhuanJia.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_false));
                mSearchKeJian.setTextColor(getBaseContext().getResources().getColor(R.color.white));
                mSearchKeJian.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_true));
                mSearchZhiNan.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_false));
                mSearchZhiNan.setTextColor(getBaseContext().getResources().getColor(R.color.popup_bg));
                TEXTTYPE = 1;
            }
        });
        mSearchZhiNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setHint(getBaseContext().getString(R.string.search_author_keyword));
                mSearchZhuanJia.setTextColor(getBaseContext().getResources().getColor(R.color.popup_bg));
                mSearchZhuanJia.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_false));
                mSearchKeJian.setTextColor(getBaseContext().getResources().getColor(R.color.popup_bg));
                mSearchKeJian.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_false));
                mSearchZhiNan.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_true));
                mSearchZhiNan.setTextColor(getBaseContext().getResources().getColor(R.color.white));
                TEXTTYPE = 2;
            }
        });
        mSearchZhuanJia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setHint(getBaseContext().getString(R.string.search_expert_keyword));
                mSearchZhuanJia.setTextColor(getBaseContext().getResources().getColor(R.color.white));
                mSearchZhuanJia.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_true));
                mSearchKeJian.setTextColor(getBaseContext().getResources().getColor(R.color.popup_bg));
                mSearchKeJian.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_false));
                mSearchZhiNan.setBackground(getBaseContext().getResources().getDrawable(R.drawable.search_bt_false));
                mSearchZhiNan.setTextColor(getBaseContext().getResources().getColor(R.color.popup_bg));
                TEXTTYPE = 3;
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("取消".equals(mBack.getText().toString())){
                    finish();
                }else if("返回".equals(mBack.getText().toString())){
                    mBack.setText("取消");
                    mLinaearLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mGoSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        mGoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hand.sendEmptyMessage(2);
            }
        });
    }



    private void initData(final int type) {
        CHYHttpClientUsage.getInstanse().getSearchData(text,TEXTTYPE,isVip,new JsonHttpResponseHandler(Constants.ENCODING_GBK){
            @Override
            public void onStart() {
                super.onStart();
            }
            @Override
            public void onFinish() {
                super.onFinish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response!=null){
                        if(response.getInt("state")==1){
                            Gson gson = new Gson();
                            if(type == 1 || type == 3){
                                mKJBeans = gson.fromJson(response.toString(),ResourceListArrayBeans.class);
                            }else if(type == 2){
                                mZNBeans = gson.fromJson(response.toString(),ResourceSeacrhZNBeans.class);
                            }
                            hand.sendEmptyMessage(1);
                        }else{
                            hand.sendEmptyMessage(4);
                        }
                    }else{
                        hand.sendEmptyMessage(4);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPgb.setVisibility(View.GONE);
            switch (msg.what){
                case 1:
                    mPgb.setVisibility(View.GONE);
                    mGoSearch.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if(TEXTTYPE == 1 || TEXTTYPE == 3){
                        WindowManager wm = (WindowManager)Resource_SearchActivity.this.getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        text = mEdit.getText().toString();
                        mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
                        mKJAdapter = new ResourceListAdapter(mKJBeans.getJsonArray(),getBaseContext(),text,1,width,false);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.setAdapter(mKJAdapter);
                        mKJAdapter.SetOnItemClickListener(new ResourceListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                ResourceListArrayBeans.JsonArrayBean bean = mKJBeans.getJsonArray().get(position);
                                String url = bean.getDataUrl();
                                if(url.contains("?")) {
                                    url = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                                }else {
                                    url = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                                }
                                CollegeActivity.startCitCollegeActivity(Resource_SearchActivity.this,bean.getTitle(), url);
                            }
                        });
                    }else if(TEXTTYPE == 2){
                        mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
                        mZNAdapter = new ResourceListAdapter(mZNBeans.getJsonArray(),getBaseContext(),3,true);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.setAdapter(mZNAdapter);
                        mZNAdapter.SetOnItemClickListener(new ResourceListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                ResourceSeacrhZNBeans.JsonArrayBean bean = mZNBeans.getJsonArray().get(position);
                                if (mDownloadList.size() > 0) {
                                    boolean contain = false;
                                    DownloadPDFBeans download = new DownloadPDFBeans();
                                    for (int i = 0; i < mDownloadList.size(); i++) {
                                        if ((bean.getTitle()+"-"+bean.getTypeField()).equals(download.getTitle())) {
                                            if(download.getIsVip() == isVip){
                                                contain = true;
                                            }else{
                                                try {
                                                    FileUtils.forceDelete(new File(download.getPathUrl()));
                                                    contain = false;
                                                    break;
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else {
                                            contain = false;
                                        }
                                    }
                                    if(contain){
                                        int sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
                                        if(sdkVersion >=21) {
                                            /*PdfActivity.startPdfActivity(Resource_SearchActivity.this, download.getPathUrl(), download.getTitle(),isVip);*/
                                        }else {
                                            File file = new File(download.getPathUrl());
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            try {
                                                startActivity(intent);
                                            }catch (Exception e) {
                                                Toast.makeText(Resource_SearchActivity.this, R.string.attach_open_tips,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }else{
                                        download(bean);
                                    }
                                }else{
                                    download(bean);
                                }
                            }
                        });
                    }
                    break;
                case 2:
                    if(mEdit.getText().toString() != null){
                        try {
                            text = URLEncoder.encode(URLEncoder.encode(mEdit.getText().toString(), "UTF-8"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        text = "";
                    }
                    if(!"".equals(text)){
                        mBack.setText("返回");
                        mLinaearLayout.setVisibility(View.GONE);
                        mPgb.setVisibility(View.VISIBLE);
                        initData(TEXTTYPE);
                    }else{
                        Toast.makeText(getApplication(),"请选择或输入搜索内容",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    mPgb.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mResultNull.setVisibility(View.VISIBLE);
                    mGoSearch.setVisibility(View.GONE);
                    mResultNull.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hand.sendEmptyMessage(5);
                        }
                    });
                    break;
                case 5:
                    mEdit.setText("");
                    mBack.setText("取消");
                    mResultNull.setVisibility(View.GONE);
                    mLinaearLayout.setVisibility(View.VISIBLE);
                    mGoSearch.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private void download(final ResourceSeacrhZNBeans.JsonArrayBean bean){
        /*final DownloadRequest request = new DownloadRequest.Builder()
                .setTitle(bean.getTitle()+"-"+bean.getTypeField() + ".pdf")
                .setUri(bean.getPdfUrl())
                .setFolder(new File(AppApplication.instance().getDownloadPath()))
                .build();
        DownloadManager.getInstance().download(request, bean.getPdfUrl(), new CallBack() {
            @Override
            public void onStarted() {
                Log.e("GYW","--onStarted");
                mProgressDialog = ProgressDialog.show(Resource_SearchActivity.this, null, "正在加载...");
            }
            @Override
            public void onConnecting() {
                Log.e("GYW","--onConnecting");
            }
            @Override
            public void onConnected(long total, boolean isRangeSupport) {
                Log.e("GYW","--onConnected");
            }
            @Override
            public void onProgress(long finished, long total, int progress) {
                Log.e("GYW","--onProgress");
            }
            @Override
            public void onCompleted() {
                Log.e("GYW","--onCompleted");
                mProgressDialog.dismiss();
                DownloadPDFBeans collectionBean = new DownloadPDFBeans();
                collectionBean.setDataId(bean.getDataId());
                collectionBean.setType(bean.getTypeField());
                collectionBean.setTitle(bean.getTitle()+"-"+bean.getTypeField());
                collectionBean.setIsVip(isVip);
                collectionBean.setPathUrl(AppApplication.instance().getDownloadPath()+ File.separator +  bean.getTitle()+"-"+bean.getTypeField() + ".pdf");
                mDownloadList.add(collectionBean);
                dataSave.setDataList("All",mDownloadList);
                int sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
                if(sdkVersion >=21) {
                    PdfActivity.startPdfActivity(Resource_SearchActivity.this, AppApplication.instance().getDownloadPath() + File.separator + bean.getTitle()+"-"+bean.getTypeField() + ".pdf", bean.getTitle()+"-"+bean.getTypeField(),isVip);
                }else {
                    File file = new File(AppApplication.instance().getDownloadPath() + File.separator + bean.getTitle()+"-"+bean.getTypeField() + ".pdf");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(Resource_SearchActivity.this, R.string.attach_open_tips,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onDownloadPaused() {
                Log.e("GYW","--onDownloadPaused");
            }
            @Override
            public void onDownloadCanceled() {
                Log.e("GYW","--onDownloadCanceled");
            }
            @Override
            public void onFailed(DownloadException e) {
                Log.e("GYW","--onFailed");
            }
        });*/
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(Resource_SearchActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            hand.sendEmptyMessage(2);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart(Constants.FRAGMENT_RESOURCE_SEARCH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd(Constants.FRAGMENT_RESOURCE_SEARCH);
    }
}
