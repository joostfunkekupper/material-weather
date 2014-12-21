package com.joostfunkekupper.materialweather.realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmDataSource {

    private Context mContext;

    private Realm mRealm;

    public RealmDataSource(Context context) {
        mContext = context;

        mRealm = Realm.getInstance(context);
    }

    public void destroy() {
        mRealm.close();
    }

    public void addCity(City city) {
        City foundCity = doesCityExist(city.getId());
        if (foundCity != null) {
            mRealm.beginTransaction();
            foundCity.setTemperature(city.getTemperature());
            mRealm.commitTransaction();
            return;
        }

        addNewCity(city);
    }

    public void addNewCity(City city) {
        mRealm.beginTransaction();

        City realmCity = mRealm.createObject(City.class);
        realmCity.setId(city.getId());
        realmCity.setName(city.getName());
        realmCity.setCountry(city.getCountry());
        realmCity.setTemperature(city.getTemperature());

        mRealm.commitTransaction();
    }

    public City doesCityExist(String id) {
        RealmResults<City> results = mRealm.where(City.class).equalTo("id", id).findAll();

        if (results.size() > 0)
            return mRealm.where(City.class).equalTo("id", id).findAll().get(0);
        else
            return null;
    }
}
