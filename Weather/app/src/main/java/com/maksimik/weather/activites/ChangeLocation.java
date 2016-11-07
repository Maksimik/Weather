package com.maksimik.weather.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.maksimik.weather.R;

public class ChangeLocation extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    TextView tvCurrentLocation;
    TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_location);
        toolbarInitialize();
        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(this);
        TextView textView = (TextView) findViewById(R.id.cityName);
        textView.setText(getIntent().getStringExtra("city"));
        tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);

        tvLocation = (TextView) findViewById(R.id.tvLocation);
    }

    private void toolbarInitialize() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);
        setTitle(getString(R.string.change_location));

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            MyLocationListener.SetUpLocationListener(this);

            tvCurrentLocation.setTextColor(getResources().getColor(R.color.item_selected));

            tvLocation.setText(String.format("Coordinates: lat = %1$.4f, lon = %2$.4f",
                    MyLocationListener.imHere.getLatitude(), MyLocationListener.imHere.getLongitude()));
        } else {
            tvLocation.setText("");
            tvCurrentLocation.setTextColor(getResources().getColor(R.color.colorBold));
        }
    }
}