package com.mediaworx.noteme.notelist.utils;


import android.util.Log;

import com.mediaworx.noteme.notelist.comparator.NotesComparator;
import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.common.filtering.FilterType;
import com.mediaworx.noteme.notelist.filter.NotesFilter;
import com.mediaworx.noteme.notelist.model.Note;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.ArrayList;
import java.util.Collections;

public class NoteListFunctions {

    private static final String TAG = NoteListFunctions.class.getSimpleName();

    private static void sortNoteList(ArrayList<Note> noteList, SortType sortType){
        Log.d(TAG, "sortNoteList()");
        Log.d(TAG, "sortType="+ sortType);

        NotesComparator comparator = new NotesComparator(sortType);
        Collections.sort(noteList, comparator);
    }

    private static void filterNoteList(ArrayList<Note> noteList, FilterType filterType){
        Log.d(TAG, "filterNoteList()");
        Log.d(TAG, "filterCriteriaType="+ filterType);

        ArrayList<Note> filteredList = new ArrayList<>();
        filteredList.addAll(noteList);
        filteredList = NotesFilter.meetCriteria(filterType, filteredList);

        noteList.clear();
        noteList.addAll(filteredList);
    }

    public static void updateDisplayList(ArrayList<Note> displayList, NoteList dataList){
        Log.d(TAG, "refreshDisplayList()");

        displayList.clear();
        displayList.addAll(dataList.getNotes());

        filterNoteList(displayList, dataList.getFilterType());
        sortNoteList(displayList, dataList.getSortType());
    }

    public static void mockNoteList(int size, NoteList dataList, ArrayList<Note> displayList) {

        for(Note note: dataList.getNotes())
            dataList.removeNote(note.getId());

        char c = 'a';
        for(int i = 0; i < size; i++){

            Note note = new Note(i);
            note.setText(i +" "+ c++);
            if(i < 3)note.setDone(true);
            dataList.putNote(note);
        }
        updateDisplayList(displayList, dataList);
    }
}