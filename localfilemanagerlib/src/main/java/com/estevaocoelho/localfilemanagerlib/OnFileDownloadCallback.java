package com.estevaocoelho.localfilemanagerlib;

import java.io.File;

/**
 * Callback interface to return file if success on download
 * or to return error
 * Created by estevaocoelho on 23/11/17.
 */

public interface OnFileDownloadCallback {
    /**
     * Callback method of download success
     */
    void onItemDownloaded(File file);

    /**
     * Callback of error on download
     */
    void onItemDownloadError();
}
