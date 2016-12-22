package com.maksimik.weather.activites;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.maksimik.weather.R;
import com.maksimik.weather.adapters.ViewPagerAdapter;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.model.Forecast;
import com.maksimik.weather.utils.Contract;
import com.maksimik.weather.utils.WeatherManager;

public class WeatherEveryThreeHours extends AppCompatActivity implements Contract.View {

    private Forecast forecast;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_every_three_hours);

        initToolbar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), null);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        WeatherManager weatherManager = new WeatherManager(getBaseContext(), this);
        forecast = new Forecast();
        int id = getIntent().getIntExtra(Constants.CITY_ID_KEY, 0);

        if (id != 0) {
            weatherManager.getWeatherFromDb(id);
        }
    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        setTitle("Подробно");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showData(Forecast forecast) {
        if (forecast != null) {

            this.forecast.setListWeatherHours(forecast.getListWeatherHours());

            pagerAdapter.setDayWeathers(forecast.getListWeatherHours());
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showProgress(boolean isInProgress) {

    }
}
