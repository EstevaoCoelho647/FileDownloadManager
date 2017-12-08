package com.estevaocoelho.localfilemanagerlib;

/**
 * Created by estevaocoelho on 23/11/17.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileManager.create(this);
    }
}
