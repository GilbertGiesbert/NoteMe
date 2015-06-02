package com.mediaworx.noteme.notelist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;
import com.mediaworx.noteme.common.application.PreferencesManager;
import com.mediaworx.noteme.common.constants.IntentExtras;
import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.common.sorting.SortingDialog;
import com.mediaworx.noteme.common.sorting.SortingDialogListener;
import com.mediaworx.noteme.navigation.NavigationDrawerActivity;
import com.mediaworx.noteme.notelist.adapter.NoteListsArrayAdapter;
import com.mediaworx.noteme.notelist.dao.NoteListDAO;
import com.mediaworx.noteme.notelist.dao.factory.NoteListDAOFactory;
import com.mediaworx.noteme.notelist.model.NoteList;
import com.mediaworx.noteme.notelist.utils.NoteListsOverviewFunctions;

import java.util.ArrayList;
import java.util.List;


public class NoteListOverviewActivity extends NavigationDrawerActivity implements View.OnClickListener, SortingDialogListener {

    private static final String TAG = NoteListOverviewActivity.class.getSimpleName();

    private static final String TAG_SORTING_DIALOG = "TAG_SORTING_DIALOG";

    private NoteListDAO noteListDAO;

    private static final SortType[] SORT_TYPES = {  SortType.TITLE,
                                                    SortType.TITLE_REVERSE,
                                                    SortType.DATE_CREATED,
                                                    SortType.DATE_CREATED_REVERSE};

    private List<NoteList> noteLists = new ArrayList<>();;

    private ListView lv_noteLists;
    private NoteListsArrayAdapter noteListsArrayAdapter;

    private SortingDialog sortingDialog;


    @Override
    protected int getMainContentLayoutId() {
        Log.d(TAG, "getMainContentLayoutId()");
        return R.layout.notelistoverview_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);


        noteListDAO = NoteListDAOFactory.getDAO(App.getAppContext());


        initListHead();
        initListView();
        initBackground();
        initSortingDialog();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        // update list data
        noteLists.clear();
        noteLists.addAll(noteListDAO.readAll());

        // sort data
        NoteListsOverviewFunctions.sortNoteLists(noteLists);

        // notify
        noteListsArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        Log.d(TAG, "onPause()");

        if(noteListsArrayAdapter.isAnyEntryInEditMode()){
            closeEntryInEdit(false);
            lv_noteLists.setFocusable(true);
        }

        for(NoteList noteList: noteLists){
            noteListDAO.updateLight(noteList);
        }

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notelistoverview, menu);
        return true;
    }

    private void initListHead() {
        Log.d(TAG, "initListHead()");

        ImageButton ib_addNewList = (ImageButton) findViewById(R.id.ib_addNewNoteList);
        ib_addNewList.setOnClickListener(this);

        Button bt_addNewList = (Button) findViewById(R.id.bt_addNewNoteList);
        bt_addNewList.setOnClickListener(this);

        ImageButton bt_sort = (ImageButton) findViewById(R.id.bt_overview_sort);
        bt_sort.setOnClickListener(this);
    }

    private void initListView() {
        Log.d(TAG, "initListView()");

        noteListsArrayAdapter = new NoteListsArrayAdapter(this, noteLists);

        lv_noteLists = (ListView) findViewById(R.id.lv_noteLists);

        lv_noteLists.setAdapter(noteListsArrayAdapter);
        lv_noteLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int clickedEntryPosition, long id) {
                Log.d(TAG, "onItemLongClick()");
                Log.d(TAG, "clickedEntryPosition=" + clickedEntryPosition);


                if (noteListsArrayAdapter.isAnyEntryInEditMode()) {

                    int entryInEditPosition = noteListsArrayAdapter.getEntryInEditModePosition();
                    String entryText = readEntryInEdit();

                    if (entryText.isEmpty()) {
                        String text = getResources().getString(R.string.emptyTextHint);
                        Toast.makeText(NoteListOverviewActivity.this, text, Toast.LENGTH_SHORT).show();

                    } else {
                        noteLists.get(entryInEditPosition).setTitle(entryText);
                    }
                }

                noteListsArrayAdapter.setEntryInEditModePosition(clickedEntryPosition);
                noteListsArrayAdapter.notifyDataSetChanged();

                return true;
            }
        });
        lv_noteLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int clickedEntryPosition, long id) {
                Log.d(TAG, "onItemClick()");
                Log.d(TAG, "clickedEntryPosition=" + clickedEntryPosition);

                NoteList noteList = noteLists.get(clickedEntryPosition);

                long listId = noteList.getId();

                Log.d(TAG, "listId=" + listId);

                Intent intent = new Intent(NoteListOverviewActivity.this, NoteListActivity.class);
                intent.putExtra(IntentExtras.NOTE_LIST_ID, listId);
                startActivity(intent);
            }
        });
    }

    private void initBackground() {
        Log.d(TAG, "initBackground()");

        LinearLayout ll_background = (LinearLayout) findViewById(R.id.ll_activity_noteListOverview);
        ll_background.setOnClickListener(this);
    }

    private void initSortingDialog() {
        Log.d(TAG, "initSortingDialog()");

        sortingDialog = new SortingDialog();
        sortingDialog.setSelectableSortTypes(SORT_TYPES);
        sortingDialog.setSortTypeToPreselect(PreferencesManager.getNoteListsSortType());
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");

        int viewId = v.getId();
        String s_viewId = getResources().getResourceEntryName(viewId);// to view in debugger
        Log.d(TAG, "viewId="+s_viewId);


        switch(viewId){

            // user clicked 'somewhere' on the background to close edit of entry
            case R.id.ll_activity_noteListOverview:

                if(noteListsArrayAdapter.isAnyEntryInEditMode()){
                    closeEntryInEdit(true);
                    lv_noteLists.setFocusable(true);
                }

                break;

            case R.id.ib_addNewNoteList:
            case R.id.bt_addNewNoteList:

                Intent intent = new Intent(this, NoteListActivity.class);
                intent.putExtra(IntentExtras.BUILD_NEW_NOTE_LIST, true);
                startActivity(intent);
                break;

            case R.id.bt_overview_sort:

                if(noteListsArrayAdapter.isAnyEntryInEditMode()){
                    closeEntryInEdit(true);
                    lv_noteLists.setFocusable(true);
                }

                showSortingDialog();
                break;

            case R.id.bt_noteListOverview_entry_confirm:
                if(noteListsArrayAdapter.isAnyEntryInEditMode())
                    closeEntryInEdit(true);
                lv_noteLists.setFocusable(true);
                break;

            case R.id.bt_noteListOverview_entry_delete:

                int entryInEditPosition = noteListsArrayAdapter.getEntryInEditModePosition();
                NoteList entryInEdit = noteLists.get(entryInEditPosition);

                // delete data object
                noteLists.remove(entryInEditPosition);
                // delete file object
                noteListDAO.delete(entryInEdit);

                noteListsArrayAdapter.resetEntryInEditMode();
                noteListsArrayAdapter.notifyDataSetChanged();

                lv_noteLists.setFocusable(true);

                break;
        }
    }

    private String readEntryInEdit(){
        Log.d(TAG, "readEntryInEdit()");

        int entryInEditPosition = noteListsArrayAdapter.getEntryInEditModePosition();

        View ll_entryInEditMode = lv_noteLists.getChildAt(entryInEditPosition);

        EditText et_entryInEditMode = (EditText) ll_entryInEditMode.findViewById(R.id.et_noteListOverview_entry_text_editmode);

        String text = et_entryInEditMode.getText().toString();
        text = text.trim();

        return text;
    }

    public void closeEntryInEdit(boolean showEmptyTextHint){
        Log.d(TAG, "closeEntryInEdit()");

        int entryInEditPosition = noteListsArrayAdapter.getEntryInEditModePosition();
        String entryInEditText = readEntryInEdit();

        if(entryInEditText.isEmpty()) {

            if(showEmptyTextHint){
                String text = getResources().getString(R.string.emptyTextHint);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }

        }else{
            noteLists.get(entryInEditPosition).setTitle(entryInEditText);
        }

        noteListsArrayAdapter.resetEntryInEditMode();
        noteListsArrayAdapter.notifyDataSetChanged();
    }

    public void showSortingDialog() {
        Log.d(TAG, "showSortDialog()");
        sortingDialog.show(getFragmentManager(), TAG_SORTING_DIALOG);
    }

    public void onSortTypeSelected(SortType sortTypeSelected) {
        Log.d(TAG, "onSortTypeSelected()");
        Log.d(TAG, "sortTypeSelected=" + sortTypeSelected);

        PreferencesManager.setNoteListsSortType(sortTypeSelected);

        NoteListsOverviewFunctions.sortNoteLists(noteLists);

        noteListsArrayAdapter.notifyDataSetChanged();
    }
}