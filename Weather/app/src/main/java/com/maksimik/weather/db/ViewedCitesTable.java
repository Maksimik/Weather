package com.maksimik.weather.db;

import com.maksimik.weather.db.annotations.Table;
import com.maksimik.weather.db.annotations.type.dbInteger;
import com.maksimik.weather.db.annotations.type.dbString;

@Table(name = "ViewedCites")
public final class ViewedCitesTable {

    @dbInteger
    public static final String ID = "_id";

    @dbString
    public static final String NAME = "name";

}
