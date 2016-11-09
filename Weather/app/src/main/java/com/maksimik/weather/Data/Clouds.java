package com.maksimik.weather.data;


import android.os.Parcel;
import android.os.Parcelable;

public class Clouds implements Parcelable {

    private double mAll;

    public Clouds(double all) {
        this.mAll = all;
    }

    public double getAll() {
        return mAll;
    }

    public void setAll(double all) {
        this.mAll = all;
    }

    protected Clouds(Parcel in) {
        mAll = in.readDouble();
    }

    public static final Creator<Clouds> CREATOR = new Creator<Clouds>() {
        @Override
        public Clouds createFromParcel(Parcel in) {
            return new Clouds(in);
        }

        @Override
        public Clouds[] newArray(int size) {
            return new Clouds[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mAll);
    }
}
