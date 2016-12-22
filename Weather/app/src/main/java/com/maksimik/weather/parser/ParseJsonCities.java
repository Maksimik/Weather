package com.maksimik.weather.parser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ParseJsonCities {

    public HashMap<String, Integer> parseJsonCites(String responce) {

        HashMap<String, Integer> listCities;

        JSONObject dataJsonObj;


        try {
            listCities = new HashMap<>();
            dataJsonObj = new JSONObject(responce);
            JSONArray list = dataJsonObj.getJSONArray("cities");

            for (int i = 0; i < list.length(); i++) {

                JSONObject city = list.getJSONObject(i);

                listCities.put(city.getString("cityName"), city.getInt("cityId"));

            }
            return listCities;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseJsonCity(String responce) {

        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(responce);

            return dataJsonObj.getJSONObject("city").getString("cityName");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
