package com.maksimik.weather.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.maksimik.weather.R;
import com.maksimik.weather.adapters.ViewPagerAdapter;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.model.City;
import com.maksimik.weather.model.Forecast;
import com.maksimik.weather.utils.Contract;
import com.maksimik.weather.utils.WeatherManager;


public class MainActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener {

    private Forecast forecast;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbarInitialize();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), null);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorBold, R.color.item_selected);


        forecast = new Forecast();


        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        int id = sPref.getInt(Constants.HOME_CITY_ID_KEY, 0);
        String name = sPref.getString(Constants.HOME_CITY_NAME_KEY, "");
        sPref.edit().remove(Constants.CITY_ID_KEY).apply();
        sPref.edit().remove(Constants.CITY_NAME_KEY).apply();

        if (id != 0) {
            forecast.setCity(new City(id, name));
            setTitle(name);
            WeatherManager.getInstance(getBaseContext(), this).getWeatherFromDb(id);
        }


    }

    private void toolbarInitialize() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
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

        Intent intent;
        switch (item.getItemId()) {
            case R.id.Settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.aboutTheProgramm:
                intent = new Intent(this, AboutTheProgram.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void showData(Forecast forecast) {

        if (forecast != null) {
            this.forecast.setListWeatherHours(forecast.getListWeatherHours());

            pagerAdapter.setDayWeathers(this.forecast.getListWeatherHours());
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
        WeatherManager.getInstance(getBaseContext(), this).getWeatherFromDb(forecast.getCity().getId());
    }

    @Override
    public void showProgress(boolean isInProgress) {
        mSwipeRefreshLayout.setRefreshing(isInProgress);
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(this, ChangeLocation.class);

        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (forecast.getCity() != null) {
            WeatherManager.getInstance(getBaseContext(), this).getWeather(forecast.getCity().getId());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int homeCityId = sPref.getInt(Constants.HOME_CITY_ID_KEY, 0);
        String homeCityName = sPref.getString(Constants.HOME_CITY_NAME_KEY, "");
        int id = 0;
        String name = "";
        int cityId = sPref.getInt(Constants.CITY_ID_KEY, 0);
        String cityName = sPref.getString(Constants.CITY_NAME_KEY, "");
        if (cityId != 0) {
            id = cityId;
            name = cityName;
        } else if (homeCityId != 0) {
            id = homeCityId;
            name = homeCityName;
        }
        if ((forecast.getCity() == null || forecast.getCity().getId() != id) && id != 0) {

            WeatherManager.getInstance(getBaseContext(), this).getWeather(id);

            forecast.setCity(new City(id, name));
            setTitle(name);
        }
    }
}
