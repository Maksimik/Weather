package com.maksimik.weather.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.maksimik.weather.db.DbHelper;
import com.maksimik.weather.db.IDbOperations;
import com.maksimik.weather.db.ViewedCitesTable;
import com.maksimik.weather.db.WeatherTable;

import java.util.ArrayList;

public class PresenterViewedCites implements ContractViewedCites.Presenter {

    private ContractViewedCites.View view;
    private IDbOperations operations;
    private Handler handler;

    public PresenterViewedCites(Context context, @NonNull ContractViewedCites.View view) {

        this.view = view;
        operations = new DbHelper(context, 1);
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void getListViewedCitesFromDb() {

        new Thread() {
            @Override
            public void run() {

                Cursor cursor = operations.query("SELECT * FROM "
                        + DbHelper.getTableName(ViewedCitesTable.class));

                ArrayList<String> name=null;
                ArrayList<Integer> id=null;
                if (cursor.moveToFirst()) {

                    name=new ArrayList<>();
                    id = new ArrayList<>();

                    do {
                        name.add(cursor.getString(cursor.getColumnIndex(ViewedCitesTable.NAME)));
                        id.add(cursor.getInt(cursor.getColumnIndex(ViewedCitesTable.ID)));

                    } while (cursor.moveToNext());
                }

                cursor.close();
                notifyResponse(id,name);

            }
        }.start();

    }

    @Override
    public void addCites(final int id, final String name) {

        new Thread() {
            @Override
            public void run() {

                Cursor cursor = operations.query("SELECT * FROM "
                        + DbHelper.getTableName(ViewedCitesTable.class)
                        + " WHERE "
                        + ViewedCitesTable.ID
                        + "=?", Integer.toString(id));

                if (!cursor.moveToFirst()) {

                    ContentValues values = new ContentValues();

                    values.put(ViewedCitesTable.ID, id);
                    values.put(ViewedCitesTable.NAME, name);

                    operations.insert(ViewedCitesTable.class, values);

                }
                cursor.close();
                notifyResponse();
            }
        }.start();


    }

    @Override
    public void deleteCites(final int id) {
        new Thread() {
            @Override
            public void run() {

                operations.delete(ViewedCitesTable.class, ViewedCitesTable.ID+"=?", String.valueOf(id));

                operations.delete(WeatherTable.class, WeatherTable.ID+"=?", String.valueOf(id));

            }
        }.start();
    }

    private void notifyResponse() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.showFinish();
            }
        });
    }

    private void notifyResponse(final ArrayList<Integer> id, final ArrayList<String> name) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.showListCites(id, name);

            }
        });
    }
}
