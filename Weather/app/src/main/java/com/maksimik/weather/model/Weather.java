package com.maksimik.weather.model;


import java.io.Serializable;

public class Weather implements Serializable {

    private int mId;
    private String mMain;
    private String mDescription;
    private String mIcon;

    public Weather(int id,String main,String description, String icon){
        this.mId=id;
        this.mMain=main;
        this.mDescription=description;
        this.mIcon=icon;
    }
    public Weather() {
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String Description) {
        this.mDescription = Description;
    }

    public String getMain() {
        return mMain;
    }

    public void setMain(String main) {
        this.mMain = main;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

}
