package com.mediaworx.noteme.notelist.dao.impl;

import android.util.Log;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;
import com.mediaworx.noteme.common.storage.StorageType;
import com.mediaworx.noteme.notelist.dao.NoteListDAO;
import com.mediaworx.noteme.notelist.model.NoteList;
import com.mediaworx.noteme.common.storage.FileStorage;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Data access interface for {@link com.mediaworx.noteme.notelist.model.NoteList} objects.
 *
 * @author mko
 *
 */
public class NoteListFileStorageDAO implements NoteListDAO {

    private static final String TAG = NoteListFileStorageDAO.class.getSimpleName();

    public static final String FILE_STORAGE_FOLDER_NAME_NOTE_LIST = "noteList";
    public static  final String FILE_STORAGE_FILE_NAME_NOTE_LIST = "noteList_";

    protected FileStorage<NoteList> fileStorage;

    public NoteListFileStorageDAO() {
        fileStorage = new FileStorage<NoteList>();
    }

    @Override
    public int updateLight(NoteList noteList) {
        return 0;
    }

    private class NoteListFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String filename) {
            if(filename.startsWith(FILE_STORAGE_FILE_NAME_NOTE_LIST)){
                return true;
            }
            return false;
        }
    }

    public ArrayList<String> listAllNoteListFiles(){
        Log.i(TAG, "listAllNoteListFiles()");
        ArrayList<String> allNoteListFiles = fileStorage.listFileNames(App.getAppContext(), StorageType.INTERNAL, FILE_STORAGE_FOLDER_NAME_NOTE_LIST, new NoteListFilenameFilter());
        return allNoteListFiles;
    }


    /**
     * Method to read a note list from the storage.
     *
     * @param noteListId
     * @return
     */
    @Override
    public NoteList read(Long noteListId) {
        Log.i(TAG, "readNoteListById()");
        Log.i(TAG, "noteListId="+noteListId);

        NoteList noteList = readByFileName(FILE_STORAGE_FILE_NAME_NOTE_LIST + noteListId);
        return noteList;
    }


    protected NoteList readByFileName(String noteListFileName) {
        Log.i(TAG, "readNoteListByFileName()");
        Log.i(TAG, "noteListFileName="+noteListFileName);

        NoteList noteList = fileStorage.readData(App.getAppContext(), StorageType.INTERNAL, FILE_STORAGE_FOLDER_NAME_NOTE_LIST, noteListFileName);
        return noteList;
    }


    @Override
    public ArrayList<NoteList> readAll(){
        Log.i(TAG, "readAllNoteLists()");

        ArrayList<String> noteListFiles = listAllNoteListFiles();
        Log.i(TAG, "noteListFiles="+noteListFiles);

        ArrayList<NoteList> noteLists = new ArrayList<>();
        for(String noteListFileName: noteListFiles){
            noteLists.add(readByFileName(noteListFileName));
        }
        return noteLists;
    }

    /**
     * Method to write a note list to the storage.
     *
     */
    @Override
    public NoteList create(){
        ArrayList<NoteList> noteLists = readAll();
        Set<Long> assignedNoteListIds = new HashSet<>();

        for(NoteList tmpNoteList: noteLists){
            assignedNoteListIds.add(tmpNoteList.getId());

        }

        long newId = 1;
        while(assignedNoteListIds.contains(newId)){
            newId++;
        }

        NoteList noteList = new NoteList(newId);

        boolean writeSuccessful = fileStorage.writeData(App.getAppContext(), StorageType.INTERNAL, FILE_STORAGE_FOLDER_NAME_NOTE_LIST, FILE_STORAGE_FILE_NAME_NOTE_LIST +noteList.getId(), noteList);

        if (writeSuccessful)
            return noteList;
        else
            return null;
    }


    @Override
    public NoteList create(NoteList value) {
        return null;
    }

    @Override
    public int update(NoteList noteList) {
        ArrayList<NoteList> noteLists = readAll();

        boolean writeSuccessful = fileStorage.writeData(App.getAppContext(), StorageType.INTERNAL, FILE_STORAGE_FOLDER_NAME_NOTE_LIST, FILE_STORAGE_FILE_NAME_NOTE_LIST +noteList.getId(), noteList);

        if(writeSuccessful)
            return 1;
        else
            return 0;
    }

    @Override
    public int delete(final NoteList noteList) {
        return delete(noteList.getId());
    }

    @Override
    public int delete(Long noteListId) {
        return fileStorage.deleteData(App.getAppContext(), StorageType.INTERNAL, FILE_STORAGE_FOLDER_NAME_NOTE_LIST, FILE_STORAGE_FILE_NAME_NOTE_LIST +noteListId.longValue());
    }

}