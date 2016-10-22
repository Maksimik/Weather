package com.maksimik.weather.Data;


public class Weather {
    private String mMain;
    private String mDescription;

    /*public Weather(String main, String description){
        this.mMain=main;
        this.mDescription=description;
    }*/

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getMain() {
        return mMain;
    }

    public void setMain(String mMain) {
        this.mMain = mMain;
    }
}
