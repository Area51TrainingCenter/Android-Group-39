package com.josemgu91.popularmovies.network;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServerResponseParser {

    private final static String KEY_RESULTS = "results";

    private final static String KEY_MOVIE_ID = "id";
    private final static String KEY_MOVIE_POSTER_PATH = "poster_path";
    private final static String KEY_MOVIE_ORIGINAL_TITLE = "original_title";
    private final static String KEY_MOVIE_PLOT_SYNOPSIS = "overview";
    private final static String KEY_MOVIE_USER_RATING = "vote_average";
    private final static String KEY_MOVIE_POPULARITY = "popularity";
    private final static String KEY_MOVIE_RELEASE_DATE = "release_date";

    private final static String KEY_REVIEW_ID = "id";
    private final static String KEY_REVIEW_AUTHOR = "author";
    private final static String KEY_REVIEW_CONTENT = "content";

    private final static String KEY_VIDEO_ID = "id";
    private final static String KEY_VIDEO_NAME = "name";
    private final static String KEY_VIDEO_VIDEO_ID = "key";

    public static Movie parseMovie(final String jsonString) throws JSONException {
        return new MovieParser().parse(new JSONObject(jsonString));
    }

    public static List<Movie> parseMovies(final String jsonString) throws JSONException {
        return parseBulkResult(jsonString, new MovieParser());
    }

    public static List<Review> parseReviews(final String jsonString) throws JSONException {
        return parseBulkResult(jsonString, new ReviewParser());
    }

    public static List<Video> parseVideos(final String jsonString) throws JSONException {
        return parseBulkResult(jsonString, new VideoParser());
    }

    private static <E> List<E> parseBulkResult(final String jsonString, final Parser<E> parser) throws JSONException {
        final ArrayList<E> results = new ArrayList<>();
        final JSONObject jsonResponse = new JSONObject(jsonString);
        final JSONArray jsonArray = jsonResponse.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject jsonObject = jsonArray.getJSONObject(i);
            final E result = parser.parse(jsonObject);
            results.add(result);
        }
        return results;
    }

    private interface Parser<E> {

        public E parse(final JSONObject jsonObject) throws JSONException;

    }

    private static class MovieParser implements Parser<Movie> {

        @Override
        public Movie parse(JSONObject jsonObject) throws JSONException {
            final String id = jsonObject.getString(KEY_MOVIE_ID);
            final String posterPath = jsonObject.getString(KEY_MOVIE_POSTER_PATH);
            final String originalTitle = jsonObject.getString(KEY_MOVIE_ORIGINAL_TITLE);
            final String plotSynopsis = jsonObject.getString(KEY_MOVIE_PLOT_SYNOPSIS);
            final String releaseDate = jsonObject.getString(KEY_MOVIE_RELEASE_DATE);
            final float popularity = (float) jsonObject.getDouble(KEY_MOVIE_POPULARITY);
            final float userRating = (float) jsonObject.getDouble(KEY_MOVIE_USER_RATING);
            return new Movie(id, posterPath, originalTitle, plotSynopsis, releaseDate, userRating, popularity);
        }
    }

    private static class ReviewParser implements Parser<Review> {

        @Override
        public Review parse(JSONObject jsonObject) throws JSONException {
            final String id = jsonObject.getString(KEY_REVIEW_ID);
            final String author = jsonObject.getString(KEY_REVIEW_AUTHOR);
            final String content = jsonObject.getString(KEY_REVIEW_CONTENT);
            return new Review(id, author, content);
        }
    }

    private static class VideoParser implements Parser<Video> {

        @Override
        public Video parse(JSONObject jsonObject) throws JSONException {
            final String id = jsonObject.getString(KEY_VIDEO_ID);
            final String name = jsonObject.getString(KEY_VIDEO_NAME);
            final String videoKey = jsonObject.getString(KEY_VIDEO_VIDEO_ID);
            return new Video(
                    id, name,
                    Uri.parse("https://www.youtube.com/watch?v=" + videoKey)
            );
        }
    }

}
