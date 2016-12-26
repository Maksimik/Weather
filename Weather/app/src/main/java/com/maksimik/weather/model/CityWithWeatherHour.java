package com.maksimik.weather.model;

public class CityWithWeatherHour {

    private City city;
    private WeatherHour weatherHour;

    public CityWithWeatherHour(City city, WeatherHour weatherHour) {
        this.city = city;
        this.weatherHour = weatherHour;
    }

    public CityWithWeatherHour() {

    }


    public WeatherHour getWeatherHour() {
        return weatherHour;
    }

    public void setWeatherHour(WeatherHour weatherHour) {
        this.weatherHour = weatherHour;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
