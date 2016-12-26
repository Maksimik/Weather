package com.maksimik.weather.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.maksimik.weather.R;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.model.CityWithWeatherHour;
import com.maksimik.weather.utils.ContractViewedCites;
import com.maksimik.weather.utils.PresenterViewedCites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewChangeLocationActivity extends AppCompatActivity implements ContractViewedCites.View, CompoundButton.OnCheckedChangeListener {

    private TextView tvCurrentLocation;
    private PresenterViewedCites presenterViewedCites;
    private SharedPreferences sPref;
    private ListView lvSimple;
    private ArrayList<Integer> listId;
    private ArrayList<String> listName;
    private ArrayList<Map<String, Object>> data;
    private SimpleAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_change_location);

        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        initToolbar();

        setTitle("Изменить местоположение");

        lvSimple = (ListView) findViewById(R.id.listCites);

        presenterViewedCites = new PresenterViewedCites(this, this);
        presenterViewedCites.getListViewedCitesFromDb();

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(this);

        TextView textView = (TextView) findViewById(R.id.cityName);

        String name = sPref.getString(Constants.HOME_CITY_NAME_KEY, "");

        if (name != "") {
            textView.setText(name);
        }

        tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);
        if(sPref.getBoolean(Constants.DETERMINE_CURRENT_LOCATION_KEY, false)){
            switchCompat.setChecked(true);
        }

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            sPref.edit().putBoolean(Constants.DETERMINE_CURRENT_LOCATION_KEY, true).apply();

            tvCurrentLocation.setTextColor(getResources().getColor(R.color.item_selected));

        } else {

            sPref.edit().putBoolean(Constants.DETERMINE_CURRENT_LOCATION_KEY, false).apply();

            tvCurrentLocation.setTextColor(getResources().getColor(R.color.colorBold));
        }
    }

    public void onClickAddCites(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void showListCites(ArrayList<Integer> id, ArrayList<String> name) {
        if (name != null) {

            listId = id;
            listName = name;

            final String ATTRIBUTE_NAME = "name";
            Map<String, Object> m;

            data = new ArrayList<>();
            for (String key : name) {

                m = new HashMap<>();

                m.put(ATTRIBUTE_NAME, key);
                data.add(m);
            }

            String[] from = {ATTRIBUTE_NAME};
            int[] to = {R.id.tvName};

            sAdapter = new SimpleAdapter(this, data, R.layout.list_item_city, from, to);

            lvSimple.setAdapter(sAdapter);

            lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                    sPref.edit().putInt(Constants.CITY_ID_KEY, listId.get(pos)).apply();
                    sPref.edit().putString(Constants.CITY_NAME_KEY, listName.get(pos)).apply();
                    finish();

                }
            });
        }

    }

    @Override
    public void showListCitesWithWeather(ArrayList<CityWithWeatherHour> list, String image) {

    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void showFinish() {
        finish();
    }


    public void showPopup(final View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = lvSimple.getPositionForView((View) v.getParent());
                switch (item.getItemId()) {
                    case R.id.delete:

                        presenterViewedCites.deleteCites(listId.get(position));

                        listId.remove(position);
                        listName.remove(position);
                        data.remove(position);

                        sAdapter.notifyDataSetChanged();
                        return true;

                    case R.id.chooseMyHouse:

                        sPref.edit().putInt(Constants.HOME_CITY_ID_KEY, listId.get(position)).apply();
                        sPref.edit().putString(Constants.HOME_CITY_NAME_KEY, listName.get(position)).apply();
                        finish();

                        return true;
                    default:
                        return false;
                }
            }
        });

        inflater.inflate(R.menu.popupmenu, popup.getMenu());
        popup.show();
    }

}