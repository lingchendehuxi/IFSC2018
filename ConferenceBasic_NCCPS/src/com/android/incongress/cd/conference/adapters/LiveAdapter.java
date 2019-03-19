package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.LiveClassBean;
import com.android.incongress.cd.conference.beans.LiveInfoBean;
import com.android.incongress.cd.conference.beans.LiveListInfoBean;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LiveInfoBean> mBeans;
    private Context mContext;
    private static int IS_BEGIN = 0;
    private static int NO_BEGIN = 1;
    private itemOnclick itemOnclick;

    public LiveAdapter(Context ctx, List<LiveInfoBean> beans, itemOnclick itemOnclick) {
        this.mContext = ctx;
        this.mBeans = beans;
        this.itemOnclick = itemOnclick;
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (DateUtil.isStart(mBeans.get(position).getStartTime())) {
            return IS_BEGIN;
        } else {
            return NO_BEGIN;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView;
        if (viewType == NO_BEGIN) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_live_order_video, parent, false);
            return new MViewHolder(convertView);
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_live_play_video, parent, false);
            return new MViewHolder(convertView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final LiveInfoBean bean = mBeans.get(position);
        if (holder.getItemViewType() == NO_BEGIN) {
            if (TextUtils.isEmpty(bean.getStartTime())) {
                ToastUtils.showToast("直播时间格式错误，请联系指导员");
            } else {
                Long[] strings = DateUtil.getRemainsTime(bean.getStartTime());
                ((MViewHolder) holder).tv_day.setText(longOverString(strings[0]));
                ((MViewHolder) holder).tv_hour.setText(longOverString(strings[1]));
                ((MViewHolder) holder).tv_minute.setText(longOverString(strings[2]));
            }
            if (bean.getLookCount() == 0) {
                ((MViewHolder) holder).ll_person.setVisibility(View.GONE);
            } else {
                ((MViewHolder) holder).ll_person.setVisibility(View.VISIBLE);
                ((MViewHolder) holder).tv_person.setText(String.valueOf(bean.getLookCount()));
            }
            //验证是否已经预约
            if (ConferenceDbUtils.liveIsOrdered(bean.getSessionId())) {
                ((MViewHolder) holder).tv_order.setText(mContext.getString(R.string.orderedLive));
                ((MViewHolder) holder).ll_order.setBackground(mContext.getResources().getDrawable(R.drawable.booked_course));
                ((MViewHolder) holder).ll_order.setClickable(false);
            } else {
                ((MViewHolder) holder).tv_order.setText(mContext.getString(R.string.orderLive));
                ((MViewHolder) holder).ll_order.setBackground(mContext.getResources().getDrawable(R.drawable.book_course));
                ((MViewHolder) holder).ll_order.setClickable(true);
            }
        }
        StringUtils.setTextShow(((MViewHolder) holder).tv_address, bean.getLiveClassName());
        StringUtils.setTextShow(((MViewHolder) holder).tv_title, bean.getSessionName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnclick.onItemClick(position);
            }
        });

    }

    class MViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address, tv_title, tv_person, tv_day, tv_hour, tv_minute, tv_order;
        LinearLayout ll_person, ll_order;

        public MViewHolder(View view) {
            super(view);
            tv_address = view.findViewById(R.id.tv_address);
            tv_title = view.findViewById(R.id.tv_title);
            tv_person = view.findViewById(R.id.tv_person);
            tv_day = view.findViewById(R.id.tv_day);
            tv_hour = view.findViewById(R.id.tv_hour);
            tv_minute = view.findViewById(R.id.tv_minute);
            ll_order = view.findViewById(R.id.ll_order);
            ll_person = view.findViewById(R.id.ll_person);
            tv_order = view.findViewById(R.id.tv_order);
        }
    }

    /**
     * 直播预约
     */
    private void orderLive(int sessionId, final LiveInfoBean bean) {
        CHYHttpClientUsage.getInstanse().doGetOrderLive(sessionId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (JSONCatch.parseInt("state", response) == 1) {
                    ToastUtils.showToast("预约成功");
                } else {
                    ToastUtils.showToast("预约失败，请联系管理员");
                    return;
                }
                bean.save();
                //添加闹钟提醒
                Alert alertbean = new Alert();
                if (!TextUtils.isEmpty(bean.getStartTime()) && !TextUtils.isEmpty(bean.getEndTime())) {
                    String[] beginStrings = bean.getStartTime().split(" ");
                    String[] endStrings = bean.getEndTime().split(" ");
                    alertbean.setDate(beginStrings[0]);
                    alertbean.setStart(beginStrings[1]);
                    alertbean.setEnd(endStrings[1]);
                } else {
                    ToastUtils.showToast("直播时间不正确，请联系管理员");
                    return;
                }
                alertbean.setEnable(1);
                alertbean.setRelativeid(String.valueOf(bean.getSessionId()));
                alertbean.setRepeatdistance("5");
                alertbean.setRepeattimes("0");
                alertbean.setRoom(bean.getLiveClassName());
                alertbean.setIdenId(bean.getClassId());
                alertbean.setTitle(bean.getSessionName());
                alertbean.setLiveUrl(bean.getLiveUrl());
                alertbean.setType(AlertBean.TYPE_LIVE); //3代表直播

                alertbean.save();
                AlermClock.addClock(alertbean);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }

    private String longOverString(Long date) {
        if (date == 0) {
            return String.valueOf(date) + "0";
        } else {
            return String.valueOf(date);
        }
    }

    public interface itemOnclick {
        void onItemClick(int position);
    }
}
