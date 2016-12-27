package com.maksimik.weather.db;

import com.maksimik.weather.db.annotations.Table;
import com.maksimik.weather.db.annotations.type.dbDouble;
import com.maksimik.weather.db.annotations.type.dbInteger;
import com.maksimik.weather.db.annotations.type.dbIntegerPrimaryKeyAutoincrement;
import com.maksimik.weather.db.annotations.type.dbLong;
import com.maksimik.weather.db.annotations.type.dbString;

@Table(name = "WEATHER")
public final class WeatherTable {

    @dbIntegerPrimaryKeyAutoincrement
    public static final String ID = "_id";

    @dbInteger
    public static final String CITY_ID = "city_id";

    @dbLong
    public static final String DATE = "date";

    @dbDouble
    public static final String TEMP = "temp";

    @dbDouble
    public static final String TEMP_MIN = "tempMin";

    @dbDouble
    public static final String TEMP_MAX = "tempMax";

    @dbDouble
    public static final String PRESSURE = "pressure";

    @dbDouble
    public static final String HUMIDITY = "hymidity";

    @dbInteger
    public static final String WEATHER_ID = "weather_id";

    @dbString
    public static final String MAIN = "main";

    @dbString
    public static final String DESCRIPTION = "description";

    @dbString
    public static final String ICON = "icon";

    @dbDouble
    public static final String CLOUDS = "clouds";

    @dbDouble
    public static final String SPEED = "speed";

    @dbDouble
    public static final String DEG = "deg";

    @dbDouble
    public static final String RAIN = "rain";

    @dbDouble
    public static final String SNOW = "snow";

}
