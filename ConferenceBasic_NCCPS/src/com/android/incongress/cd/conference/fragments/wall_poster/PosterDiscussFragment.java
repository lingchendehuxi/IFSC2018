package com.android.incongress.cd.conference.fragments.wall_poster;

import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.MeetingCommunityCommentsAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommunityTopicContentBean;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.DZBBDiscussResponseBean;
import com.android.incongress.cd.conference.data.JsonParser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.widget.IncongressEditText;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.android.incongress.cd.conference.widget.RefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 参与讨论 详情页面
 *
 * @author Jakcy
 */
public class PosterDiscussFragment extends BaseActivity implements OnClickListener {

    private IncongressEditText mIncongressEditText;
    private ListView mListView;
    private RefreshLayout mRefreshLayout;
    private ImageView ivback;

    private Button mPostButton;
    private int mPageIndex = 1;//当前页数
    private int mPageSize = 12;//每页显示的数据条数
    private int mConnectTime = 0;// 因为hession有Bug 所有采用的是如果连接5次还没有连接上就提示无法连接请重试
    public final static int MSG_NO_MORE_DATA = 0x1003;//没有更多的数据
    public final static int MSG_REFRESH_DATA = 0x1004;//刷新数据
    public final static int MSG_ERROR_SUBMIT = 0x1005;
    public final static int MSG_SUCCESS = 0x1000;//正确
    public final static int MSG_ERROR = 0x1002;//出现错误
    public final static int MSG_REFESHDONE = 0x2000;//刷新完成
    private List<CommunityTopicContentBean> mList;
    private MeetingCommunityCommentsAdapter mAdapter;
    private DZBBBean.ArrayBean mDZBBBean;
    private IncongressTextView mNoDataView;
    private int mPosterCommentId = 0;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hysq_comments_post:
                sendTopic();
                mPostButton.setClickable(false);
                break;
            default:
                break;
        }
    }

    private void sendTopic() {
        String msg = mIncongressEditText.getText().toString().trim();
        if (msg != null && msg.length() > 0) {
            sendTopicContent(msg);
        } else {
            TostShowMsg(R.string.hysq_home_comment_send_msg_hint);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getTopicContentList();
    }


    private void getTopicContentList() {
        CHYHttpClientUsage.getInstanse().doGetPosterDiscussListByPid(mDZBBBean.getPosterId(), mPageIndex, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<CommunityTopicContentBean> strList = JsonParser.parseDZBBContent(response.toString());

                if (strList.size() == 0) {
                    mhandler.sendEmptyMessage(MSG_NO_MORE_DATA);
                } else {
                    mList.addAll(strList);
                    mhandler.sendEmptyMessage(MSG_SUCCESS);
                }

                mhandler.sendEmptyMessage(MSG_REFESHDONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mhandler.sendEmptyMessage(MSG_ERROR);
            }
        });
    }

    private void getFirstContent() {
        CHYHttpClientUsage.getInstanse().doGetPosterDiscussByID(mPosterCommentId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                CommunityTopicContentBean bean = JsonParser.parseDZBBOneContent(response.toString());
                if (bean == null) {
                    mhandler.sendEmptyMessage(MSG_NO_MORE_DATA);
                } else {
                    mList.add(0, bean);
                    mhandler.sendEmptyMessage(MSG_SUCCESS);
                }
                mhandler.sendEmptyMessage(MSG_REFESHDONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mhandler.sendEmptyMessage(MSG_ERROR);
            }
        });
    }


    private void TostShowMsg(int strId) {
        Toast.makeText(PosterDiscussFragment.this, strId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 发送评论
     */
    private void sendTopicContent(String content) {
        String userName = AppApplication.username;
        try {
            content = URLEncoder.encode(content, Constants.ENCODING_UTF8);
            userName = URLEncoder.encode(userName, Constants.ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        CHYHttpClientUsage.getInstanse().doCreatePosterDiscuss(AppApplication.userId, AppApplication.userType, userName, content, Constants.getConId(), mDZBBBean.getPosterId(), new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                DZBBDiscussResponseBean bean = JsonParser.parseDiscussSuccess(response.toString());
                mPosterCommentId = bean.getPosterDiscussId();
                if (bean != null && bean.getState() == 1) {
                    //此处如果是游客需要保存游客的身份信息 也需要持久化
                    int userType = AppApplication.userType;
                    if (userType == 0) {
                        SharePreferenceUtils.saveUserString(Constants.USER_IS_LOGIN, "true");

                        AppApplication.userId = bean.getUserId();
                        AppApplication.username = bean.getUserName();
                        AppApplication.userType = bean.getUserType();

                        //存储到sharepreference
                        SharePreferenceUtils.saveUserInt(Constants.USER_ID, bean.getUserId());
                        SharePreferenceUtils.saveUserString(Constants.USER_NAME, bean.getUserName());
                        SharePreferenceUtils.saveUserInt(Constants.USER_TYPE, bean.getUserType());//表明是用户
                    }
                    mhandler.sendEmptyMessage(MSG_REFRESH_DATA);
                } else {
                    mhandler.sendEmptyMessage(MSG_ERROR_SUBMIT);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mhandler.sendEmptyMessage(MSG_ERROR);
            }
        });
    }

    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_SUCCESS:
                    mPageIndex++;
                    mAdapter.setContent(mList);
                    mAdapter.notifyDataSetChanged();
                    if (mList.size() > 0) {
                        mListView.setVisibility(View.VISIBLE);
                        mNoDataView.setVisibility(View.GONE);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoDataView.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_ERROR:
                    mConnectTime++;
                    if (mConnectTime == 5) {
                        Toast.makeText(PosterDiscussFragment.this, R.string.incongress_connect_fail, Toast.LENGTH_SHORT).show();
                        mConnectTime = 0;
                        mPostButton.setClickable(true);
                    } else {
                        getTopicContentList();
                    }
                    break;
                case MSG_NO_MORE_DATA:
                    if (mList.size() > 0) {
                        mListView.setVisibility(View.VISIBLE);
                        mNoDataView.setVisibility(View.GONE);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoDataView.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_REFRESH_DATA:
                    mPostButton.setClickable(true);
                    hideShurufa();
                    TostShowMsg(R.string.hysq_home_comment_send_msg_success_hint);
                    getFirstContent();
                    mIncongressEditText.setText("");
                    mConnectTime = 0;
                    finish();
                    break;
                case MSG_ERROR_SUBMIT:
                    mConnectTime++;
                    if (mConnectTime == 5) {
                        TostShowMsg(R.string.hysq_home_comment_send_msg_error_hint);
                        mIncongressEditText.setText("");
                        mConnectTime = 0;
                    } else {
                        sendTopic();
                    }
                case MSG_REFESHDONE:
                    mRefreshLayout.refreshComplete();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void setContentView() {
        setContentView(R.layout.dzbbdiscussion_comments);
    }

    @Override
    protected void initViewsAction() {
        mDZBBBean = (DZBBBean.ArrayBean) getIntent().getSerializableExtra("bean");
        mIncongressEditText = (IncongressEditText) findViewById(R.id.hysq_comments_edit);
        mListView = (ListView) findViewById(R.id.hysq_comments_list);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.pull_refresh_scrollview);
        mNoDataView = (IncongressTextView) findViewById(R.id.exhibitor_no_data);
        mPostButton = (Button) findViewById(R.id.hysq_comments_post);
        mPostButton.setOnClickListener(this);

        SpannableStringBuilder message = new SpannableStringBuilder();

        LayoutInflater inflater = this.getLayoutInflater();
        mList = new ArrayList<CommunityTopicContentBean>();
        mAdapter = new MeetingCommunityCommentsAdapter(inflater, mList);
        mListView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageIndex = 1;
                mList.clear();
                getTopicContentList();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new RefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getTopicContentList();
            }
        });
        ivback = (ImageView) findViewById(R.id.title_back);
        ivback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}