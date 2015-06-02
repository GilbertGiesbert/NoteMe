package com.mediaworx.noteme.notelist.dao.factory;

import android.content.Context;

import com.mediaworx.noteme.common.application.PreferencesManager;
import com.mediaworx.noteme.common.storage.StorageType;
import com.mediaworx.noteme.notelist.dao.NoteListDAO;
import com.mediaworx.noteme.notelist.dao.impl.NoteListDatabaseDAO;
import com.mediaworx.noteme.notelist.dao.impl.NoteListFileStorageDAO;

/**
 * Created by martink on 04.03.2015.
 */
public class NoteDAOFactory {


    public static NoteListDAO getDAO(Context context) {

        NoteListDAO dao;

        // get note lists
        StorageType storageType = PreferencesManager.getStorageType();

        switch (storageType) {
            case INTERNAL: dao = new NoteListFileStorageDAO(); break;
            case SD_CARD: dao = new NoteListFileStorageDAO(); break;
            case DATABASE: dao = new NoteListDatabaseDAO(context); break;
            case CLOUD: dao = new NoteListDatabaseDAO(context); break;
            default: dao = new NoteListFileStorageDAO(); break;
        }

        return dao;

    }
}
