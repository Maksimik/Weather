package com.maksimik.weather.activites;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maksimik.weather.R;
import com.maksimik.weather.adapters.CitiesRecyclerViewAdapter;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.loader.ImageLoader;
import com.maksimik.weather.model.City;
import com.maksimik.weather.model.CityWithWeatherHour;
import com.maksimik.weather.utils.ContractViewedCites;
import com.maksimik.weather.utils.PresenterViewedCites;

import java.util.ArrayList;

public class ChangeLocationActivity extends AppCompatActivity implements ContractViewedCites.View {

    private PresenterViewedCites presenterViewedCites;
    private RecyclerView recyclerView;
    private SharedPreferences sPref;
    private ImageLoader imageLoader;
    private ImageView cityImage;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);
        String name = sPref.getString(Constants.HOME_CITY_NAME_KEY, "");
        int id = sPref.getInt(Constants.HOME_CITY_ID_KEY, 0);
        TextView cityName = (TextView) findViewById(R.id.cityName);
        cityImage = (ImageView) findViewById(R.id.cityImage);

        if (id != 0) {

            cityName.setText(name);

        }

        presenterViewedCites = new PresenterViewedCites(ChangeLocationActivity.this, this);
        presenterViewedCites.getListViewedCities(id);

        imageLoader = new ImageLoader();

        setTitle("Местоположения");

    }

    private void initToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.change_locattion_toolbar);
        setSupportActionBar(toolBar);

        setTitle("");

        if (getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClickAddCites(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
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
    public void showListCitesWithWeather(final ArrayList<CityWithWeatherHour> list, String image) {

        imageLoader.displayImage(image, cityImage);

        if (list != null) {
            mAdapter = new CitiesRecyclerViewAdapter(list, this, imageLoader, new CitiesRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(CityWithWeatherHour cityWithWeatherHour) {
                    sPref.edit().putInt(Constants.CITY_ID_KEY, cityWithWeatherHour.getCity().getId()).apply();
                    sPref.edit().putString(Constants.CITY_NAME_KEY, cityWithWeatherHour.getCity().getName()).apply();
                    finish();
                }
            }, new CitiesRecyclerViewAdapter.OnMenuItemClickListener() {


                @Override
                public boolean onMenuItemClick(City city, int pos, MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.delete:
                            presenterViewedCites.deleteCites(city.getId());
                            list.remove(pos);
                            return true;

                        case R.id.chooseMyHouse:
                            sPref.edit().putInt(Constants.HOME_CITY_ID_KEY, city.getId()).apply();
                            sPref.edit().putString(Constants.HOME_CITY_NAME_KEY, city.getName()).apply();
                            finish();

                            return true;
                        default:
                            return false;
                    }
                }
            });
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showFinish() {
        mAdapter.notifyDataSetChanged();
    }

    public void onClickSelectionCity(View view) {
        sPref.edit().remove(Constants.CITY_ID_KEY).apply();
        sPref.edit().remove(Constants.CITY_NAME_KEY).apply();
        finish();
    }
}