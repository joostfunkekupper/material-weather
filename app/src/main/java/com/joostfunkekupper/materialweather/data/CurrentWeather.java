package com.joostfunkekupper.materialweather.data;

import java.util.List;

public class CurrentWeather {

    public String id; // City id
    public String name; // City name

    public Sys sys;
    public List<Weather> weather;
    public Main main;
}
