package com.android.incongress.cd.conference.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.AllUserListBean;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.MySectionIndexer;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView.PinnedHeaderAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;


public class AllSocialFriendAdapter extends BaseAdapter implements PinnedHeaderAdapter, OnScrollListener {
	private List<AllUserListBean.JsonArrayBean> mList;
	private MySectionIndexer mIndexer;
	private int mLocationPosition = -1;
	private int isSearching = View.VISIBLE;

	private OnFriendStatusClickListener mFriendStatusListener;

	public void addFriendStatusCLickListener(OnFriendStatusClickListener listener) {
		this.mFriendStatusListener = listener;
	}

	public AllSocialFriendAdapter(List<AllUserListBean.JsonArrayBean> mList, MySectionIndexer mIndexer) {
		this.mIndexer = mIndexer;
		this.mList = mList;
	}

	public List<AllUserListBean.JsonArrayBean> getData() {
		return  mList;
	}

	@Override
	public int getCount() {
		int count=mList.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.item_all_social_friend);

			holder = new ViewHolder();
			holder.group_title = (TextView) convertView.findViewById(R.id.hysqhome_jiangzhe_group_title);
			holder.name = (TextView) convertView.findViewById(R.id.hysqhome_jiangzhe_textItem);
			holder.status = (TextView) convertView.findViewById(R.id.tv_status);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final AllUserListBean.JsonArrayBean bean = mList.get(position);
		
		int section = mIndexer.getSectionForPosition(position);
		if (mIndexer.getPositionForSection(section) == position) {
			holder.group_title.setVisibility(isSearching);
			holder.group_title.setText(bean.getFirstLetter());
		} else {
			holder.group_title.setVisibility(View.GONE);
		}

		holder.status.setVisibility(View.VISIBLE);
		if(bean.getState() == 0) {
			holder.status.setText("添加");
		}else if(bean.getState() == 1 || bean.getState() == 2) {
			holder.status.setText("等待审核");
		}else {
			holder.status.setVisibility(View.GONE);
		}

		holder.status.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mFriendStatusListener != null) {
					mFriendStatusListener.onClick(bean.getState(), bean.getUserInfoId(), bean.getFriendsUserType(), position);
				}
			}
		});
		holder.name.setText(bean.getUserName());

		return convertView;
	}

	public static class ViewHolder {
		public TextView status;
		public TextView group_title;
		public TextView name;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		if (isSearching == View.GONE) {
			return PINNED_HEADER_GONE;
		}
		int realPosition = position;
		if (realPosition < 0 || (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = mIndexer.getSectionForPosition(realPosition);
		int nextSectionPosition = mIndexer.getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		if (position<0) {
			return;
		}
		int realPosition = position;
		int section = mIndexer.getSectionForPosition(realPosition);
		String title = (String) mIndexer.getSections()[section];
		((TextView) header.findViewById(R.id.hysqhome_jiangzhe_group_textItem)).setText(title);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			PinnedHeaderListView v = (PinnedHeaderListView) view;
			v.configureHeaderView(firstVisibleItem);
		}
	}

	public interface OnFriendStatusClickListener{
		void onClick(int status,int targetUserId, int targetUserType, int position);
	}

}
