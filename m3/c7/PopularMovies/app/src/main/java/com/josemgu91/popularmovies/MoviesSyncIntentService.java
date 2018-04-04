package com.josemgu91.popularmovies;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;

public class MoviesSyncIntentService extends IntentService {

    public static final String PARAM_MOVIE_ID = "com.josemgu91.popularmovies.MOVIE_ID";

    public MoviesSyncIntentService() {
        super("MoviesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && isNetworkAvailable()) {
            try {
                if (intent.hasExtra(PARAM_MOVIE_ID)) {
                    MovieDetailsSyncTask.sync(intent.getStringExtra(PARAM_MOVIE_ID), this);
                } else {
                    MoviesSyncTask.sync(this);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
