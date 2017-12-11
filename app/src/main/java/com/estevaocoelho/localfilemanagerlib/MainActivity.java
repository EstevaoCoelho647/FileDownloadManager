package com.estevaocoelho.localfilemanagerlib;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import static android.Manifest.*;

/**
 * Created by estevaocoelho on 23/11/17.
 */

public class MainActivity extends AppCompatActivity {
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);
        Button buttonDownload = findViewById(R.id.buttonDownload);

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Verify permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    int result = ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{permission.READ_EXTERNAL_STORAGE},
                                1);
                    } else {
                        doDownload();
                    }
                }
            }
        });
    }

    private void doDownload() {
        FileManager.getFileManagerInstance().getFileFromLocalOrDownload(
                "http://blog.fulfillinghappiness.com/wp-content/uploads/2012/05/shutterstock_79429915.jpg",
                "success.jpg",
                new OnFileDownloadCallback() {
                    @Override
                    public void onItemDownloaded(File file) {
                        //Your file was downloaded with success!
                        Picasso.with(MainActivity.this).load(file).into(mImageView);
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onItemDownloadError() {
                        //Error on download. Verify Log status
                        Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    // permission denied
                    Toast.makeText(MainActivity.this,
                            "Permission denied to read your External storage",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // permission was granted
                    doDownload();
                }
                return;
            }
        }
    }
}
