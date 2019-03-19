package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Map;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by Admin on 2018/6/8.
 */

public class NewMeetingInfoFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener{

    private ViewPager mViewPager;
    private LinearLayout ll_dots;
    private ArrayList<ImageView> dotsList;
    private TextView nMapName;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.searchfragmentnew, null);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        nMapName = (TextView) view.findViewById(R.id.map_name);
        ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);
        // 初始化小点
        initDots();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                dpToPixels(2, getActivity()));
        Map bean = ConferenceDbUtils.getAllMaps().get(0);
        String mapName = "";
        if(bean.getMapRemark().contains("#@#")){
            if (AppApplication.systemLanguage == 1) {
                mapName = bean.getMapRemark().split("#@#")[0];
            }else{
                mapName = bean.getMapRemark().split("#@#")[1];
            }
        }else{
            mapName = bean.getMapRemark();
        }
        nMapName.setText(mapName);
        //mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
        //mCardShadowTransformer.enableScaling(true);
        mFragmentCardShadowTransformer.enableScaling(true);
        //mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(true, mFragmentCardShadowTransformer);
        // mViewPager.setPageTransformer(true, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mFragmentCardAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Map bean = ConferenceDbUtils.getAllMaps().get(position);
                String mapName = "";
                if(bean.getMapRemark().contains("#@#")){
                    if (AppApplication.systemLanguage == 1) {
                        mapName = bean.getMapRemark().split("#@#")[0];
                    }else{
                        mapName = bean.getMapRemark().split("#@#")[1];
                    }
                }else{
                    mapName = bean.getMapRemark();
                }
                nMapName.setText(mapName);
                for (int i = 0; i < ConferenceDbUtils.getAllMaps().size(); i++) {
                    //判断小点点与当前的图片是否对应，对应设置为亮色 ，否则设置为暗色
                    if (i == position % ConferenceDbUtils.getAllMaps().size()) {
                        dotsList.get(i).setImageDrawable(
                                getResources().getDrawable(
                                        R.drawable.dots_focuse));
                    } else {
                        dotsList.get(i).setImageDrawable(
                                getResources().getDrawable(
                                        R.drawable.dots_normal));
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }
    private void initDots() {
        //创建存放小点点的集合
        dotsList = new ArrayList<ImageView>();
        //每次初始化之前清空集合
        dotsList.clear();
        // 每次初始化之前  移除  布局中的所有小点
        ll_dots.removeAllViews();
        for (int i = 0; i < ConferenceDbUtils.getAllMaps().size(); i++) {
            //创建小点点图片
            ImageView imageView = new ImageView(getActivity());
            Drawable drawable = null;
            if (i == 0) {
                // 亮色图片
                drawable = getResources().getDrawable(R.drawable.dots_focuse);

            } else {
                drawable = getResources().getDrawable(R.drawable.dots_normal);
            }
            imageView.setImageDrawable(drawable);
            // 考虑屏幕适配
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(getActivity(), 10), dip2px(getActivity(), 10));
            //设置小点点之间的间距
            params.setMargins(dip2px(getActivity(), 5), 0, dip2px(getActivity(), 5), 0);
            //将小点点添加大线性布局中
            ll_dots.addView(imageView, params);
            // 将小点的控件添加到集合中
            dotsList.add(imageView);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MEETINGMAPINFO);
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MEETINGMAPINFO);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
