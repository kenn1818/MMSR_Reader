package com.example.pc.mmsr_reader;

import android.app.Application;
import android.content.Context;

/**
 * Created by pc on 11/6/2017.
 */

public class Mmsr extends Application {
    private static Mmsr sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Mmsr getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
