package com.maksimik.weather.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.maksimik.weather.backend.myApi.MyApi;
import com.maksimik.weather.backend.myApi.model.MyBean;
import com.maksimik.weather.data.Forecast;

import java.io.IOException;

import static android.content.ContentValues.TAG;


public class MainPresenter implements Contract.Presenter {

    private Contract.View view;
    private Handler handler;
    private Forecast forecast;
    private String id = "627904";

    public MainPresenter(@NonNull Contract.View view) {
        this.view = view;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onReady() {
        view.showProgress(true);
        loadData();
    }

    private void loadData() {

        new Thread() {
            @Override
            public void run() {

                try {
                    MyApi.GetContent call = ApiManager.get().myApi().getContent(id);
                    MyBean bean = call.execute();
                    String response = bean.getData();
                    getWeatherForecast(response);
                    notifyResponse();

                } catch (IOException e) {
                    Log.e(TAG, "run: ", e);
                    notifyError(e);
                }
            }
        }.start();
    }

    private void getWeatherForecast(String response) {
        ParseJsonOverJSONObject parseJsonOverJSONObject = new ParseJsonOverJSONObject();
        forecast = parseJsonOverJSONObject.parseJsonOverJSONObject(response);
    }


    private void notifyResponse() {
        handler.post(new Runnable() {
            //ParseJsonOverJSONObject parseJsonOverJSONObject = new ParseJsonOverJSONObject();

            //ParseJsonOverGson parseJsonOverGson = new ParseJsonOverGson();
            @Override
            public void run() {
                view.showProgress(false);
                view.showData(forecast);
            }
        });
    }

    private void notifyError(final Throwable e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.showProgress(false);
                view.showError(e.getMessage());
            }
        });
    }
}

