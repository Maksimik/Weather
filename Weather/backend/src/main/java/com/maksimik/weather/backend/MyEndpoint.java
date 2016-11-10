/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.maksimik.weather.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.IOException;
import com.example.http.HttpClientWeather;

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
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @ApiMethod(name = "GetContent")
    public MyBean getContent(@Named("id") String id) throws IOException {

        MyBean response = new MyBean();
        String data = new HttpClientWeather().get(BASE_URL + "forecast?id=" + id + "&APPID=" + ACCESS_KEY);
        response.setData(data);
        return response;
    }
}
