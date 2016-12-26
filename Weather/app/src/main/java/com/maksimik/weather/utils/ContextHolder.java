package com.maksimik.weather.utils;

import android.content.Context;

public class ContextHolder {

    private static ContextHolder instance;

    private Context context;

    private ContextHolder() {
    }

    public static ContextHolder getInstance() {
        if (instance == null) {
            instance = new ContextHolder();
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context pContext) {
        context = pContext;
    }
}