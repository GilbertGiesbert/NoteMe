package com.mediaworx.noteme.common.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by martink on 02.03.2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "noteMeDB.db";

    // Table Names
    public static final String TABLE_NOTE_LISTS = "NoteLists";
    public static final String TABLE_NOTES = "Notes";

    // Common column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_LAST_UPDATED_AT = "last_updated_at";

    // Note lists Table - column names
    public static final String NOTE_LISTS_COLUMN_TITLE = "title";
    public static final String NOTE_LISTS_COLUMN_COLOR = "color";

    // Notes Table - column names
    public static final String NOTES_COLUMN_TITLE = "title";
    public static final String NOTES_COLUMN_DONE = "done";
    public static final String NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST = "fk_note_list";

    // Note lists table create statement
    private static final String CREATE_TABLE_NOTE_LISTS = "CREATE TABLE " + TABLE_NOTE_LISTS
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + NOTE_LISTS_COLUMN_TITLE + " TEXT not null,"
            + NOTE_LISTS_COLUMN_COLOR + " TEXT,"
            + COLUMN_CREATED_AT + " TIMESTAMP ,"
            + COLUMN_LAST_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")";

    // Note lists table create statement
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + NOTES_COLUMN_TITLE + " TEXT not null,"
            + NOTES_COLUMN_DONE + " BOOLEAN,"
            + COLUMN_CREATED_AT + " TIMESTAMP,"
            + COLUMN_LAST_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST + " INTEGER,"
            + " FOREIGN KEY ("+NOTES_COLUMN_FOREIGN_KEY_NOTE_LIST+") REFERENCES "
            + TABLE_NOTE_LISTS +" ("+COLUMN_ID+"));";



    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Is called by the framework, if the database is accessed but not yet created.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE_LISTS);
        db.execSQL(CREATE_TABLE_NOTES);
    }

    /**
     *  Called, if the database version is increased in your application code.
     *  This method allows you to update an existing database schema or to drop the existing
     *  database and recreate it via the onCreate() method.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void deleteAllRows() {
        getWritableDatabase().delete(TABLE_NOTES, null, null);
        getWritableDatabase().delete(TABLE_NOTE_LISTS, null, null);
    }
}
