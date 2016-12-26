package com.maksimik.weather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maksimik.weather.backend.myApi.MyApi;
import com.maksimik.weather.backend.myApi.model.MyBean;
import com.maksimik.weather.db.DbHelper;
import com.maksimik.weather.db.IDbOperations;
import com.maksimik.weather.db.WeatherTable;
import com.maksimik.weather.model.Clouds;
import com.maksimik.weather.model.DayWeather;
import com.maksimik.weather.model.Forecast;
import com.maksimik.weather.model.Main;
import com.maksimik.weather.model.Rain;
import com.maksimik.weather.model.Snow;
import com.maksimik.weather.model.Weather;
import com.maksimik.weather.model.WeatherHour;
import com.maksimik.weather.model.Wind;
import com.maksimik.weather.parsers.ParseJsonCities;
import com.maksimik.weather.parsers.ParseJsonForecast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeatherManager implements Contract.Presenter {

    private Contract.View view;
    private Handler handler;
    private Forecast forecast;


    private IDbOperations operations;
    private static Contract.Presenter INSTANCE;

    public static Contract.Presenter getInstance(Context context, Contract.View view) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherManager(context, view);
        }
        return INSTANCE;
    }

    //TODO переделать
    public WeatherManager(Context context, @NonNull Contract.View view) {
        this.view = view;
        operations = new DbHelper(context, 1);
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void getWeather(int id) {

        view.showProgress(true);
        loadData(String.valueOf(id));

    }

    @Override
    public void getWeather(double lat, double lon) {
        view.showProgress(true);
        loadData(String.valueOf(lat), String.valueOf(lon));
    }

    @Override
    public void getWeatherFromDb(int id, boolean limit) {

        loadDataFromDb(id, limit);

    }

    private void loadData(final String lat, final String lon) {

        new Thread() {
            @Override
            public void run() {

                try {
                    MyApi.GetWeatherGeographicCoordinates call = ApiManager.get().myApi().getWeatherGeographicCoordinates(lat, lon);
                    MyBean bean = call.execute();
                    String response = bean.getData();
                    ParseJsonForecast parseJsonForecast = new ParseJsonForecast();
                    forecast = parseJsonForecast.parseJsonForecast(response);
                    MyApi.GetCity callCity = ApiManager.get().myApi().getCity(String.valueOf(forecast.getCity().getId()));
                    bean = callCity.execute();
                    response = bean.getData();
                    ParseJsonCities parseJsonCities = new ParseJsonCities();
                    String name = parseJsonCities.parseJsonCity(response);
                    if (name != null) {
                        forecast.getCity().setName(name);
                    }
                    notifyResponse();

                    String sql = WeatherTable.CITY_ID + "=?";
                    operations.delete(WeatherTable.class, sql, String.valueOf(forecast.getCity().getId()));

                    setDateDb(forecast);

                } catch (IOException e) {
                    notifyError("Нет подключения к интернету");
                }
            }
        }.start();
    }

    private void loadData(final String id) {

        new Thread() {
            @Override
            public void run() {

                try {
                    MyApi.GetWeather call = ApiManager.get().myApi().getWeather(id);
                    MyBean bean = call.execute();
                    String response = bean.getData();

                    ParseJsonForecast parseJsonForecast = new ParseJsonForecast();
                    forecast = parseJsonForecast.parseJsonForecast(response);

                    notifyResponse();

                    String sql = WeatherTable.CITY_ID + "=?";
                    operations.delete(WeatherTable.class, sql, id);

                    setDateDb(forecast);

                } catch (IOException e) {
                    notifyError("Нет подключения к интернету");
                }
            }
        }.start();
    }

    private void loadDataFromDb(final int id, final boolean limit) {

        new Thread() {
            @Override
            public void run() {

                forecast = getDataDb(id, limit);
                notifyResponse();

            }
        }.start();
    }

    private void notifyResponse() {
        handler.post(new Runnable() {
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
        ContentValues values;
        for (DayWeather dayWeather : forecast.getListWeatherHours()) {
            for (WeatherHour weatherHour : dayWeather.getDayWeather()) {
                values = new ContentValues();

                values.put(WeatherTable.CITY_ID, forecast.getCity().getId());

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
        operations.bulkInsert(WeatherTable.class, listContantValues);
    }

    @Nullable
    private Forecast getDataDb(int id, boolean limit) {

        Date temp = new Date();

        String[] arg = {Long.toString(temp.getTime()), Integer.toString(id)};
        String sql = "SELECT * FROM "
                + DbHelper.getTableName(WeatherTable.class)
                + " WHERE (" + WeatherTable.DATE + ">=?) AND ("
                + WeatherTable.CITY_ID
                + "=?)";
        if (limit) {
            sql=sql+" LIMIT 1";
        }
        Cursor cursor = operations.query(sql, arg);

        if (cursor.moveToFirst()) {
            Forecast f = new Forecast();
            WeatherHour weatherHour;
            DayWeather dayWeather = new DayWeather();
            int i = 0;
            do {
                long date = cursor.getLong(cursor.getColumnIndex(WeatherTable.DATE));
                if (i == 0) {
                    temp = new Date(date);
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
//TODO Rain, Snow
                weatherHour = new WeatherHour(date, main, weather, new Clouds(cursor.getDouble(cursor.getColumnIndex(WeatherTable.CLOUDS))),
                        new Wind(cursor.getDouble(cursor.getColumnIndex(WeatherTable.SPEED)),
                                cursor.getDouble(cursor.getColumnIndex(WeatherTable.DEG))),new Rain(0), new Snow(0));

                dayWeather.add(weatherHour);

                if ((new Date(date)).getDate() != temp.getDate() ||((new Date(date)).getDate() == temp.getDate() && i==0)) {
                    temp = new Date(date);
                    f.add(dayWeather);
                    dayWeather = new DayWeather();
                }
                i++;
            } while (cursor.moveToNext());
            f.add(dayWeather);
            return f;
        }

        cursor.close();
        return null;
    }

}

