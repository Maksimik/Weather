package com.maksimik.weather.model;

public class CityWithWeatherHour {

    private City city;
    private WeatherHour weatherHour;

    public CityWithWeatherHour(final City city, final WeatherHour weatherHour) {
        this.city = city;
        this.weatherHour = weatherHour;
    }

    public CityWithWeatherHour() {

    }

    public WeatherHour getWeatherHour() {
        return weatherHour;
    }

    public void setWeatherHour(final WeatherHour weatherHour) {
        this.weatherHour = weatherHour;
    }

    public City getCity() {
        return city;
    }

    public void setCity(final City city) {
        this.city = city;
    }
}
