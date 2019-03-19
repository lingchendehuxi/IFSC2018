package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.LiveInfoBean;
import com.android.incongress.cd.conference.fragments.live.LiveListInfoFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.LiveForOrderInfoBean;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LiveDetailListAdapter extends BaseAdapter {
    private List<LiveForOrderInfoBean> mBeans;
    private Context mContext;
    private LiveListInfoFragment fragment;

    public LiveDetailListAdapter(Context ctx, List<LiveForOrderInfoBean> beans, LiveListInfoFragment fragment) {
        this.mContext = ctx;
        this.mBeans = beans;
        this.fragment = fragment;
    }

    @Override
    public Object getItem(int i) {
        return mBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final MViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_live_detail_info_order, viewGroup, false);
            holder = new MViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (MViewHolder) view.getTag();
        }
        final LiveForOrderInfoBean bean = mBeans.get(position);
        StringUtils.setTextShow(holder.tv_title, bean.getSessionGroupName());
        ArrayList<?> list1 = (ArrayList<?>) bean.getZcrArray();
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String jsonString = gson.toJson(list1);
        holder.tv_holder.setText(StringUtils.getNewString(jsonString));
        holder.tv_time.setText(bean.getSessionDay() + " " + bean.getTime());
        holder.ll_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderLive(bean.getSessionGroupId());
                holder.tv_order.setText(mContext.getString(R.string.orderedLive));
                holder.ll_order.setBackground(mContext.getResources().getDrawable(R.drawable.booked_course));
                holder.ll_order.setClickable(false);
                fragment.saveBean(bean);
            }
        });
        if(ConferenceDbUtils.liveIsOrdered(bean.getSessionGroupId())){
            holder.tv_order.setText(mContext.getString(R.string.orderedLive));
            holder.ll_order.setBackground(mContext.getResources().getDrawable(R.drawable.booked_course));
            holder.ll_order.setClickable(false);
        }
        return view;
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time, tv_title, tv_holder,tv_order;
        LinearLayout ll_order;

        public MViewHolder(View view) {
            super(view);
            tv_time = view.findViewById(R.id.tv_time);
            tv_title = view.findViewById(R.id.tv_title);
            tv_holder = view.findViewById(R.id.tv_holder);
            ll_order = view.findViewById(R.id.ll_order);
            tv_order = view.findViewById(R.id.tv_order);
        }
    }

    /**
     * 直播预约
     */
    public void orderLive(int sessionId) {
        CHYHttpClientUsage.getInstanse().doGetOrderLive(sessionId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ToastUtils.showToast("预约成功");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }


}
