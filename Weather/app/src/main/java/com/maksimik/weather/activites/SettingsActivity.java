package com.maksimik.weather.activites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.maksimik.weather.R;
import com.maksimik.weather.constants.Constants;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences sPref;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();
        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        final SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(this);

        if(sPref.getBoolean(Constants.DETERMINE_CURRENT_LOCATION_KEY, false)){
            switchCompat.setChecked(true);
        }
    }

    private void initToolbar() {
        final Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        setTitle(R.string.settings);

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
    public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {

        if (isChecked) {
            sPref.edit().putBoolean(Constants.DETERMINE_CURRENT_LOCATION_KEY, true).apply();

        } else {

            sPref.edit().putBoolean(Constants.DETERMINE_CURRENT_LOCATION_KEY, false).apply();

        }

    }
}