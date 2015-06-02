package com.mediaworx.noteme.common.storage;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.mediaworx.noteme.notelist.dao.impl.NoteListFileStorageDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FileStorage<T> {

    private static final String TAG = FileStorage.class.getSimpleName();


    private String[] storageFolders = new String[] {NoteListFileStorageDAO.FILE_STORAGE_FOLDER_NAME_NOTE_LIST};

    public boolean writeData(Context context,
                             StorageType storageType,
                             String folderPath,
                             String fileName,
                             T object){
        Log.d(TAG, "writeData()");
        Log.d(TAG, "fileName="+fileName);

        boolean writeSuccessful = false;

        try{
            // get the folder
            File folder = new File(getAbsoluteFolderPath(context, storageType, folderPath));
            folder.mkdirs();

            String fullFilePath = folder.getAbsolutePath() + File.separator + fileName;
            Log.d(TAG, "fullFilePath="+fullFilePath);

            File file = new File(fullFilePath);

            ObjectOutputStream objectWriter = new ObjectOutputStream(new FileOutputStream(file));

            objectWriter.writeObject(object);
            objectWriter.close();

            writeSuccessful = true;

        }catch (Exception e){
            Log.d(TAG, "problems writing file", e);
        }

        Log.d(TAG, "writeSuccessful="+writeSuccessful);
        return writeSuccessful;
    }

    public int deleteData(Context context,
                          StorageType storageType,
                          String folderPath,
                          String fileName){

        int delteNumberOfRows = 0;
        Log.d(TAG, "deleteData()");
        Log.d(TAG, "fileName="+fileName);

        boolean deleteSuccessful = false;

        try{
            // get the folder
            File folder = new File(getAbsoluteFolderPath(context, storageType, folderPath));
            if(folder.exists()){

                String fullFilePath = folder.getAbsolutePath() + File.separator + fileName;
                Log.d(TAG, "fullFilePath="+fullFilePath);

                File file = new File(fullFilePath);

                deleteSuccessful = file.delete();
                if (deleteSuccessful)
                    delteNumberOfRows = 1;

            }

        }catch (Exception e){
            Log.d(TAG, "problems deleting file", e);
        }

        Log.d(TAG, "deleteSuccessful="+deleteSuccessful);
        return delteNumberOfRows;
    }


    public int deleteData(Context context,
                          StorageType storageType){

        int delteNumberOfRows = 0;
        Log.d(TAG, "deleteData()");

        boolean deleteSuccessful = false;

        try{
            // get the folder
            for (String folderName : storageFolders) {

                File folder = new File(getAbsoluteFolderPath(context, storageType, folderName));
                if (folder.exists()) {

                    for (File file : folder.listFiles())
                        file.delete();

                    if (folder.listFiles().length == 0)
                        deleteSuccessful = true;
                }
            }
        }catch (Exception e){
            Log.d(TAG, "problems deleting folder", e);
        }

        Log.d(TAG, "deleteSuccessful="+deleteSuccessful);
        return delteNumberOfRows;
    }

    public T readData(Context context, StorageType storageType, String folderPath, String fileName){
        Log.d(TAG, "readData()");
        Log.d(TAG, "fileName="+fileName);

        T data = null;
        boolean readSuccessful = false;

        ObjectInput in;
        try{

            File folder = new File(getAbsoluteFolderPath(context, storageType, folderPath));
            if(folder.exists()) {

                String fullFilePath = folder.getAbsolutePath() + File.separator + fileName;
                Log.d(TAG, "fullFilePath=" + fullFilePath);
                File file = new File(fullFilePath);
                FileInputStream fileIn = new FileInputStream(file);

                in = new ObjectInputStream(fileIn);
                data = (T) in.readObject();
                in.close();

                readSuccessful = true;
            }
        }
        catch (Exception e){
            Log.e(TAG, "problems reading file", e);
        }

        Log.d(TAG, "readSuccessful="+readSuccessful);
        return readSuccessful ? data : null;
    }

    /**
     *
     * Lists names of files in specified folder that match filenameFilter.
     * Lists names of all files if filenameFilter is null.
     *
     * @param context
     * @param storageType
     * @param folderPath
     * @param filenameFilter
     * @return
     */

    public ArrayList<String> listFileNames(Context context, StorageType storageType, String folderPath, FilenameFilter filenameFilter){
        Log.d(TAG, "listFileNames()");
        Log.d(TAG, "storageType="+storageType);
        Log.d(TAG, "folderPath="+folderPath);
        Log.d(TAG, "filenameFilter="+filenameFilter);

        ArrayList<String> fileNames = new ArrayList<>();
        try{

            File folder = new File(getAbsoluteFolderPath(context, storageType, folderPath));
            String[] fileNamesFound = folder.list(filenameFilter);

            if(fileNamesFound != null){
                Log.d(TAG, "fileNamesFound="+ Arrays.asList(fileNamesFound));
                fileNames.addAll(Arrays.asList(fileNamesFound));
            }else{
                Log.d(TAG, "found no matching files");
            }
        }
        catch (Exception e){
            Log.e(TAG, "problems reading file names", e);
        }
        Log.d(TAG, "fileNames="+Arrays.asList(fileNames));
        return fileNames;
    }

    /**
     * Method to get the absolute folder path of a passed relative path.
     *
     * @param context
     * @param storageType
     * @param relativeFolderPath
     * @return
     */
    private static String getAbsoluteFolderPath(Context context, StorageType storageType, String relativeFolderPath){
        Log.d(TAG, "getAbsoluteFolderPath()");
        Log.d(TAG, "storageType="+storageType);

        if(relativeFolderPath == null) return null;

        String basePath;
        if(StorageType.INTERNAL.equals(storageType)){
            basePath = context.getFilesDir().getAbsolutePath();
        }else{
            basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        Log.d(TAG, "basePath="+basePath);
        Log.d(TAG, "relativeFolderPath="+relativeFolderPath);

        relativeFolderPath = relativeFolderPath.trim();
        if(relativeFolderPath.length() > 0){
            return basePath + File.separator + relativeFolderPath;
        }else{
            return basePath;
        }
    }
}