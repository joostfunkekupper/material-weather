package com.joostfunkekupper.materialweather.realm;

import io.realm.RealmObject;

public class City extends RealmObject{

    private String id;
    private String name;
    private String country;
    private double temperature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public City() {}

    public City(String id, String name, String country, double temperature) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.temperature = temperature;
    }
}
