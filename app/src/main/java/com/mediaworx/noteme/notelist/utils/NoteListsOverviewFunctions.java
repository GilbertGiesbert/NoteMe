package com.mediaworx.noteme.notelist.utils;


import android.util.Log;

import com.mediaworx.noteme.common.application.PreferencesManager;
import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.notelist.comparator.NoteListsComparator;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteListsOverviewFunctions {

    private static final String TAG = NoteListsOverviewFunctions.class.getSimpleName();


    public static void sortNoteLists(List<NoteList> noteLists){
        Log.d(TAG, "sortNoteLists()");

        SortType sortType = PreferencesManager.getNoteListsSortType();
        Log.d(TAG, "sortType="+ sortType);

        NoteListsComparator comparator = new NoteListsComparator(sortType);
        Collections.sort(noteLists, comparator);
    }
}