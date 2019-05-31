package com.android.incongress.cd.conference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.ResourceListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DownloadPDFBeans;
import com.android.incongress.cd.conference.beans.ResourceGuideBeans;
import com.android.incongress.cd.conference.beans.ResourceListArrayBeans;
import com.android.incongress.cd.conference.utils.ListDataSave;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.android.incongress.cd.conference.widget.SharePopupWindow;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.finalteam.toolsfinal.io.FileUtils;
import cz.msebera.android.httpclient.Header;

public class ResourceListActivity extends FragmentActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView,mGuideRecyclerView;
    private LinearLayout mRetry;
    private static final String RESOURCE_CONFERENCES = "resource_conferencesId";
    private static final String RESOURCE_TITLE = "resource_title";
    private static final String RESOURCE_VIP = "resource_vip";
    private static final String RESOURCE_TYPE = "resource_type";
    private String mConferencesId,mTitle;
    private int isvip,type;
    private ImageView mIvBack,mIvShare;
    private TextView mTs;
    private IncongressTextView mIvTitle;
    private ResourceListArrayBeans mArrayBean;
    private ResourceGuideBeans mGuideBean;
    private ResourceListAdapter mAdapter,mGuideAdapter;
    private Context mContext;
    private int width;
    private ListDataSave dataSave;
    private List<DownloadPDFBeans> mDownloadList;
    protected ProgressDialog mProgressDialog;
    private SharePopupWindow mSharePopupWindow;
    public static void resourceListActivity(Context context, String title, String conferencesId,int type) {
        Intent intent = new Intent();
        intent.setClass(context, ResourceListActivity.class);
        intent.putExtra(RESOURCE_TITLE, title);
        intent.putExtra(RESOURCE_CONFERENCES, conferencesId);
        intent.putExtra(RESOURCE_TYPE,type);
        context.startActivity(intent);
    }
    public static void resourceListActivity(Context context, String title, String conferencesId,int type,int isvip) {
        Intent intent = new Intent();
        intent.setClass(context, ResourceListActivity.class);
        intent.putExtra(RESOURCE_TITLE, title);
        intent.putExtra(RESOURCE_CONFERENCES, conferencesId);
        intent.putExtra(RESOURCE_VIP,isvip);
        intent.putExtra(RESOURCE_TYPE,type);
        context.startActivity(intent);

    }
//http://app.incongress.cn/webapp/discussion/CSCO2016H5/spList0.html?isClear=1&conId=
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_resource_list);
        mContext = this;
        mConferencesId = getIntent().getStringExtra(RESOURCE_CONFERENCES);
        mTitle = getIntent().getStringExtra(RESOURCE_TITLE);
        type = getIntent().getIntExtra(RESOURCE_TYPE,0);
        dataSave = new ListDataSave(mContext, "download");
        mDownloadList = dataSave.getDataList("All");
        if(type == 3){
            isvip = getIntent().getIntExtra(RESOURCE_VIP,0);
        }
        WindowManager wm = (WindowManager)ResourceListActivity.this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        mRetry = (LinearLayout)findViewById(R.id.resource_cs);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.resource_swiperefresh_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.resource_recycle_list);
        mGuideRecyclerView = (RecyclerView) findViewById(R.id.resource_guide);
        mTs = (TextView) findViewById(R.id.guide_msg);
        mIvBack = (ImageView) findViewById(R.id.title_back);
        mIvShare = (ImageView) findViewById(R.id.title_isShare);
        mIvTitle = (IncongressTextView) findViewById(R.id.title_text);
        mIvTitle.setText(mTitle);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSharePopupWindow();
                mSharePopupWindow.setAnimationStyle(R.style.popupwindow_anim_alpha);
                mSharePopupWindow.showAtLocation(findViewById(R.id.resource_swiperefresh_list), Gravity.BOTTOM, 0, 0);
                lightOff();
            }
        });
        mRecyclerView.setNestedScrollingEnabled(false);
        mGuideRecyclerView.setNestedScrollingEnabled(false);
        mSwipeRefreshLayout.setColorSchemeResources( R.color.black);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(type == 1){
                    initData();
                }else{
                    initDataOther();
                }
            }
        });
        if(type == 1){
            initData();
            mIvShare.setVisibility(View.VISIBLE);
        }else{
            initDataOther();
            mIvShare.setVisibility(View.INVISIBLE);
        }
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1){
                    initData();
                }else{
                    initDataOther();
                }
            }
        });
    }
    private void initSharePopupWindow() {
//		String sessionName = "";
//		if (AppApplication.systemLanguage == 1) {
//			sessionName = mSessionBeanList.get(mPosition).getSessionName();
//		} else {
//			sessionName = mSessionBeanList.get(mPosition).getSessionNameEN();
//		}

        mSharePopupWindow = new SharePopupWindow(ResourceListActivity.this, "", mTitle);
        /*mSharePopupWindow.setmShareUrl("http://app.incongress.cn/webapp/discussion/CSCO2016H5/spList0.html?isClear=1&conId="+mConferencesId
                + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode());
        mSharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });*/
    }
    private void initData() {
        CHYHttpClientUsage.getInstanse().doGetDataByConId(mConferencesId,new JsonHttpResponseHandler(Constants.ENCODING_GBK){
            @Override
            public void onStart() {
                super.onStart();
            }
            @Override
            public void onFinish() {
                super.onFinish();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.getInt("state")==1){
                        Gson gson = new Gson();
                        mArrayBean = gson.fromJson(response.toString(),ResourceListArrayBeans.class);
                        handler.sendEmptyMessage(1);
                    }else{
                        mRetry.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initDataOther() {
        CHYHttpClientUsage.getInstanse().doGetZhiNanList(mConferencesId,isvip,new JsonHttpResponseHandler(Constants.ENCODING_GBK){
            @Override
            public void onStart() {
                super.onStart();
            }
            @Override
            public void onFinish() {
                super.onFinish();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.getInt("state")==1){
                        Gson gson = new Gson();
                        mGuideBean = gson.fromJson(response.toString(),ResourceGuideBeans.class);
                        handler.sendEmptyMessage(2);
                    }else{
                        mRetry.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void download(final ResourceGuideBeans.ZhiNanListBean bean){
        /*final DownloadRequest request = new DownloadRequest.Builder()
                .setTitle(bean.getTitle()+"-"+bean.getTypeField() + ".pdf")
                .setUri(bean.getPdfUrl())
                .setFolder(new File(AppApplication.instance().getDownloadPath()))
                .build();
        DownloadManager.getInstance().download(request, bean.getPdfUrl(), new CallBack() {
            @Override
            public void onStarted() {
                Log.e("GYW","--onStarted");
                mProgressDialog = ProgressDialog.show(mContext, null, "正在加载...");
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
                collectionBean.setIsVip(isvip);
                collectionBean.setPathUrl(AppApplication.instance().getDownloadPath()+ File.separator +  bean.getTitle()+"-"+bean.getTypeField() + ".pdf");
                mDownloadList.add(collectionBean);
                dataSave.setDataList("All",mDownloadList);
                int sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
                if(sdkVersion >=21) {
                    PdfActivity.startPdfActivity(mContext, AppApplication.instance().getDownloadPath() + File.separator + bean.getTitle()+"-"+bean.getTypeField() + ".pdf", bean.getTitle()+"-"+bean.getTypeField(),isvip);
                }else {
                    File file = new File(AppApplication.instance().getDownloadPath() + File.separator + bean.getTitle()+"-"+bean.getTypeField() + ".pdf");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(mContext, R.string.attach_open_tips,Toast.LENGTH_SHORT).show();
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
private boolean first = false;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mRetry.setVisibility(View.GONE);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
                    mAdapter = new ResourceListAdapter(mArrayBean.getJsonArray(),mContext,"",type,width,true);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.SetOnItemClickListener(new ResourceListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ResourceListArrayBeans.JsonArrayBean bean = mArrayBean.getJsonArray().get(position);
                            String url = bean.getDataUrl();
                            if(url.contains("?")) {
                                url = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                            }else {
                                url = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                            }
                            CollegeActivity.startCitCollegeActivity(mContext,bean.getTitle(), url);
                        }
                    });
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 2:
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mRetry.setVisibility(View.GONE);
                    if(!"".equals(mGuideBean.getMsg())){
                        if(!first){
                            mTs.setVisibility(View.VISIBLE);
                            mTs.setText(mGuideBean.getMsg());
                            handler.sendEmptyMessageDelayed(3,3000);
                        }
                    }
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
                    mGuideRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
                    mAdapter = new ResourceListAdapter(mGuideBean.getZhiNanList(),mContext,type);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(mAdapter);
                    if(mGuideBean.getDiaoChaList().size()>0){
                        mGuideAdapter = new ResourceListAdapter(mGuideBean.getDiaoChaList(),mContext,width,2);
                        mGuideRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mGuideRecyclerView.setAdapter(mGuideAdapter);
                    }
                    mAdapter.SetOnItemClickListener(new ResourceListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            final ResourceGuideBeans.ZhiNanListBean bean = mGuideBean.getZhiNanList().get(position);
                            if (mDownloadList.size() > 0) {
                                boolean contain = false;
                                DownloadPDFBeans download = new DownloadPDFBeans();
                                for (int i = 0; i < mDownloadList.size(); i++) {
                                    download= mDownloadList.get(i);
                                    if ((bean.getTitle()+"-"+bean.getTypeField()).equals(download.getTitle())) {
                                        if(download.getIsVip() == isvip){
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
                                        /*PdfActivity.startPdfActivity(mContext, download.getPathUrl(), download.getTitle(),isvip);*/
                                    }else {
                                        File file = new File(download.getPathUrl());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        try {
                                            startActivity(intent);
                                        }catch (Exception e) {
                                            Toast.makeText(mContext, R.string.attach_open_tips,Toast.LENGTH_SHORT).show();
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
                    mGuideAdapter.SetOnItemClickListener(new ResourceListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ResourceGuideBeans.DiaoChaListBean bean = mGuideBean.getDiaoChaList().get(position);
                            String url = bean.getLinkUrl();
                            if(url.contains("?")) {
                                url = url + "&userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                            }else {
                                url = url + "?userId=" + AppApplication.userId + "&userType=" + AppApplication.userType + "&lan=" + AppApplication.getSystemLanuageCode();
                            }
                            CollegeActivity.startCitCollegeActivity(mContext,bean.getTitle(), url);
                        }
                    });
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 3:
                    first = true;
                    mTs.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(type == 1){
            MobclickAgent.onPageStart("资源_课件_"+mTitle);
        }else{
            MobclickAgent.onPageStart("资源_指南_"+mTitle);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(type == 1){
            MobclickAgent.onPageEnd("资源_课件_"+mTitle);
        }else{
            MobclickAgent.onPageEnd("资源_指南_"+mTitle);
        }
    }
    /**
     * 内容区域变量
     */
    protected void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变暗
     */
    protected void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }
}
