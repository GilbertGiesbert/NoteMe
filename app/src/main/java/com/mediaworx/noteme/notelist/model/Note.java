package com.mediaworx.noteme.notelist.model;


import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    private long id;

    private Date dateCreated;
    private Date lastEdit;
    private boolean done;
    private String text;

    private NoteList noteList;

    public Note() {
        this.id = -1;
        this.text = "";
        this.dateCreated = new Date();
        this.lastEdit = new Date();
        this.done = false;
    }

    public Note(long id) {
        this();
        this.id = id;
    }

    public Note(NoteList noteList) {
        this();
        this.noteList = noteList;
    }

    public Note(long id,NoteList noteList) {
        this(id);
        this.noteList = noteList;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Note(int id) {
        this.id = id;
        dateCreated = new Date();
        lastEdit = new Date();
        done = false;
        text = "";
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
        lastEdit = new Date();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        lastEdit = new Date();
    }

    public NoteList getNoteList() {
        return noteList;
    }

    public void setNoteList(NoteList noteList) {
        this.noteList = noteList;
    }
}