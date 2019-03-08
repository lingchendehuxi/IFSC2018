package com.android.incongress.cd.conference.fragments.me;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Note;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.IncongressEditText;
import com.android.incongress.cd.conference.widget.IncongressTextView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Admin on 2018/2/12.
 */

public class MyMeetingNoteEditorActivity extends BaseActivity {
    private IncongressEditText mIncongressEditText;
    private IncongressTextView ivTitle;
    private TextView ivRight;
    private LinearLayout ivBack;
    private Note notes;
    @Override
    protected void setContentView() {
        setContentView(R.layout.mycenter_note_editor);
        notes = (Note) getIntent().getSerializableExtra("bean");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIncongressEditText = (IncongressEditText)findViewById(R.id.mycenter_note_edior);
        ivBack = (LinearLayout) findViewById(R.id.title_back_panel);
        ivTitle = (IncongressTextView) findViewById(R.id.title_text);
        ivRight = (TextView) findViewById(R.id.tv_right);
        ivTitle.setText(R.string.meeting_schedule_note);
        mIncongressEditText.setText(notes.getContents());
        mIncongressEditText.requestFocus();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=mIncongressEditText.getText().toString().trim();
                if(content==null||"".equals(content)){
                    Toast.makeText(MyMeetingNoteEditorActivity.this, R.string.metting_node_edit_no_data, Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = String.valueOf(System.currentTimeMillis());
                notes.setContents(content);
                notes.setCreatetime(time);
                notes.setUpdatetime(time);
                ConferenceDbUtils.updateOneNote(notes);
                ToastUtils.showToast(getString(R.string.note_save_success));
                finish();
            }
        });
    }

    @Override
    protected void initViewsAction() {
    }
}
