package com.maksimik.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.maksimik.weather.R;
import com.maksimik.weather.backend.myApi.MyApi;
import com.maksimik.weather.backend.myApi.model.MyBean;
import com.maksimik.weather.parsers.ParseJsonCities;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class PresenterCites implements ContractCites.Presenter {

    private final ContractCites.View view;
    private final Handler handler;

    public PresenterCites(@NonNull final ContractCites.View view) {

        this.view = view;
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void getListCites(final String str) {
        if(isNetworkAvailable()){
        new Thread() {

            @Override
            public void run() {

                try {
                    final MyApi.GetCities call = ApiManager.get().myApi().getCities(URLEncoder.encode(str, "UTF-8"));
                    final MyBean bean = call.execute();
                    final String response = bean.getData();
                    final ParseJsonCities parseJsonCities = new ParseJsonCities();
                    notifyResponse(parseJsonCities.parseJsonCites(response));

                } catch (final IOException e) {

                }
            }
        }.start();
        }else {
            notifyError(ContextHolder.getInstance().getContext().getString(R.string.no_internet_connection));
        }
    }

    private boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) ContextHolder.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private void notifyResponse(final HashMap<String, Integer> data) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.showData(data);

            }
        });
    }

    private void notifyError(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.showError(message);
            }
        });
    }

}
