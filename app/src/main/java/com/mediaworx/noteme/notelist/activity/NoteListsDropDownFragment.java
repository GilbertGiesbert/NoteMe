package com.mediaworx.noteme.notelist.activity;

import android.util.Log;

import com.mediaworx.noteme.common.dropdown.DropDownFragment;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.ArrayList;
import java.util.List;


public class NoteListsDropDownFragment extends DropDownFragment<NoteList> {

    private static final String TAG = NoteListsDropDownFragment.class.getSimpleName();

    protected List<String> getLabels(){
        Log.d(TAG, "getLabels()");

        ArrayList<String> labels = new ArrayList<>();
        for(NoteList selectable: selectables)
            labels.add(selectable.getTitle());

        return labels;
    }

    protected int getPositionToPreselect(){
        Log.d(TAG, "getPositionToPreselect()");

        for(int i = 0; i < selectables.size(); i++)
            if(selectables.get(i).getId() == preselected.getId())
                return i;

        return 0;
    }
}