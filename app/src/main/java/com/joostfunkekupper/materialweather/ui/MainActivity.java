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
import com.joostfunkekupper.materialweather.adapter.CardViewRecyclerAdapter;
import com.joostfunkekupper.materialweather.data.CurrentWeather;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    private WeatherApplication app;

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (WeatherApplication) getApplication();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardViewRecyclerAdapter(Arrays.asList("Current Location", "Sydney", "Amsterdam"));
        mRecyclerView.setAdapter(mAdapter);

        String[] params = {"London,UK"};
        new FetchCityWeatherTask().execute(params);
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
                return app.getService().fetchWeatherByCityName("London,UK", "metric");
            }
            catch (RetrofitError error) {
                return null;
            }
            catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CurrentWeather currentWeather) {
            if (currentWeather != null)
                Log.e("MainActivity", "Weather in " + currentWeather.name + " is " + currentWeather.main.temp + " degrees");
        }
    }
}
