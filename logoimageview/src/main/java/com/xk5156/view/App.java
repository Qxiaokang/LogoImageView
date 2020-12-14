package com.xk5156.view;

import android.app.Application;
import android.content.Context;

import java.io.File;

/**
 * author: xk
 * date: 2020/11/27
 * description:
 * version: 1.0
 */
public class App extends Application {
    private static Context context;
    private static String logoPath="";

    public static String getLogoPath() {
        return logoPath;
    }

    public Context getContext() {
        return context;
    }

    public App getInstance(){
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        logoPath=getCacheDir()+ File.separator+"imgs/";
    }

}
