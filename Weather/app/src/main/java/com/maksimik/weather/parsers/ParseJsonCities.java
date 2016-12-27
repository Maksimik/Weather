package com.maksimik.weather.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ParseJsonCities {

    public HashMap<String, Integer> parseJsonCites(final String responce) {

        final HashMap<String, Integer> listCities;

        final JSONObject dataJsonObj;

        try {
            listCities = new HashMap<>();
            dataJsonObj = new JSONObject(responce);
            final JSONArray list = dataJsonObj.getJSONArray("cities");

            for (int i = 0; i < list.length(); i++) {

                final JSONObject city = list.getJSONObject(i);

                listCities.put(city.getString("cityName"), city.getInt("cityId"));

            }
            return listCities;

        } catch (final JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseJsonCity(final String responce) {

        final JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(responce);

            return dataJsonObj.getJSONObject("city").getString("cityName");

        } catch (final JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
