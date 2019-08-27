package com.turner.whit.revstabletv2;

import android.app.Application;
import android.content.Context;

//The only reason this class exisits is to by-pass the problem with the Android platform
//which prohibits Utility classes (like Utils.java) from using context.

public class GlobalApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }

    public static Context getAppContext() {
        return appContext;
    }
}