package com.example.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpClient {

    public String get(String url) {
        String response = null;
        try {
            URL reqUrl = new URL(url);
            HttpURLConnection connection = ((HttpURLConnection) reqUrl.openConnection());
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            response = stringBuilder.toString();

            inputStream.close();
            reader.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
