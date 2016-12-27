package com.maksimik.weather.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.maksimik.weather.R;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.model.CityWithWeatherHour;
import com.maksimik.weather.utils.ContractCites;
import com.maksimik.weather.utils.ContractViewedCites;
import com.maksimik.weather.utils.PresenterCites;
import com.maksimik.weather.utils.PresenterViewedCites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements ContractCites.View, ContractViewedCites.View {

    private ListView lvSimple;

    private PresenterCites presenterCites;

    private EditText editText;
    private SimpleAdapter sAdapter;
    private SharedPreferences sPref;
    private ImageButton btClear;
    private LinearLayout linearLayout;
    private PresenterViewedCites presenterViewedCites;
    private String text;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        presenterViewedCites = new PresenterViewedCites(this, this);
        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        toolbarInitialize();

        editText = (EditText) findViewById(R.id.search);
        presenterCites = new PresenterCites(this);

        lvSimple = (ListView) findViewById(R.id.lv);

        linearLayout = (LinearLayout) findViewById(R.id.linerLayout);

        btClear = (ImageButton) findViewById(R.id.btClear);
        btClear.setVisibility(View.INVISIBLE);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable s) {

            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                text = editText.getText().toString();

                if (!text.isEmpty()) {
                    lvSimple.setVisibility(View.VISIBLE);
                    btClear.setVisibility(View.VISIBLE);

                    linearLayout.setVisibility(View.INVISIBLE);

                    if (text.length() == 1) {
                        presenterCites.getListCites(text);

                    } else {

                        if (sAdapter == null) {

                            presenterCites.getListCites(text.substring(0, 1));
                        } else {
                            sAdapter.getFilter().filter(text);
                        }
                    }

                } else {
                    btClear.setVisibility(View.INVISIBLE);
                    lvSimple.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void toolbarInitialize() {
        final Toolbar toolBar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(toolBar);

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
    public void showData(final HashMap<String, Integer> data) {
        if (data != null) {

            final String ATTRIBUTE_NAME = "name";
            final String ATTRIBUTE_ICON = "icon";
            Map<String, Object> m;

            final ArrayList<Map<String, Object>> list = new ArrayList<>();
            for (final String key : data.keySet()) {

                m = new HashMap<>();
                m.put(ATTRIBUTE_NAME, key);

                list.add(m);
            }

            final String[] from = {ATTRIBUTE_NAME, ATTRIBUTE_ICON};
            final int[] to = {R.id.tv};

            sAdapter = new SimpleAdapter(this, list, R.layout.list_item, from, to);
            lvSimple.setAdapter(sAdapter);

            lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {

                    final TextView tv = (TextView) view.findViewById(R.id.tv);
                    final String name = tv.getText().toString();

                    final int idCites = data.get(name);

                    if (sPref.getInt(Constants.HOME_CITY_ID_KEY, 0) == 0) {
                        sPref.edit().putInt(Constants.HOME_CITY_ID_KEY, idCites).apply();
                        sPref.edit().putString(Constants.HOME_CITY_NAME_KEY, name).apply();
                    } else {
                        sPref.edit().putInt(Constants.CITY_ID_KEY, idCites).apply();
                        sPref.edit().putString(Constants.CITY_NAME_KEY, name).apply();
                    }

                    presenterViewedCites.addCites(idCites, name);

                }

            });
            sAdapter.getFilter().filter(text);
        }
    }

    @Override
    public void showListCitesWithWeather(final ArrayList<CityWithWeatherHour> list, final String image) {

    }

    @Override
    public void showError(final String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
    }

    @Override
    public void showFinish() {

        final Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickClear(final View view) {
        editText.setText("");
    }

}


