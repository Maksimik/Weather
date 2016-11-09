package com.maksimik.weather.data;

import android.graphics.Bitmap;

import java.util.HashMap;

public class ListIcon {
    public HashMap<String, Bitmap> mList;

    public ListIcon() {
        mList = new HashMap<String, Bitmap>();
    }

    public Bitmap getIcon(String key) {
        return mList.get(key);
    }

    public void add(String key, Bitmap bitmap) {
        mList.put(key, bitmap);
    }

    public HashMap<String, Bitmap> getList() {
        return mList;
    }
}
