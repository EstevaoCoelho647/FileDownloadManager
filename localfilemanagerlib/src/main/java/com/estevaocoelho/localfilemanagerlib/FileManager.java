package com.estevaocoelho.localfilemanagerlib;

import android.app.Application;
import android.util.Log;

import java.io.File;

/**
 * Created by estevaocoelho on 23/11/17.
 */

public class FileManager {
    public static final String TAG = "FileManager";
    private static FileManager fileManagerInstance;

    private Application application;
    private String basePath;

    /**
     * Constructor
     *
     * @param application Application instance
     * @param basePath    Base path of directory
     */
    private FileManager(Application application, String basePath) {
        this.application = application;
        this.basePath = basePath;
    }

    /**
     * Constructor
     *
     * @param application Application instance
     */
    private FileManager(Application application) {
        this.application = application;
        this.basePath = application.getBaseContext().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * Method to create a instance
     *
     * @param application Application instance
     * @param basePath    Base path of directory
     */
    public static void create(Application application, String basePath) {
        if (fileManagerInstance == null) {
            fileManagerInstance = new FileManager(application, basePath);
            Log.d(TAG, "Created new FileManager instance");
        } else Log.d(TAG, "FileManager instance already exists");
    }

    public static void create(Application application) {
        if (fileManagerInstance == null) {
            fileManagerInstance = new FileManager(application);
            Log.d(TAG, "Created new FileManager instance");
        } else Log.d(TAG, "FileManager instance already exists");
    }

    public synchronized static FileManager getFileManagerInstance() {
        if (fileManagerInstance == null)
            throw new IllegalStateException("FileManager instance is null. Please instantiate your FileManager on Application class");
        else
            return fileManagerInstance;
    }

    public void getFileFromLocalOrDownload(String url, String fileName, String subPath, OnFileDownloadCallback downloadCallback) {
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

    public void getFileFromLocalOrDownload(String url, String fileName, OnFileDownloadCallback downloadCallback) {
        getFileFromLocalOrDownload(url, fileName, null, downloadCallback);
    }
}
