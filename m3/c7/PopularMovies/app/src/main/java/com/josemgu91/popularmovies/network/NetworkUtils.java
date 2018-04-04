package com.josemgu91.popularmovies.network;

import android.net.Uri;
import android.support.annotation.IntDef;
import android.util.Log;

import com.josemgu91.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();

    private final static String TMDB_BASE_URL = "http://api.themoviedb.org/3";
    private final static String TMDB_IMAGE_URL = "http://image.tmdb.org/t/p/";

    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_LANGUAGE = "language";

    private final static String PATH_POPULAR_MOVIES = "movie/popular";
    private final static String PATH_TOP_RATED_MOVIES = "movie/top_rated";
    private final static String PATH_VIDEOS = "movie/%s/videos";
    private final static String PATH_REVIEWS = "movie/%s/reviews";
    private final static String PATH_MOVIE_DETAILS = "movie/%s";

    private final static String PATH_THUMBNAIL_IMAGE_SIZE = "w342";
    private final static String PATH_BIG_IMAGE_SIZE = "w780";

    private final static String DEFAULT_LANGUAGE = "en-US";

    public final static int IMAGE_SIZE_SMALL = 0;
    public final static int IMAGE_SIZE_BIG = 1;

    @IntDef(flag = false,
            value = {
                    IMAGE_SIZE_SMALL,
                    IMAGE_SIZE_BIG,
            })
    public @interface ImageSize {
    }

    private static Uri.Builder createBaseUri() throws MalformedURLException {
        return Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIES_DB_API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, DEFAULT_LANGUAGE);
    }

    private static Uri.Builder createBaseImageUri() throws MalformedURLException {
        return Uri.parse(TMDB_IMAGE_URL).buildUpon();
    }

    private static String getResponseFromServer(final Uri uri) throws IOException {
        Log.d(TAG, "Requesting: " + uri.toString());
        final HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        try {
            final int responseCode = httpURLConnection.getResponseCode();
            final InputStreamReader inputStreamReader = new InputStreamReader(
                    responseCode < 400 ?
                            httpURLConnection.getInputStream() :
                            httpURLConnection.getErrorStream()
            );
            final StringBuilder stringBuilder = new StringBuilder();
            final int bufferSize = 2048;
            final char[] buffer = new char[bufferSize];
            int n = 0;
            while ((n = inputStreamReader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, n);
            }
            return stringBuilder.toString();
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static String getPopularMovies() throws IOException {
        final Uri uri = createBaseUri()
                .appendEncodedPath(PATH_POPULAR_MOVIES)
                .build();
        return getResponseFromServer(uri);
    }

    public static String getTopRatedMovies() throws IOException {
        final Uri uri = createBaseUri()
                .appendEncodedPath(PATH_TOP_RATED_MOVIES)
                .build();
        return getResponseFromServer(uri);
    }

    public static String getVideos(final String movieId) throws IOException {
        final Uri uri = createBaseUri()
                .appendEncodedPath(String.format(PATH_VIDEOS, movieId))
                .build();
        return getResponseFromServer(uri);
    }

    public static String getReviews(final String movieId) throws IOException {
        final Uri uri = createBaseUri()
                .appendEncodedPath(String.format(PATH_REVIEWS, movieId))
                .build();
        return getResponseFromServer(uri);
    }

    public static String getDetails(final String movieId) throws IOException {
        final Uri uri = createBaseUri()
                .appendEncodedPath(String.format(PATH_MOVIE_DETAILS, movieId))
                .build();
        return getResponseFromServer(uri);
    }

    public static Uri createImageUri(final String posterPath, @ImageSize final int size) throws MalformedURLException {
        final String imageSizePath;
        if (size == IMAGE_SIZE_SMALL) {
            imageSizePath = PATH_THUMBNAIL_IMAGE_SIZE;
        } else {
            imageSizePath = PATH_BIG_IMAGE_SIZE;
        }
        return createBaseImageUri()
                .appendEncodedPath(imageSizePath)
                .appendEncodedPath(posterPath)
                .build();
    }

}