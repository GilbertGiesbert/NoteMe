package com.mediaworx.noteme.notelist.comparator;


import android.util.Log;

import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.Comparator;

public class NoteListsComparator implements Comparator<NoteList> {

    private static final String TAG = NoteListsComparator.class.getSimpleName();

    private SortType sortType;

    public NoteListsComparator(SortType sortType){
        Log.d(TAG, "NoteListsComparator()");
        Log.d(TAG, "sortType="+ sortType);
        this.sortType = sortType;
    }

    public void setSortType(SortType sortType){
        Log.d(TAG, "setSortType()");
        Log.d(TAG, "sortType="+ sortType);
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
    public int compare(NoteList leftList, NoteList rightList) {
        if (leftList != null && rightList != null) {
            switch (sortType) {

                case DATE_CREATED:
                    return leftList.getDateCreated().compareTo(rightList.getDateCreated());

                case DATE_CREATED_REVERSE:
                    return rightList.getDateCreated().compareTo(leftList.getDateCreated());

                case TITLE:
                    return leftList.getTitle().compareTo(rightList.getTitle());

                case TITLE_REVERSE:
                    return rightList.getTitle().compareTo(leftList.getTitle());
            }
        }
        int equal = 0;
        return equal;
    }
}