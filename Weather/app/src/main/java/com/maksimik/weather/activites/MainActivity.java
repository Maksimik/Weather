package com.maksimik.weather.activites;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.maksimik.weather.R;
import com.maksimik.weather.data.Clouds;
import com.maksimik.weather.data.DayWeather;
import com.maksimik.weather.data.Forecast;
import com.maksimik.weather.data.Main;
import com.maksimik.weather.data.Weather;
import com.maksimik.weather.data.WeatherHour;
import com.maksimik.weather.data.Wind;
import com.maksimik.weather.db.DbHelper;
import com.maksimik.weather.db.IDbOperations;
import com.maksimik.weather.db.WeatherTable;
import com.maksimik.weather.fragments.FiveFragment;
import com.maksimik.weather.fragments.FoureFragment;
import com.maksimik.weather.fragments.OneFragment;
import com.maksimik.weather.fragments.ThreeFragment;
import com.maksimik.weather.fragments.TwoFragment;
import com.maksimik.weather.utils.Contract;
import com.maksimik.weather.utils.MainPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Contract.View {

    private Contract.Presenter presenter;
    private ProgressBar progressBar;
    private Forecast forecast;
    private ViewPager viewPager;
    private IDbOperations operations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        progressBar = ((ProgressBar) findViewById(R.id.progressIndicator));
        progressBar.setVisibility(View.INVISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        operations = new DbHelper(this, 1);
        presenter = new MainPresenter(this);
        toolbarInitialize();
        new DbTask().execute();
    }

    private void toolbarInitialize() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            case R.id.weatherUpdate:
                progressBar.setVisibility(View.VISIBLE);
                presenter.onReady();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void showData(Forecast forecast) {

        this.forecast = forecast;
        operations.delete(WeatherTable.class, null);
        FillingDB();
        FillingViewPage(viewPager.getCurrentItem());
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        progressBar.setVisibility(isInProgress ? View.VISIBLE : View.GONE);
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd");
        final SimpleDateFormat format2 = new SimpleDateFormat("EEE dd");
//TODO make fine add Fragment
        adapter.addFragment(new OneFragment(), "Сегодня \n" + format1.format(new Date()));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        adapter.addFragment(new TwoFragment(), format2.format(new Date(c.getTimeInMillis())));
        c.add(Calendar.DAY_OF_MONTH, 1);
        adapter.addFragment(new ThreeFragment(), format2.format(new Date(c.getTimeInMillis())));
        c.add(Calendar.DAY_OF_MONTH, 1);
        adapter.addFragment(new FoureFragment(), format2.format(new Date(c.getTimeInMillis())));
        c.add(Calendar.DAY_OF_MONTH, 1);
        adapter.addFragment(new FiveFragment(), format2.format(new Date(c.getTimeInMillis())));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FillingViewPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(this, ChangeLocation.class);
        if (forecast != null) {
            if (forecast.getCity() != null) {
                intent.putExtra("city", forecast.getCity().getName());
            }
        }
        startActivity(intent);
    }

    public void FillingViewPage(final int position) {

//TODO remake and flies when there is no data
        if (forecast != null) {
            int idList;
            if (position == 0) {
                idList = R.id.list1;
            } else if (position == 1) {
                idList = R.id.list2;
            } else if (position == 2) {
                idList = R.id.list3;
            } else if (position == 3) {
                idList = R.id.list4;
            } else {
                idList = R.id.list5;
            }

            final String ATTRIBUTE_TIME = "time";
            final String ATTRIBUTE_TEMP = "temp";
            final String ATTRIBUTE_ICON = "icon";
            final String ATTRIBUTE_DESCRIPTION = "description";
            Map<String, Object> m;
            ListView lvSimple;
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

            ArrayList<Map<String, Object>> data = new ArrayList<>();

            for (int i = 0; i < forecast.getDayWeather(position).size(); i++) {
                m = new HashMap<>();
                m.put(ATTRIBUTE_TIME, dateFormat.format(forecast.getDayWeather(position).getWeatherHour(i).getDate()));
                m.put(ATTRIBUTE_ICON, forecast.getDayWeather(position).getWeatherHour(i).getWeather().getIcon());
                m.put(ATTRIBUTE_DESCRIPTION, getString(getResources().getIdentifier("forecast_" + forecast.getDayWeather(position).getWeatherHour(i).getWeather().getIcon(), "string", getPackageName())));
                m.put(ATTRIBUTE_TEMP, (int) forecast.getDayWeather(position).getWeatherHour(i).getMain().getTemp() + "°");
                data.add(m);
            }


            String[] from = {ATTRIBUTE_TIME, ATTRIBUTE_ICON, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_TEMP};
            int[] to = {R.id.textViewTime, R.id.imageView, R.id.textViewDescription, R.id.textViewTemp};
            SimpleAdapter sAdapter = new SimpleAdapter(getBaseContext(), data, R.layout.list_item, from, to);
            sAdapter.setViewBinder(new MyViewBinder());
            lvSimple = (ListView) findViewById(idList);

            lvSimple.setAdapter(sAdapter);
            System.out.println("pposition: " + position);
            lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    Intent intent = new Intent(getBaseContext(), WeatherDetails.class);
                    intent.putExtra("weather", forecast.getDayWeather(position).getWeatherHour(pos));

                    startActivity(intent);


                }
            });

        }
    }

    public void FillingDB() {
        List<ContentValues> listContantValues = new ArrayList<>();
        //ContentValues values = new ContentValues();
        ContentValues values;
        for (DayWeather dayWeather : forecast.getListWeatherHours()) {
            for (WeatherHour weatherHour : dayWeather.getDayWeather()) {
                values = new ContentValues();

                values.put(WeatherTable.DATE, weatherHour.getDate());
                values.put(WeatherTable.TEMP, weatherHour.getMain().getTemp());
                values.put(WeatherTable.TEMP_MIN, weatherHour.getMain().getTempMin());
                values.put(WeatherTable.TEMP_MAX, weatherHour.getMain().getTempMax());
                values.put(WeatherTable.PRESSURE, weatherHour.getMain().getPressure());
                values.put(WeatherTable.HUMIDITY, weatherHour.getMain().getHumidity());
                values.put(WeatherTable.WEATHER_ID, weatherHour.getWeather().getId());
                values.put(WeatherTable.Main, weatherHour.getWeather().getMain());
                values.put(WeatherTable.DESCRIPTION, weatherHour.getWeather().getDescription());
                values.put(WeatherTable.ICON, weatherHour.getWeather().getIcon());
                values.put(WeatherTable.CLOUDS, weatherHour.getClouds().getAll());
                values.put(WeatherTable.SPEED, weatherHour.getWind().getSpeed());
                values.put(WeatherTable.DEG, weatherHour.getWind().getDeg());


                listContantValues.add(values);
            }
        }
        int rowID = operations.bulkInsert(WeatherTable.class, listContantValues);

        Log.i("TAG", "row inserted, ID = " + rowID);
    }

    @Nullable
    private Forecast GetDataDB() {


        Cursor cursor = operations.query("SELECT * FROM " + DbHelper.getTableName(WeatherTable.class));
        if (cursor.moveToFirst()) {

            Date temp = new Date();
            Forecast f = new Forecast();
            WeatherHour weatherHour;

            DayWeather dayWeather = new DayWeather();
            int i = 0;
            do {
                long date = cursor.getLong(cursor.getColumnIndex(WeatherTable.DATE));
                if (i == 0) {
                    temp = new Date(date);
                    i++;
                }
                Main main = new Main(cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MIN)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP_MAX)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.PRESSURE)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.HUMIDITY)));

                System.out.println(cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP)));
                Weather weather = new Weather(cursor.getInt(cursor.getColumnIndex(WeatherTable.WEATHER_ID)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.Main)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.ICON)));


                weatherHour = new WeatherHour(date, main, weather, new Clouds(cursor.getDouble(cursor.getColumnIndex(WeatherTable.CLOUDS))),
                        new Wind(cursor.getDouble(cursor.getColumnIndex(WeatherTable.SPEED)),
                                cursor.getDouble(cursor.getColumnIndex(WeatherTable.DEG))));
//TODO // FIXME: 10.11.2016
                if ((new Date(date)).getDate() != temp.getDate()) {
                    temp = new Date(date);
                    f.add(dayWeather);
                    dayWeather = new DayWeather();
                }
                dayWeather.add(weatherHour);

            } while (cursor.moveToNext());

            return f;
        }
        cursor.close();
        return null;
    }

    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
//            if ((view instanceof ImageView) & (data instanceof Bitmap)) {
//                ImageView iv = (ImageView) view;
//                Bitmap bm = (Bitmap) data;
//                iv.setImageBitmap(bm);
//                return true;
//            }
//            return false;
//       }
            if (view.getId() == R.id.imageView) {
                ImageView iv = (ImageView) view;
                iv.setImageResource(getResources().getIdentifier("image" + data.toString(), "drawable", getPackageName()));
                return true;
            }
            return false;
        }
    }

    private class DbTask extends AsyncTask<Void, Void, Forecast> {

        @Override
        protected Forecast doInBackground(Void... params) {
            return GetDataDB();
        }

        @Override
        protected void onPostExecute(Forecast f) {
            super.onPostExecute(f);
            forecast = f;
            FillingViewPage(viewPager.getCurrentItem());
        }
    }
}
