package com.maksimik.weather;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvert implements JsonDeserializer<Date> {


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        try {
            return simpleDateFormat.parse(json.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
