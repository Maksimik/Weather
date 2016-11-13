package com.maksimik.weather.activites;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.maksimik.weather.R;
import com.maksimik.weather.model.WeatherHour;

public class WeatherDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_details);

        initToolbar();

        WeatherHour weatherHour=(WeatherHour) getIntent().getSerializableExtra("weather");

        TextView tvTemp = (TextView) findViewById(R.id.tvTemp);
        TextView tvCloudValues = (TextView) findViewById(R.id.tvCloudValues);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        ImageView iv = (ImageView) findViewById(R.id.imageIcon);

        iv.setImageResource(getResources().getIdentifier("image" + weatherHour.getWeather().getIcon(), "drawable", getPackageName()));
        tvTemp.setText(weatherHour.getMain().getTemp() + "°");
        tvCloudValues.setText(weatherHour.getClouds().getAll() + "%");
        tvDescription.setText(getString(getResources().getIdentifier("forecast_" + weatherHour.getWeather().getIcon(), "string", getPackageName())));
    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);
        setTitle("Weather details");

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
}