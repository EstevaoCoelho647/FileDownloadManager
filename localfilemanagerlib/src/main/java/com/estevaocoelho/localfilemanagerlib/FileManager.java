package com.estevaocoelho.localfilemanagerlib;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

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
     * @param basePath    base path to download files
     */
    private FileManager(Application application, String basePath) {
        this.application = application;
        this.basePath = basePath;
    }


    private FileManager(Application application) {
        this.application = application;
        this.basePath = application.getApplicationContext().getExternalFilesDir("FileManagerLib").getAbsolutePath();
    }

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
            throw new IllegalStateException("instance is null. Create your FileManager on Application class");
        else
            return fileManagerInstance;
    }


    public void getFileFromLocalOrDownload(String url, String fileName, String subPath, OnFileDownloadCallback downloadCallback) {
        String pathToDownload = basePath;
        File baseFile = new File(basePath);

        if (subPath != null) {
            pathToDownload = String.format("%s/%s", basePath, subPath);
            baseFile = new File(pathToDownload);
        }

        if (!baseFile.exists())
            baseFile.mkdir();

        File file = new File(baseFile.getAbsoluteFile(), fileName);

        if (FileManagerUtil.fileAlreadyExists(file)) {
            downloadCallback.OnItemDownloaded(file);
        } else {
            new FileManagerDownloader(file, url, downloadCallback).execute();
        }
    }

    public void getFileFromLocalOrDownload(String url, String fileName, OnFileDownloadCallback downloadCallback) {
        getFileFromLocalOrDownload(url, fileName, null, downloadCallback);
    }
}
