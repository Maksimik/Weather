package com.maksimik.weather.db.annotations;


import com.maksimik.weather.db.CitesTable;
import com.maksimik.weather.db.ViewedCitesTable;
import com.maksimik.weather.db.WeatherTable;

public class Contract {
    public static final Class<?>[] MODELS =
            {
                    WeatherTable.class,
                    CitesTable.class,
                    ViewedCitesTable.class
            };

}
