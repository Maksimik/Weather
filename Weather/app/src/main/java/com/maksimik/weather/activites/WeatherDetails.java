package com.maksimik.weather.activites;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.maksimik.weather.data.WeatherHour;
import com.maksimik.weather.R;

public class WeatherDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_details);
        toolbarInitialize();
        WeatherHour weatherHour = getIntent().getParcelableExtra("weather");
        TextView tvTemp = (TextView) findViewById(R.id.tvTemp);
        TextView tvCloudValues = (TextView) findViewById(R.id.tvCloudValues);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvTemp.setText(weatherHour.getMain().getTemp() + "°");
        tvCloudValues.setText(weatherHour.getClouds().getAll() + "%");
        tvDescription.setText(weatherHour.getWeather().getDescription());


    }

    private void toolbarInitialize() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);
        setTitle("Weather details");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}