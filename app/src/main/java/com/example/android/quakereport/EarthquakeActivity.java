/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    final String QUERY_URL_STRING = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    ListView mEarthquakeListView;
    Earthquake.EarthquakeArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        mEarthquakeListView = findViewById(R.id.list);

        //Fetch the data from the URL and display it
        QuakeAsyncTask task = new QuakeAsyncTask();
        task.execute(QUERY_URL_STRING);

    }

    public void updateUI(final ArrayList<Earthquake> quakeArray) {
        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new Earthquake.EarthquakeArrayAdapter(
                this, quakeArray);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEarthquakeListView.setAdapter(mAdapter);

        mEarthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openWebPage(quakeArray.get(position).getPageUrl());
            }
        });
    }


    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private class QuakeAsyncTask extends AsyncTask<String,Void,ArrayList<Earthquake>> {

        @Override
        protected ArrayList<Earthquake> doInBackground(String... strings) {
            if (strings == null || strings.length == 0 || TextUtils.isEmpty(strings[0])){
                Log.e(LOG_TAG, "AsyncTask got an invalid input: " +strings.toString());
                return null;
            }

            return QueryUtils.fetchEarthquakeData(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {

            if (earthquakes == null || earthquakes.size() == 0){
                Log.e(LOG_TAG, "asyncTask returned empty array");
                return;
            }

            Log.d(LOG_TAG, "the http request returned: " + earthquakes.size() + " earthquakes");
            updateUI(earthquakes);

        }
    }

}
