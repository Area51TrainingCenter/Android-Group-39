package com.josemgu91.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.josemgu91.popularmovies.network.NetworkUtils;
import com.josemgu91.popularmovies.network.Review;
import com.josemgu91.popularmovies.network.ServerResponseParser;
import com.josemgu91.popularmovies.network.Video;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MovieDetailsSyncTask {

    synchronized public static void sync(final String movieId, final Context context) throws IOException, JSONException {

        final ContentResolver contentResolver = context.getContentResolver();

        final Cursor movieCursor = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.REMOTE_ID},
                MovieContract.MovieEntry._ID + "=?",
                new String[]{movieId},
                null);

        final String remoteId;
        if (movieCursor != null && movieCursor.moveToNext()) {
            remoteId = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.REMOTE_ID));
        } else {
            return;
        }

        final String videosJsonString = NetworkUtils.getVideos(remoteId);
        final List<Video> videos = ServerResponseParser.parseVideos(videosJsonString);
        final String reviewsJsonString = NetworkUtils.getReviews(remoteId);
        final List<Review> reviews = ServerResponseParser.parseReviews(reviewsJsonString);

        contentResolver.delete(
                ContentUris.withAppendedId(MovieContract.ReviewEntry.CONTENT_URI, Integer.valueOf(movieId)),
                null,
                null
        );

        if (reviews != null) {
            for (final Review review : reviews) {
                final ContentValues reviewContentValues = new ContentValues();
                reviewContentValues.put(MovieContract.ReviewEntry.AUTHOR, review.getAuthor());
                reviewContentValues.put(MovieContract.ReviewEntry.CONTENT, review.getContent());
                reviewContentValues.put(MovieContract.ReviewEntry.MOVIE_ID, movieId);
                contentResolver.insert(MovieContract.ReviewEntry.CONTENT_URI,
                        reviewContentValues);
            }
        }

        contentResolver.delete(
                ContentUris.withAppendedId(MovieContract.VideoEntry.CONTENT_URI, Integer.valueOf(movieId)),
                null,
                null
        );

        if (videos != null) {
            for (final Video video : videos) {
                final ContentValues videoContentValues = new ContentValues();
                videoContentValues.put(MovieContract.VideoEntry.MOVIE_ID, movieId);
                videoContentValues.put(MovieContract.VideoEntry.TITLE, video.getName());
                videoContentValues.put(MovieContract.VideoEntry.URL, video.getUrl().toString());
                contentResolver.insert(MovieContract.VideoEntry.CONTENT_URI,
                        videoContentValues);
            }
        }
    }

}
