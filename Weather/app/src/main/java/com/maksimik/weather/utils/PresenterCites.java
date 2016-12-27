package com.maksimik.weather.utils;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.maksimik.weather.backend.myApi.MyApi;
import com.maksimik.weather.backend.myApi.model.MyBean;
import com.maksimik.weather.parsers.ParseJsonCities;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class PresenterCites implements ContractCites.Presenter {

    private ContractCites.View view;
    private Handler handler;

    public PresenterCites(@NonNull ContractCites.View view) {

        this.view = view;
        handler = new Handler(Looper.getMainLooper());

    }


    @Override
    public void getListCites(final String str) {
        new Thread() {
            @Override
            public void run() {

                try {
                    MyApi.GetCities call = ApiManager.get().myApi().getCities(URLEncoder.encode(str, "UTF-8"));
                    MyBean bean = call.execute();
                    String response = bean.getData();
                    ParseJsonCities parseJsonCities = new ParseJsonCities();
                    notifyResponse(parseJsonCities.parseJsonCites(response));

                } catch (IOException e) {
//                    notifyError("Нет подключения к интернету");
                }
            }
        }.start();
    }

    private void notifyResponse(final HashMap<String, Integer> data) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.showData(data);

            }
        });
    }

}
