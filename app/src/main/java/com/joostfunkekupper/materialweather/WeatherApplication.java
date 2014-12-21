package com.joostfunkekupper.materialweather;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joostfunkekupper.materialweather.data.APIService;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class WeatherApplication extends Application {

    private APIService service;

    public APIService getService() {
        return service;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.API_URL))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        service = restAdapter.create(APIService.class);
    }
}
