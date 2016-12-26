package com.maksimik.weather.parsers;

import com.maksimik.weather.model.City;
import com.maksimik.weather.model.Clouds;
import com.maksimik.weather.model.DayWeather;
import com.maksimik.weather.model.Forecast;
import com.maksimik.weather.model.Main;
import com.maksimik.weather.model.Rain;
import com.maksimik.weather.model.Snow;
import com.maksimik.weather.model.Weather;
import com.maksimik.weather.model.WeatherHour;
import com.maksimik.weather.model.Wind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ParseJsonForecast {

    public Forecast parseJsonForecast(String responce) {

        City mCity;
        Weather weather;
        Forecast forecast = new Forecast();
        JSONObject dataJsonObj;
        WeatherHour weatherHour;
        DayWeather dayWeather = new DayWeather();


        try {
            dataJsonObj = new JSONObject(responce);

            JSONObject city = dataJsonObj.getJSONObject("city");
            //TODO name city
            mCity = new City(city.getInt("id"), "");

            forecast.setCity(mCity);

            Clouds clouds;
            Wind wind;
            Rain rain;
            Snow snow;
            Main mainWeather;
            long date;
            Date temp = new Date();

            JSONArray list = dataJsonObj.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {

                JSONObject listWeather = list.getJSONObject(i);

                clouds = new Clouds(listWeather.getJSONObject("clouds").getDouble("all"));

                wind = new Wind(listWeather.getJSONObject("wind").getDouble("speed"), listWeather.getJSONObject("wind").getDouble("deg"));

                rain = new Rain(0);
                snow = new Snow(0);

                if (listWeather.has("rain")) {
                    if (listWeather.getJSONObject("rain").has("3h")) {
                        rain.setValue(listWeather.getJSONObject("rain").getDouble("3h"));
                    }
                }

                if (listWeather.has("snow")) {
                    if (listWeather.getJSONObject("snow").has("3h")) {
                        snow.setValue(listWeather.getJSONObject("snow").getDouble("3h"));
                    }
                }

                JSONObject main = listWeather.getJSONObject("main");
                mainWeather = new Main();
                mainWeather.setTemp((int) Math.round(main.getDouble("temp") - 273.15));
                mainWeather.setTempMin(Math.round(main.getDouble("temp_min") - 273.15));
                mainWeather.setTempMax(Math.round(main.getDouble("temp_max") - 273.15));
                mainWeather.setPressure(main.getDouble("pressure"));
                mainWeather.setHumidity(main.getDouble("humidity"));

                weather = new Weather();
                JSONArray weathers = listWeather.getJSONArray("weather");
                for (int j = 0; j < weathers.length(); j++) {
                    weather.setId(weathers.getJSONObject(j).getInt("id"));
                    weather.setMain(weathers.getJSONObject(j).getString("main"));
                    weather.setDescription(weathers.getJSONObject(j).getString("description"));
                    weather.setIcon(weathers.getJSONObject(j).getString("icon"));
                }

                date = listWeather.getLong("dt") * 1000;

                weatherHour = new WeatherHour(date, mainWeather, weather, clouds, wind, rain, snow);
//TODO deprecate
                if ((new Date(date)).getDate() != temp.getDate()) {
                    temp = new Date(date);
                    dayWeather.add(weatherHour);
                    forecast.add(dayWeather);
                    dayWeather = new DayWeather();
                }
                dayWeather.add(weatherHour);
            }
            forecast.add(dayWeather);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecast;
    }
}