package com.maksimik.weather.utils;

import com.maksimik.weather.model.CityWithWeatherHour;

import java.util.ArrayList;

public interface ContractViewedCites {

    interface View {

        void showListCitesWithWeather(ArrayList<CityWithWeatherHour> list, String image);

        void showError(String message);

        void showFinish();

    }

    interface Presenter {

        void getListViewedCities(int id);

        void addCites(int id, String name);

        void deleteCites(int id);

    }

}
