package com.maksimik.weather.activites;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        presenterViewedCites = new PresenterViewedCites(this, this);
        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        toolbarInitialize();

        editText = (EditText) findViewById(R.id.search);
        presenterCites = new PresenterCites(this, this);

        lvSimple = (ListView) findViewById(R.id.lv);

        linearLayout = (LinearLayout) findViewById(R.id.linerLayout);

        btClear = (ImageButton) findViewById(R.id.btClear);
        btClear.setVisibility(View.INVISIBLE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = editText.getText().toString();

                if (text.length() != 0) {
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
        Toolbar toolBar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(toolBar);

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
    public void showData(final HashMap<String, Integer> data) {
        if (data != null) {
            final String ATTRIBUTE_NAME = "name";
            Map<String, Object> m;

            final ArrayList<Map<String, Object>> list = new ArrayList<>();
            for (String key : data.keySet()) {

                m = new HashMap<>();

                m.put(ATTRIBUTE_NAME, key);
                list.add(m);
            }

            String[] from = {ATTRIBUTE_NAME};
            int[] to = {R.id.tv};


            sAdapter = new SimpleAdapter(this, list, R.layout.list_item, from, to);

            lvSimple.setAdapter(sAdapter);


            lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    TextView tv = (TextView) view.findViewById(R.id.tv);
                    String name = tv.getText().toString();


                    int idCites = data.get(name);
                    //TODO add to if or if(sh.name!=name)
                    if (sPref.getString(Constants.HOME_CITY_NAME_KEY, "") == "") {
                        sPref.edit().putInt(Constants.HOME_CITY_ID_KEY, idCites).apply();
                        sPref.edit().putString(Constants.HOME_CITY_NAME_KEY, name).apply();
                    } else {
                        sPref.edit().putInt(Constants.CITY_ID_KEY, idCites).apply();
                        sPref.edit().putString(Constants.CITY_NAME_KEY, name).apply();
                    }

                    presenterViewedCites.addCites(idCites, name);

                }

//                    System.out.println("_f___" + name);

            });
            sAdapter.getFilter().filter(text);
        }
    }

    @Override
    public void showListCites(ArrayList<Integer> id, ArrayList<String> name) {

    }

    @Override
    public void showListCitesWithWeather(ArrayList<CityWithWeatherHour> list, String image) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showFinish() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickClear(View view) {
        editText.setText("");
    }

}


