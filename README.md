# FileDownloadManager - Control your downloaded or not files
[![](https://jitpack.io/v/EstevaoCoelho647/FileDownloadManager.svg)](https://jitpack.io/#EstevaoCoelho647/FileDownloadManager)

This lib is a simple way to control dowloads of files. It's very helpfull in applications that have to download users images and keep file. If the application need to show same file agains the lib will verify if this file already exists, In positive case will return the file only. In negative case will download file and return it to your application.


## Prerequisites

This lib is supported on SDK >= 15.
Your application need to have 3 permissions:

    android.permission.INTERNET
    android.permission.READ_EXTERNAL_STORAGE
    android.permission.WRITE_EXTERNAL_STORAGE
    
*READ_EXTERNAL_STORAGE permission in SKD >= 21 need realtime permission.

## Configuring on the App
### Installing

Firstly add jitpack repository

    //root build.gradle
    ...
    allprojects {
        repositories {
           ...
           maven { url 'https://jitpack.io' } //add this line
        }
    }
Add gradle dependency

    implementation 'com.github.EstevaoCoelho647:FileDownloadManager:VERSION'

## Easy use
### 1. Create instance
In your application create a FileManager instance. Using this method all files will be saved in base path already defined in lib. The path is: _Android/data/your.application.package/files_

     @Override
        public void onCreate() {
            super.onCreate();

            //Instantiate FileManager with Application instance
            FileManager.create(this);
        }

### 2. Download something
In your Activity get the FileManager instance and call method _getFileFromLocalOrDownload()_. You need to pass the url file and the file name.*

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
  
_*File name need to have the file type as : "fileName.**jpg**", "fileName.**mp3**"..._

## Optional settings
### 1. Create instance and change base path
To change the base path you need to pass a new parameter in constructor.

     @Override
        public void onCreate() {
            super.onCreate();

            //Instantiate FileManager with Application instance
            FileManager.create("basePath/anotherPath");
        }

### 1. Download files to another directory.
Using a new parameter we can save the file in a specific directory

Example

Base path: _Android/data/your.application.package/files_

Sub path: _images/user_

The lib will save your file in _Android/data/your.application.package/files/images/user_

**Code:**


       FileManager.getFileManagerInstance().getFileFromLocalOrDownload(
                "http://blog.fulfillinghappiness.com/wp-content/uploads/2012/05/shutterstock_79429915.jpg",
                "success.jpg",
                "images/user", //subpath here
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


## Warning
In **SDK >= 21** you need to add realtime permission:

     //         Verify permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    int result = ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{permission.READ_EXTERNAL_STORAGE},
                                1);
                    } else {
                        // Permission granted
                    }
                }
            }
        });



