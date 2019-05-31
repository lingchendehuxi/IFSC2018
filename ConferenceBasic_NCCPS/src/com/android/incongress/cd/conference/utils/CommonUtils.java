package com.android.incongress.cd.conference.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class CommonUtils {

    public static View initView(Context mContext,int id){
        View view = LayoutInflater.from(mContext).inflate(id, null);
        return view;
    }
    
    public static String formatTime(int min,int max){
    	StringBuilder sb = new StringBuilder();
    	if (min<10) {
			sb.append("0");
		}
    	sb.append(min);
    	sb.append(":00-");
    	if (max<10) {
			sb.append("0");
		}
    	sb.append(max);
    	sb.append(":00");
    	return sb.toString();
    }
    public static String getToday(){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//时间格式
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    	String str=formatter.format(curDate);
    	return str;
    }
    public static String getTime(){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    	String str=formatter.format(curDate);
    	return str;
    }
    
    public static String fortmatDate(Date date){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式
    	String str=formatter.format(date);
    	return str;
    }
    
    public static String getTimeFromformat(String format){
    	SimpleDateFormat formatter = new SimpleDateFormat(format);//时间格式
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    	String str=formatter.format(curDate);
    	return str;
    }
    
    public static String formatTime(String time){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	Date datatime;
		try {
			datatime = formatter.parse(time);
			final String dateFormat = AppApplication.getContext().getString(R.string.month_day_year);
	        return DateFormat.format(dateFormat, datatime).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return "";
    }
    public static String formatTimeYueRi(String time){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	Date datatime;
		try {
			datatime = formatter.parse(time);
			final String dateFormat = AppApplication.getContext().getString(R.string.month_day_no_year);
	        return DateFormat.format(dateFormat, datatime).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
//    	String times[]=time.split("-");
//    	StringBuilder sb = new StringBuilder();
//    	if(times.length>2){
//    		int yue=Integer.parseInt(times[1]);
//    		int ri=Integer.parseInt(times[2]);
//    		sb.append(yue+"月");
//    		sb.append(ri+"日");
//    	}
		return "";
    }
	public static String CommunityTimeCompare(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 时间格式
		try {
			Date datetime = formatter.parse(time);
			Date now=new Date();
			long between=(now.getTime()-datetime.getTime())/1000;
			long day=between/(24*3600);
			long hour=between%(24*3600)/3600;
			long minute=between%3600/60;
			Context res = AppApplication.getContext();
			if(day>=1){
				if(time.length()>5){
					String value=time.substring(5);
					return  value;
				}else{
					return time;
				}
			}
/*			if(day>=1&&day<4){
				return res.getString(R.string.days_ago,day);
			}else if(day>0){
				hour=day*24+hour+minute/60;
			}else{*/
				hour=hour+minute/60;
		//	}
			if(hour>1){
				return res.getString(R.string.hours_ago,hour);
			}
			if(minute>1){
				return res.getString(R.string.mins_ago,minute);
			}else{
				return res.getString(R.string.new_ago);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
	/**
	 @param bottomNavigationBar，需要修改的 BottomNavigationBar
	 @param space 图片与文字之间的间距
	 @param imgLen 单位：dp，图片大小，应 <= 36dp
	 @param textSize 单位：dp，文字大小，应 <= 20dp

	 使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
	 代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间间距为6dp
	 **/

	public static void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize){
		Class barClass = bottomNavigationBar.getClass();
		Field[] fields = barClass.getDeclaredFields();
		for(int i = 0; i < fields.length; i++){
			Field field = fields[i];
			field.setAccessible(true);
			if(field.getName().equals("mTabContainer")){
				try{
					//反射得到 mTabContainer
					LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
					for(int j = 0; j < mTabContainer.getChildCount(); j++){
						//获取到容器内的各个Tab
						View view = mTabContainer.getChildAt(j);
						//获取到Tab内的各个显示控件
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
						FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
						container.setLayoutParams(params);
						container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));

						//获取到Tab内的文字控件
						TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
						//计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
						labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
						labelView.setIncludeFontPadding(false);
						labelView.setPadding(0,0,0,dip2px(20-textSize - space/2));

						//获取到Tab内的图像控件
						ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
						//设置图片参数，其中，MethodUtils.dip2px()：换算dp值
						params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
						params.setMargins(0,0,0,space/2);
						params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
						iconView.setLayoutParams(params);
					}
				} catch (IllegalAccessException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static int dip2px(float dpValue) {
		final float scale = AppApplication.getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
