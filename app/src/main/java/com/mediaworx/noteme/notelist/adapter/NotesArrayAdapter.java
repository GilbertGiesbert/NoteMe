package com.mediaworx.noteme.notelist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.dragdrop.StableArrayAdapter;
import com.mediaworx.noteme.notelist.activity.NoteListActivity;
import com.mediaworx.noteme.notelist.model.Note;

import java.util.ArrayList;

public class NotesArrayAdapter extends StableArrayAdapter<Note> {

    private static final String TAG = NotesArrayAdapter.class.getSimpleName();

    private static final int ROW_RESOURCE_ID = R.layout.notelist_entry_container;

    private final Context context;

    private static final int NONE = -1;
    private int entryInEditModePosition;


    static class ViewHolderInReadMode {
        public CheckBox cb_done;
        public TextView tv_text;
    }

    static class ViewHolderInEditMode {
        public CheckBox cb_done;
        public EditText et_text;
        public ImageButton ib_confirm;
        public ImageButton ib_details;
        public ImageButton ib_delete;
    }

    public NotesArrayAdapter(Context context, ArrayList<Note> notes) {
        super(context, ROW_RESOURCE_ID, notes);
        this.context = context;
        resetEntryInEditMode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if(isEntryInEditMode(position))
                return getRowInEditMode(position, rowView);

        else
            return getRowInReadMode(position, rowView);
    }

    private View getRowInEditMode(int position, View rowView) {

        ViewHolderInEditMode viewHolder;

        // get viewHolder
        if (rowView == null || !(rowView.getTag() instanceof ViewHolderInEditMode)) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(ROW_RESOURCE_ID, null);
            LinearLayout ll_entry_editMode = (LinearLayout) inflater.inflate(R.layout.notelist_entry_editmode, null);

            viewHolder = new ViewHolderInEditMode();

            // CAUTION! DO NOT set position tags here
            // i.e. viewHolder.viewXy.setTag(position)

            viewHolder.cb_done = (CheckBox) ll_entry_editMode.findViewById(R.id.cb_entry_selected_editmode);
            viewHolder.cb_done.setOnClickListener((NoteListActivity)context);
            viewHolder.et_text = (EditText) ll_entry_editMode.findViewById(R.id.et_entry_text_editmode);
            viewHolder.ib_confirm = (ImageButton) ll_entry_editMode.findViewById(R.id.ib_entry_confirm);
            viewHolder.ib_confirm.setOnClickListener((NoteListActivity) context);
            viewHolder.ib_details = (ImageButton) ll_entry_editMode.findViewById(R.id.ib_entry_details);
            viewHolder.ib_details.setOnClickListener((NoteListActivity) context);
            viewHolder.ib_delete = (ImageButton) ll_entry_editMode.findViewById(R.id.ib_entry_delete);
            viewHolder.ib_delete.setOnClickListener((NoteListActivity) context);

            ((LinearLayout)rowView).addView(ll_entry_editMode);
            rowView.setTag(viewHolder);

        }else
            viewHolder = (ViewHolderInEditMode) rowView.getTag();


        // fill viewHolder
        viewHolder.cb_done.setChecked(itemList.get(position).isDone());
        viewHolder.cb_done.setTag(position);
        viewHolder.et_text.setText(itemList.get(position).getText());
        viewHolder.et_text.requestFocus();

        return rowView;
    }

    private View getRowInReadMode(int position, View rowView) {

        ViewHolderInReadMode viewHolder;

        // get viewHolder
        if (rowView == null || !(rowView.getTag() instanceof ViewHolderInReadMode)) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(ROW_RESOURCE_ID, null);
            LinearLayout ll_entry_readMode = (LinearLayout) inflater.inflate(R.layout.notelist_entry_readmode, null);

            viewHolder = new ViewHolderInReadMode();

            // CAUTION! DO NOT set position tags here
            // i.e. viewHolder.viewXy.setTag(position)

            viewHolder.cb_done = (CheckBox) ll_entry_readMode.findViewById(R.id.cb_entry_selected_readmode);
            viewHolder.cb_done.setOnClickListener((NoteListActivity)context);
            viewHolder.tv_text = (TextView) ll_entry_readMode.findViewById(R.id.tv_entry_text_readmode);

            ((LinearLayout)rowView).addView(ll_entry_readMode);
            rowView.setTag(viewHolder);

        }else
            viewHolder = (ViewHolderInReadMode) rowView.getTag();


        // fill viewHolder
        viewHolder.cb_done.setChecked(itemList.get(position).isDone());
        viewHolder.cb_done.setTag(position);
        viewHolder.tv_text.setText(itemList.get(position).getText());

        return rowView;
    }

    public int getEntryInEditModePosition(){
        Log.d(TAG, "getEntryInEditModePosition()");
        return entryInEditModePosition;
    }

    private boolean isEntryInEditMode(int position){
        return position == entryInEditModePosition;
    }

    public boolean isAnyEntryInEditMode(){
        Log.d(TAG, "isAnyEntryInEditMode()");
        return entryInEditModePosition != NONE;
    }

    public void setEntryInEditModePosition(int position){
        Log.d(TAG, "setEntryInEditModePosition()");
        Log.d(TAG, "position="+position);
        entryInEditModePosition = position;
    }

    public void resetEntryInEditMode(){
        Log.d(TAG, "resetEntryInEditMode()");
        entryInEditModePosition = NONE;
    }
}