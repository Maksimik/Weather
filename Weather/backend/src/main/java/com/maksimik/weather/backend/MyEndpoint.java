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

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
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

    public static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=London&APPID=57eac87bbf864b3de29a4c2274497ced";

    @ApiMethod(name = "GetContent")
    public MyBean getContent() throws IOException {;
        MyBean response = new MyBean();
        String data = new HttpClient().get(URL);
        response.setData(data);
        return response;
    }

}
