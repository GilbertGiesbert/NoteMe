package com.mediaworx.noteme.common.storage;



import com.mediaworx.noteme.common.application.App;
import com.mediaworx.noteme.common.application.PreferencesManager;


/**
 * Created by martink on 06.03.2015.
 */
public class StorageHelper {


    public static void deleteContent() {

        StorageType storageType = PreferencesManager.getStorageType();


        switch (storageType) {
            case INTERNAL:
                new FileStorage<>().deleteData(App.getAppContext(), StorageType.INTERNAL);
            case SD_CARD:
                new FileStorage<>().deleteData(App.getAppContext(), StorageType.SD_CARD);
            case DATABASE:
                new SQLiteHelper(App.getAppContext()).deleteAllRows();
                break;
            case CLOUD:
                break;

        }

    }
}