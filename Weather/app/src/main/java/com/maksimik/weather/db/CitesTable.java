package com.maksimik.weather.db;

import com.maksimik.weather.db.annotations.Table;
import com.maksimik.weather.db.annotations.type.dbDouble;
import com.maksimik.weather.db.annotations.type.dbInteger;
import com.maksimik.weather.db.annotations.type.dbIntegerPrimaryKeyAutoincrement;
import com.maksimik.weather.db.annotations.type.dbLong;
import com.maksimik.weather.db.annotations.type.dbString;

@Table(name = "Cites")
public final class CitesTable {

    @dbInteger
    public static final String ID = "_id";

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String NAME_UPPER = "name_upper";

//    @dbString
//    public static final String COUNTRY = "country";
//
//    @dbDouble
//    public static final String LON = "lon";
//
//    @dbDouble
//    public static final String LAT = "lat";

}
