package com.maksimik.weather.Data;

import java.text.DateFormat;

/**
 * Created by 1 on 22.10.2016.
 */

public class Date {
    private DateFormat mDateFormat;

    public Date(DateFormat dateFormat){
        this.mDateFormat=dateFormat;
    }

    public DateFormat getDateFormat() {
        return mDateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.mDateFormat = dateFormat;
    }
}
