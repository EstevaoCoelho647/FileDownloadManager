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
 * AsyncTask to run in another thread the download of file
 * Created by estevaocoelho on 23/11/17.
 */

class FileManagerDownloader extends AsyncTask<Void, Void, Void> {
    private File file;
    private String url;
    private OnFileDownloadCallback downloadCallback;
    private boolean success;

    /**
     * Constructor of FileManagerDownlader
     *
     * @param file             to save your archive
     * @param url              to download archive
     * @param downloadCallback to return the file of downloaded archive
     */
    public FileManagerDownloader(File file, String url, OnFileDownloadCallback downloadCallback) {
        this.file = file;
        this.url = url;
        this.downloadCallback = downloadCallback;
    }

    /**
     * Method to download and save in a file
     */
    @Override
    protected Void doInBackground(Void... voids) {
        //Initializing vars
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {
            //Trying to connect with URL
            URL url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            //if server connection response is not 200
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                //Log server result
                Log.d(FileManager.TAG, "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage());
            } else {
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(file);

                //write on file the data
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
            }
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        } finally {
            //Closing connection and files
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

    /**
     * When download is done this method is called
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        //if success on download return file else return error callback
        if (success)
            downloadCallback.onItemDownloaded(file);
        else downloadCallback.onItemDownloadError();
    }
}
