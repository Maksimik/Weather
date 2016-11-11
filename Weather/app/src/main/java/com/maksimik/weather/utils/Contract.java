package com.maksimik.weather.utils;

import com.maksimik.weather.data.Forecast;

public interface Contract {
    interface View {

        void showData(Forecast forecastData);

        void showError(String message);

        void showProgress(boolean isInProgress);
    }

    interface Presenter {
        void onReady();
    }
}
