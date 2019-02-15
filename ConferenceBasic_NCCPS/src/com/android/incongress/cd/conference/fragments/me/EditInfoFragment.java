package com.android.incongress.cd.conference.fragments.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.widget.ClearEditText;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class EditInfoFragment extends BaseFragment {
    private ClearEditText et_temp;
    private TextView title_text;
    private ImageView title_back;
    private static String TEMP_STRING = "temp";
    private static String TEMP_TITLE = "temp_title";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.edit_info_fragment, null);
        et_temp = view.findViewById(R.id.et_temp);
        title_text = view.findViewById(R.id.title_text);
        title_back = view.findViewById(R.id.title_back);
        title_text.setText(getArguments().getString(TEMP_TITLE));
        et_temp.setText(getArguments().getString(TEMP_STRING));
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).performBackClick();
            }
        });
        return view;
    }

    public static EditInfoFragment getInstance(String temp1,String temp2) {
        EditInfoFragment fragment = new EditInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEMP_TITLE, temp1);
        bundle.putString(TEMP_STRING, temp2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
