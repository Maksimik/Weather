package com.maksimik.weather;

public class City {
    private int id;
    private String name;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String setName() {
        return name;
    }

    public int setId() {
        return id;
    }
}
