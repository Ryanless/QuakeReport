package com.example.android.quakereport;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static String LOG_QUERY_TAG = "QueryUtils";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    public static ArrayList<Earthquake> fetchEarthquakeData (String url){
        URL requestUrl = createUrl(url);
        String response;
        ArrayList<Earthquake> quakesArray = null;
        try {
            response = QueryUtils.makeHTTPRequest(requestUrl);
            quakesArray = QueryUtils.extractEarthquakes(response);
        }
        catch (IOException e) {
            Log.e(LOG_QUERY_TAG, "IOException thrown: ");
            e.printStackTrace();
        }

        return quakesArray;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Earthquake> extractEarthquakes(String response) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //2 steps: Get the complete JSON object and then get the "features" JSON array from the object
            JSONArray earthquakeJSONArray = new JSONObject(response).getJSONArray("features");
            for (int i = 0; i < earthquakeJSONArray.length(); i++){

                //2 steps: Get the complete feature/earthquake JSONobject and then get the "properties" JSON object
                JSONObject earthquakeJSON = earthquakeJSONArray.getJSONObject(i).getJSONObject("properties");
                float magnitude = (float) earthquakeJSON.getDouble("mag");
                String place = earthquakeJSON.getString("place");
                long unixTime = earthquakeJSON.getLong("time");
                String pageUrl = earthquakeJSON.getString("url");
                earthquakes.add(new Earthquake(magnitude,place,unixTime, pageUrl));

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Creates and url from a String
     * @param urlString
     * @return
     */
    private static URL createUrl(String urlString){
        URL url = null;
        try {
            url = new URL(urlString);

        } catch (MalformedURLException e) {
            Log.e(LOG_QUERY_TAG, "Error with crating url: " + e);
        }
        return url;
    }

    private static String makeHTTPRequest (URL url) throws IOException{
        //check if input is valid
        if (url == null) {
            Log.e(LOG_QUERY_TAG, "Invalid url given for the HTTP request");
            return null;
        }
        String JSONResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromInputStream(inputStream);
            }
            else {
                Log.e(LOG_QUERY_TAG, "Http Request failed with the code: " + urlConnection.getResponseCode());
            }

        }catch (IOException e) {
            Log.e(LOG_QUERY_TAG, "IOException thrown: ");
            e.printStackTrace();
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return JSONResponse;
    }

    private static String readFromInputStream (InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return  output.toString();
    }

}