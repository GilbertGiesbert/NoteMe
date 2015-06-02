package com.mediaworx.noteme.notelist.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.notelist.activity.NoteListOverviewActivity;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.ArrayList;
import java.util.List;

public class NoteListsArrayAdapter extends ArrayAdapter<NoteList> {

    private static final String TAG = NoteListsArrayAdapter.class.getSimpleName();

    private final Context context;
    private final List<NoteList> noteLists;

    private static final int NONE = -1;
    private int entryInEditModePosition;

    static class ViewHolderForReadMode {
        public TextView tv_text;
    }

    static class ViewHolderForEditMode {
        public EditText et_text;
        public ImageButton bt_confirm;
        public ImageButton bt_delete;
    }

    public NoteListsArrayAdapter(Context context, List<NoteList> noteLists) {
        super(context, R.layout.notelistoverview_entry_container, noteLists);
        this.context = context;
        this.noteLists = noteLists;
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

        // reuse or recreate?
        if (rowView == null || !(rowView.getTag() instanceof ViewHolderForEditMode)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.notelistoverview_entry_container, null);

            LinearLayout ll_entry_editMode = (LinearLayout) inflater.inflate(R.layout.notelistoverview_entry_editmode, null);

            // configure view holder
            ViewHolderForEditMode viewHolder = new ViewHolderForEditMode();
            viewHolder.et_text = (EditText) ll_entry_editMode.findViewById(R.id.et_noteListOverview_entry_text_editmode);
            viewHolder.bt_confirm = (ImageButton) ll_entry_editMode.findViewById(R.id.bt_noteListOverview_entry_confirm);
            viewHolder.bt_delete = (ImageButton) ll_entry_editMode.findViewById(R.id.bt_noteListOverview_entry_delete);

            viewHolder.bt_confirm.setOnClickListener((NoteListOverviewActivity)context);
            viewHolder.bt_delete.setOnClickListener((NoteListOverviewActivity)context);

            ((LinearLayout)rowView).addView(ll_entry_editMode);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolderForEditMode holder = (ViewHolderForEditMode) rowView.getTag();
        holder.et_text.setText(noteLists.get(position).getTitle());
        holder.et_text.requestFocus();
        holder.et_text.setBackgroundColor(noteLists.get(position).getColor().getColorValue());

        return rowView;
    }

    private View getRowInReadMode(int position, View rowView) {

        // reuse or recreate?
        if (rowView == null || !(rowView.getTag() instanceof ViewHolderForReadMode)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.notelistoverview_entry_container, null);

            LinearLayout ll_entry_readMode = (LinearLayout) inflater.inflate(R.layout.notelistoverview_entry_readmode, null);

            // configure view holder
            ViewHolderForReadMode viewHolder = new ViewHolderForReadMode();
            viewHolder.tv_text = (TextView) ll_entry_readMode.findViewById(R.id.tv_noteListOverview_entry_text_readmode);

            ((LinearLayout)rowView).addView(ll_entry_readMode);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolderForReadMode holder = (ViewHolderForReadMode) rowView.getTag();
        holder.tv_text.setText(noteLists.get(position).getTitle());
        holder.tv_text.setBackgroundColor(noteLists.get(position).getColor().getColorValue());

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