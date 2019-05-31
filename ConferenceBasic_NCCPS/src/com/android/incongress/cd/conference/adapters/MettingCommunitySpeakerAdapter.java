package com.android.incongress.cd.conference.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.MySectionIndexer;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView.PinnedHeaderAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;


public class MettingCommunitySpeakerAdapter extends BaseAdapter implements PinnedHeaderAdapter, OnScrollListener {
	private List<Speaker> mList;
	private MySectionIndexer mIndexer;
	private int mLocationPosition = -1;
	private int isSearching = View.VISIBLE;
	private String ag;

	public MettingCommunitySpeakerAdapter(List<Speaker> mList, MySectionIndexer mIndexer) {
		this.mIndexer = mIndexer;
		this.mList = mList;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.social_item);

			holder = new ViewHolder();
			holder.group_title = (TextView) convertView.findViewById(R.id.hysqhome_jiangzhe_group_title);
			holder.name = (TextView) convertView.findViewById(R.id.hysqhome_jiangzhe_textItem);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		Speaker bean = mList.get(position);
		ag = bean.getFirstLetter();
		int section = mIndexer.getSectionForPosition(position);
		if (mIndexer.getPositionForSection(section) == position) {
			holder.group_title.setVisibility(isSearching);
			holder.group_title.setText(bean.getFirstLetter());
		} else {
			holder.group_title.setVisibility(View.GONE);
		}

		if(AppApplication.systemLanguage == 1) {
			holder.name.setText(bean.getSpeakerName());
		}else {
			holder.name.setText(bean.getEnName());
		}

		return convertView;
	}

	public static class ViewHolder {
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
		try {
			int realPosition = position;
			int section = mIndexer.getSectionForPosition(realPosition);
			String title = (String) mIndexer.getSections()[section];
			((TextView) header.findViewById(R.id.hysqhome_jiangzhe_group_textItem)).setText(title);
		}catch (Exception e){
			e.printStackTrace();
		}
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
}
