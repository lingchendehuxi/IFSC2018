package com.android.incongress.cd.conference.widget.popup;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.AllRoomsAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.AllRooms;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/12/15.
 * Now Or Next 模块中的会议时选择
 */

public class ChooseRomPopupWindow extends BasePopupWindow {
    private View mPopupView;

    /**
     * 会议室集合
     **/
    private RecyclerView mTflRooms;
    private LinearLayout mRoomsSelection;
    private TextView mRoomsDetermine,mSelectionText;
    private ImageView mSelectionSelect;
    private AllRoomsAdapter mRoomAdapter;
    private List<Class> mAllClasses;
    private List<AllRooms> mAllRooms = new ArrayList<>();
    private boolean tag = false;
    /** 当前选中的会议室 **/
    private List<Class> mChooseClasses = new ArrayList<>();

    public List<Class> getCurrentClass() {
        /*Set<Integer> positions = mTflRooms.getSelectedList();


        for(Integer i: positions) {
            mChooseClasses.add(mAllClasses.get(i));
        }*/
        mChooseClasses.clear();
        for(int i = 0;i<mAllRooms.size();i++){
            if(mAllRooms.get(i).isSelect()){
                mChooseClasses.add(mAllClasses.get(i));
            }
        }
        return mChooseClasses;
    }

    public ChooseRomPopupWindow(Activity activity) {
        super(activity);

        mTflRooms = (RecyclerView) mPopupView.findViewById(R.id.rooms_view);
        mRoomsSelection = (LinearLayout) mPopupView.findViewById(R.id.rooms_selection);
        mRoomsDetermine = (TextView) mPopupView.findViewById(R.id.rooms_determine);
        mSelectionText = (TextView) mPopupView.findViewById(R.id.selection_text);
        mSelectionSelect = (ImageView) mPopupView.findViewById(R.id.selection_select);
        mAllClasses = ConferenceDbUtils.getAllClasses();
        for (int i = 0;i<mAllClasses.size();i++){
            Class classBean = mAllClasses.get(i);
            AllRooms bean = new AllRooms();
            bean.setSelect(tag);
            bean.setClassesId(classBean.getClassesId());
            bean.setClassesCode(classBean.getClassesCode());
            bean.setClassesCapacity(classBean.getClassesCapacity());
            bean.setClassesLocation(classBean.getClassesLocation());
            bean.setConferencesId(classBean.getConferencesId());
            bean.setLevel(classBean.getLevel());
            bean.setMapName(classBean.getMapName());
            bean.setClassCodeEn(classBean.getClassCodeEn());
            mAllRooms.add(bean);
        }
        getState();
        if (mAllRooms != null && mAllRooms.size() > 0) {
            mRoomAdapter = new AllRoomsAdapter(mContext, mAllRooms);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mTflRooms.setLayoutManager(mLayoutManager);
            mTflRooms.setItemAnimator(new DefaultItemAnimator());
            mTflRooms.setAdapter(mRoomAdapter);

            mRoomAdapter.SetOnItemClickListener(new AllRoomsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mAllRooms.get(position).setSelect(!mAllRooms.get(position).isSelect());
                    mRoomAdapter.notifyDataSetChanged();
                    getState();
                }
            });

           /* mTflRooms.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    return false;
                }
            });*/
            mRoomsDetermine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(getCurrentClass().size()==0){
                       ToastUtils.showLongToast("请选择至少一个会议室");
                   }else{
                       SharePreferenceUtils.saveAppBoolean("popup",true);
                       dismiss();
                   }
                }
            });
            mRoomsSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0 ;i<mAllRooms.size();i++){
                        mAllRooms.get(i).setSelect(!tag);
                    }
                    tag = !tag;
                    getState();
                    mRoomAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void getState(){
        boolean selection = true;
        for (int i = 0 ;i<mAllRooms.size();i++){
            if(mAllRooms.get(i).isSelect()){
                selection = true;
            }else{
                selection = false;
                break;
            }
        }
        if(selection){
            mSelectionSelect.setImageResource(R.drawable.room_selected);
            mSelectionText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_room_tag_selected));
            mSelectionText.setTextColor(mContext.getResources().getColor(R.color.alpha_theme_color));
        }else{
            mSelectionSelect.setImageResource(R.drawable.room_unselected);
            mSelectionText.setTextColor(Color.GRAY);
            mSelectionText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_room_tag_normal));
        }
    }
    @Override
    protected Animation getShowAnimation() {
        return getDefaultAlphaInAnimation();
    }

    @Override
    public Animation getExitAnimation() {
        return getDefaultAlphaOutAnimation();
    }

    @Override
    protected View getClickToDismissView() {
        return mPopupView.findViewById(R.id.ll_all);
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_choose_rooms);
    }

    @Override
    public View getAnimaView() {
        return mPopupView;
    }

    @Override
    public View getPopupViewById(int resId) {
        mPopupView = LayoutInflater.from(mContext).inflate(resId, null);
        return mPopupView;
    }
}

