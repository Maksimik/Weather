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

public class WeatherEveryThreeHoursActivity extends AppCompatActivity implements Contract.View {

    private Forecast forecast;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_every_three_hours);

        initToolbar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), null);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        final Contract.Presenter weatherManager = new WeatherManager(this, this);
        forecast = new Forecast();
        final int id = getIntent().getIntExtra(Constants.CITY_ID_KEY, 0);

        if (id != 0) {
            weatherManager.getWeatherFromDb(id, false);
        }
    }

    private void initToolbar() {
        final Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        setTitle("Подробно");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showData(final Forecast forecast) {
        if (forecast != null) {

            this.forecast.setListWeatherHours(forecast.getListWeatherHours());

            pagerAdapter.setDayWeathers(forecast.getListWeatherHours());
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void showError(final String message) {

    }

    @Override
    public void showProgress(final boolean isInProgress) {

    }
}
