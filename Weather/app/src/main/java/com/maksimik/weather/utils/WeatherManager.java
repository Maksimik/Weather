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

    private final Contract.View view;
    private final Handler handler;
    private Forecast forecast;

    private final IDbOperations operations;
    private static Contract.Presenter INSTANCE;

    public static Contract.Presenter getInstance(final Context context, final Contract.View view) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherManager(context, view);
        }
        return INSTANCE;
    }

    //TODO переделать
    public WeatherManager(final Context context, @NonNull final Contract.View view) {
        this.view = view;
        operations = new DbHelper(context, 1);
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void getWeather(final int id) {

        view.showProgress(true);
        loadData(String.valueOf(id));

    }

    @Override
    public void getWeather(final double lat, final double lon) {
        view.showProgress(true);
        loadData(String.valueOf(lat), String.valueOf(lon));
    }

    @Override
    public void getWeatherFromDb(final int id, final boolean limit) {

        loadDataFromDb(id, limit);

    }

    private void loadData(final String lat, final String lon) {

        new Thread() {

            @Override
            public void run() {

                try {
                    final MyApi.GetWeatherGeographicCoordinates call = ApiManager.get().myApi().getWeatherGeographicCoordinates(lat, lon);
                    final MyBean bean = call.execute();
                    final String response = bean.getData();
                    final ParseJsonForecast parseJsonForecast = new ParseJsonForecast();
                    forecast = parseJsonForecast.parseJsonForecast(response);

                    final MyApi.GetCity callCity = ApiManager.get().myApi().getCity(String.valueOf(forecast.getCity().getId()));
                    final MyBean myBeen = callCity.execute();
                    final String myResponse = myBeen.getData();
                    final ParseJsonCities parseJsonCities = new ParseJsonCities();
                    final String name = parseJsonCities.parseJsonCity(myResponse);
                    if (name != null) {
                        forecast.getCity().setName(name);
                    }
                    notifyResponse();

                    final String sql = WeatherTable.CITY_ID + "=?";
                    operations.delete(WeatherTable.class, sql, String.valueOf(forecast.getCity().getId()));

                    setDateDb(forecast);

                } catch (final IOException e) {
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
                    final MyApi.GetWeather call = ApiManager.get().myApi().getWeather(id);
                    final MyBean bean = call.execute();
                    final String response = bean.getData();

                    final ParseJsonForecast parseJsonForecast = new ParseJsonForecast();
                    forecast = parseJsonForecast.parseJsonForecast(response);

                    notifyResponse();

                    final String sql = WeatherTable.CITY_ID + "=?";
                    operations.delete(WeatherTable.class, sql, id);

                    setDateDb(forecast);

                } catch (final IOException e) {
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

    private void setDateDb(final Forecast forecast) {

        final List<ContentValues> listContantValues = new ArrayList<>();
        ContentValues values;
        for (final DayWeather dayWeather : forecast.getListWeatherHours()) {
            for (final WeatherHour weatherHour : dayWeather.getDayWeather()) {
                values = new ContentValues();

                values.put(WeatherTable.CITY_ID, forecast.getCity().getId());

                values.put(WeatherTable.DATE, weatherHour.getDate());
                values.put(WeatherTable.TEMP, weatherHour.getMain().getTemp());
                values.put(WeatherTable.TEMP_MIN, weatherHour.getMain().getTempMin());
                values.put(WeatherTable.TEMP_MAX, weatherHour.getMain().getTempMax());
                values.put(WeatherTable.PRESSURE, weatherHour.getMain().getPressure());
                values.put(WeatherTable.HUMIDITY, weatherHour.getMain().getHumidity());
                values.put(WeatherTable.WEATHER_ID, weatherHour.getWeather().getId());
                values.put(WeatherTable.MAIN, weatherHour.getWeather().getMain());
                values.put(WeatherTable.DESCRIPTION, weatherHour.getWeather().getDescription());
                values.put(WeatherTable.ICON, weatherHour.getWeather().getIcon());
                values.put(WeatherTable.CLOUDS, weatherHour.getClouds().getAll());
                values.put(WeatherTable.SPEED, weatherHour.getWind().getSpeed());
                values.put(WeatherTable.DEG, weatherHour.getWind().getDeg());
                values.put(WeatherTable.RAIN, weatherHour.getRain().getValue());
                values.put(WeatherTable.DEG, weatherHour.getSnow().getValue());

                listContantValues.add(values);
            }
        }
        operations.bulkInsert(WeatherTable.class, listContantValues);
    }

    @Nullable
    private Forecast getDataDb(final int id, final boolean limit) {

        Date temp = new Date();

        final String[] arg = {Long.toString(temp.getTime()), Integer.toString(id)};
//        TODO
        String sql = "SELECT * FROM "
                + DbHelper.getTableName(WeatherTable.class)
                + " WHERE (" + WeatherTable.DATE + ">=?) AND ("
                + WeatherTable.CITY_ID
                + "=?)";
        if (limit) {
            sql = sql + " LIMIT 1";
        }
        final Cursor cursor = operations.query(sql, arg);

        if (cursor.moveToFirst()) {
            final Forecast f = new Forecast();
            WeatherHour weatherHour;
            DayWeather dayWeather = new DayWeather();
            int i = 0;
            do {
                final long date = cursor.getLong(cursor.getColumnIndex(WeatherTable.DATE));
                if (i == 0) {
                    temp = new Date(date);
                }
                final Main main = new Main(cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MIN)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MAX)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.PRESSURE)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.HUMIDITY)));

                final Weather weather = new Weather(cursor.getInt(cursor.getColumnIndex(WeatherTable.WEATHER_ID)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.MAIN)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.ICON)));

                weatherHour = new WeatherHour(date, main, weather,
                        new Clouds(cursor.getDouble(cursor.getColumnIndex(WeatherTable.CLOUDS))),
                        new Wind(cursor.getDouble(cursor.getColumnIndex(WeatherTable.SPEED)),
                                cursor.getDouble(cursor.getColumnIndex(WeatherTable.DEG))),
                        new Rain(cursor.getDouble(cursor.getColumnIndex(WeatherTable.RAIN))),
                        new Snow(cursor.getDouble(cursor.getColumnIndex(WeatherTable.SNOW))));

                dayWeather.add(weatherHour);
//TODO deprecated
                if ((new Date(date)).getDate() != temp.getDate() || ((new Date(date)) == temp && i == 0)) {
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

