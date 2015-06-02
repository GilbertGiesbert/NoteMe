package com.mediaworx.noteme.common.storage;

import com.mediaworx.noteme.common.application.PreferencesManager;

/**
 * Created by martink on 04.03.2015.
 */
public class DAOFactory {



    public GenericDAO getDAOFactory() {

        StorageType storageType = PreferencesManager.getStorageType();

        GenericDAO dao = null;
        switch(storageType) {
            case INTERNAL: break;
            case SD_CARD: break;
            case DATABASE: break;
            case CLOUD: break;
        }

        return dao;

    }


}
