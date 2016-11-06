package com.maksimik.weather.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.http.HttpClientWeather;
import com.maksimik.weather.Data.Forecast;
import com.maksimik.weather.Data.ListIcon;
import com.maksimik.weather.backend.myApi.MyApi;
import com.maksimik.weather.backend.myApi.model.MyBean;

import java.io.IOException;
import java.util.HashSet;

import static android.content.ContentValues.TAG;


public class MainPresenter implements Contract.Presenter {

    private Contract.View view;
    private Handler handler;
    private Forecast forecast;
    private String id = "627904";
    private ListIcon listIcon;

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
        HashSet<String> listCodeIcon = parseJsonOverJSONObject.listIcon;
        int i = 0;
        listIcon = new ListIcon();
        for (String temp : listCodeIcon) {
            byte[] bytes = loadIcon(temp);
            //listIcon.add(temp, BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            if (bitmap == null) {
//                System.out.println("i stupid");
//            } else {
//                System.out.println("dadada");
//            }
            listIcon.add(temp, bitmap);
            //System.out.println(temp);
        }
    }

    private byte[] loadIcon(String code) {
        return new HttpClientWeather().getIcon("http://openweathermap.org/img/w/" + code + ".png");
       /*try {
            MyApi.GetIcon call = ApiManager.get().myApi().getIcon();
            IconBean bean = call.execute();
            return bean.getIconBean().getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
*/

   /*     try {

            URL reqUrl = new URL("http://openweathermap.org/img/w/10d.png");
            HttpURLConnection connection = ((HttpURLConnection) reqUrl.openConnection());
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            final BitmapFactory.Options options = new BitmapFactory.Options();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(inputStream.available());
            byte[] chunk = new byte[1 << 16];
            int bytesRead;
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap==null){
                System.out.println("i stupid");
            }
            else
            {
                System.out.println("dadada");
            }
            return bytes;
        }catch (IOException e){

        }
        return null;*/
    }

    private void notifyResponse() {
        handler.post(new Runnable() {
            //ParseJsonOverJSONObject parseJsonOverJSONObject = new ParseJsonOverJSONObject();

            //ParseJsonOverGson parseJsonOverGson = new ParseJsonOverGson();
            @Override
            public void run() {
                view.showProgress(false);
                view.showData(forecast, listIcon);
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

