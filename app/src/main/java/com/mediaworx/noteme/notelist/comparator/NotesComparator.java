package com.mediaworx.noteme.notelist.comparator;


import android.util.Log;

import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.notelist.model.Note;

import java.util.Comparator;

public class NotesComparator implements Comparator<Note> {

    private static final String TAG = NotesComparator.class.getSimpleName();

    private SortType sortType;

    public NotesComparator(SortType sortType){
        Log.i(TAG, "NoteComparator()");
        Log.i(TAG, "sortType="+ sortType);
        this.sortType = sortType;
    }

    public void setSortType(SortType sortType){
        Log.i(TAG, "setOrderBy()");
        Log.i(TAG, "sortType="+ sortType);
        this.sortType = sortType;
    }

    /**
    * @return   an integer < 0 if {@code leftList} is less than {@code rightList},
    *           0 if they are equal,
     *          and > 0 if {@code leftList} is greater than {@code rightList}.
    * @throws ClassCastException
    *         if objects are not of the correct type.
    */
    @Override
    public int compare(Note leftNote, Note rightNote) {

        switch(sortType){

            case DATE_CREATED:
                return leftNote.getDateCreated().compareTo(rightNote.getDateCreated());

            case DATE_CREATED_REVERSE:
                return rightNote.getDateCreated().compareTo(leftNote.getDateCreated());

            case LAST_EDITED_BOTTOM:
                return leftNote.getLastEdit().compareTo(rightNote.getLastEdit());

            case LAST_EDITED_TOP:
                return rightNote.getLastEdit().compareTo(leftNote.getLastEdit());

            case ALPHABETICALLY:
                return leftNote.getText().compareTo(rightNote.getText());

            case ALPHABETICALLY_REVERSE:
                return rightNote.getText().compareTo(leftNote.getText());

            case CHECKED_FIRST:

                if(leftNote.isDone() && !rightNote.isDone())
                    return -1;

                else if(!leftNote.isDone() && rightNote.isDone())
                    return 1;

                else
                    return 0;


            case UNCHECKED_FIRST:

                if(!leftNote.isDone() && rightNote.isDone())
                    return -1;

                else if(leftNote.isDone() && !rightNote.isDone())
                    return 1;

                else
                    return 0;
        }
        int equal = 0;
        return equal;
    }
}