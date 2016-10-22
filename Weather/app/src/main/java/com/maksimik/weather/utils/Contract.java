package com.maksimik.weather.utils;

import com.maksimik.weather.Data.Forecast;

/**
 * Created by 1 on 21.10.2016.
 */

public interface Contract {
    interface View {
        void showData(Forecast data);

        void showError(String message);

        void showProgress(boolean isInProgress);
    }

    interface Presenter {
        void onReady();
    }
}
