package com.maksimik.weather.model;

import java.io.Serializable;

public class Weather implements Serializable {

    private int mId;
    private String mMain;
    private String mDescription;
    private String mIcon;

    public Weather(final int id, final String main, final String description, final String icon) {
        this.mId = id;
        this.mMain = main;
        this.mDescription = description;
        this.mIcon = icon;
    }

    public Weather() {
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(final String Description) {
        this.mDescription = Description;
    }

    public String getMain() {
        return mMain;
    }

    public void setMain(final String main) {
        this.mMain = main;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(final String icon) {
        this.mIcon = icon;
    }

    public int getId() {
        return mId;
    }

    public void setId(final int id) {
        this.mId = id;
    }

}
