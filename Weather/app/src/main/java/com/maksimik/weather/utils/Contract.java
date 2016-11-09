package com.maksimik.weather.utils;

import com.maksimik.weather.data.Forecast;
import com.maksimik.weather.data.ListIcon;

public interface Contract {
    interface View {
        void showData(Forecast forecastData, ListIcon iconData);

        void showError(String message);

        void showProgress(boolean isInProgress);
    }

    interface Presenter {
        void onReady();
    }
}
