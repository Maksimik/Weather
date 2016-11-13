package com.maksimik.weather.utils;

import com.maksimik.weather.model.City;
import com.maksimik.weather.model.Clouds;
import com.maksimik.weather.model.Coord;
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

class ParseJsonOverJSONObject {
   // public HashSet<String> listIcon;

    Forecast parseJsonOverJSONObject(String responce) {

        City mCity = new City();
        Weather weather;
        Forecast forecast = new Forecast();
        JSONObject dataJsonObj;
        WeatherHour weatherHour;
        //listIcon = new HashSet<>();
        DayWeather dayWeather = new DayWeather();


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
            long date;
            Date temp = new Date();
            //String icon = null;
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            JSONArray list = dataJsonObj.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {

                JSONObject listWeather = list.getJSONObject(i);

                clouds = new Clouds(listWeather.getJSONObject("clouds").getDouble("all"));

                wind = new Wind(listWeather.getJSONObject("wind").getDouble("speed"), listWeather.getJSONObject("wind").getDouble("deg"));

                rain = new Rain();
                snow = new Snow();
                               /* if (listWeather.getJSONObject("rain").has("3h")) {
                    rain.setValue(listWeather.getJSONObject("rain").getDouble("3h"));
                    System.out.println("rain:"+ listWeather.getJSONObject("rain").getDouble("3h"));
                } else {
                    rain.setHas(false);
                }

                if (listWeather.getJSONObject("snow").has("3h")) {
                    snow.setValue(listWeather.getJSONObject("snow").getDouble("3h"));
                    System.out.println("snow:"+ listWeather.getJSONObject("snow").getDouble("3h"));

                } else {
                    snow.setHas(false);
                }*/
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
                    //listIcon.add(weather.getIcon());
                }

                date = listWeather.getLong("dt") * 1000;

                //weatherHour = new WeatherHour(date, mainWeather, weather, clouds, wind, rain, snow);
                weatherHour = new WeatherHour(date, mainWeather, weather, clouds, wind);

                if ((new Date(date)).getDate() != temp.getDate()) {
                    temp = new Date(date);
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