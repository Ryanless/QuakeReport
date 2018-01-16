package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michail on 15.01.2018.
 */

public class Earthquake {
    private float mMagnitude;
    private String mPlace;
    private long mTimeUnixStamp;
    private Date mDate;

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("DD. MMM, yyyy");
    static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");


    public Earthquake(float magnitude, String place, long timeUnixStamp) {
        this.mMagnitude = magnitude;
        this.mPlace = place;
        this.mTimeUnixStamp = timeUnixStamp;
        this.mDate = new Date(mTimeUnixStamp);
    }

    public float getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    //who knows if I will ever use this method
    public long getTimeUnixStamp() {
        return mTimeUnixStamp;
    }

    public String getDate() {
        return dateFormatter.format(mDate);
    }

    public String getTime() {
        return timeFormatter.format(mDate);
    }


    /**
     * ArrayAdapter to display the Earthquake class in a ListView
     * It's made a inner class instead of a separate class because it's only used
     * together with the outer Earthquake class.
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

            TextView timeTV = rootView.findViewById(R.id.list_item_time_tv);
            timeTV.setText(currentEarthquake.getTime());



            return rootView;
        }
    }


}
