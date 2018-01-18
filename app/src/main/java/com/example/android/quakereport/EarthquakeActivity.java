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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    final String QUERY_URL_STRING = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        QuakeAsyncTask task = new QuakeAsyncTask();
        task.execute(QUERY_URL_STRING);



        // Create a fake list of earthquakes.
        final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(QueryUtils.SAMPLE_JSON_RESPONSE);

        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        Earthquake.EarthquakeArrayAdapter adapter = new Earthquake.EarthquakeArrayAdapter(
                this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openWebPage(earthquakes.get(position).getPageUrl());
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
            URL requestUrl = QueryUtils.createUrl(strings[0]);

            Log.d(LOG_TAG, "backgroundTask finished");
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {

            //TODO: make it update UI
            //logs the result in the log
            if (earthquakes == null || earthquakes.size() == 0){
                Log.d(LOG_TAG, "asyncTask returned empty array");
            }
            else {
                for (int i = 0; i < earthquakes.size(); i++){
                    Log.d(LOG_TAG, earthquakes.get(i).toString());
                }
            }

        }
    }

}
