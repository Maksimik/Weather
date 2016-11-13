package com.maksimik.weather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.maksimik.weather.backend.myApi.MyApi;
import com.maksimik.weather.backend.myApi.model.MyBean;
import com.maksimik.weather.db.DbHelper;
import com.maksimik.weather.db.IDbOperations;
import com.maksimik.weather.db.WeatherTable;
import com.maksimik.weather.model.Clouds;
import com.maksimik.weather.model.DayWeather;
import com.maksimik.weather.model.Forecast;
import com.maksimik.weather.model.Main;
import com.maksimik.weather.model.Weather;
import com.maksimik.weather.model.WeatherHour;
import com.maksimik.weather.model.Wind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeatherManager implements Contract.Presenter {

    private Contract.View view;
    private Handler handler;
    private Forecast forecast;
    private String id = "627904";

    private IDbOperations operations;
    private static Contract.Presenter INSTANCE;

    public static Contract.Presenter getInstance(Context context, Contract.View view) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherManager(context, view);
        }
        return INSTANCE;
    }

    private WeatherManager(Context context, @NonNull Contract.View view) {
        this.view = view;
        operations = new DbHelper(context, 1);
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void getWeather() {

        view.showProgress(true);
        loadData();

    }

    @Override
    public void getWeatherFromDb() {

        loadDataFromDb();

    }

    private void loadData() {

        new Thread() {
            @Override
            public void run() {

                try {
                    MyApi.GetContent call = ApiManager.get().myApi().getContent(id);
                    MyBean bean = call.execute();
                    String response = bean.getData();

                    ParseJsonOverJSONObject parseJsonOverJSONObject = new ParseJsonOverJSONObject();
                    forecast = parseJsonOverJSONObject.parseJsonOverJSONObject(response);

                    notifyResponse();

                    operations.delete(WeatherTable.class, null);
                    setDateDb(forecast);

                } catch (IOException e) {
                    notifyError("Нет подключения к интернету");
                }
            }
        }.start();
    }

    private void loadDataFromDb() {

        new Thread() {
            @Override
            public void run() {

                forecast = getDataDb();
                notifyResponse();

            }
        }.start();
    }

    private void notifyResponse() {
        handler.post(new Runnable() {
            //ParseJsonOverJSONObject parseJsonOverJSONObject = new ParseJsonOverJSONObject();

            //ParseJsonOverGson parseJsonOverGson = new ParseJsonOverGson();
            @Override
            public void run() {
                view.showProgress(false);
                view.showData(forecast);
            }
        });
    }

    private void notifyError(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.showProgress(false);
                view.showError(message);
            }
        });
    }

    private void setDateDb(Forecast forecast) {

        List<ContentValues> listContantValues = new ArrayList<>();
        //ContentValues values = new ContentValues();
        ContentValues values;
        for (DayWeather dayWeather : forecast.getListWeatherHours()) {
            for (WeatherHour weatherHour : dayWeather.getDayWeather()) {
                values = new ContentValues();

                values.put(WeatherTable.DATE, weatherHour.getDate());
                values.put(WeatherTable.TEMP, weatherHour.getMain().getTemp());
                values.put(WeatherTable.TEMP_MIN, weatherHour.getMain().getTempMin());
                values.put(WeatherTable.TEMP_MAX, weatherHour.getMain().getTempMax());
                values.put(WeatherTable.PRESSURE, weatherHour.getMain().getPressure());
                values.put(WeatherTable.HUMIDITY, weatherHour.getMain().getHumidity());
                values.put(WeatherTable.WEATHER_ID, weatherHour.getWeather().getId());
                values.put(WeatherTable.Main, weatherHour.getWeather().getMain());
                values.put(WeatherTable.DESCRIPTION, weatherHour.getWeather().getDescription());
                values.put(WeatherTable.ICON, weatherHour.getWeather().getIcon());
                values.put(WeatherTable.CLOUDS, weatherHour.getClouds().getAll());
                values.put(WeatherTable.SPEED, weatherHour.getWind().getSpeed());
                values.put(WeatherTable.DEG, weatherHour.getWind().getDeg());


                listContantValues.add(values);
            }
        }
        int rowID = operations.bulkInsert(WeatherTable.class, listContantValues);

        Log.i("TAG", "row inserted, ID = " + rowID);
    }

    @Nullable
    private Forecast getDataDb() {

        Date temp = new Date();

        Cursor cursor = operations.query("SELECT * FROM "
                        + DbHelper.getTableName(WeatherTable.class)
                        + " WHERE " + WeatherTable.DATE + ">=?",
                Long.toString(temp.getTime()));

        if (cursor.moveToFirst()) {
            Forecast f = new Forecast();
            WeatherHour weatherHour;

            DayWeather dayWeather = new DayWeather();
            int i = 0;
            do {
                long date = cursor.getLong(cursor.getColumnIndex(WeatherTable.DATE));
                if (i == 0) {
                    temp = new Date(date);
                    i++;
                }
                Main main = new Main(cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MIN)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MAX)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.PRESSURE)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.HUMIDITY)));

                Weather weather = new Weather(cursor.getInt(cursor.getColumnIndex(WeatherTable.WEATHER_ID)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.Main)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.ICON)));


                weatherHour = new WeatherHour(date, main, weather, new Clouds(cursor.getDouble(cursor.getColumnIndex(WeatherTable.CLOUDS))),
                        new Wind(cursor.getDouble(cursor.getColumnIndex(WeatherTable.SPEED)),
                                cursor.getDouble(cursor.getColumnIndex(WeatherTable.DEG))));
                //TODO FIXME: 10.11.2016
                if ((new Date(date)).getDate() != temp.getDate()) {
                    temp = new Date(date);
                    f.add(dayWeather);
                    dayWeather = new DayWeather();
                }
                dayWeather.add(weatherHour);

            } while (cursor.moveToNext());

            return f;
        }
        cursor.close();
        return null;
    }

}

