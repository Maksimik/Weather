package com.maksimik.weather.utils;


import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maksimik.weather.db.CitesTable;
import com.maksimik.weather.db.DbHelper;
import com.maksimik.weather.db.IDbOperations;

import java.util.HashMap;

public class PresenterCites implements ContractCites.Presenter {

    private ContractCites.View view;
    private IDbOperations operations;
    private Handler handler;

    public PresenterCites(Context context, @NonNull ContractCites.View view) {

        this.view = view;
        operations = new DbHelper(context, 1);
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void getListCitesFromDb(final String str) {

        new Thread() {
            @Override
            public void run() {

                notifyResponse(getCitesListDb(str));

            }
        }.start();

    }

    private void notifyResponse(final HashMap<String, Integer> data) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.showData(data);

            }
        });
    }


    @Nullable
    private HashMap<String, Integer> getCitesListDb(String str) {

        Cursor cursor = operations.query("SELECT * FROM "
                + DbHelper.getTableName(CitesTable.class)
                + " WHERE " + CitesTable.NAME_UPPER
                + " like ?", str.toUpperCase() + "%");

        if (cursor.moveToFirst()) {
            //ArrayList<String> list = new ArrayList<>();
            HashMap<String, Integer> list = new HashMap<>();

            do {
                String name = cursor.getString(cursor.getColumnIndex(CitesTable.NAME));
                int id = cursor.getInt(cursor.getColumnIndex(CitesTable.ID));

                //System.out.println("id: " + id + " name: " + name);

                list.put(name, id);
                //list.add(name);
            } while (cursor.moveToNext());

            return list;
        }

        cursor.close();
        return null;
    }

}
