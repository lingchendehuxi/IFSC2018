package com.android.incongress.cd.conference.fragments.me;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MyMeetingNotesAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Note;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * 笔记管理界面
 */
public class NoteManageActionFragment extends BaseFragment {

    private ListView mListView;
    private List<Note> mNotes;
    private MyMeetingNotesAdapter mAdapter;
    private TextView mTvTips;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mycenter_note, null);

        mListView = (ListView) view.findViewById(R.id.mycenter_note_list);
        mNotes = ConferenceDbUtils.getAllNotes();
        mAdapter = new MyMeetingNotesAdapter(mNotes);
        mListView.setAdapter(mAdapter);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_tips).setMessage(R.string.mymeeting_note_delete_msg).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.deleteNote(position);

                        if(mAdapter.getDatas() == null || mAdapter.getDatas().size() == 0)
                            mTvTips.setVisibility(View.VISIBLE);
                    }
                }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).show();
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note notes = (Note) mAdapter.getItem(position);
                MyMeetingNoteEditor editor = new MyMeetingNoteEditor();
                editor.setDatasource(notes);
                action(editor, R.string.mymeeting_note_title, false, false, false);
            }
        });

        if (mAdapter.getCount() == 0) {
            mTvTips.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
