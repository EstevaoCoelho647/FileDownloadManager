package com.estevaocoelho.localfilemanagerlib;

import android.app.Application;
import android.util.Log;

import java.io.File;

/**
 * Created by estevaocoelho on 23/11/17.
 */

public class FileManager {
    static final String TAG = "FileManager";
    private static FileManager fileManagerInstance;
    private String basePath;

    /**
     * Constructor
     *
     * @param basePath Base path of directory
     */
    private FileManager(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Constructor
     * Base path will be Android/data/your.application.package/files
     *
     * @param application Application instance
     */
    private FileManager(Application application) {
        this.basePath = application.getBaseContext().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * Method to create a FileManager instance with a customized base path
     *
     * @param basePath Base path of directory
     */
    public static void create(String basePath) {
        if (fileManagerInstance == null) {
            fileManagerInstance = new FileManager(basePath);
            Log.d(TAG, "Created new FileManager instance");
        } else Log.d(TAG, "FileManager instance already exists");
    }

    /**
     * Method to create a FileManager instance with default base path
     *
     * @param application Application instance
     */
    static void create(Application application) {
        if (fileManagerInstance == null) {
            fileManagerInstance = new FileManager(application);
            Log.d(TAG, "Created new FileManager instance");
        } else Log.d(TAG, "FileManager instance already exists");
    }

    /**
     * Singleton to get File Manager Instance
     *
     * @return instance of FileManager
     */
    synchronized static FileManager getFileManagerInstance() {
        if (fileManagerInstance == null)
            throw new IllegalStateException("FileManager instance is null. Please instantiate your FileManager on Application class");
        else
            return fileManagerInstance;
    }

    /**
     * Method to use to get a file:
     * if file not exists method will download your archive and save in a file
     *
     * @param url              to download file
     * @param fileName         this is a name of file. Need to have type like ".jpg", ".mp3", ".mp4"...
     * @param subPath          Also base path you can choose a sub path that will concatenated with base path
     * @param downloadCallback Callback to return your downloaded file
     */
    void getFileFromLocalOrDownload(String url, String fileName, String subPath, OnFileDownloadCallback downloadCallback) {
        File baseFile = new File(basePath);

        if (subPath != null) {
            if (subPath.contains("/")) {
                String[] split = subPath.split("/");
                for (String aSplit : split) {
                    File newFile = new File(baseFile, aSplit);
                    newFile.mkdir();
                    baseFile = new File(baseFile, aSplit);
                }
            } else
                baseFile = new File(baseFile, subPath);
        }
        baseFile.mkdirs();

        File outputFile = new File(baseFile, fileName);
        if (FileManagerUtil.fileAlreadyExists(outputFile)) {
            downloadCallback.OnItemDownloaded(outputFile);
        } else {
            new FileManagerDownloader(outputFile, url, downloadCallback).execute();
        }
    }

    /**
     * Method to use to get a file:
     * if file not exists method will download your archive and save in a file
     *
     * @param url              to download file
     * @param fileName         this is a name of file. Need to have type like ".jpg", ".mp3", ".mp4"...
     * @param downloadCallback Callback to return your downloaded file
     */
    void getFileFromLocalOrDownload(String url, String fileName, OnFileDownloadCallback downloadCallback) {
        getFileFromLocalOrDownload(url, fileName, null, downloadCallback);
    }
}
