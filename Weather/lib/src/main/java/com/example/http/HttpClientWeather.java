package com.example.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpClientWeather {

    public String get(String url) {
        String response = null;
        try {
            URL reqUrl = new URL(url);
            HttpURLConnection connection = ((HttpURLConnection) reqUrl.openConnection());
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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

    public byte[] getIcon(String url) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            URL reqUrl = new URL(url);
            HttpURLConnection connection = ((HttpURLConnection) reqUrl.openConnection());
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();

            byteArrayOutputStream = new ByteArrayOutputStream(inputStream.available());

            byte[] chunk = new byte[1 << 16];
            int bytesRead;
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            inputStream.close();
            connection.disconnect();
            //byteArrayOutputStream.close();
            return bytes;

            //return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
