package com.mediaworx.noteme.common.storage;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;

/**
 * Created by martink on 04.03.2015.
 */
public enum StorageType {

    INTERNAL (R.string.storageType_file),
    SD_CARD  (R.string.storageType_sd_card), // caution location of sd card folder is not always physically on sd card
    DATABASE (R.string.storageType_database),
    CLOUD (R.string.storageType_cloud);


    private int labelId;

    private StorageType(int labelId){
        this.labelId = labelId;
    }

    public String getLabel(){
        return App.getAppContext().getResources().getString(labelId);
    }

    public static String[] labelsAsArray(){

        String[] labels = new String[StorageType.values().length];
        for(int i = 0; i < labels.length; i++)
            labels[i] = StorageType.values()[i].getLabel();
        return labels;
    }
}
