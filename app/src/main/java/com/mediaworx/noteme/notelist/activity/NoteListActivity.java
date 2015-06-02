package com.mediaworx.noteme.notelist.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.adapter.ColorPickerArrayAdapter;
import com.mediaworx.noteme.common.application.PreferencesManager;
import com.mediaworx.noteme.common.labledtypes.ColorType;
import com.mediaworx.noteme.common.constants.IntentExtras;
import com.mediaworx.noteme.common.dragdrop.DragDropListView;
import com.mediaworx.noteme.common.dropdown.OnDropDownSelectedListener;
import com.mediaworx.noteme.common.filtering.FilterDialog;
import com.mediaworx.noteme.common.filtering.FilterDialogListener;
import com.mediaworx.noteme.common.filtering.FilterType;
import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.common.sorting.SortingDialog;
import com.mediaworx.noteme.common.sorting.SortingDialogListener;
import com.mediaworx.noteme.navigation.NavigationDrawerActivity;
import com.mediaworx.noteme.notedetails.NoteDetailsActivity;
import com.mediaworx.noteme.notelist.adapter.NotesArrayAdapter;
import com.mediaworx.noteme.notelist.dao.NoteListDAO;
import com.mediaworx.noteme.notelist.dao.factory.NoteListDAOFactory;
import com.mediaworx.noteme.notelist.model.Note;
import com.mediaworx.noteme.notelist.model.NoteList;
import com.mediaworx.noteme.notelist.utils.NoteListFunctions;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends NavigationDrawerActivity implements   View.OnClickListener,
                                                                            EditTitleFragmentListener,
                                                                            OnDropDownSelectedListener<NoteList>,
                                                                            FilterDialogListener,
                                                                            SortingDialogListener {

    private static final String TAG = NoteListActivity.class.getSimpleName();

    private static final String TAG_TITLE_FRAGMENT = "TAG_TITLE_FRAGMENT";
    private static final String TAG_FILTER_DIALOG = "TAG_FILTER_DIALOG";
    private static final String TAG_SORTING_DIALOG = "TAG_SORTING_DIALOG";

    private static final FilterType[] FILTER_TYPES = {  FilterType.FILTER_NOFILTER,
                                                        FilterType.FILTER_CHECKED,
                                                        FilterType.FILTER_UNCHECKED};

    private static final SortType[] SORT_TYPES = {  SortType.DATE_CREATED,
                                                    SortType.DATE_CREATED_REVERSE,
                                                    SortType.LAST_EDITED_TOP,
                                                    SortType.LAST_EDITED_BOTTOM,
                                                    SortType.ALPHABETICALLY,
                                                    SortType.ALPHABETICALLY_REVERSE,
                                                    SortType.CHECKED_FIRST,
                                                    SortType.UNCHECKED_FIRST};

    // add new notes at top of list
    private static final int ENTRY_POSITION_FOR_NEW_NOTES = 0;

    private static final ArrayList<Note> displayList = new ArrayList<>();
    private static NoteList dataList;


    private MenuItem mi_colorPalette;
    private Dialog d_colorDialog;

    private SortingDialog sortingDialog;
    private FilterDialog filterDialog;


    private NoteListDAO noteListDAO;

    private DragDropListView<Note> lv_notes;
    private NotesArrayAdapter notesArrayAdapter;

    @Override
    protected int getMainContentLayoutId() {
        Log.d(TAG, "getMainContentLayoutId()");
        return R.layout.notelist_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        initNoteListData();

        initTitle(savedInstanceState);
        initNewAndSortButtons();
        initListView();
        initBackground();

        initSortingDialog();
        initFilterDialog();
    }

    @Override
    public void onPause(){
        Log.d(TAG, "onPause()");

        if(isTitleInEditMode()){
            storeTitle();
            switchTitleToReadMode();
        }

        if(notesArrayAdapter.isAnyEntryInEditMode()){
            closeEntryInEdit();
            lv_notes.setFocusable(true);
        }

        noteListDAO.update(dataList);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notelist, menu);
        mi_colorPalette = menu.findItem(R.id.mi_color);
        tintMenuIcon(mi_colorPalette, dataList.getColor().getColorValue());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        String s_itemId = getResources().getResourceEntryName(itemId);// to view in debugger
        Log.d(TAG, "itemId=" + s_itemId);

        switch (itemId){
            case R.id.mi_color:

                if(isTitleInEditMode()){
                    storeTitle();
                    switchTitleToReadMode();
                }

                if(notesArrayAdapter.isAnyEntryInEditMode()){
                    closeEntryInEdit();
                    lv_notes.setFocusable(true);
                }

                showColorDialog();

                return true;

            case R.id.mi_settings:

                Intent intent = new Intent(this, NoteListSettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.mi_mock:
                NoteListFunctions.mockNoteList(40, dataList, displayList);
                notesArrayAdapter.notifyDataSetChanged(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tintMenuIcon(MenuItem menuItem, int colorResourceId){
        Log.d(TAG, "tintMenuIcon()");
        Log.d(TAG, "colorResourceId=" + colorResourceId);

        Drawable newIcon = menuItem.getIcon();
        newIcon.mutate().setColorFilter(colorResourceId, PorterDuff.Mode.SRC_IN);
        menuItem.setIcon(newIcon);
    }

    private void setBackgroundColor(ColorType colorType){
        Log.d(TAG, "setBackgroundColor()");
        Log.d(TAG, "noteListColor=" + colorType);

        LinearLayout ll_activity = (LinearLayout) findViewById(R.id.ll_activity_noteList);
        ll_activity.setBackgroundColor(colorType.getColorValue());
    }

    private void updateNoteListColor(int noteListColorIndex){
        Log.d(TAG, "updateNoteListColor()");
        Log.d(TAG, "noteListColorIndex=" + noteListColorIndex);

        ColorType colorType = ColorType.values()[noteListColorIndex];
        setBackgroundColor(colorType);

        tintMenuIcon(mi_colorPalette, colorType.getColorValue());

        dataList.setColor(colorType);
    }


    public void showColorDialog() {
        Log.d(TAG, "showColorDialog()");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ListView listView = new ListView(this);
        listView.setAdapter(new ColorPickerArrayAdapter(this, ColorType.values()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateNoteListColor(position);
                d_colorDialog.dismiss();
            }
        });

        builder.setView(listView);

        d_colorDialog = builder.create();
        d_colorDialog.show();
    }

    private void initNoteListData(){
        Log.d(TAG, "initNoteListData()");

        noteListDAO = NoteListDAOFactory.getDAO(this);

        boolean buildNewNoteList = getIntent().getBooleanExtra(IntentExtras.BUILD_NEW_NOTE_LIST, false);
        Log.d(TAG, "buildNewNoteList=" + buildNewNoteList);
        if(buildNewNoteList){

            dataList = noteListDAO.create();
            // if intent survives activity death and recreation
            // avoid creating multiple new note lists by
            // resetting IntentExtras.BUILD_NEW_NOTE_LIST
            getIntent().putExtra(IntentExtras.BUILD_NEW_NOTE_LIST, false);

        }else{
            int noId = -1;
            long noteListId = getIntent().getLongExtra(IntentExtras.NOTE_LIST_ID, noId);
            Log.d(TAG, "noteListId=" + noteListId);

            if(noteListId != noId) {

                dataList = noteListDAO.read(noteListId);

            }else {
                // do nothing (uses current dataList)
            }
        }
    }

    private void initTitle(Bundle savedInstanceState) {
        Log.d(TAG, "initTitle()");

        Fragment fragment;
        if(PreferencesManager.getNoteListTitleStyle() == NoteListTitleStyle.DROPDOWN) {
            fragment = new NoteListsDropDownFragment();
            ((NoteListsDropDownFragment) fragment).setLayoutIds(R.layout.notelist_fragment_dropdowntitle, R.id.sp_noteList_title);

            List<NoteList> noteLists = noteListDAO.readAll();
            ((NoteListsDropDownFragment) fragment).updateSelectables(dataList, noteLists);

        }else {
            fragment = new EditTitleFragment();
            ((EditTitleFragment) fragment).setLayoutContainerId(R.layout.notelist_fragment_edittitle);
        }

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ll_noteList_titleFragmentContainer, fragment, TAG_TITLE_FRAGMENT)
                    .commit();
    }

    private void initNewAndSortButtons() {
        Log.d(TAG, "initNewAndSortButtons()");

        ImageButton ib = (ImageButton) findViewById(R.id.ib_noteList_addNewEntry);
        ib.setOnClickListener(this);

        Button bt  = (Button) findViewById(R.id.bt_noteList_addNewEntry);
        bt.setOnClickListener(this);

        ImageButton bt_sort = (ImageButton) findViewById(R.id.bt_noteList_sort);
        bt_sort.setOnClickListener(this);

        ImageButton bt_filter = (ImageButton) findViewById(R.id.bt_noteList_filter);
        bt_filter.setOnClickListener(this);
    }

    private void initListView() {
        Log.d(TAG, "initListView()");

        NoteListFunctions.updateDisplayList(displayList, dataList);

        notesArrayAdapter = new NotesArrayAdapter(this, displayList);

        lv_notes = (DragDropListView<Note>) findViewById(R.id.listview);

        lv_notes.setItemList(displayList);
        lv_notes.setAdapter(notesArrayAdapter);
        lv_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int clickedEntryPosition, long id) {
                Log.d(TAG, "onItemClick()");
                Log.d(TAG, "clickedEntryPosition=" + clickedEntryPosition);

                if(notesArrayAdapter.isAnyEntryInEditMode()){

                    int entryInEditPosition = notesArrayAdapter.getEntryInEditModePosition();
                    String entryText = readEntry(entryInEditPosition);

                    long noteId = displayList.get(entryInEditPosition).getId();

                    if(entryText.isEmpty()){

                        dataList.removeNote(noteId);
                        displayList.remove(entryInEditPosition);

                        // update clickedEntryPosition if there was removal of empty entry above clicked entry
                        if(clickedEntryPosition > entryInEditPosition)
                            clickedEntryPosition = clickedEntryPosition -1;
                    }else{
                        dataList.getNote(noteId).setText(entryText);
                        displayList.get(entryInEditPosition).setText(entryText);
                    }
                }

                notesArrayAdapter.setEntryInEditModePosition(clickedEntryPosition);
                notesArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initBackground() {
        Log.d(TAG, "initBackground()");

        LinearLayout ll_background = (LinearLayout) findViewById(R.id.ll_activity_noteList);
        ll_background.setBackgroundColor(dataList.getColor().getColorValue());
        ll_background.setOnClickListener(this);
    }

    private void initSortingDialog() {
        Log.d(TAG, "initSortingDialog()");

        sortingDialog = new SortingDialog();
        sortingDialog.setSelectableSortTypes(SORT_TYPES);
        sortingDialog.setSortTypeToPreselect(dataList.getSortType());
    }

    private void initFilterDialog() {
        Log.d(TAG, "initFilterDialog()");

        filterDialog = new FilterDialog();
        filterDialog.setSelectableFilters(FILTER_TYPES);
        filterDialog.setFilterToPreselect(dataList.getFilterType());
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");

        int viewId = v.getId();
        String s_viewId = getResources().getResourceEntryName(viewId);// to view in debugger
        Log.d(TAG, "viewId=" + s_viewId);

        int entryInEditPosition;
        long noteId;

        switch(viewId){

            // user clicked 'somewhere' on the background to close edit of title or entry
            case R.id.ll_activity_noteList:

                if(isTitleInEditMode()){
                    storeTitle();
                    switchTitleToReadMode();
                }

                if(notesArrayAdapter.isAnyEntryInEditMode()){
                    closeEntryInEdit();
                    lv_notes.setFocusable(true);
                }

                break;

            case R.id.ib_noteList_addNewEntry:
            case R.id.bt_noteList_addNewEntry:

                if(isTitleInEditMode()){
                    storeTitle();
                    switchTitleToReadMode();
                }

                if(notesArrayAdapter.isAnyEntryInEditMode()) {
                    entryInEditPosition = notesArrayAdapter.getEntryInEditModePosition();
                    String entryInEditText = readEntry(entryInEditPosition);

                    noteId = displayList.get(entryInEditPosition).getId();

                    if(entryInEditText.isEmpty()){
                        dataList.removeNote(noteId);
                        displayList.remove(entryInEditPosition);
                    }else{
                        dataList.getNote(noteId).setText(entryInEditText);
                        displayList.get(entryInEditPosition).setText(entryInEditText);
                    }
                }


                Note newNote = dataList.createNote();
                displayList.add(ENTRY_POSITION_FOR_NEW_NOTES, newNote);


                notesArrayAdapter.setEntryInEditModePosition(ENTRY_POSITION_FOR_NEW_NOTES);
                notesArrayAdapter.notifyDataSetChanged(true);

                break;

            case R.id.bt_noteList_sort:

                if(isTitleInEditMode()){
                    storeTitle();
                    switchTitleToReadMode();
                }

                if(notesArrayAdapter.isAnyEntryInEditMode()){
                    closeEntryInEdit();
                    lv_notes.setFocusable(true);
                }

                showSortingDialog();
                break;

            case R.id.bt_noteList_filter:

                if(isTitleInEditMode()){
                    storeTitle();
                    switchTitleToReadMode();
                }

                if(notesArrayAdapter.isAnyEntryInEditMode()){
                    closeEntryInEdit();
                    lv_notes.setFocusable(true);
                }

                showFilterDialog();
                break;

            case R.id.cb_entry_selected_readmode:
            case R.id.cb_entry_selected_editmode:

                int checkBoxPosition = (int) v.getTag();
                Log.d(TAG, "checkBoxPosition=" + checkBoxPosition);

                boolean checkBoxState = ((CheckBox)v).isChecked();
                Log.d(TAG, "checkBoxState=" + checkBoxState);

                noteId = displayList.get(checkBoxPosition).getId();

                dataList.getNote(noteId).setDone(checkBoxState);
                displayList.get(checkBoxPosition).setDone(checkBoxState);

                if(dataList.getSortType()== SortType.CHECKED_FIRST)
                    sortingDialog.doNotPreselect(SortType.CHECKED_FIRST);
                if(dataList.getSortType()== SortType.UNCHECKED_FIRST)
                    sortingDialog.doNotPreselect(SortType.UNCHECKED_FIRST);

                if(dataList.getFilterType()== FilterType.FILTER_CHECKED)
                    filterDialog.doNotPreselect(FilterType.FILTER_CHECKED);
                if(dataList.getFilterType()== FilterType.FILTER_UNCHECKED)
                    filterDialog.doNotPreselect(FilterType.FILTER_UNCHECKED);

                break;

            case R.id.ib_entry_confirm:
                if(notesArrayAdapter.isAnyEntryInEditMode())
                    closeEntryInEdit();
                lv_notes.setFocusable(true);
                break;

            case R.id.ib_entry_details:

                entryInEditPosition = notesArrayAdapter.getEntryInEditModePosition();
                noteId = displayList.get(entryInEditPosition).getId();

                if(notesArrayAdapter.isAnyEntryInEditMode())
                    closeEntryInEdit();
                lv_notes.setFocusable(true);

                Intent intent = new Intent(this, NoteDetailsActivity.class);
                intent.putExtra(IntentExtras.NOTE_LIST_ID, dataList.getId());
                intent.putExtra(IntentExtras.NOTE_ID, noteId);
                startActivity(intent);

                break;

            case R.id.ib_entry_delete:

                entryInEditPosition = notesArrayAdapter.getEntryInEditModePosition();
                noteId = displayList.get(entryInEditPosition).getId();

                dataList.removeNote(noteId);
                displayList.remove(entryInEditPosition);

                notesArrayAdapter.resetEntryInEditMode();
                notesArrayAdapter.notifyDataSetChanged(true);

                lv_notes.setFocusable(true);

                break;
        }
    }


    private String readEntry(int position){
        Log.d(TAG, "readEntry()");
        Log.d(TAG, "position=" + position);

        View ll_entryInEditMode = lv_notes.getViewForID(notesArrayAdapter.getItemId(position));

        EditText et_entryInEditMode = (EditText) ll_entryInEditMode.findViewById(R.id.et_entry_text_editmode);

        String text = et_entryInEditMode.getText().toString();
        text = text.trim();

        return text;
    }

    public void closeEntryInEdit(){
        Log.d(TAG, "closeEntryInEdit()");

        int entryInEditPosition = notesArrayAdapter.getEntryInEditModePosition();

        String entryText = readEntry(entryInEditPosition);
        Log.d(TAG, "entryText=" + entryText);

        long noteId = displayList.get(entryInEditPosition).getId();

        String oldText = dataList.getNote(noteId).getText();
        Log.d(TAG, "oldText=" + oldText);



        if(entryText.isEmpty()){
            dataList.removeNote(noteId);
            displayList.remove(entryInEditPosition);

        }else if(!entryText.equals(oldText)){

            dataList.getNote(noteId).setText(entryText);
            displayList.get(entryInEditPosition).setText(entryText);

            if(dataList.getSortType()== SortType.ALPHABETICALLY)
                sortingDialog.doNotPreselect(SortType.ALPHABETICALLY);
            if(dataList.getSortType()== SortType.ALPHABETICALLY_REVERSE)
                sortingDialog.doNotPreselect(SortType.ALPHABETICALLY_REVERSE);
            if(dataList.getSortType()== SortType.LAST_EDITED_TOP)
                sortingDialog.doNotPreselect(SortType.LAST_EDITED_TOP);
            if(dataList.getSortType()== SortType.LAST_EDITED_BOTTOM)
                sortingDialog.doNotPreselect(SortType.LAST_EDITED_BOTTOM);
        }

        notesArrayAdapter.resetEntryInEditMode();
        notesArrayAdapter.notifyDataSetChanged();
    }

    public void switchTitleToReadMode(){
        Log.d(TAG, "switchTitleToReadMode()");

        EditTitleFragment fr_editTitle = (EditTitleFragment)getSupportFragmentManager().findFragmentByTag(TAG_TITLE_FRAGMENT);

        fr_editTitle.switchTitleToReadMode();
    }

    public void storeTitle(){
        Log.d(TAG, "storeTitle()");

        EditTitleFragment fr_editTitle = (EditTitleFragment)getSupportFragmentManager().findFragmentByTag(TAG_TITLE_FRAGMENT);

        fr_editTitle.storeTitle();
    }

    private boolean isTitleInEditMode(){
        Log.d(TAG, "isTitleInEditMode()");

        Fragment fr_title = getSupportFragmentManager().findFragmentByTag(TAG_TITLE_FRAGMENT);
        if(fr_title instanceof EditTitleFragment){

            return ((EditTitleFragment) fr_title).isTitleInEditMode();
        }
        return false;
    }

    public void showSortingDialog() {
        Log.d(TAG, "showSortingDialog()");
        sortingDialog.show(getFragmentManager(), TAG_SORTING_DIALOG);
    }

    public void onSortTypeSelected(SortType sortTypeSelected){
        Log.d(TAG, "onSortTypeSelected()");
        Log.d(TAG, "sortTypeSelected=" + sortTypeSelected);

        dataList.setSortType(sortTypeSelected);
        NoteListFunctions.updateDisplayList(displayList, dataList);

        notesArrayAdapter.notifyDataSetChanged(true);
    }

    public void showFilterDialog() {
        Log.d(TAG, "showFilterDialog()");
        filterDialog.show(getFragmentManager(), TAG_FILTER_DIALOG);
    }

    public void onFilterSelected(FilterType filterSelected){
        Log.d(TAG, "onFilterSelected()");
        Log.d(TAG, "filterSelected=" + filterSelected);

        dataList.setFilterType(filterSelected);
        NoteListFunctions.updateDisplayList(displayList, dataList);

        notesArrayAdapter.notifyDataSetChanged(true);
    }

    @Override
    public void onConfirmTitle(String confirmedTitle) {
        Log.d(TAG, "onConfirmTitle()");
        Log.d(TAG, "confirmedTitle=" + confirmedTitle);

        dataList.setTitle(confirmedTitle);
    }

    @Override
    public void onEditTitle() {
        Log.d(TAG, "onEditTitle()");

        if(notesArrayAdapter.isAnyEntryInEditMode()){
            closeEntryInEdit();
            lv_notes.setFocusable(true);
        }
    }

    @Override
    public String getOriginalTitle() {
        Log.d(TAG, "getOriginalTitle()");

        return dataList.getTitle();
    }

    @Override
    public void onDropDownSelected(NoteList selected) {
        Log.d(TAG, "onDropDownSelected()");
        Log.d(TAG, "selected=" + selected);

        long listId = selected.getId();
        Intent intent = new Intent(this, NoteListActivity.class);
        intent.putExtra(IntentExtras.NOTE_LIST_ID, listId);
        startActivity(intent);
    }
}