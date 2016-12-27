package com.maksimik.weather.db.annotations;

import com.maksimik.weather.db.ViewedCitesTable;
import com.maksimik.weather.db.WeatherTable;

public final class Contract {

    public static final Class<?>[] MODELS =
            {
                    WeatherTable.class,
                    ViewedCitesTable.class
            };

}
