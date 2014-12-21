package com.joostfunkekupper.materialweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joostfunkekupper.materialweather.R;
import com.joostfunkekupper.materialweather.realm.City;

import java.util.List;

public class CityCardViewRecyclerAdapter extends RecyclerView.Adapter<CityCardViewRecyclerAdapter.ViewHolder> {

    private List<City> mDataSet;

    public CityCardViewRecyclerAdapter(List<City> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.city_weather_card_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.location.setText(mDataSet.get(position).getName());
        holder.temperature.setText(String.format("%s°",
                    mDataSet.get(position).getTemperature()));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView temperature;
        public TextView location;
        public ViewHolder(View cardView) {
            super(cardView);

            location = (TextView) cardView.findViewById(R.id.location);
            temperature = (TextView) cardView.findViewById(R.id.temperature);
        }
    }
}
