package com.maksimik.weather;

/**
 * Created by 1 on 21.10.2016.
 */

public class City {
    private String name;
    private int id;

    public City(String name, int id){
        this.id=id;
        this.name=name;
    }

    public String setName(){
        return name;
    }
    public int setId(){
        return id;
    }
}
