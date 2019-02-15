package com.android.incongress.cd.conference.widget.refresh_view;

public class XRefreshHolder {

	public int mOffsetY;

	public void move(int deltaY) {
		mOffsetY += deltaY;
	}

	public boolean hasHeaderPullDown() {
		return mOffsetY > 0;
	}

	public boolean hasFooterPullUp() {
		return mOffsetY < 0;
	}
	public boolean isOverHeader(int deltaY){
		return mOffsetY<-deltaY;
		
	}
}
