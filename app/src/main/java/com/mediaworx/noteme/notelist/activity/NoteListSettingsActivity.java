package com.mediaworx.noteme.notelist.activity;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.PreferencesManager;

public class NoteListSettingsActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = NoteListSettingsActivity.class.getSimpleName();

    static class ViewHolder {
        public RadioButton rb_title_edit;
        public RadioButton rb_title_dropdown;
    }

    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notelistsettings_activity);

        initViewHolder();
    }



    private void initViewHolder(){
        Log.d(TAG, "initViewHolder()");

        viewHolder = new ViewHolder();

        viewHolder.rb_title_edit = (RadioButton) findViewById(R.id.rb_titelstyle_edittext);
        viewHolder.rb_title_edit.setOnClickListener(this);

        viewHolder.rb_title_dropdown = (RadioButton) findViewById(R.id.rb_titelstyle_dropdown);
        viewHolder.rb_title_dropdown.setOnClickListener(this);

        Button bt_edit = (Button) findViewById(R.id.bt_titelstyle_edittext);
        bt_edit.setOnClickListener(this);

        Button bt_dropdown = (Button) findViewById(R.id.bt_titelstyle_dropdown);
        bt_dropdown.setOnClickListener(this);

        if(PreferencesManager.getNoteListTitleStyle() == NoteListTitleStyle.DROPDOWN){
            viewHolder.rb_title_edit.setChecked(false);
            viewHolder.rb_title_dropdown.setChecked(true);

        }else{

            viewHolder.rb_title_edit.setChecked(true);
            viewHolder.rb_title_dropdown.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");

        int viewId = v.getId();
        String s_viewId = getResources().getResourceEntryName(viewId);// to view in debugger
        Log.d(TAG, "viewId="+s_viewId);


        switch(viewId){

            case R.id.bt_titelstyle_edittext:
            case R.id.rb_titelstyle_edittext:

                PreferencesManager.setNoteListTitleStyle(NoteListTitleStyle.EDIT_TEXT);
                viewHolder.rb_title_edit.setChecked(true);
                viewHolder.rb_title_dropdown.setChecked(false);
                break;

            case R.id.bt_titelstyle_dropdown:
            case R.id.rb_titelstyle_dropdown:

                PreferencesManager.setNoteListTitleStyle(NoteListTitleStyle.DROPDOWN);
                viewHolder.rb_title_edit.setChecked(false);
                viewHolder.rb_title_dropdown.setChecked(true);
                break;
        }
    }
}