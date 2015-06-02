package com.mediaworx.noteme.notelist.filter;

import com.mediaworx.noteme.common.filtering.FilterCombinationType;
import com.mediaworx.noteme.common.filtering.FilterType;
import com.mediaworx.noteme.notelist.model.Note;

import java.util.ArrayList;

public class NotesFilter {

    private static final String TAG = NotesFilter.class.getSimpleName();

    public static ArrayList<Note> meetCriteria(FilterType filterType, ArrayList<Note> source) {

        ArrayList<Note> result = new ArrayList<>();

        switch (filterType) {

            case FILTER_NOFILTER:
                result.addAll(source);
                break;

            case FILTER_CHECKED:
                for (Note note : source) {
                    if (note.isDone())
                        result.add(note);
                }
                break;

            case FILTER_UNCHECKED:
                for (Note note : source) {
                    if (!note.isDone())
                        result.add(note);
                }
                break;
        }
        return result;
    }

    public static ArrayList<Note> meetCriterias(FilterType firstCriteria, FilterType secondCriteria, FilterCombinationType filterCombinationType, ArrayList<Note> source){

        ArrayList<Note> result = new ArrayList<>();

        switch (filterCombinationType) {

            case AND:
                result.addAll(meetCriteria(firstCriteria, source));
                result.addAll(meetCriteria(secondCriteria, result));
                break;

            case OR:
                ArrayList<Note> firstResult = meetCriteria(firstCriteria, source);
                ArrayList<Note> secondResult = meetCriteria(secondCriteria, source);

                result.addAll(firstResult);

                for(Note note: secondResult){
                    if(!firstResult.contains(note))
                        result.add(note);
                }
                break;
        }
        return result;
    }
}