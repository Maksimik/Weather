package com.maksimik.weather.utils;

import com.maksimik.weather.Data.City;
import com.maksimik.weather.Data.Clouds;
import com.maksimik.weather.Data.Coord;
import com.maksimik.weather.Data.Forecast;
import com.maksimik.weather.Data.Main;
import com.maksimik.weather.Data.Rain;
import com.maksimik.weather.Data.Snow;
import com.maksimik.weather.Data.Weather;
import com.maksimik.weather.Data.WeatherHour;
import com.maksimik.weather.Data.Wind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class ParseJsonOverJSONObject {
    Forecast parseJsonOverJSONObject(String responce) {

        City mCity = new City();
        Weather weather = new Weather();
        Forecast forecast = new Forecast();
        JSONObject dataJsonObj;
        WeatherHour weatherHour;

        try {
            dataJsonObj = new JSONObject(responce);

            JSONObject city = dataJsonObj.getJSONObject("city");
            mCity.setId(city.getInt("id"));
            mCity.setName(city.getString("name"));
            mCity.setCountry(city.getString("country"));

            JSONObject coord = city.getJSONObject("coord");

            mCity.setCoord(new Coord(coord.getDouble("lon"), coord.getDouble("lat")));
            forecast.setCity(mCity);

            Clouds clouds;
            Wind wind;
            Rain rain;
            Snow snow;
            Main mainWeather;
            com.maksimik.weather.Data.Date date;

            JSONArray list = dataJsonObj.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {

                JSONObject listWeather = list.getJSONObject(i);
                //Date
                Date timeStampDate = new Date((long) (listWeather.getLong("dt") * 1000));
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
                date = new com.maksimik.weather.Data.Date(dateFormat);

                clouds = new Clouds(listWeather.getJSONObject("clouds").getDouble("all"));

                wind = new Wind(listWeather.getJSONObject("wind").getDouble("speed"), listWeather.getJSONObject("wind").getDouble("deg"));

                rain = new Rain();
                snow = new Snow();
                if (listWeather.getJSONObject("rain").has("3h")) {
                    rain.setValue(listWeather.getJSONObject("rain").getDouble("3h"));
                } else {
                    rain.setHas(false);
                }

                if (listWeather.getJSONObject("snow").has("3h")) {
                    snow.setValue(listWeather.getJSONObject("snow").getDouble("3h"));

                } else {
                    snow.setHas(false);
                }


                JSONObject main = listWeather.getJSONObject("main");
                mainWeather = new Main();
                mainWeather.setTemp(main.getDouble("temp"));
                mainWeather.setTempMin(main.getDouble("temp_min"));
                mainWeather.setTempMax(main.getDouble("temp_max"));
                mainWeather.setPressure(main.getDouble("pressure"));
                mainWeather.setHumidity(main.getDouble("humidity"));

                JSONArray weathers = listWeather.getJSONArray("weather");
                for (int j = 0; j < weathers.length(); j++) {
                    weather.setMain(weathers.getJSONObject(j).getString("main"));
                    weather.setDescription(weathers.getJSONObject(j).getString("description"));
                }
                weatherHour = new WeatherHour(date, mainWeather, weather, clouds, wind, rain, snow);
                forecast.addWeatherHour(weatherHour);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecast;
    }
}
