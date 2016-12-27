package com.maksimik.weather.activites;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.maksimik.weather.db.DbHelper;
import com.maksimik.weather.db.IDbOperations;
import com.maksimik.weather.db.WeatherTable;

import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CleaningDbAsyncTask().execute();

    }

    private class CleaningDbAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... voids) {

            final IDbOperations operations = new DbHelper(SplashActivity.this, 1);

            final String sql = WeatherTable.DATE + "<?";

            operations.delete(WeatherTable.class, sql, Long.toString((new Date()).getTime()));

            return null;
        }

        @Override
        protected void onPostExecute(final Void v) {
            final Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }


}