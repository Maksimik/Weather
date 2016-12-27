package com.maksimik.weather.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maksimik.weather.R;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.model.City;
import com.maksimik.weather.model.CityWithWeatherHour;
import com.maksimik.weather.model.Coord;
import com.maksimik.weather.model.Forecast;
import com.maksimik.weather.utils.Contract;
import com.maksimik.weather.utils.MyLocationListener;
import com.maksimik.weather.utils.WeatherManager;


public class HomeActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences sPref;
    private CityWithWeatherHour cityWithWeatherHour;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeatherManager weatherManager;
    private Coord coord;
    private TextView tvCityName;
    private ImageView btnHome;

    private TextView tvTemp;
    private TextView tvDescription;
    private TextView tvWindSpeed;
    private TextView tvHumidity;
    private TextView tvPressure;
    private TextView tvTempMinMax;
    private TextView tvCloudStart;
    private TextView tvRain;
    private TextView tvSnow;
    private LinearLayout imageWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();

        tvTemp = (TextView) findViewById(R.id.tvTempHome);
        tvDescription = (TextView) findViewById(R.id.tvDescriptionHome);
        tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeedHome);
        tvHumidity = (TextView) findViewById(R.id.tvHumidityHome);
        tvPressure = (TextView) findViewById(R.id.tvPressureHome);
        tvTempMinMax = (TextView) findViewById(R.id.tvTempMinMaxHome);
        tvCloudStart = (TextView) findViewById(R.id.tvCloudHome);
        tvRain = (TextView) findViewById(R.id.tvRainHome);
        tvSnow = (TextView) findViewById(R.id.tvSnowHome);

        imageWeather = (LinearLayout) findViewById(R.id.imageWeather);
        imageWeather.setBackgroundResource(R.drawable.imgbackground);

        tvRain.setText("-");
        tvSnow.setText("-");
        tvPressure.setText("-");
        tvHumidity.setText("-");
        tvTemp.setText("-");


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        cityWithWeatherHour = new CityWithWeatherHour();

        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        int id = sPref.getInt(Constants.HOME_CITY_ID_KEY, 0);
        String name = sPref.getString(Constants.HOME_CITY_NAME_KEY, "");

        sPref.edit().remove(Constants.CITY_ID_KEY).apply();
        sPref.edit().remove(Constants.CITY_NAME_KEY).apply();

        tvCityName = (TextView) findViewById(R.id.cityNameHome);
        btnHome = (ImageView) findViewById(R.id.btnHome);

        weatherManager = new WeatherManager(HomeActivity.this, this);

        if (id != 0) {
            cityWithWeatherHour.setCity(new City(id, name));
            setTitle("");

            tvCityName.setText(name);
            btnHome.setVisibility(View.VISIBLE);

//            WeatherManager.getInstance(HomeActivity.this, this).getWeatherFromDb(id);
            weatherManager.getWeatherFromDb(id, true);
        }
    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.homeToolbar);
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
                intent = new Intent(this, AboutTheProgramActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    public void onClickSearch(View view) {

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);

    }

    public void onClickOtherCities(View view) {
        Intent intent = new Intent(this, ChangeLocationActivity.class);
        startActivity(intent);
    }

    public void onClickDetails(View view) {

        Intent intent = new Intent(this, WeatherEveryThreeHoursActivity.class);
        intent.putExtra(Constants.CITY_ID_KEY, cityWithWeatherHour.getCity().getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {

        if (coord != null) {
            weatherManager.getWeather(coord.getLat(), coord.getLon());
        } else if (cityWithWeatherHour.getCity() != null) {
            weatherManager.getWeather(cityWithWeatherHour.getCity().getId());
        }
    }

    @Override
    public void showData(Forecast forecast) {
        if (forecast != null) {
            if (forecast.getCity() != null && cityWithWeatherHour.getCity().getId() != forecast.getCity().getId()) {

                cityWithWeatherHour.setCity(forecast.getCity());
                tvCityName.setText(cityWithWeatherHour.getCity().getName());

                sPref.edit().putInt(Constants.HOME_CITY_ID_KEY, forecast.getCity().getId()).apply();
                sPref.edit().putString(Constants.HOME_CITY_NAME_KEY, forecast.getCity().getName()).apply();
            }

            cityWithWeatherHour.setWeatherHour(forecast.getDayWeather(0).getWeatherHour(0));

            imageWeather.setBackgroundResource(getResources().getIdentifier(Constants.IMG + forecast.getListWeatherHours().get(0).getWeatherHour(0).getWeather().getIcon(), "drawable", getPackageName()));

            if (cityWithWeatherHour.getWeatherHour() != null) {
                tvTemp.setText(String.format(getString(R.string.temp), cityWithWeatherHour.getWeatherHour().getMain().getTemp()));
                tvDescription.setText(getString(getResources().getIdentifier("forecast_" + forecast.getListWeatherHours().get(0).getWeatherHour(0).getWeather().getIcon(),
                        "string", getPackageName())));

                tvWindSpeed.setText(String.valueOf(cityWithWeatherHour.getWeatherHour().getWind().getSpeed() + "КМ/Ч"));
                tvHumidity.setText(String.valueOf(cityWithWeatherHour.getWeatherHour().getMain().getHumidity() + "%"));
                tvPressure.setText(String.valueOf(cityWithWeatherHour.getWeatherHour().getMain().getPressure()));
                tvTempMinMax.setText(cityWithWeatherHour.getWeatherHour().getMain().getTempMin() + "/" + cityWithWeatherHour.getWeatherHour().getMain().getTempMax());
                tvCloudStart.setText(String.valueOf(cityWithWeatherHour.getWeatherHour().getClouds().getAll() + "%"));

                double value = cityWithWeatherHour.getWeatherHour().getRain().getValue();
                if (value != 0) {
                    tvRain.setText(String.format(getString(R.string.rain_or_snow_home), value));
                }
                value = cityWithWeatherHour.getWeatherHour().getSnow().getValue();
                if (value != 0) {
                    tvSnow.setText(String.format(getString(R.string.rain_or_snow_home), value));
                }
            }

        }
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
//        WeatherManager.getInstance(HomeActivity.this, this).getWeatherFromDb(forecast.getCity().getId());
        weatherManager.getWeatherFromDb(cityWithWeatherHour.getCity().getId(), true);
    }

    @Override
    public void showProgress(boolean isInProgress) {

        mSwipeRefreshLayout.setRefreshing(isInProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (coord == null && sPref.getBoolean(Constants.DETERMINE_CURRENT_LOCATION_KEY, false)) {
            MyLocationListener.SetUpLocationListener(this);
            coord = new Coord(MyLocationListener.imHere.getLatitude(), MyLocationListener.imHere.getLongitude());
            weatherManager.getWeather(coord.getLat(), coord.getLon());

        } else {
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
            if ((cityWithWeatherHour.getCity() == null || cityWithWeatherHour.getCity().getId() != id) && id != 0) {

//            WeatherManager.getInstance(HomeActivity.this, this).getWeather(id);
                weatherManager.getWeather(id);
                cityWithWeatherHour.setCity(new City(id, name));
                if (homeCityId == id) {
                    btnHome.setVisibility(View.VISIBLE);
                } else {
                    btnHome.setVisibility(View.INVISIBLE);
                }
                tvCityName.setText(name);
                setTitle("");
            }
        }
    }
}