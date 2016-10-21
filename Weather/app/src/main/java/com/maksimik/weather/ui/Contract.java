package com.maksimik.weather.ui;

/**
 * Created by 1 on 21.10.2016.
 */

public interface Contract {
    interface View {
        void showData(String data);

        void showError(String message);

        void showProgress(boolean isInProgress);
    }

    interface Presenter {
        void onReady();
    }
}
