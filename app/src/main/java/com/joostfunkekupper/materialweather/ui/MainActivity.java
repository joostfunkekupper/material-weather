package com.joostfunkekupper.materialweather.ui;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.joostfunkekupper.materialweather.R;
import com.joostfunkekupper.materialweather.WeatherApplication;
import com.joostfunkekupper.materialweather.adapter.CityCardViewRecyclerAdapter;
import com.joostfunkekupper.materialweather.data.CurrentWeather;
import com.joostfunkekupper.materialweather.data.CurrentWeatherList;
import com.joostfunkekupper.materialweather.realm.City;
import com.joostfunkekupper.materialweather.realm.RealmDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import retrofit.RetrofitError;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private WeatherApplication app;

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;

    private RealmDataSource mRealmDataSource;

    private List<City> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (WeatherApplication) getApplication();

        realm = Realm.getInstance(this);

        mRealmDataSource = new RealmDataSource(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (realm.where(City.class).findAll().size() == 0)
            createSampleData();

        ArrayList<String> params = new ArrayList<>();
        for (Iterator<City> cityIterator = realm.where(City.class).findAll().iterator(); cityIterator.hasNext();){
            City city = cityIterator.next();
            cityList.add(city);
            params.add(city.getId());
        }

        mAdapter = new CityCardViewRecyclerAdapter(cityList);
        mRecyclerView.setAdapter(mAdapter);

        new FetchAllCitiesWeatherTask().execute(params);
    }

    @Override
    protected void onDestroy() {
        if (realm != null)
            realm.close();

        mRealmDataSource.destroy();

        super.onDestroy();
    }

    private void createSampleData() {
        mRealmDataSource.addCity(new City("2158177", "Melbourne", "AU", 0.0));
        mRealmDataSource.addCity(new City("2643743", "London", "UK", 0.0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchCityWeatherTask extends AsyncTask<String, Void, CurrentWeather> {

        @Override
        protected CurrentWeather doInBackground(String... params) {
            try {
                return app.getService().fetchWeatherByCityName(params[0], "metric");
            }
            catch (RetrofitError error) {
                Log.e(TAG, error.toString());
                return null;
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(CurrentWeather currentWeather) {
            if (currentWeather != null) {
                cityList.clear();

                City city = new City(currentWeather.id, currentWeather.name, currentWeather.sys.country, currentWeather.main.temp);

                cityList.add(city);

                mRealmDataSource.addCity(city);

                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class FetchAllCitiesWeatherTask extends AsyncTask<List<String>, Void, CurrentWeatherList> {

        @Override
        protected CurrentWeatherList doInBackground(List<String>... params) {
            try {
                StringBuilder ids = new StringBuilder();
                for (String p : params[0]) {
                    if (ids.length() > 0) ids.append(",");
                    ids.append(p);
                }
                return app.getService().fetchGroupWeatherByCityIds(ids.toString(), "metric");
            }
            catch (RetrofitError error) {
                Log.e(TAG, error.toString());
                return null;
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(CurrentWeatherList currentWeatherList) {
            if (currentWeatherList != null && currentWeatherList.list.size() > 0) {
                cityList.clear();

                for (CurrentWeather currentWeather : currentWeatherList.list) {
                    City city = new City(currentWeather.id, currentWeather.name, currentWeather.sys.country, currentWeather.main.temp);

                    cityList.add(city);

                    mRealmDataSource.addCity(city);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
