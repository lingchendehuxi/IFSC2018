package com.android.incongress.cd.conference.fragments.scenic_xiu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.ScenicXiuPicsViewpagerActivity;
import com.android.incongress.cd.conference.adapters.CommentAdapter;
import com.android.incongress.cd.conference.adapters.ScenicXiuGridAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommentArrayBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.ListViewForScrollView;
import com.android.incongress.cd.conference.widget.NoScrollGridView;
import com.android.incongress.cd.conference.widget.popup.CommentPopupWindow;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

import cz.msebera.android.httpclient.Header;

public class ScenicXiuCommentActivity extends BaseActivity implements View.OnClickListener {
    private ScenicXiuBean bean;
    NoScrollGridView gridViewPics;
    TextView tvPublisherName,mTitleText;
    ImageView civPublisherIcon;
    TextView tvPublishTime;
    TextView tvContent;
    ListViewForScrollView mRecyclerView;
    TextView tvNumber,goPingLun,dianzan;
    ImageView ivback,dz_img;
    LinearLayout dz_layout;
    private ScrollView scroll_view;

    private CommentAdapter mCommentAdapter;
    public static void scenicXiuCommentActivity(Context context, ScenicXiuBean bean){
        Intent intent = new Intent();
        intent.setClass(context, ScenicXiuCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_circle_details);
    }

    @Override
    protected void initViewsAction() {
        bean = (ScenicXiuBean) getIntent().getSerializableExtra("bean");
        tvPublisherName = findViewById(R.id.tv_publisher_name);
        gridViewPics = findViewById(R.id.gv_pics);
        civPublisherIcon = findViewById(R.id.civ_publisher);
        tvPublishTime = findViewById(R.id.tv_publish_time);
        tvContent = findViewById(R.id.tv_publish_content);
        mRecyclerView = findViewById(R.id.rcv_comments);
        tvNumber = findViewById(R.id.tv_comment_all_title);
        goPingLun = findViewById(R.id.pl_go);
        ivback = findViewById(R.id.title_back);
        mTitleText = findViewById(R.id.title_text);
        scroll_view = findViewById(R.id.scroll_view);
        dianzan = findViewById(R.id.dianzan);
        dz_img = findViewById(R.id.dz_img);
        dz_layout = findViewById(R.id.dz_layout);
        ivback.setOnClickListener(this);
        goPingLun.setOnClickListener(this);
        dz_layout.setOnClickListener(this);
        tvPublisherName.setText(bean.getAuthor());
        tvPublishTime.setText(bean.getTimeShow());
        mTitleText.setText("正文");
        tvNumber.setText(getString(R.string.xxx_comments1, (bean.getCommentArray().size())+""));
        if (!StringUtils.isEmpty(bean.getAuthorImg())) {
            PicUtils.loadCircleImage(this,replaceHttps(bean.getAuthorImg()),civPublisherIcon);
        } else {
            civPublisherIcon.setImageResource(R.drawable.professor_default);
        }
        dianzan.setText(bean.getLaudCount()+"");
        if (bean.getIsLaud() == 1) {
            dianzan.setTextColor(getResources().getColor(R.color.theme_color));
            dz_img.setImageResource(R.drawable.praised);
        }else {
            dianzan.setTextColor(getResources().getColor(R.color.gray));
            dz_img.setImageResource(R.drawable.praise);
        }
        String content = "";
        try {
            content = URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8);
            content = URLDecoder.decode(content, Constants.ENCODING_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        }else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        }

        gridViewPics.setVisibility(View.GONE);
        //不为空，说明有图片，至少一张
        if (!StringUtils.isEmpty(bean.getImgUrls())) {
            gridViewPics.setVisibility(View.VISIBLE);
            final String[] strPics = bean.getImgUrls().split(",");

            gridViewPics.setAdapter(new ScenicXiuGridAdapter(strPics, this));
            gridViewPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ScenicXiuPicsViewpagerActivity.startViewPagerActivity(ScenicXiuCommentActivity.this, strPics, position);
                }
            });
        }else {
            gridViewPics.setVisibility(View.GONE);
        }
        mRecyclerView.setFocusable(false);
        mCommentAdapter = new CommentAdapter(this, bean.getCommentArray());
        mRecyclerView.setAdapter(mCommentAdapter);

        mCommentAdapter.setmOnItemClickListner(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CommentArrayBean commentListBean) {
                if(AppApplication.userId!=commentListBean.getUserId()){}
                getcomment(bean.getSceneShowId()+"", commentListBean.getUserName(),commentListBean.getCommentId()+"");
            }
        });

        scroll_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if(i1>=450){
                    mTitleText.setText(bean.getAuthor());
                }else {
                    mTitleText.setText("正文");
                }
            }
        });
    }

    private void getcomment(String sceneShowId,String name,String parentId) {
        CommentPopupWindow mCommentPop = new CommentPopupWindow(this,  SharePreferenceUtils.getUser(Constants.USER_NAME),name,sceneShowId, parentId,new JsonHttpResponseHandler(Constants.ENCODING_GBK){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int state = response.getInt("state");
                    if (state == 1) {
                        Toast.makeText(ScenicXiuCommentActivity.this,"评论成功", Toast.LENGTH_SHORT).show();
                        JSONArray array = response.getJSONArray("commentArray");
                        CommentArrayBean tempComment = new CommentArrayBean();
                        JSONObject object = array.getJSONObject(0);
                        tempComment.setCommentId(object.getInt("commentId"));
                        tempComment.setContent(object.getString("content"));
                        tempComment.setUserId(object.getInt("userId"));
                        tempComment.setUserName(object.getString("userName"));
                        tempComment.setUserImg(object.getString("userImg"));
                        tempComment.setTimeShow(object.getString("timeShow"));
                        tempComment.setParentId(object.getInt("parentId"));
                        tempComment.setParentName(object.getString("parentName"));
                        if (tempComment != null) {
                            bean.getCommentArray().add(0, tempComment);
                            mCommentAdapter.notifyDataSetChanged();
                            tvNumber.setText(getString(R.string.xxx_comments1, (bean.getCommentArray().size())+""));
                            Intent intent = new Intent(Constants.ACTION_COMMENT_UPDATE);
                            sendBroadcast(intent);
                        }
                    } else {
                        Toast.makeText(ScenicXiuCommentActivity.this,response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mCommentPop.showPopupWindow();
    }
    private String replaceHttps(String url) {
        if(url.contains("https"))
            return url.replace("https","http");
        else
            return url;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.pl_go:
                if (!AppApplication.isUserLogIn()) {
                    startActivity(new Intent(ScenicXiuCommentActivity.this, LoginActivity.class));
                    return;
                }

                getcomment(bean.getSceneShowId()+"","","-1");
                break;
            case R.id.dz_layout:
                if (!AppApplication.isUserLogIn()) {
                    startActivity(new Intent(ScenicXiuCommentActivity.this, LoginActivity.class));
                    return;
                }
                if(bean.getIsLaud() != 1){
                    CHYHttpClientUsage.getInstanse().doSceneShowLaud(bean.getSceneShowId() + "", AppApplication.userId+ "", AppApplication.userType + "", new JsonHttpResponseHandler("gbk") {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                int state = response.getInt("state");
                                if (state == 1) {
                                    bean.setLaudCount(bean.getLaudCount()+1);
                                    bean.setIsLaud(1);
                                    dianzan.setText(bean.getLaudCount()+"");
                                    dianzan.setTextColor(getResources().getColor(R.color.theme_color));
                                    dz_img.setImageResource(R.drawable.praised);
                                    Intent intent = new Intent(Constants.ACTION_COMMENT_UPDATE);
                                    sendBroadcast(intent);
                                } else {
                                    String tips = response.getString("msg");
                                    if (!StringUtils.isEmpty(tips))
                                        ToastUtils.showToast(tips);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ScenicXiuCommentActivity.this,"您已经赞过了",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

}
