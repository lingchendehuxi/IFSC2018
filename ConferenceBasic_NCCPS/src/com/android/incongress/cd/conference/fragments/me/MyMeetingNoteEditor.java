package com.android.incongress.cd.conference.fragments.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Note;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.widget.IncongressEditText;

public class MyMeetingNoteEditor extends BaseFragment implements OnClickListener {

	private static final int EDITOR = 0;
	private static final int UPDATE = 1;
	
	private IncongressEditText mIncongressEditText;
	private SessionBean sessionBean;
	private ClassesBean classesBean;
	private MeetingBean meetingBean;
	private Note notes;
	private int type = EDITOR;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mycenter_note_editor, null);
//		MobclickAgent.onEvent(getActivity(),UMengPage.Page_Note); //统计页面
		mIncongressEditText = (IncongressEditText) view.findViewById(R.id.mycenter_note_edior);
		if (type == UPDATE) {
			mIncongressEditText.setText(notes.getContents());
		}
		mIncongressEditText.requestFocus();
		toggleShurufa();
		return view;
		
	}
	@Override
	public void onClick(View v) {
//		MobclickAgent.onEvent(getActivity(),UMengEvent.Metting_Schedule_Speech_Note_Ok);
		if(type == EDITOR){
			String content=mIncongressEditText.getText().toString().trim();
			if(content==null||"".equals(content)){
				Toast.makeText(getActivity(), R.string.metting_node_edit_no_data, Toast.LENGTH_SHORT).show();
				return;
			}
			String time = String.valueOf(System.currentTimeMillis());
			notes = new Note();
			notes.setContents(content);
			notes.setCreatetime(time);
			notes.setDate(meetingBean.getMeetingDay());
			notes.setEnd(meetingBean.getEndTime());
			notes.setClassid(String.valueOf(classesBean.getClassesId()));
			notes.setMeetingid(String.valueOf(meetingBean.getMeetingId()));
			notes.setRoom(classesBean.getClassesCode());
			notes.setSessionid(String.valueOf(sessionBean.getSessionGroupId()));
			notes.setStart(meetingBean.getStartTime());
			notes.setTitle(meetingBean.getTopic());
			notes.setUpdatetime(time);
			ConferenceDbUtils.addOneNote(notes);
		}else {
			notes.setUpdatetime(String.valueOf(System.currentTimeMillis()));
			notes.setContents(mIncongressEditText.getText().toString());
			ConferenceDbUtils.updateOneNote(notes);

		}
		 HomeActivity activity = (HomeActivity) getActivity();
         activity.perfromBackPressTitleEntry();
         activity.hideShurufa();
	}
	
	public void setDatasource(SessionBean sessionBean,ClassesBean classesBean,MeetingBean meetingBean){
		this.sessionBean = sessionBean;
		this.classesBean = classesBean;
		this.meetingBean = meetingBean;
		type = EDITOR;
	}
	public void setDatasource(Note notes){
		this.notes = notes;
		type = UPDATE;
	}
}
