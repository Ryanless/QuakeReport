package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michail on 15.01.2018.
 */

public class Earthquake {
    private float magnitude;
    private String place;
    private int timeUnixStamp;


    public Earthquake(float magnitude, String place, int timeUnixStamp) {
        this.magnitude = magnitude;
        this.place = place;
        this.timeUnixStamp = timeUnixStamp;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    //who knows if I will ever use this method
    public int getTimeUnixStamp() {
        return timeUnixStamp;
    }

    public String getDate() {
        return String.valueOf(timeUnixStamp);
    }


    /**
     * ArrayAdapter to display the Earthquake class in a ListView
     */
    public static class EarthquakeArrayAdapter extends ArrayAdapter<Earthquake> {


        public EarthquakeArrayAdapter(@NonNull Context context,  @NonNull ArrayList<Earthquake> earthquakes) {
            super(context, 0, earthquakes);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View rootView = convertView;

            if (rootView == null){
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_earthquake_layout, parent, false);
            }

            Earthquake currentEarthquake = getItem(position);

            TextView magnitudeTV = rootView.findViewById(R.id.list_item_magnitude_tv);
            magnitudeTV.setText( String.valueOf(currentEarthquake.getMagnitude()));

            TextView placeTV = rootView.findViewById(R.id.list_item_place_tv);
            placeTV.setText( currentEarthquake.getPlace());

            TextView dateTV = rootView.findViewById(R.id.list_item_date_tv);
            dateTV.setText(currentEarthquake.getDate());



            return rootView;
        }
    }


}
