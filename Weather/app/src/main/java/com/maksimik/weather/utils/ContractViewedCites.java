package com.maksimik.weather.utils;

import com.maksimik.weather.model.City;

import java.util.ArrayList;
import java.util.HashMap;

public interface ContractViewedCites {

    interface View {

        void showListCites(ArrayList<Integer> id, ArrayList<String> name);

        void showError(String message);

        void showFinish();


    }

    interface Presenter {

        void getListViewedCitesFromDb();

        void addCites(int id, String name);

        void deleteCites(int id);

    }

}
