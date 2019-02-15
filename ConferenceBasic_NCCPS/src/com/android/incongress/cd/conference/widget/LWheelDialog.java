package com.android.incongress.cd.conference.widget;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.mobile.incongress.cd.conference.basic.csccm.R;

public class LWheelDialog extends Dialog implements LWheelView.LWheelViewListener,View.OnClickListener {
	/**
	 * 类型
	 */
	private LWheelDialogType type = LWheelDialogType.ALL;
	/**
	 * 回掉监听
	 */
	private LWheelView.LWheelViewListener listener = null;
	/**
	 * 滚轮
	 */
	private LWheelView mWheelHourFrom, mWheelMinuteFrom, mWheelHourTo, mWheelMinuteTo;
	/**
	 * 显示
	 */
	private String hourFrom, minuteFrom,hourTo, minuteTo;

	private View esc,enter;

	public enum LWheelDialogType {
		ALL, TIME, DATE
	}

	/**
	 * 数据类型
	 * 
	 * @author Administrator
	 *
	 */
	private enum DataType {
		YEAR, MONTH, DAY, HOUR, MINUTE, SECONDS
	}

	/**
	 * 日历
	 */
	private Calendar calendar = Calendar.getInstance();

	public LWheelDialog(Context context, LWheelDialogType type, LWheelView.LWheelViewListener listener) {
		super(context);
		this.type = type;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.wheel);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		mWheelHourFrom = (LWheelView) findViewById(R.id.wheel_hour_from);
		mWheelMinuteFrom = (LWheelView) findViewById(R.id.wheel_minute_from);
		mWheelHourTo = (LWheelView) findViewById(R.id.wheel_hour_to);
		mWheelMinuteTo = (LWheelView) findViewById(R.id.wheel_minute_to);

		esc = findViewById(R.id.wheel_esc);
		enter = findViewById(R.id.wheel_enter);

		esc.setOnClickListener(this);
		enter.setOnClickListener(this);
		initWheel();
	}

	private void initWheel() {
		LWheelViewOption option = new LWheelViewOption(new LWheelViewOption.Builder().setCycle(false));
		mWheelHourFrom.setOption(option);
		mWheelHourTo.setOption(option);
		mWheelMinuteFrom.setOption(option);
		mWheelMinuteTo.setOption(option);

		mWheelHourFrom.setListener(this);
		mWheelMinuteFrom.setListener(this);
		mWheelHourTo.setListener(this);
		mWheelMinuteTo.setListener(this);

		switch (type) {
		case ALL:
			initDate();
			initTime();
			break;
		case DATE:
			initDate();
			break;
		case TIME:
			initTime();
			break;
		default:
			break;
		}
	}
	
	private void initDate(){
		changeData(DataType.YEAR);
		changeData(DataType.MONTH);
		changeData(DataType.DAY);
	}
	
	private void initTime(){
		changeData(DataType.HOUR);
//		mWheelHourFrom.selected(calendar.get(Calendar.HOUR_OF_DAY));
//		mWheelHourTo.selected(calendar.get(Calendar.HOUR_OF_DAY));
		mWheelHourFrom.selected(0);
		mWheelHourTo.selected(0);
		changeData(DataType.MINUTE);
//		mWheelMinuteFrom.selected(calendar.get(Calendar.MINUTE));
//		mWheelMinuteTo.selected(calendar.get(Calendar.MINUTE));
		mWheelMinuteFrom.selected(0);
		mWheelMinuteTo.selected(0);
		changeData(DataType.SECONDS);
	}

	private void changeData(DataType dt) {
		ArrayList<String> list = new ArrayList<String>();
		switch (dt) {
		case YEAR:
			for (int i = 0; i < 50; i++) {
				list.add(i + 1980 + "");
			}
			break;
		case MONTH:
			for (int i = 0; i < 12; i++) {
				list.add(i + 1 + "");
			}
			break;
		case DAY:
//			for (int i = 0; i < getDaysByYearMonth(calendar, Integer.parseInt(year), Integer.parseInt(month)); i++) {
//				list.add(i + 1 + "");
//			}
			break;
		case HOUR:
			for (int i = 8; i < 21; i++) {
				list.add(i + "");
			}
			mWheelHourFrom.setData(list);
			mWheelHourTo.setData(list);
			break;
		case MINUTE:
			for (int i = 0; i < 6; i++) {
				int temp = i;
				list.add(temp * 10 + "");
			}
			mWheelMinuteFrom.setData(list);
			mWheelMinuteTo.setData(list);
			break;
		case SECONDS:
			for (int i = 0; i < 60; i++) {
				list.add(i + "");
			}
			break;
		}
	}

	/**
	 * 根据年 月 获取对应的月份 天数
	 */
	public static int getDaysByYearMonth(Calendar a, int year, int month) {
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	@Override
	public void onLWheelViewChange(LWheelView view, String value, int position) {
		if (listener != null) {
			listener.onLWheelViewChange(view, value, position);
		}
		switch (view.getId()) {
			case R.id.wheel_hour_from:
				this.hourFrom = value;
				break;
			case R.id.wheel_minute_from:
				this.minuteFrom = value;
				break;
			case R.id.wheel_hour_to:
				this.hourTo = value;
				break;
			case R.id.wheel_minute_to:
				this.minuteTo = value;
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_enter:
			switch (type) {
			case ALL:
				mCheckedListener.onCheckedClicked( addZeroToTime(hourFrom) + ":" + addZeroToTime(minuteFrom) + "-" + addZeroToTime(hourTo) + ":" + addZeroToTime(minuteTo));
				break;
			case DATE:
//				Toast.makeText(getContext(), year + "年" + month + "月" + day + "日", Toast.LENGTH_SHORT).show();
				break;
			case TIME:
//				Toast.makeText(getContext(), hourFrom + ":" + minuteFrom + ":" + seconds, Toast.LENGTH_SHORT).show();
				break;
			}
			//dismiss();
			break;
		case R.id.wheel_esc:
			dismiss();
			break;
		default:
			break;
		}
	}

	public interface OnCheckedListener {
		void onCheckedClicked(String time);
	}

	private OnCheckedListener mCheckedListener;

	public void setCheckedListener(OnCheckedListener listener) {
		this.mCheckedListener = listener;
	}

	private String addZeroToTime(String time) {
		if(time.length() == 1) {
			return 0 + time;
		}else {
			return time;
		}
	}
}
