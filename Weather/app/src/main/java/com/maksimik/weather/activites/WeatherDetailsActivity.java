package com.maksimik.weather.activites;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.maksimik.weather.R;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.model.WeatherHour;

public class WeatherDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        initToolbar();

        final WeatherHour weatherHour = (WeatherHour) getIntent().getSerializableExtra(Constants.WEATHER_DETAILS_KEY);

        final TextView tvTemp = (TextView) findViewById(R.id.tvTemp);
        final TextView tvCloud = (TextView) findViewById(R.id.tvCloudValues);
        final TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        final ImageView iv = (ImageView) findViewById(R.id.imageIcon);
        final TextView tvRain = (TextView) findViewById(R.id.tvRainValue);
        final TextView tvSnow = (TextView) findViewById(R.id.tvSnowValue);
        final TextView tvPressure = (TextView) findViewById(R.id.tvPressureValue);
        final TextView tvHumidity = (TextView) findViewById(R.id.tvHumidityValue);
        final TextView tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeedValue);


        double value = weatherHour.getRain().getValue();

        if (value != 0) {
            tvRain.setText(String.format(getString(R.string.rain_or_snow_home), value));
        } else {
            tvRain.setText("-");
        }

        double v = weatherHour.getSnow().getValue();

        if (v != 0) {
            tvSnow.setText(String.format(getString(R.string.rain_or_snow_home), v));
        } else {
            tvSnow.setText("-");
        }

        iv.setImageResource(getResources().getIdentifier(Constants.IMAGE + weatherHour.getWeather().getIcon(), "drawable", getPackageName()));
        tvTemp.setText(String.format(getString(R.string.temp), weatherHour.getMain().getTemp()));
        tvCloud.setText(String.valueOf(weatherHour.getClouds().getAll() + "%"));
        tvPressure.setText(String.valueOf(weatherHour.getMain().getPressure()));
        tvHumidity.setText(String.valueOf(weatherHour.getMain().getHumidity() + "%"));
        tvWindSpeed.setText(String.format(getString(R.string.wind_speed_value), weatherHour.getWind().getSpeed()));
        tvDescription.setText(getString(getResources().getIdentifier("forecast_" + weatherHour.getWeather().getIcon(), "string", getPackageName())));
    }

    private void initToolbar() {
        final Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        setTitle(getString(R.string.additionally));

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
}