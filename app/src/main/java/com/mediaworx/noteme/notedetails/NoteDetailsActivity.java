package com.mediaworx.noteme.notedetails;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.constants.Constants;
import com.mediaworx.noteme.common.constants.IntentExtras;
import com.mediaworx.noteme.common.dropdown.OnDropDownSelectedListener;
import com.mediaworx.noteme.common.labledtypes.PrioType;
import com.mediaworx.noteme.navigation.NavigationDrawerActivity;
import com.mediaworx.noteme.notelist.activity.NoteListsDropDownFragment;
import com.mediaworx.noteme.notelist.dao.NoteListDAO;
import com.mediaworx.noteme.notelist.dao.factory.NoteListDAOFactory;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.List;

public class NoteDetailsActivity extends NavigationDrawerActivity implements OnDropDownSelectedListener<NoteList>{

    private static final String TAG = NoteDetailsActivity.class.getSimpleName();

    private static final String TAG_NOTELISTDROPDOWN_FRAGMENT = "TAG_NOTELISTDROPDOWN_FRAGMENT";

    private NoteListDAO noteListDAO;

    private long noteListId;
    private long noteId;

    private static NoteList noteList;

    @Override
    protected int getMainContentLayoutId() {
        return R.layout.notedetails_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        noteListDAO = NoteListDAOFactory.getDAO(this);

        noteListId = getIntent().getLongExtra(IntentExtras.NOTE_LIST_ID, Constants.NO_ID);
        noteId = getIntent().getLongExtra(IntentExtras.NOTE_ID, Constants.NO_ID);

        initParentListDropdown(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        noteList = noteListDAO.read(noteListId);

        initNoteText();
        shapeParentListDropdown();
        initPrioritySeeker();
    }

    private void initParentListDropdown(Bundle savedInstanceState){
        Log.d(TAG, "initParentListDropdown()");

        NoteListsDropDownFragment dropdown = new NoteListsDropDownFragment();
        dropdown.setLayoutIds(R.layout.notedetails_dropdown_parentlist, R.id.sp_noteDetails_parentList);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ll_placeholder_dropdown_parentlist, dropdown, TAG_NOTELISTDROPDOWN_FRAGMENT)
                    .commit();
    }


    private void initNoteText(){
        Log.d(TAG, "initNoteText()");

        String noteText = noteList.getNote(noteId).getText();
        noteText = "\" "+noteText+" \"";

        TextView tv_noteDetails_noteText = (TextView) findViewById(R.id.tv_noteDetails_noteText);
        tv_noteDetails_noteText.setText(noteText);
    }

    private void shapeParentListDropdown(){
        Log.d(TAG, "shapeParentListDropdown()");

        List<NoteList> noteLists = noteListDAO.readAll();
        NoteListsDropDownFragment dropdown = (NoteListsDropDownFragment) getSupportFragmentManager().findFragmentByTag(TAG_NOTELISTDROPDOWN_FRAGMENT);
        dropdown.updateSelectables(noteList, noteLists);
    }

    @Override
    public void onDropDownSelected(NoteList selected) {
        Log.d(TAG, "onDropDownSelected()");
        Log.d(TAG, "selected=" + selected);

        Toast.makeText(NoteDetailsActivity.this, "selected="+selected.getTitle(), Toast.LENGTH_SHORT).show();
    }


    private void initPrioritySeeker() {
        Log.d(TAG, "initPrioritySeeker()");

        SeekBar sb_priority = (SeekBar) findViewById(R.id.sb_noteDetails_priority);
        sb_priority.setMax(PrioType.values().length-1);

        sb_priority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged()");
                Log.d(TAG, "progress="+progress);

                PrioType prioType = PrioType.values()[progress];
                Log.d(TAG, "prioType="+prioType);

                Toast.makeText(NoteDetailsActivity.this, "prio="+prioType.getLabel(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}