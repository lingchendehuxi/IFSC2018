package com.android.incongress.cd.conference;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.EsmoVersionBean;
import com.android.incongress.cd.conference.beans.EsmosBean;
import com.android.incongress.cd.conference.model.ConferenceDb;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.newf.ChooseConferenceFragment;
import com.android.incongress.cd.conference.utils.BaseAsyncTask;
import com.android.incongress.cd.conference.utils.FileUtils;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/11/1.
 * 会议下载页
 *
 * 每个会议的数据更新也是在这个地方
 */

public class ConferencedDownloadDetailActivity extends BaseActivity {

    private EsmosBean mEsmoBean;
    private EsmoVersionBean mEsmoVersionBean;
    private int mConferenceId;

    private String mFilePath;//下载保存的位置和解压后的位置
    private int mCurrentDataVersion = 0;

    @BindView(R.id.iv_top_logo)
    ImageView mIvTopLogo;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.tv_remark)
    TextView mTvRemark;
    @BindView(R.id.bt_handle)
    Button mBtHandle;
    @BindView(R.id.bt_delete)
    Button mBtDelete;

    /**
     * 是否删除成功
     **/
    boolean mDeleteSucees = false;
    /**
     * 本地是否已经有数据
     **/
    boolean mIsFolderExist = false;

    //三种情况，分别是下载，更新和打开
    private static final int TYPE_DOWNLOAD = 0X0001;
    private static final int TYPE_UPDATE = 0X0002;
    private static final int TYPE_OPEN = 0X0003;

    //默认情况下是下载，即本地并没有数据
    private int mCurrentHandleType = TYPE_DOWNLOAD;

    @OnClick(R.id.bt_delete)
    void onDeleteClick() {
        showDialog(R.string.conference_delete_tips, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new BaseAsyncTask(ConferencedDownloadDetailActivity.this) {
                    @Override
                    protected void backgroundWork() {
                        mDeleteSucees = FileUtils.deleteDirectory(mFilePath);
                        if(mDeleteSucees) {
                            ConferenceDbUtils.updateConferenceDataVersion(mConferenceId,0);
                            mCurrentDataVersion = 0;
                        }
                    }

                    @Override
                    protected void preWork() {
                        showProgressBar("正在删除...");
                    }

                    @Override
                    protected void postWork() {
                        dismissProgressBar();
                        ToastUtils.showToast(mDeleteSucees ? "删除成功" : "删除失败");
                        if(mDeleteSucees) {
                            ConferenceDbUtils.updateConferenceExistStatus(mConferenceId, 0);
                            EventBus.getDefault().post(new HomeActivity.UpdateConferenceEvent(mConferenceId,false, false));
                            mIsFolderExist = false;
                        }
                        queryConferenceDataVersion();
                    }

                    @Override
                    protected void cancelWork() {

                    }
                }.execute();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }, false);
    }

    @OnClick(R.id.bt_handle)
    void onHandleClick() {
        if(mEsmoBean != null && mEsmoBean.getState() == 1) {
            switch (mCurrentHandleType) {
                case TYPE_DOWNLOAD:
                case TYPE_UPDATE:
                    showProgressBar("正在下载并处理数据");
                    downloadDataAndUnZip(0);
                    break;
                case TYPE_OPEN:
//                   EventBus.getDefault().post(new HomeActivity.AdEvents(true));
//                   setResult(RESULT_OK);
//                   finish();
//                    startActivity(new Intent(ConferencedDownloadDetailActivity.this,AdvertisesActivity.class));
                    break;
            }
        }
    }

    @OnClick(R.id.iv_back)
    void onBackClick() {
        setResult(RESULT_CANCELED);
        finish();
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.fragment_conference_download_detail);
    }

    @Override
    protected void initViewsAction() {
        mConferenceId = getIntent().getIntExtra(Constants.CONFERENCE_ID, 0);
        if (mConferenceId == 0)
            goErrorAndFinish();

        mEsmoBean = ConferenceDbUtils.getEsmoById(mConferenceId);

        if (mEsmoBean == null)
            goErrorAndFinish();
        Constants.conId = mConferenceId;

        mFilePath = AppApplication.instance().getSDPath() + "/cd_incongress/incongress" + mConferenceId + "/";

        if (FileUtils.isFolderExist(mFilePath)) {
            //已经存在，则是打开
            mIsFolderExist = true;
            mBtHandle.setText("打开");
        } else {
            //不存在，则是下载
            mBtDelete.setVisibility(View.GONE);
            mIsFolderExist = false;
            mBtHandle.setText("下载");
        }

        mCurrentDataVersion = ConferenceDbUtils.getConferenceDataVersion(mConferenceId);
        if (mCurrentDataVersion == -1) {
//            if (mConferenceId == 194) {
//                ConferenceDbUtils.addConferenceDataVersion(mConferenceId, 49);
//                mCurrentDataVersion = 49;
//            } else {
//                ConferenceDbUtils.addConferenceDataVersion(mConferenceId, 0);
//                mCurrentDataVersion = 0;
//            }
            ConferenceDbUtils.addConferenceDataVersion(mConferenceId, 0);
            mCurrentDataVersion = 0;
        }
        PicUtils.loadImageUrl(this,mEsmoBean.getBackgroundUrl(),mIvTopLogo);
        PicUtils.loadImageUrl(this,mEsmoBean.getIconUrl(),mIvLogo);

        mTvTime.setText(mEsmoBean.getConferencesDays());
        mTvLocation.setText(mEsmoBean.getConferencesAddress());


        if(mEsmoBean.getState() == 1) {
            mTvRemark.setText(mEsmoBean.getRemark().replace("\\n", "\n"));
            queryConferenceDataVersion();
        }else {
            mBtHandle.setBackgroundColor(getResources().getColor(R.color.gray));
            mBtHandle.setTextColor(getResources().getColor(R.color.white));
            mTvRemark.setText("敬请期待");
        }
    }

    /**
     * 查询大会数据
     */
    private void queryConferenceDataVersion() {
        CHYHttpClientUsage.getInstanse().doGetEsmoData(mConferenceId + "", mCurrentDataVersion + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.println("response:data:" + response);
                Gson gson = new Gson();
                mEsmoVersionBean = gson.fromJson(response.toString(), EsmoVersionBean.class);

                if (mIsFolderExist) {
                    mBtDelete.setVisibility(View.VISIBLE);
                    if (mEsmoVersionBean.getVersion().size() == 0) {
                        //没有更新
                        mBtHandle.setText("打开");
                        mCurrentHandleType = TYPE_OPEN;
                    } else {
                        //有更新,则显示更新按钮
                        mBtHandle.setText("更新");
                        mCurrentHandleType = TYPE_UPDATE;
                    }
                } else {
                    //下载所有数据包
                    mBtDelete.setVisibility(View.GONE);
                    mBtHandle.setText("下载");
                    mCurrentHandleType = TYPE_DOWNLOAD;
                }
            }
        });
    }

    private void goErrorAndFinish() {
        ToastUtils.showToast("数据出错...");
        finish();
    }

    /**
     * 下载数据并解压到指定文件夹
     */
    private void downloadDataAndUnZip(final int position) {
        if (mEsmoVersionBean != null && (mEsmoVersionBean.getVersion().size() - 1) >= position) {
            final EsmoVersionBean.VersionBean versionBean = mEsmoVersionBean.getVersion().get(position);

            String fileDownloadPath = Constants.get_NEWSPREFIX() + versionBean.getZipUrl().replace("\n", "");//下载地址
            String fileDownloadName = fileDownloadPath.substring(fileDownloadPath.lastIndexOf("/") + 1);//文件名称

            //如果文件夹不存在，则创建
            File dir = new File(mFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File zipFile = new File(mFilePath + fileDownloadName);


            //如果文件已经存在，则不需要下载，直接解压
            if(zipFile.length() > 0) {
                updateConferenceData(zipFile, position, versionBean);
            }else{
                AsyncHttpClient httpClient = AppApplication.getHttpClient();
                httpClient.get(this, fileDownloadPath, new FileAsyncHttpResponseHandler(zipFile) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        ToastUtils.showToast("下载失败");
                        dismissProgressBar();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {
                        updateConferenceData(file, position, versionBean);
                    }
                });
            }
        }
    }

    private void updateConferenceData(File file, int position, EsmoVersionBean.VersionBean versionBean) {
        FileInputStream zis;
        try {
            zis = new FileInputStream(file);
            FileUtils.unZip(zis, mFilePath);
            mCurrentDataVersion = versionBean.getVersion();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (position == (mEsmoVersionBean.getVersion().size() - 1)) {
            new BaseAsyncTask(ConferencedDownloadDetailActivity.this) {
                @Override
                protected void backgroundWork() {
                    ConferenceDb.createDB(mFilePath, true);
                    AppApplication.adList = ConferenceDbUtils.getAllAds();
                    ConferenceDbUtils.updateConferenceDataVersion(mConferenceId, mCurrentDataVersion);
                    ConferenceDbUtils.updateConferenceExistStatus(mConferenceId, 1);
                }

                @Override
                protected void preWork() {
                }

                @Override
                protected void postWork() {
                    ToastUtils.showToast("下载成功");

                    EventBus.getDefault().post(new HomeActivity.UpdateConferenceEvent(mConferenceId,true, false));
                    dismissProgressBar();
                    mBtDelete.setVisibility(View.VISIBLE);
                    mBtHandle.setText("打开");
                    mCurrentHandleType = TYPE_OPEN;
                }

                @Override
                protected void cancelWork() {

                }
            }.execute();
        } else {
            downloadDataAndUnZip(position + 1);
        }
    }
}
