package com.maksimik.weather.utils;


import java.util.HashMap;

public interface ContractCites {

    interface View {

        void showData(HashMap<String, Integer> data);

        void showError(String message);

    }

    interface Presenter {

        void getListCitesFromDb(String str);
    }

}
