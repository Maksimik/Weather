package com.maksimik.weather.utils;

import com.maksimik.weather.model.Forecast;

public interface Contract {
    interface View {

        void showData(Forecast forecastData);

        void showError(String message);

        void showProgress(boolean isInProgress);
    }

    interface Presenter {

        void getWeather(int id);

        void getWeather(double lat, double lon);

        void getWeatherFromDb(int id);
    }
}
