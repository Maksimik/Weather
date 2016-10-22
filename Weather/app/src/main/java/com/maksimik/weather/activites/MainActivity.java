package com.maksimik.weather.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maksimik.weather.Data.Forecast;
import com.maksimik.weather.R;
import com.maksimik.weather.utils.Contract;
import com.maksimik.weather.utils.MainPresenter;

public class MainActivity extends AppCompatActivity implements Contract.View {

    private Contract.Presenter presenter;
    private TextView responseView;
    private ProgressBar progressBar;
    private Forecast forecast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        presenter = new MainPresenter((Contract.View) this);
        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = ((ProgressBar) findViewById(R.id.progressIndicator));
        presenter.onReady();
        //responseView.setText("name city: "+forecast.getCity().getName()+"  temp:"+forecast.getWeatherHour(0).getMain().getTemp());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Settings) {
            return true;
        }else if (id == R.id.weatherUpdate) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();
    }

    public void onClickSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
    @Override
    public void showData(Forecast forecast) {

        //responseView.setText("name city: "+forecast.getCity().getName()+"  temp:"+forecast.getWeatherHour(0).getMain().getTemp());
        responseView.setText("name city: "+forecast.getCity().getName()+"  country:"+forecast.getCity().getCountry());
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        progressBar.setVisibility(isInProgress ? View.VISIBLE : View.GONE);
    }
}
