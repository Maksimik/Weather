package com.maksimik.weather.utils;

import com.maksimik.weather.model.City;
import com.maksimik.weather.model.CityWithWeatherHour;
import com.maksimik.weather.model.WeatherHour;

import java.util.ArrayList;
import java.util.HashMap;

public interface ContractViewedCites {

    interface View {

        void showListCites(ArrayList<Integer> id, ArrayList<String> name);

        void showListCitesWithWeather(ArrayList<CityWithWeatherHour> list, String image);

        void showError(String message);

        void showFinish();


    }

    interface Presenter {

        void getListViewedCities(int id);

        void getListViewedCitesFromDb();

        void addCites(int id, String name);

        void deleteCites(int id);

    }

}
