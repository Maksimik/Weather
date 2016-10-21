package com.maksimik.weather.ui;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.maksimik.weather.City;
import com.maksimik.weather.backend.myApi.MyApi;
import com.maksimik.weather.backend.myApi.model.MyBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class MainPresenter implements Contract.Presenter {

    private Contract.View view;
    private Handler handler;

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
                    MyApi.GetContent call = ApiManager.get().myApi().getContent();
                    MyBean bean = call.execute();
                    String response = bean.getData();
                    notifyResponse(response);
                } catch (IOException e) {
                    Log.e(TAG, "run: ", e);
                    notifyError(e);
                }
            }
        }.start();
    }

    private City parseJsonOverJSONObject(String responce) {
        JSONObject dataJsonObj = null;
        City city;
        int id=0;
        String name="";
        try {
            dataJsonObj = new JSONObject(responce);
            JSONObject coord = dataJsonObj.getJSONObject("coord");
            JSONObject wind = dataJsonObj.getJSONObject("wind");

            name = dataJsonObj.getString("name");
            int cod = dataJsonObj.getInt("cod");
            id = dataJsonObj.getInt("id");
            double speed = wind.getDouble("speed");
            double deg = wind.getDouble("deg");
            double lon = coord.getDouble("lon");
            double lat = coord.getDouble("lat");
            JSONArray weathers = dataJsonObj.getJSONArray("weather");
            int idWeather=0;
            for (int i = 0; i < weathers.length(); i++) {
                JSONObject weather = weathers.getJSONObject(i);
                idWeather = weather.getInt("id");
            }
            System.out.println("cod: " + cod);
            System.out.println("lon: " + lon);
            System.out.println("lat: " + lat);
            System.out.println("speed: " + speed);
            System.out.println("deg: " + deg);
            System.out.println("idWeather: " + idWeather);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new City(id,name);
    }
    private City parseJsonOverGson(String response) {
        Gson gson =  new Gson();
        City city = gson.fromJson(response, City.class);
        return city;
    }

    private void notifyResponse(final String response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.showProgress(false);
                //view.showData(response);
                //City city=parseJsonOverJSONObject(response);
                City city=parseJsonOverGson(response);
                view.showData("Idi:"+city.setId()+"    Name city:"+city.setName());
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

