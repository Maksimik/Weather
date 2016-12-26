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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CleaningDbAsyncTask().execute();

    }

    private class CleaningDbAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            IDbOperations operations = new DbHelper(SplashActivity.this, 1);
            String sql = WeatherTable.DATE + "<?";
//            TODO delete
            System.out.println(operations.delete(WeatherTable.class, sql, Long.toString((new Date()).getTime())));
//            operations.delete(WeatherTable.class, sql, Long.toString((new Date()).getTime()));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }


}