package com.android.incongress.cd.conference.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.incongress.cd.conference.HomeActivity;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class BaseFragment extends Fragment {

    public MainCallBack mCallBack;

    protected ProgressDialog mProgressDialog;

    protected void showProgressBar(String msg) {
        if (getActivity() != null)
            mProgressDialog = ProgressDialog.show(getActivity(), null, msg);
    }

    protected void dismissProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public interface MainCallBack {
        void onCreateViewFinish();
    }

    public void setCallBack(MainCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCallBack != null) {
            mCallBack.onCreateViewFinish();
        }
    }

    /**
     * 显示一个信息对话框
     */
    protected void showDialog(String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, positiveListener)
                .setNegativeButton(R.string.negative_button, negativeListener).setCancelable(cancelable)
                .setMessage(message).show();
    }

    /**
     * id标题
     * @param fragment
     * @param id
     * @param adTop
     * @param adBottom
     * @param isNavShow
     */
    public void action(BaseFragment fragment, int id, boolean adTop, boolean adBottom, boolean isNavShow) {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.addFragment(this, fragment);

        activity.setTitleEntry(true, false, false, null, id, adTop, adBottom, true, isNavShow);
    }

    /**
     * String标题
     *
     * @param fragment
     * @param title
     * @param adTop
     * @param adBottom
     * @param isNavShow
     */
    public void action(BaseFragment fragment, String title, boolean adTop, boolean adBottom, boolean isNavShow) {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.addFragment(this, fragment);

        activity.setTitleEntry(true, false, false, null, title, adTop, adBottom, true, isNavShow);
    }

    /**
     * String标题+自定义右上角
     *
     * @param fragment
     * @param title
     * @param view
     * @param adTop
     * @param adBottom
     * @param isNavShow
     */
    public void action(BaseFragment fragment, String title, View view, boolean adTop, boolean adBottom, boolean isNavShow) {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.addFragment(this, fragment);

        activity.setTitleEntry(true, false, true, view, title, adTop, adBottom, true, isNavShow);
    }

    /**
     * id标题 + 自定义右上角
     * @param fragment
     * @param id
     * @param view
     * @param adTop
     * @param adBottom
     * @param isNavShow
     */
    public void action(BaseFragment fragment, int id, View view, boolean adTop, boolean adBottom, boolean isNavShow) {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.addFragment(this, fragment);

        activity.setTitleEntry(true, false, true, view, id, adTop, adBottom, true, isNavShow);
    }

    /**
     * id标题 + 自定义右上角 + 自定义标题
     * @param fragment
     * @param id
     * @param view
     * @param adTop
     * @param adBottom
     * @param customTitleView
     * @param isNavShow
     */
    public void action(BaseFragment fragment, int id, View view, boolean adTop, boolean adBottom, View customTitleView, boolean isNavShow) {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.addFragment(this, fragment);
        activity.setTitleEntry(true, false, true, view, id, adTop, adBottom, true, true, customTitleView, isNavShow);
    }

    /**
     * 回退
     */
    public void performback() {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.perfromBackPressTitleEntry();
    }

    /**
     * 隐藏输入法
     */
    public void hideShurufa() {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.hideShurufa();
    }

    public boolean toggleShurufa() {
        HomeActivity activity = (HomeActivity) getActivity();
        return activity.toggleShurufa();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (i == 0) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * 内容区域变量
     */
    protected void lightOn() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1.0f;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变暗
     */
    protected void lightOff() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.3f;
        getActivity().getWindow().setAttributes(lp);
    }

}
