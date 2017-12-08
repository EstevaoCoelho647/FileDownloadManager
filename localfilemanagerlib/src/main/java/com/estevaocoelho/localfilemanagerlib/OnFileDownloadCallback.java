package com.estevaocoelho.localfilemanagerlib;

import java.io.File;

/**
 * Created by estevaocoelho on 23/11/17.
 */

public interface OnFileDownloadCallback {
        void OnItemDownloaded(File file);

        void OnItemDownloadError();
}
