package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.bumptech.glide.Glide;
import com.mobile.incongress.cd.conference.basic.csccm.R;


public class CardFragment extends BaseFragment {

    private CardView mCardView;
    private String mMapUrl,mMapName;
    private ImageView mMapImg,mMapOnclick;
    private int currentPostion;

    public static final CardFragment getInstance(String path, String name,int currentPositon) {
        CardFragment fragment = new CardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imgpath", path);
        bundle.putString("imgname",name);
        bundle.putInt("currentPositon",currentPositon);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapUrl = AppApplication.instance().getSDPath() + Constants.FILESDIR + getArguments().getString("imgpath");
        mMapName = getArguments().getString("imgname");
        currentPostion = getArguments().getInt("currentPositon");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        mCardView = (CardView) view.findViewById(R.id.cardView);
        mMapImg = (ImageView) view.findViewById(R.id.map_img);
        mMapOnclick = (ImageView) view.findViewById(R.id.map_onclick);

        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * CardAdapter.MAX_ELEVATION_FACTOR);
        PicUtils.loadImageUrl(getContext(),mMapUrl,mMapImg);
        mMapOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingGuideRoomMapViewPage fragment = new MeetingGuideRoomMapViewPage();
                Bundle bundle = new Bundle();
                bundle.putInt("currentPosition",currentPostion);
                fragment.setArguments(bundle);
                action(fragment, null);
            }
        });
        return view;
    }

    public CardView getCardView() {
        return mCardView;
    }
}
