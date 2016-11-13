package com.maksimik.weather.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.maksimik.weather.R;
import com.maksimik.weather.model.Forecast;
import com.maksimik.weather.utils.Contract;
import com.maksimik.weather.utils.WeatherManager;


public class MainActivity extends AppCompatActivity implements Contract.View {

    private ProgressBar progressBar;
    private Forecast forecast;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbarInitialize();

        progressBar = ((ProgressBar) findViewById(R.id.progressIndicator));
        progressBar.setVisibility(View.INVISIBLE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), null);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        WeatherManager.getInstance(getBaseContext(), this).getWeatherFromDb();

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
                WeatherManager.getInstance(getBaseContext(), this).getWeather();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void showData(Forecast forecast) {

        this.forecast = forecast;

        pagerAdapter.setDayWeathers(forecast.getListWeatherHours());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        progressBar.setVisibility(isInProgress ? View.VISIBLE : View.GONE);
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

}
