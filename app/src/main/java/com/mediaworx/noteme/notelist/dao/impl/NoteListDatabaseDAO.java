package com.mediaworx.noteme.notelist.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mediaworx.noteme.common.labledtypes.ColorType;
import com.mediaworx.noteme.common.storage.SQLiteHelper;
import com.mediaworx.noteme.notelist.dao.NoteDAO;
import com.mediaworx.noteme.notelist.dao.NoteListDAO;
import com.mediaworx.noteme.notelist.model.Note;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martink on 03.03.2015.
 */
public class NoteListDatabaseDAO implements NoteListDAO {


    private SQLiteDatabase database;

    private SQLiteHelper dbHelper;

    private NoteDAO noteDAO;

    private String[] noteListsColumns = { SQLiteHelper.COLUMN_ID,
            SQLiteHelper.NOTE_LISTS_COLUMN_TITLE, SQLiteHelper.NOTE_LISTS_COLUMN_COLOR, SQLiteHelper.COLUMN_LAST_UPDATED_AT, SQLiteHelper.COLUMN_CREATED_AT };


    private String[] noteColumns = { SQLiteHelper.COLUMN_ID, SQLiteHelper.NOTES_COLUMN_TITLE,
            SQLiteHelper.NOTES_COLUMN_DONE, SQLiteHelper.COLUMN_LAST_UPDATED_AT, SQLiteHelper.COLUMN_CREATED_AT };

    public NoteListDatabaseDAO(Context context) {
        noteDAO = new NoteDatabaseDAO(context);
        dbHelper = new SQLiteHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    @Override
    public NoteList read(Long noteListId) {
        if (database == null)
            open();

        Cursor cursor = database.query(SQLiteHelper.TABLE_NOTE_LISTS,
                noteListsColumns, SQLiteHelper.COLUMN_ID + " = " + noteListId, null,
                null, null, null);


//        Cursor cursor = database.rawQuery("SELECT * " +
//                " FROM " + SQLiteHelper.TABLE_NOTE_LISTS + "," + SQLiteHelper.TABLE_NOTES +
//                " WHERE " + SQLiteHelper.TABLE_NOTES + SQLiteHelper.NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST + " = " + SQLiteHelper.TABLE_NOTE_LISTS + SQLiteHelper.COLUMN_ID
//                , null);

        if (cursor != null)
            cursor.moveToFirst();


        cursor.moveToFirst();

        NoteList noteList = new NoteList(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
        noteList.setTitle((cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTE_LISTS_COLUMN_TITLE))));
        noteList.setColor(ColorType.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTE_LISTS_COLUMN_COLOR))));
        //noteList.setDateCreated(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CREATED_AT)));
       // noteList.setLastEdit(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_LAST_UPDATED_AT)));


        cursor = database.query(SQLiteHelper.TABLE_NOTES,
                noteColumns, SQLiteHelper.NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST + " = " + noteListId, null,
                null, null, null);

//        String selectNotesQuery = "SELECT  * FROM " + SQLiteHelper.TABLE_NOTES + " WHERE "
//                + SQLiteHelper.NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST + " = " + noteListId;

        // looping through all rows and adding to list
        if (cursor != null)
            cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Note note = new Note(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)),noteList);
            note.setText((cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTES_COLUMN_TITLE))));
            note.setDone(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTES_COLUMN_DONE))));
            noteList.putNote(note);
            cursor.moveToNext();
        }
        cursor.close();

        return noteList;
    }

    @Override
    public List<NoteList> readAll() {
        if (database == null)
            open();

        List<NoteList> noteLists = new ArrayList<NoteList>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_NOTE_LISTS,
                noteListsColumns, null, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            NoteList noteList = new NoteList(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
            noteList.setTitle(cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTE_LISTS_COLUMN_TITLE)));
            noteList.setColor(ColorType.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.NOTE_LISTS_COLUMN_COLOR))));
            noteLists.add(noteList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return noteLists;
    }

    @Override
    public NoteList create(NoteList noteList) {
        return null;
    }

    @Override
    public NoteList create() {
        NoteList createdNoteList = null;

        if (database == null)
            open();

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.NOTE_LISTS_COLUMN_TITLE, "");
        values.put(SQLiteHelper.NOTE_LISTS_COLUMN_COLOR, ColorType.WHITE.toString());
        long insertId = database.insert(SQLiteHelper.TABLE_NOTE_LISTS, null,
                values);

        if (insertId != -1)
            createdNoteList = new NoteList(insertId);

        if (createdNoteList != null)
            update(createdNoteList);

        return createdNoteList;
    }

    @Override
    public int updateLight(NoteList noteList) {
        if (database == null)
            open();

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.NOTE_LISTS_COLUMN_TITLE, noteList.getTitle());
        values.put(SQLiteHelper.NOTE_LISTS_COLUMN_COLOR, noteList.getColor().toString());

        // updating row
        int updatedRows =  database.update(SQLiteHelper.TABLE_NOTE_LISTS, values, SQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(noteList.getId()) });


        return updatedRows;
    }


    @Override
    public int update(NoteList noteList) {
        int updatedRows = updateLight(noteList);

        if (updatedRows == 1) {
            // get all notes to delete the non existing one
            List<Long> noteIds = noteDAO.readAllNoteIds(noteList.getId());

            for (Note note : noteList.getNotes()) {
                if (note.getId() != -1 && noteIds != null && noteIds.contains(note.getId())) {
                    noteDAO.update(note);
                    // do not delete the upated one
                    if (noteIds.contains(note.getId()))
                        noteIds.remove(note.getId());
                } else
                    noteDAO.create(note);
            }

            // deletion
            for (Long noteId : noteIds) {
                noteDAO.delete(noteId);

            }
        }
        return updatedRows;
    }

    @Override
    public int delete(NoteList noteList) {
        long id = noteList.getId();
        return delete(id);
    }

    @Override
    public int delete(Long id) {
        System.out.println("Comment deleted with id: " + id);
        int deletedRows =  database.delete(SQLiteHelper.TABLE_NOTE_LISTS, SQLiteHelper.COLUMN_ID
                + " = " + id, null);

        List<Long> noteIds = noteDAO.readAllNoteIds(id);

        // deletion
        for (Long noteId : noteIds) {
            noteDAO.delete(noteId);

        }
        return deletedRows;
    }

//    private NoteList createNoteList(Cursor cursor) {
//        NoteList noteList = new NoteList(cursor.getLong(0));
//        noteList.setTitle(cursor.getString(1));
//        noteList.setColor(ColorType.valueOf(cursor.getString(1)));
//        return noteList;
//    }
}
