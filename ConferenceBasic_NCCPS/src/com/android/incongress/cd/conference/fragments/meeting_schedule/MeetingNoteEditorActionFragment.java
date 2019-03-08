package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Note;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.IncongressEditText;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * 记笔记界面
 */
public class MeetingNoteEditorActionFragment extends BaseFragment implements OnClickListener {

    private IncongressEditText mIncongressEditText;
    private Session mSessionBean;
    private Class mClassesBean;
    private Note mNotes;

    private int mType = TYPE_NEW;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mycenter_note_editor, null);
        mIncongressEditText = (IncongressEditText) view.findViewById(R.id.mycenter_note_edior);
        if (mType == TYPE_UPDATE) {
            mIncongressEditText.setText(mNotes.getContents());
        }
        mIncongressEditText.requestFocus();
        toggleShurufa();
        return view;

    }

    @Override
    public void onClick(View v) {
        if (mType == TYPE_NEW) {
            String content = mIncongressEditText.getText().toString().trim();
            if (content == null || "".equals(content)) {
                Toast.makeText(getActivity(), R.string.metting_node_edit_no_data, Toast.LENGTH_SHORT).show();
                return;
            }
            String time = String.valueOf(System.currentTimeMillis());
            mNotes = new Note();
            mNotes.setContents(content);
            mNotes.setCreatetime(time);
            mNotes.setDate(mSessionBean.getSessionDay());
            mNotes.setEnd(mSessionBean.getEndTime());
            mNotes.setClassid(String.valueOf(mClassesBean.getClassesId()));
            mNotes.setMeetingid(String.valueOf(mSessionBean.getSessionGroupId()));
            mNotes.setRoom(mClassesBean.getClassesCode());
            mNotes.setSessionid(String.valueOf(mSessionBean.getSessionGroupId()));
            mNotes.setStart(mSessionBean.getStartTime());
            mNotes.setTitle(mSessionBean.getSessionName());
            mNotes.setUpdatetime(time);
            ConferenceDbUtils.addOneNote(mNotes);
        } else {
            mNotes.setUpdatetime(String.valueOf(System.currentTimeMillis()));
            mNotes.setContents(mIncongressEditText.getText().toString());
            ConferenceDbUtils.updateOneNote(mNotes);
        }

        ToastUtils.showToast(getString(R.string.note_save_success));
        HomeActivity activity = (HomeActivity) getActivity();
        activity.perfromBackPressTitleEntry();
        activity.hideShurufa();
    }

    public static final String BUNDLE_TYPE = "type";
    public static final String BUNDLE_SESSION_ID = "sessionID";
    public static final String BUNDLE_NOTE = "note";


    public static final int TYPE_NEW = 0;
    public static final int TYPE_UPDATE = 1;

    public static MeetingNoteEditorActionFragment getInstance(int type, int sessionId) {
        MeetingNoteEditorActionFragment editorFragment = new MeetingNoteEditorActionFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_TYPE, type);
        bundle.putInt(BUNDLE_SESSION_ID, sessionId);

        editorFragment.setArguments(bundle);
        return editorFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(BUNDLE_TYPE);
        if (mType == TYPE_NEW) {
            mSessionBean = ConferenceDbUtils.getSessionBySessionId(getArguments().getInt(BUNDLE_SESSION_ID)+"");
            mClassesBean = ConferenceDbUtils.findClassByClassId(mSessionBean.getClassesId());
        } else if (mType == TYPE_UPDATE) {
            mNotes = ConferenceDbUtils.getNoteBySessionId(getArguments().getInt(BUNDLE_SESSION_ID)+"");
        }
    }

    public void setRightView(View view) {
        view.setOnClickListener(this);
    }
}
