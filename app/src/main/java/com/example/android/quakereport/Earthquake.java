package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
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
    private String mPageUrl;

    static SimpleDateFormat sDateFormatter = new SimpleDateFormat("DD. MMM, yyyy");
    static SimpleDateFormat sTimeFormatter = new SimpleDateFormat("HH:mm");
    static DecimalFormat sMagnitudeFormatter = new DecimalFormat("0.0");


    public Earthquake(float magnitude, String place, long timeUnixStamp, String pageUrl) {
        this.mMagnitude = magnitude;
        this.mPlace = place;
        this.mTimeUnixStamp = timeUnixStamp;
        this.mDate = new Date(mTimeUnixStamp);
        this.mPageUrl = pageUrl;
    }

    public float getMagnitude() {
            return mMagnitude;
    }

    public String getMagnitudeFormatted() {
        return sMagnitudeFormatter.format(mMagnitude);
    }

    public String getPlace() {
        return mPlace;
    }


    public String getDate() {
        return sDateFormatter.format(mDate);
    }

    public String getTime() {
        return sTimeFormatter.format(mDate);
    }

    public String getPageUrl() {
        return mPageUrl;
    }

    @Override
    public String toString() {
        String message = "Earthquake: ";
        message += "mag: " + mMagnitude;
        message += " place: " + mPlace;
        message += " date: " + getDate();

        return message;
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

            final Earthquake currentEarthquake = getItem(position);

            TextView magnitudeTV = rootView.findViewById(R.id.list_item_magnitude_tv);
            magnitudeTV.setText( currentEarthquake.getMagnitudeFormatted());

            // Set the proper background color on the magnitude circle.
            // Fetch the background from the TextView, which is a GradientDrawable.
            GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTV.getBackground();

            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);


            //preparation for setting the 2 places Strings
            String[] placeArray = buildPlaceStrings(currentEarthquake.getPlace());
            //then setting both place strings
            TextView placeModTV = rootView.findViewById(R.id.list_item_place_modifier_tv);
            placeModTV.setText(placeArray[0]);
            TextView placeTV = rootView.findViewById(R.id.list_item_place_tv);
            placeTV.setText(placeArray[1]);

            //setting both date & time strings
            TextView dateTV = rootView.findViewById(R.id.list_item_date_tv);
            dateTV.setText(currentEarthquake.getDate());
            TextView timeTV = rootView.findViewById(R.id.list_item_time_tv);
            timeTV.setText(currentEarthquake.getTime());

            return rootView;
        }


        /**
         * sets the mPlaceArray this way ["82km N of", "Tokyo, Japan" ]
         *
         * @return
         */
        String[] buildPlaceStrings(String placeString){
            String [] placeArray = new String[2];

            int index = placeString.indexOf(" of ");

            if (index == -1){
                placeArray[0] = getContext().getString( R.string.near_the);
                placeArray[1] = placeString;
            }
            else {
                placeArray[0] = placeString.substring(0, index + 3);
                placeArray[1] = placeString.substring(index + 4, placeString.length());
            }

            return  placeArray;
        }

        private int getMagnitudeColor(float magnitude) {
            int color;
            int magnitudeFloor = (int) Math.floor(magnitude);
            switch (magnitudeFloor){
                case 0:
                case 1:
                    color = R.color.magnitude1;
                    break;
                case 2:
                    color = R.color.magnitude2;
                    break;
                case 3:
                    color = R.color.magnitude3;
                    break;
                case 4:
                    color = R.color.magnitude4;
                    break;
                case 5:
                    color = R.color.magnitude5;
                    break;
                case 6:
                    color = R.color.magnitude6;
                    break;
                case 7:
                    color = R.color.magnitude7;
                    break;
                case 8:
                    color = R.color.magnitude8;
                    break;
                case 9:
                    color = R.color.magnitude9;
                    break;
                case 10:
                    color = R.color.magnitude10plus;
                    break;
                default:
                    color = R.color.magnitude10plus;
                    Log.e("EarthQuakeAdapter", "Invalid magnitude to color: " +magnitudeFloor);

            }

            return ContextCompat.getColor(getContext(), color);
        }
    }




}
