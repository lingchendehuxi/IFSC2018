package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.ScenicXiuPicsViewpagerActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommentArrayBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.fragments.scenic_xiu.ScenicXiuFragment;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.widget.ListViewForScrollView;
import com.android.incongress.cd.conference.widget.NoScrollGridView;
import com.android.incongress.cd.conference.utils.CommentUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.transformer.CircleTransform;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据type类型的不同，设置不同的view
 * 1新闻 2通知 3展商活动 4发帖 5企业活动 6提问
 */
public class ScenicXiuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private NewsAndActivitysListener mListener;
    private List<ScenicXiuBean> mDatas;
    private Context mContext;

    private static final int TYPE_NEW = 1;
    private static final int TYPE_NOTIFACATION = 2;
    private static final int TYPE_MAKE_POST = 4;
    private static final int TYPE_COMPANY_ACTIVITY = 5;
    private static final int TYPE_QUESTION = 6;

    //回调，使用
    public static final int TYPE_MEDIA_CENTER = 10;
    public static final int TYPE_LIVE_SHOW = 11;
    public static final int TYPE_NOTIFY = 12;
    public static final int TYPE_NEWS = 13;

    private List<View> mOperationViewList = new ArrayList<>();

    public ScenicXiuAdapter(List<ScenicXiuBean> beans, NewsAndActivitysListener listener, Context context) {
        this.mDatas = beans;
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NEW) {
            //新闻 --> 没有评论和点赞
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_news, parent, false);
            ViewHolder1News holder = new ViewHolder1News(view);
            AppApplication.applyFont(mContext,view,"fonts/zd.TTF");
            return holder;
        } else if (viewType == TYPE_NOTIFACATION) {
            //通知 --> 没有评论和点赞
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_notifacation, parent, false);
            ViewHolder2Notifacation holder = new ViewHolder2Notifacation(view);
            AppApplication.applyFont(mContext,view,"fonts/zd.TTF");
            return holder;
        } else if (viewType == TYPE_MAKE_POST) {
            //发帖
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_make_post, parent, false);
            ViewHolder4MakePost holder = new ViewHolder4MakePost(view);
            AppApplication.applyFont(mContext,view,"fonts/zd.TTF");
            return holder;
        } else if (viewType == TYPE_COMPANY_ACTIVITY) {
            //企业活动
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_company_activity, parent, false);
            ViewHolder5CompanyActivitys holder = new ViewHolder5CompanyActivitys(view);
            AppApplication.applyFont(mContext,view,"fonts/zd.TTF");
            return holder;
        } else if (viewType == TYPE_QUESTION) {
            //提问
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_question, parent, false);
            ViewHolder6Question holder = new ViewHolder6Question(view);
            AppApplication.applyFont(mContext,view,"fonts/zd.TTF");
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);

        //新闻类型,不能点赞和评论
        if (viewType == TYPE_NEW) {
            final ScenicXiuBean bean = mDatas.get(position);
            ((ViewHolder1News) holder).tvPublisherName.setText(bean.getAuthor());
            ((ViewHolder1News) holder).tvPublishTime.setText(bean.getTimeShow());

            //防止闪烁
            if (StringUtils.isEmpty(bean.getLogoUrl())) {
                ((ViewHolder1News) holder).ivShow.setVisibility(View.GONE);
                ((ViewHolder1News) holder).tvIntroduction.setVisibility(View.VISIBLE);
                ((ViewHolder1News) holder).tvIntroduction.setText(bean.getIntroduction());
            } else {
                ((ViewHolder1News) holder).tvIntroduction.setVisibility(View.GONE);
                ((ViewHolder1News) holder).ivShow.setVisibility(View.VISIBLE);
                String url = bean.getLogoUrl();
                if(url.contains("https:"))
                    url = url.replaceFirst("s","");
                PicUtils.loadImageUrl(mContext,url,((ViewHolder1News) holder).ivShow);
            }

            ((ViewHolder1News) holder).tvContent.setText(bean.getTitle());
            ((ViewHolder1News) holder).rlNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.doWhenNewsOrActivityClicked(TYPE_NEWS, bean.getHtmlUrl(), bean.getTitle());
                }
            });
            if(AppApplication.systemLanguage == 1){
                ((ViewHolder1News) holder).scenicxiu_news.setImageResource(R.drawable.scenicxiu_news);
                ((ViewHolder1News) holder).news_tips.setImageResource(R.drawable.news_tips);
            }else{
                ((ViewHolder1News) holder).scenicxiu_news.setImageResource(R.drawable.scenicxiu_news_en);
                ((ViewHolder1News) holder).news_tips.setImageResource(R.drawable.news_tips_en);
            }
        }

        //通知类型
        else if (viewType == TYPE_NOTIFACATION) {
            final ScenicXiuBean bean = mDatas.get(position);
            ((ViewHolder2Notifacation) holder).tvPublisherName.setText(bean.getAuthor());
            ((ViewHolder2Notifacation) holder).tvPublishTime.setText(bean.getTimeShow());

            if (StringUtils.isEmpty(bean.getLogoUrl())) {
                ((ViewHolder2Notifacation) holder).ivShow.setVisibility(View.GONE);
                ((ViewHolder2Notifacation) holder).tvIntroduction.setVisibility(View.VISIBLE);
                ((ViewHolder2Notifacation) holder).tvIntroduction.setText(bean.getIntroduction());
            } else {
                ((ViewHolder2Notifacation) holder).tvIntroduction.setVisibility(View.GONE);
                ((ViewHolder2Notifacation) holder).ivShow.setVisibility(View.VISIBLE);
                String url = bean.getLogoUrl();
                if(url.contains("https:"))
                    url = url.replaceFirst("s","");
                PicUtils.loadImageUrl(mContext,url,((ViewHolder2Notifacation) holder).ivShow);
            }

            ((ViewHolder2Notifacation) holder).tvContent.setText(bean.getTitle());

            ((ViewHolder2Notifacation) holder).rlNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.doWhenNewsOrActivityClicked(TYPE_NOTIFY, bean.getHtmlUrl(), bean.getTitle());
                }
            });
            if(AppApplication.systemLanguage == 1){
                ((ViewHolder2Notifacation) holder).scenicxiu_news.setImageResource(R.drawable.scenicxiu_news);
                ((ViewHolder2Notifacation) holder).notifacation_tip.setImageResource(R.drawable.notifacation_tip);
            }else{
                ((ViewHolder2Notifacation) holder).scenicxiu_news.setImageResource(R.drawable.scenicxiu_news_en);
                ((ViewHolder2Notifacation) holder).notifacation_tip.setImageResource(R.drawable.notifacation_tip_en);
            }
        }
        //发帖类型
        else if (viewType == TYPE_MAKE_POST) {
            final ScenicXiuBean bean = mDatas.get(position);

            ((ViewHolder4MakePost) holder).tvPublisherName.setText(bean.getAuthor());
            ((ViewHolder4MakePost) holder).tvPublishTime.setText(bean.getTimeShow());

            ((ViewHolder4MakePost) holder).tvCommentNum.setText(mContext.getString(R.string.xxx_comments, bean.getCommentCount() + ""));
            ((ViewHolder4MakePost) holder).tvPraiseNum.setText(mContext.getString(R.string.xxx_praise, bean.getLaudCount() + ""));

            if (bean.getCommentCount() == 0) {
                ((ViewHolder4MakePost) holder).tvCommentNum.setVisibility(View.GONE);
            }else {
                ((ViewHolder4MakePost) holder).tvCommentNum.setVisibility(View.VISIBLE);
            }

            if (bean.getLaudCount() == 0) {
                ((ViewHolder4MakePost) holder).tvPraiseNum.setVisibility(View.GONE);
            }else {
                ((ViewHolder4MakePost) holder).tvPraiseNum.setVisibility(View.VISIBLE);
            }

            if (!StringUtils.isEmpty(bean.getAuthorImg())) {
                String url = bean.getAuthorImg();
                if(url.contains("https:"))
                    url = url.replaceFirst("s","");
                Glide.with(mContext).load(url).transform(new CircleTransform(mContext)).placeholder(R.drawable.professor_default).into(((ViewHolder4MakePost) holder).civPublisherIcon);
            } else {
                ((ViewHolder4MakePost) holder).civPublisherIcon.setImageResource(R.drawable.professor_default);
            }
            ((ViewHolder4MakePost) holder).tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doComment(1, position, bean.getSceneShowId(), "", "", -1, ((ViewHolder4MakePost) holder).llMoreOperationContainer);
                }
            });

            if (bean.getIsLaud() == 1) {
                ((ViewHolder4MakePost) holder).tvPraiseNum.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praised);
                praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                ((ViewHolder4MakePost) holder).tvPraiseNum.setCompoundDrawables(praiseDrawable, null, null, null);
            }else {
                ((ViewHolder4MakePost) holder).tvPraiseNum.setTextColor(mContext.getResources().getColor(R.color.gray));
                Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praise);
                praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                ((ViewHolder4MakePost) holder).tvPraiseNum.setCompoundDrawables(praiseDrawable, null, null, null);
            }

            String content = "";
            try {
                content = URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8);
                content = URLDecoder.decode(content, Constants.ENCODING_UTF8);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(TextUtils.isEmpty(content)) {
                ((ViewHolder4MakePost) holder).tvContent.setVisibility(View.GONE);
            }else {
                ((ViewHolder4MakePost) holder).tvContent.setVisibility(View.VISIBLE);
                ((ViewHolder4MakePost) holder).tvContent.setText(content);
            }

            ((ViewHolder4MakePost) holder).gridViewPics.setVisibility(View.GONE);
            //不为空，说明有图片，至少一张
            if (!StringUtils.isEmpty(bean.getImgUrls())) {
                ((ViewHolder4MakePost) holder).gridViewPics.setVisibility(View.VISIBLE);
                final String[] strPics = bean.getImgUrls().split(",");

                ((ViewHolder4MakePost) holder).gridViewPics.setAdapter(new ScenicXiuGridAdapter(strPics, mContext));
                ((ViewHolder4MakePost) holder).gridViewPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ScenicXiuPicsViewpagerActivity.startViewPagerActivity(mContext, strPics, position);
                    }
                });
            }else {
                ((ViewHolder4MakePost) holder).gridViewPics.setVisibility(View.GONE);
            }

            ((ViewHolder4MakePost) holder).llMoreOperationContainer.setVisibility(View.GONE);
            ((ViewHolder4MakePost) holder).ivMoreOperationClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doWhenMoreViewClick(((ViewHolder4MakePost) holder).llMoreOperationContainer);
                }
            });

            ((ViewHolder4MakePost) holder).tvPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doPraise(((ViewHolder4MakePost) holder).tvPraiseNum, position, bean.getSceneShowId(), AppApplication.userId, AppApplication.userType);
                    ((ViewHolder4MakePost) holder).llMoreOperationContainer.setVisibility(View.GONE);
                }
            });

            //评论列表 适配数据
            ((ViewHolder4MakePost) holder).lvComments.setDividerHeight(0);
            ((ViewHolder4MakePost) holder).lvComments.setAdapter(new ScenicXiuCommentAdapter(mContext, bean.getCommentArray()));

            ((ViewHolder4MakePost) holder).lvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int commentPosition, long id) {
                    CommentArrayBean commentBean = bean.getCommentArray().get(commentPosition);
                    doComment(2, position, bean.getSceneShowId(), commentBean.getUserId() + "", commentBean.getUserName(), commentBean.getCommentId(), ((ViewHolder4MakePost) holder).llMoreOperationContainer);
                }
            });
        } else if (viewType == TYPE_COMPANY_ACTIVITY) {
            //企业活动类型
            final ScenicXiuBean bean = mDatas.get(position);

            //设置发布者名字
            ((ViewHolder5CompanyActivitys) holder).tvPublisherName.setText(bean.getAuthor());
            //设置
            ((ViewHolder5CompanyActivitys) holder).tvPublishTime.setText(bean.getTimeShow());

            //评论以及点赞
            ((ViewHolder5CompanyActivitys) holder).tvCommentNum.setText(mContext.getString(R.string.xxx_comments, bean.getCommentCount() + ""));
            ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setText(mContext.getString(R.string.xxx_praise, bean.getLaudCount() + ""));
            if (bean.getCommentCount() == 0) {
                ((ViewHolder5CompanyActivitys) holder).tvCommentNum.setVisibility(View.GONE);
            }else {
                ((ViewHolder5CompanyActivitys) holder).tvCommentNum.setVisibility(View.VISIBLE);
            }
            if (bean.getLaudCount() == 0) {
                ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setVisibility(View.GONE);
            }else {
                ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setVisibility(View.VISIBLE);
            }

            if (bean.getIsLaud() == 1) {
                ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praised);
                praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setCompoundDrawables(praiseDrawable, null, null, null);
            }else {
                ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setTextColor(mContext.getResources().getColor(R.color.gray));
                Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praise);
                praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setCompoundDrawables(praiseDrawable, null, null, null);
            }

            //头像
            if (!StringUtils.isEmpty(bean.getAuthorImg())) {
                String url = bean.getAuthorImg();
                if(url.contains("https:"))
                    url = url.replaceFirst("s","");
                Glide.with(mContext).load(url).transform(new CircleTransform(mContext)).placeholder(R.drawable.professor_default).into(((ViewHolder5CompanyActivitys) holder).civPublisherIcon);
            }

            //评论操作
            ((ViewHolder5CompanyActivitys) holder).tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doComment(1, position, bean.getSceneShowId(), "", "", -1, ((ViewHolder5CompanyActivitys) holder).llMoreOperationContainer);
                }
            });

            //点赞操作
            ((ViewHolder5CompanyActivitys) holder).tvPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doPraise(((ViewHolder5CompanyActivitys) holder).tvPraiseNum, position, bean.getSceneShowId(), AppApplication.userId, AppApplication.userType);
                    ((ViewHolder5CompanyActivitys) holder).llMoreOperationContainer.setVisibility(View.GONE);
                }
            });


            String content = "";
            try {
                content = URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8);
                content = URLDecoder.decode(content, Constants.ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ((ViewHolder5CompanyActivitys) holder).tvContent.setText(content);

            ((ViewHolder5CompanyActivitys) holder).gridViewPics.setVisibility(View.GONE);
            //不为空，说明有图片，至少一张
            if (!StringUtils.isEmpty(bean.getImgUrls())) {
                ((ViewHolder5CompanyActivitys) holder).gridViewPics.setVisibility(View.VISIBLE);
                final String[] strPics = bean.getImgUrls().split(",");
                ((ViewHolder5CompanyActivitys) holder).gridViewPics.setAdapter(new ScenicXiuGridAdapter(strPics, mContext));

                ((ViewHolder5CompanyActivitys) holder).gridViewPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ScenicXiuPicsViewpagerActivity.startViewPagerActivity(mContext, strPics, position);
                    }
                });
            }
            ((ViewHolder5CompanyActivitys) holder).llMoreOperationContainer.setVisibility(View.GONE);
            ((ViewHolder5CompanyActivitys) holder).ivMoreOperationClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doWhenMoreViewClick(((ViewHolder5CompanyActivitys) holder).llMoreOperationContainer);
                }
            });

            //评论列表 适配数据
            ((ViewHolder5CompanyActivitys) holder).lvComments.setDividerHeight(0);
            ((ViewHolder5CompanyActivitys) holder).lvComments.setAdapter(new ScenicXiuCommentAdapter(mContext, mDatas.get(position).getCommentArray()));
        } else if (viewType == TYPE_QUESTION) {
            //提问只有点赞，没有评论
            final ScenicXiuBean bean = mDatas.get(position);
            try {
                String question = URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8);
                question = URLDecoder.decode(question, Constants.ENCODING_UTF8);
                ((ViewHolder6Question) holder).tvQuestion.setText(question + "@" + bean.getAnswerUserName());
                CommentUtils.addAnswerLinks(mContext.getResources().getColor(R.color.theme_color), "@" + bean.getAnswerUserName(), ((ViewHolder6Question) holder).tvQuestion);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!StringUtils.isEmpty(bean.getAuthorImg())) {
                String url = bean.getAuthorImg();
                if(url.contains("https:"))
                    url = url.replaceFirst("s","");
                Glide.with(mContext).load(url).transform(new CircleTransform(mContext)).placeholder(R.drawable.professor_default).into(((ViewHolder6Question) holder).civAuthor);
            }

            ((ViewHolder6Question) holder).tvTime.setText(bean.getTimeShow());

            try {
                String answer = URLDecoder.decode(bean.getAnswerContent(), Constants.ENCODING_UTF8);
                answer = URLDecoder.decode(answer, Constants.ENCODING_UTF8);
                ((ViewHolder6Question) holder).tvAnswer.setText(bean.getAnswerUserName() + ":" + answer);
                CommentUtils.addLinks(mContext.getResources().getColor(R.color.theme_color), bean.getAnswerUserName(), ((ViewHolder6Question) holder).tvAnswer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bean.getLaudCount() == 0) {
                ((ViewHolder6Question) holder).tvPraiseNum.setVisibility(View.INVISIBLE);
            } else {
                ((ViewHolder6Question) holder).tvPraiseNum.setText(mContext.getString(R.string.xxx_praise, bean.getLaudCount() + ""));
            }

            if (bean.getIsLaud() == 1) {
                ((ViewHolder6Question) holder).tvPraiseNum.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praised);
                praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                ((ViewHolder6Question) holder).tvPraiseNum.setCompoundDrawables(praiseDrawable, null, null, null);
            }else {
                ((ViewHolder6Question) holder).tvPraiseNum.setTextColor(mContext.getResources().getColor(R.color.gray));
                Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praise);
                praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                ((ViewHolder6Question) holder).tvPraiseNum.setCompoundDrawables(praiseDrawable, null, null, null);
            }

            if(!TextUtils.isEmpty(bean.getAuthorImg())) {
                String url = bean.getAuthorImg();
                if(url.contains("https:"))
                    url = url.replaceFirst("s","");
                Glide.with(mContext).load(url).transform(new CircleTransform(mContext)).placeholder(R.drawable.professor_default).into(((ViewHolder6Question) holder).civAuthor);
            }
            ((ViewHolder6Question) holder).tvAuthor.setText(bean.getAuthor());

            ((ViewHolder6Question) holder).llMoreOperationContainer.setVisibility(View.GONE);
            ((ViewHolder6Question) holder).ivMoreOperationClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doWhenMoreViewClick(((ViewHolder6Question) holder).llMoreOperationContainer);
                }
            });

            ((ViewHolder6Question) holder).tvPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doPraise(((ViewHolder6Question) holder).tvPraiseNum, position, bean.getSceneShowId(), AppApplication.userId, AppApplication.userId);
                    ((ViewHolder6Question) holder).llMoreOperationContainer.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder1News extends RecyclerView.ViewHolder {
        RelativeLayout rlNews;
        TextView tvPublisherName;
        TextView tvPublishTime;
        TextView tvContent;
        TextView tvIntroduction;
        ImageView ivShow,scenicxiu_news,news_tips;

        public ViewHolder1News(View view) {
            super(view);
            rlNews = (RelativeLayout) view.findViewById(R.id.rl_news);
            ivShow = (ImageView) view.findViewById(R.id.iv_show);
            scenicxiu_news = (ImageView) view.findViewById(R.id.scenicxiu_news);
            news_tips = (ImageView) view.findViewById(R.id.news_tips);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
            tvIntroduction = (TextView) view.findViewById(R.id.tv_publish_introduction);
        }
    }

    class ViewHolder2Notifacation extends RecyclerView.ViewHolder {
        RelativeLayout rlNotify;
        TextView tvPublisherName;
        TextView tvPublishTime;
        TextView tvContent;
        TextView tvIntroduction;
        ImageView ivShow,scenicxiu_news,notifacation_tip;

        public ViewHolder2Notifacation(View view) {
            super(view);
            rlNotify = (RelativeLayout) view.findViewById(R.id.rl_notify);
            ivShow = (ImageView) view.findViewById(R.id.iv_show);
            scenicxiu_news = (ImageView) view.findViewById(R.id.scenicxiu_news);
            notifacation_tip = (ImageView) view.findViewById(R.id.notifacation_tip);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
            tvIntroduction = (TextView) view.findViewById(R.id.tv_publish_introduction);
        }
    }

    class ViewHolder4MakePost extends RecyclerView.ViewHolder {
        NoScrollGridView gridViewPics;
        TextView tvPublisherName;
        ImageView civPublisherIcon;
        TextView tvPublishTime;
        TextView tvContent;
        TextView tvCommentNum;
        TextView tvPraiseNum;
        ImageView ivMoreOperationClick;
        LinearLayout llMoreOperationContainer;
        TextView tvComment;
        TextView tvPraise;
        ListViewForScrollView lvComments;

        public ViewHolder4MakePost(View view) {
            super(view);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            gridViewPics = (NoScrollGridView) view.findViewById(R.id.gv_pics);
            civPublisherIcon = (ImageView) view.findViewById(R.id.civ_publisher);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
            tvCommentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            tvPraiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
            ivMoreOperationClick = (ImageView) view.findViewById(R.id.iv_operate_more);
            llMoreOperationContainer = (LinearLayout) view.findViewById(R.id.ll_more_operate_container);
            lvComments = (ListViewForScrollView) view.findViewById(R.id.lv_commets);
            tvComment = (TextView) view.findViewById(R.id.tv_comment);
            tvPraise = (TextView) view.findViewById(R.id.tv_praise);
        }
    }

    class ViewHolder5CompanyActivitys extends RecyclerView.ViewHolder {
        NoScrollGridView gridViewPics;
        TextView tvPublisherName;
        ImageView civPublisherIcon;
        TextView tvPublishTime;
        TextView tvContent;
        TextView tvCommentNum;
        TextView tvPraiseNum;
        ImageView ivMoreOperationClick;
        LinearLayout llMoreOperationContainer;
        TextView tvComment;
        TextView tvPraise;
        ListViewForScrollView lvComments;

        public ViewHolder5CompanyActivitys(View view) {
            super(view);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            gridViewPics = (NoScrollGridView) view.findViewById(R.id.gv_pics);
            civPublisherIcon = (ImageView) view.findViewById(R.id.civ_publisher);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
            tvCommentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            tvPraiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
            ivMoreOperationClick = (ImageView) view.findViewById(R.id.iv_operate_more);
            llMoreOperationContainer = (LinearLayout) view.findViewById(R.id.ll_more_operate_container);
            lvComments = (ListViewForScrollView) view.findViewById(R.id.lv_commets);
            tvComment = (TextView) view.findViewById(R.id.tv_comment);
            tvPraise = (TextView) view.findViewById(R.id.tv_praise);
        }
    }

    class ViewHolder6Question extends RecyclerView.ViewHolder {
        ImageView civAuthor;
        TextView tvAuthor;
        TextView tvQuestion;
        TextView tvTime;
        TextView tvAnswer;
        TextView tvPraiseNum;
        TextView tvPraise;
        LinearLayout llMoreOperationContainer;
        ImageView ivMoreOperationClick;

        public ViewHolder6Question(View itemView) {
            super(itemView);
            civAuthor = (ImageView) itemView.findViewById(R.id.civ_author);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author_name);
            tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
            tvPraiseNum = (TextView) itemView.findViewById(R.id.tv_praise_num);
            llMoreOperationContainer = (LinearLayout) itemView.findViewById(R.id.ll_more_operate_container);
            ivMoreOperationClick = (ImageView) itemView.findViewById(R.id.iv_operate_more);
            ivMoreOperationClick.setVisibility(Constants.SCENICXIU_PRISE_COMMENT ? View.VISIBLE : View.GONE);
            tvPraise = (TextView) itemView.findViewById(R.id.tv_praise);
        }
    }

    /**
     * 清楚所有的评论框
     */
    public void clearAllCommentArea() {
        if (mOperationViewList.size() > 0) {
            for (int i = 0; i < mOperationViewList.size(); i++) {
                mOperationViewList.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 新闻中心和展商活动的回调
     */
    public interface NewsAndActivitysListener {
        void doWhenNewsOrActivityClicked(int type, String url, String title);
    }


    /**
     * 评论 1,正常评论，2 对评论进行回复
     */
    private void doComment(int type, int position, int sceneShowId, String userId, String parentName, int commentId, View view) {
        Intent intent;
        //未登陆
        if (!AppApplication.isUserLogIn()) {
            intent = new Intent(ScenicXiuFragment.GO_TO_LOGIN_FIRST);
        } else {
            //已登录
            if (type == 1) {
                intent = new Intent(ScenicXiuFragment.COMMENT_CLICK_RECEIVED_ACTION_NORMAL);
            } else {
                intent = new Intent(ScenicXiuFragment.COMMENT_CLICK_RECEIVED_ACTION_COMMENT);
            }
            intent.putExtra(ScenicXiuFragment.BROAD_POSITION, position);
            intent.putExtra(ScenicXiuFragment.BROAD_SCENIC_XIU_ID, sceneShowId);
            intent.putExtra(ScenicXiuFragment.BROAD_COMMENT_ID, commentId);
            intent.putExtra(ScenicXiuFragment.BROAD_PARENT_NAME, parentName);
            intent.putExtra(ScenicXiuFragment.BROAD_PARENT_ID, userId);
        }

        mContext.sendBroadcast(intent);
        view.setVisibility(View.GONE);
    }

    /**
     * 点赞
     */
    private void doPraise(final View view, final int position, final int scenicShowId, final int userId, final int userType) {
        //点赞
        //未登录
        if (!AppApplication.isUserLogIn()) {
            Intent intent = new Intent(ScenicXiuFragment.GO_TO_LOGIN_FIRST);
            mContext.sendBroadcast(intent);
            return;
        }

//        ScaleAnimation scale = (ScaleAnimation) AnimationUtils.loadAnimation(mContext, R.anim.scale);
//        scale.setDuration(500);
//        view.startAnimation(scale);

        CHYHttpClientUsage.getInstanse().doSceneShowLaud(scenicShowId + "", userId + "", userType + "", new JsonHttpResponseHandler("gbk") {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());
                try {
                    int state = response.getInt("state");

                    if (state == 1) {
                        if (view.getVisibility() == View.GONE) {
                            view.setVisibility(View.VISIBLE);
                        }
                        ((TextView) view).setTextColor(mContext.getResources().getColor(R.color.theme_color));
                        Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praised);
                        praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                        ((TextView) view).setCompoundDrawables(praiseDrawable, null, null, null);
                        ((TextView) view).setText(mContext.getString(R.string.xxx_praise, response.getInt("laudCount") + ""));
                        mDatas.get(position).setLaudCount(response.getInt("laudCount"));
                        mDatas.get(position).setIsLaud(1);
                    } else {
                        String tips = response.getString("msg");
                        if (!StringUtils.isEmpty(tips))
                            ToastUtils.showShorToast(tips);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showShorToast("服务器开小差了，请稍后重试");
            }
        });
    }

    private void doWhenMoreViewClick(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_from_left_right);
            view.startAnimation(animation);
            view.setVisibility(View.INVISIBLE);
            mOperationViewList.remove(view);
        } else {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_from_right_left);
            view.startAnimation(animation);
            view.setVisibility(View.VISIBLE);
            mOperationViewList.add(view);
        }
    }
}