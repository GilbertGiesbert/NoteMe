package com.mediaworx.noteme.notelist.model;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;
import com.mediaworx.noteme.common.labledtypes.ColorType;
import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.common.filtering.FilterType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class NoteList implements Serializable {

    private long id;

    private String title;

    private ColorType color;

    private Date dateCreated;
    private Date lastEdit;

    private SortType sortType;

    private FilterType filterType;

    private HashMap<Long, Note> notes;

    public NoteList(long id) {
        this.title =  App.getAppContext().getResources().getString(R.string.newList)+" "+id;
        this.notes = new HashMap<>();
        this.color = ColorType.WHITE;
        this.dateCreated = new Date();
        this.sortType = SortType.LAST_EDITED_TOP;
        this.filterType = FilterType.FILTER_NOFILTER;
        this.id = id;
    }

    public Note getNote(long noteId){
        return notes.get(noteId);
    }

    public ArrayList<Note> getNotes(){

        ArrayList<Note> notes = new ArrayList<>();
        notes.addAll(this.notes.values());
        return notes;
    }

    public void putNote(Note note){
        notes.put(note.getId(), note);
    }

    public Note createNote(){

        long newId = buildNewNoteId();
        Note newNote = new Note(newId,this);
        notes.put(newId, newNote);

        return newNote;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void removeNote(long noteId){
        notes.remove(noteId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ColorType getColor() {
        return color;
    }

    public void setColor(ColorType color) {
        this.color = color;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    private long buildNewNoteId(){

        Set<Long> assignedNoteIds = notes.keySet();

        long newId = 1;
        while(assignedNoteIds.contains(newId))
            newId++;

        return newId;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }
}