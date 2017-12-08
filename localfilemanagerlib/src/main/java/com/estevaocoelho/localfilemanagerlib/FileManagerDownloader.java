package com.estevaocoelho.localfilemanagerlib;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by estevaocoelho on 23/11/17.
 */

class FileManagerDownloader extends AsyncTask<Void, Void, Void> {

    private File file;
    private String url;
    private OnFileDownloadCallback downloadCallback;
    private boolean success;

    public FileManagerDownloader(File file, String url, OnFileDownloadCallback downloadCallback) {
        this.file = file;
        this.url = url;
        this.downloadCallback = downloadCallback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(FileManager.TAG, "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage());
            }
            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(file);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                output.write(data, 0, count);
            }
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (success)
            downloadCallback.OnItemDownloaded(file);
        else downloadCallback.OnItemDownloadError();
    }
}
