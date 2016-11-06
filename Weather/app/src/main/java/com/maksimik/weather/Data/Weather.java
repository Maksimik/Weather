package com.maksimik.weather.Data;


import android.os.Parcel;
import android.os.Parcelable;

public class Weather implements Parcelable {

    private int mId;
    private String mMain;
    private String mDescription;
    private String mIcon;

    public Weather() {
    }

    protected Weather(Parcel in) {
        mMain = in.readString();
        mDescription = in.readString();
        mIcon = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMain);
        dest.writeString(mDescription);
        dest.writeString(mIcon);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}
