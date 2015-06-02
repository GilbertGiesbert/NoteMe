package com.mediaworx.noteme.common.application;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mediaworx.noteme.common.constants.Preferences;
import com.mediaworx.noteme.common.sorting.SortType;
import com.mediaworx.noteme.common.storage.StorageType;
import com.mediaworx.noteme.notelist.activity.NoteListTitleStyle;

public class PreferencesManager {

    private static final String TAG = PreferencesManager.class.getSimpleName();

    private static String getPreferences(Preferences prefType){
        Log.d(TAG, "getPreferences()");
        Log.d(TAG, "prefType="+prefType);

        Context context = App.getAppContext();
        SharedPreferences preferences = context.getSharedPreferences(Preferences.APP_PREFERENCES+"", Context.MODE_PRIVATE);

        String empty = "";
        String foundPref = preferences.getString(prefType+"", empty);
        Log.d(TAG, "foundPref="+foundPref);

        return foundPref;
    }

    public static void setPreferences(Preferences prefType, String prefValue){
        Log.d(TAG, "setPreferences()");
        Log.d(TAG, "prefType="+prefType);
        Log.d(TAG, "prefValue="+prefValue);

        Context context = App.getAppContext();

        SharedPreferences preferences = context.getSharedPreferences(Preferences.APP_PREFERENCES+"", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(prefType+"", prefValue);
        editor.apply();
    }


    public static SortType getNoteListsSortType(){
        Log.d(TAG, "getNoteListsSortType()");

        String string = getPreferences(Preferences.PREF_NOTELISTS_SORTTYPE);
        SortType _default = SortType.DATE_CREATED_REVERSE;

        try {
            return SortType.valueOf(string);

        }catch (Exception e) {
            Log.d(TAG, "found no preferences for " + _default.getClass() + ", returning default");
            return _default;
        }
    }

    public static void setNoteListsSortType(SortType type){
        Log.d(TAG, "setNoteListsSortType()");
        Log.d(TAG, "type="+type);

        setPreferences(Preferences.PREF_NOTELISTS_SORTTYPE,""+type);
    }

    public static NoteListTitleStyle getNoteListTitleStyle(){
        Log.d(TAG, "getNoteListTitleStyle()");


        String string = getPreferences(Preferences.PREF_NOTELIST_TITLESTYLE);
        NoteListTitleStyle _default = NoteListTitleStyle.EDIT_TEXT;

        try {
            return NoteListTitleStyle.valueOf(string);

        }catch (Exception e) {
            Log.d(TAG, "no preferences for " + _default.getClass() + ", returning default");
            return _default;
        }
    }

    public static void setNoteListTitleStyle(NoteListTitleStyle style){
        Log.d(TAG, "setNoteListTitleStyle()");
        Log.d(TAG, "style="+style);

        setPreferences(Preferences.PREF_NOTELIST_TITLESTYLE,""+style);
    }


    public static void setStorageType(StorageType storageType){
        Log.d(TAG, "setStorageType()");
        Log.d(TAG, "storageType="+storageType);

        setPreferences(Preferences.PREF_STORAGE_TYPE,""+storageType);
    }

    public static StorageType getStorageType(){
        Log.d(TAG, "getStorageType()");

        String string = getPreferences(Preferences.PREF_STORAGE_TYPE);
        StorageType _default = StorageType.INTERNAL;

        try {
            return StorageType.valueOf(string);

        }catch (Exception e) {
            Log.d(TAG, "found no preferences for " + _default.getClass() + ", returning default");
            return _default;
        }
    }
}