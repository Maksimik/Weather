package com.maksimik.weather;

import android.app.Application;

import com.maksimik.weather.utils.ContextHolder;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextHolder.getInstance().setContext(this);
    }
}