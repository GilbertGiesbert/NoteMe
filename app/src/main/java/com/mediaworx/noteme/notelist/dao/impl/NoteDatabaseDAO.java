package com.mediaworx.noteme.notelist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mediaworx.noteme.common.storage.SQLiteHelper;
import com.mediaworx.noteme.notelist.dao.NoteDAO;
import com.mediaworx.noteme.notelist.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martink on 03.03.2015.
 */
public class NoteDatabaseDAO implements NoteDAO {



    // Database fields
    private SQLiteDatabase database;

    private SQLiteHelper dbHelper;


    private String[] noteColumns = { SQLiteHelper.NOTES_COLUMN_TITLE,
            SQLiteHelper.NOTES_COLUMN_DONE, SQLiteHelper.COLUMN_LAST_UPDATED_AT, SQLiteHelper.COLUMN_CREATED_AT };

    public NoteDatabaseDAO(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    @Override
    public Note read(Long noteId) {
        if (database == null)
            open();

        String selectQuery = "SELECT  * FROM " + SQLiteHelper.TABLE_NOTES + " WHERE "
                + SQLiteHelper.COLUMN_ID + " = " + noteId;

        Log.d("NoteDatabaseDAO.get", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
        note.setText((cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTES_COLUMN_TITLE))));
        note.setDone(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTES_COLUMN_DONE))));
        //note.setDateCreated(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CREATED_AT)));
        // note.setLastEdit(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_LAST_UPDATED_AT)));

        return note;
    }

    @Override
    public List<Note> readAllNotes(Long noteListId) {

        List<Note> notes = new ArrayList<>();

        if (database == null)
            open();

        String selectQuery = "SELECT  * FROM " + SQLiteHelper.TABLE_NOTES + " WHERE "
                + SQLiteHelper.NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST + " = " + noteListId;

        Log.d("NoteDatabaseDAO.get", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            Note note = new Note(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
            note.setText((cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTES_COLUMN_TITLE))));
            note.setDone(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTES_COLUMN_DONE))));
            //note.setDateCreated(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CREATED_AT)));
            // note.setLastEdit(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_LAST_UPDATED_AT)));
            notes.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return notes;
    }

    @Override
    public List<Long> readAllNoteIds(Long noteListId) {

        List<Long> noteIds = new ArrayList<>();

        if (database == null)
            open();

        String selectQuery = "SELECT  "+ SQLiteHelper.COLUMN_ID +" FROM " + SQLiteHelper.TABLE_NOTES + " WHERE "
                + SQLiteHelper.NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST + " = " + noteListId;

        Log.d("NoteDatabaseDAO.readAllNoteIds", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            noteIds.add(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return noteIds;
    }

    @Override
    public List<Note> readAll() {
        return null;
    }

    @Override
    public Note create(Note note) {


        if (database == null)
            open();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.NOTES_COLUMN_TITLE, note.getText());
        values.put(SQLiteHelper.NOTES_COLUMN_DONE, note.isDone());
        values.put(SQLiteHelper.NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST, note.getNoteList().getId());

        long insertId = database.insert(SQLiteHelper.TABLE_NOTES, null,
                values);

        note.setId(insertId);
        return note;
    }

    @Override
    public Note create() {
        Note note = null;

        if (database == null)
            open();
        ContentValues values = new ContentValues();
        long insertId = database.insert(SQLiteHelper.TABLE_NOTES, null,
                values);

        if (insertId != -1)
            note = new Note(insertId);

        if (note != null)
            update(note);

        return note;

    }

    @Override
    public int update(Note note) {
        if (database == null)
            open();

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.NOTES_COLUMN_TITLE, note.getText());
        values.put(SQLiteHelper.NOTES_COLUMN_DONE, note.isDone());

        // updating row
        return database.update(SQLiteHelper.TABLE_NOTES, values, SQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
    }


    @Override
    public int delete(Note note) {
        return delete(note.getId());
    }

    @Override
    public int delete(Long id) {
        System.out.println("note deleted with id: " + id);
        return database.delete(SQLiteHelper.TABLE_NOTES, SQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }


}
