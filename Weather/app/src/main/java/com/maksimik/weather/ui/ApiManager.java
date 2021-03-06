package com.maksimik.weather.ui;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.maksimik.weather.BuildConfig;
import com.maksimik.weather.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by 1 on 21.10.2016.
 */

public class ApiManager {
    public static final String APP_ENGINE_BASE_URL = "https://show-weather-forecast-app.appspot.com/_ah/api/";

    private static ApiManager sInstance;
    private MyApi appEngineApi;

    public static ApiManager get() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }
        return sInstance;
    }

    private ApiManager() {
    }

    public MyApi myApi() {
        if (appEngineApi == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName(BuildConfig.APPLICATION_ID)
                    .setRootUrl(APP_ENGINE_BASE_URL)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            appEngineApi = builder.build();
        }
        return appEngineApi;
    }
}
