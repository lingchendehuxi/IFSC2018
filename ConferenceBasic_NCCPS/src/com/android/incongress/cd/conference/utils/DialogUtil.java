package com.android.incongress.cd.conference.utils;


import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.incongress.cd.conference.widget.LWheelDialog;
import com.android.incongress.cd.conference.widget.LWheelView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class DialogUtil {

	private double dialogWidthProportion = 0.7;
	private double dialogHeightProportion = 0.4;
	private LWheelDialog dialog;
	/**
	 * 获取一个时间滚轮选择器
	 * @param context
	 * @param type
	 * @param listener
	 * @return
	 */
	public Dialog getWheelDialog(Activity context,LWheelDialog.LWheelDialogType type,LWheelView.LWheelViewListener listener, LWheelDialog.OnCheckedListener onCheckedListener){
		dialog = new LWheelDialog(context, type, listener);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.dir_popupwindow_anim); // 添加动画
		dialog.setCancelable(true);
		dialog.setCheckedListener(onCheckedListener);
		dialog.show();

		WindowManager m = context.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.5);
		p.width = (int) (d.getWidth());
		dialog.onWindowAttributesChanged(p);
		window.setAttributes(p);
		return dialog;
	}
	public void diss(){
		dialog.dismiss();
	}
	public DialogUtil(double dialogWidthProportion,
					  double dialogHeightProportion) {
		super();
		this.dialogWidthProportion = dialogWidthProportion;
		this.dialogHeightProportion = dialogHeightProportion;
	}

	public DialogUtil() {
		super();
	}

	public double getDialogWidthProportion() {
		return dialogWidthProportion;
	}

	public void setDialogWidthProportion(double dialogWidthProportion) {
		this.dialogWidthProportion = dialogWidthProportion;
	}

	public double getDialogHeightProportion() {
		return dialogHeightProportion;
	}

	public void setDialogHeightProportion(double dialogHeightProportion) {
		this.dialogHeightProportion = dialogHeightProportion;
	}

	public void setWidthAndHeight(double width, double height) {
		this.dialogHeightProportion = height;
		this.dialogWidthProportion = width;
	}

}
