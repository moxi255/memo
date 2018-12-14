package com.test.memo.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;
import org.litepal.util.Const;


public class MyApplication extends LitePalApplication {
    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();

        super.onCreate();

    }
    public static Context getContext(){
        return context;
    }
}
