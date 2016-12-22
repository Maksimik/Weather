/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.maksimik.weather.backend;

import com.example.http.HttpClient;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.IOException;

import javax.inject.Named;

@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.weather.maksimik.com",
                ownerName = "backend.weather.maksimik.com",
                packagePath = ""
        )
)
public class MyEndpoint {
    private static final String ACCESS_KEY = "57eac87bbf864b3de29a4c2274497ced";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?";
    private static final String BASE_CITIES_URL = "http://weather.cody.by/api/cities/";

    @ApiMethod(name = "getWeather")
    public MyBean getWeather(@Named("id") String id) throws IOException {

        MyBean response = new MyBean();
        String data = new HttpClient().get(BASE_URL + "id=" + id + "&APPID=" + ACCESS_KEY);
        response.setData(data);
        return response;
    }

    @ApiMethod(name = "getWeatherGeographicCoordinates")
    public MyBean getWeatherGeographicCoordinates(@Named("lat") String lat, @Named("lon") String lon) throws IOException {

        MyBean response = new MyBean();
        String data = new HttpClient().get(BASE_URL + "lat=" + lat + "&lon=" + lon + "&APPID=" + ACCESS_KEY);
        response.setData(data);
        return response;
    }

    @ApiMethod(name = "getCities")
    public MyBean GetCities(@Named("text") String text) throws IOException {

        MyBean response = new MyBean();
        String data = new HttpClient().get(BASE_CITIES_URL + "search/" + text);
        response.setData(data);
        return response;
    }

    @ApiMethod(name = "getCity")
    public MyBean GetCity(@Named("id") String id) throws IOException {

        MyBean response = new MyBean();
        String data = new HttpClient().get(BASE_CITIES_URL + "id/" + id);
        response.setData(data);
        return response;
    }
}
