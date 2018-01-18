package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Michail on 18.01.2018.
 */

public class QuakeAsyncLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    String mUrl;

    public QuakeAsyncLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        if (TextUtils.isEmpty(mUrl)){
            Log.e("QuakeAsyncLoader", "AsyncTask got an invalid input" );
            return null;
        }

        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}
