package com.maksimik.weather.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maksimik.weather.db.DbHelper;
import com.maksimik.weather.db.IDbOperations;
import com.maksimik.weather.db.ViewedCitesTable;
import com.maksimik.weather.db.WeatherTable;
import com.maksimik.weather.model.City;
import com.maksimik.weather.model.CityWithWeatherHour;
import com.maksimik.weather.model.Clouds;
import com.maksimik.weather.model.Main;
import com.maksimik.weather.model.Rain;
import com.maksimik.weather.model.Snow;
import com.maksimik.weather.model.Weather;
import com.maksimik.weather.model.WeatherHour;
import com.maksimik.weather.model.Wind;

import java.util.ArrayList;
import java.util.Date;

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
    public void getListViewedCities(final int idCity) {
        new Thread() {
            @Override
            public void run() {

                Cursor cursor = operations.query("SELECT * FROM "
                        + DbHelper.getTableName(ViewedCitesTable.class)
                        + " ORDER BY " + ViewedCitesTable.NAME);

                ArrayList<CityWithWeatherHour> list = null;
                String image = null;
                if (cursor.moveToFirst()) {


                    list = new ArrayList<>();
                    City city;
                    WeatherHour weatherHour;
                    do {
                        String name = cursor.getString(cursor.getColumnIndex(ViewedCitesTable.NAME));
                        Integer id = cursor.getInt(cursor.getColumnIndex(ViewedCitesTable.ID));
                        city = new City(id, name);
                        weatherHour = getDataDb(cursor.getInt(cursor.getColumnIndex(ViewedCitesTable.ID)));

                        if (idCity == id && weatherHour != null) {
                            image = weatherHour.getWeather().getIcon();
                        } else {
                            list.add(new CityWithWeatherHour(city, weatherHour));
                        }

                    } while (cursor.moveToNext());
                }

                cursor.close();
                notifyResponse(list, image);

            }
        }.start();

    }

    @Nullable
    private WeatherHour getDataDb(int id) {

        Date temp = new Date();

        String[] arg = {Long.toString(temp.getTime()), Integer.toString(id)};
        Cursor cursor = operations.query("SELECT * FROM "
                + DbHelper.getTableName(WeatherTable.class)
                + " WHERE (" + WeatherTable.DATE + ">=?) AND ("
                + WeatherTable.CITY_ID
                + "=?) LIMIT 1", arg);
        if (cursor.moveToFirst()) {
            WeatherHour weatherHour;

            long date = cursor.getLong(cursor.getColumnIndex(WeatherTable.DATE));

            Main main = new Main(cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP)),
                    cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MIN)),
                    cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MAX)),
                    cursor.getDouble(cursor.getColumnIndex(WeatherTable.PRESSURE)),
                    cursor.getDouble(cursor.getColumnIndex(WeatherTable.HUMIDITY)));

            Weather weather = new Weather(cursor.getInt(cursor.getColumnIndex(WeatherTable.WEATHER_ID)),
                    cursor.getString(cursor.getColumnIndex(WeatherTable.MAIN)),
                    cursor.getString(cursor.getColumnIndex(WeatherTable.DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(WeatherTable.ICON)));

            weatherHour = new WeatherHour(date, main, weather, new Clouds(cursor.getDouble(cursor.getColumnIndex(WeatherTable.CLOUDS))),
                    new Wind(cursor.getDouble(cursor.getColumnIndex(WeatherTable.SPEED)),
                            cursor.getDouble(cursor.getColumnIndex(WeatherTable.DEG))), new Rain(0), new Snow(0));

            return weatherHour;
        }

        cursor.close();
        return null;
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

                operations.delete(ViewedCitesTable.class, ViewedCitesTable.ID + "=?", String.valueOf(id));

                operations.delete(WeatherTable.class, WeatherTable.ID + "=?", String.valueOf(id));


                notifyResponse();
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

    private void notifyResponse(final ArrayList<CityWithWeatherHour> list, final String image) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.showListCitesWithWeather(list, image);

            }
        });
    }

}
