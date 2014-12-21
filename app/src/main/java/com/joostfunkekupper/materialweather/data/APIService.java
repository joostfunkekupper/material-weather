package com.joostfunkekupper.materialweather.data;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface APIService {

    @GET("/weather")
    CurrentWeather fetchWeatherByCityName(@Query("q") String cityName, @Query("units") String units);

    @GET("/group")
    CurrentWeatherList fetchGroupWeatherByCityIds(@Query("id") String ids, @Query("units") String units);
}
