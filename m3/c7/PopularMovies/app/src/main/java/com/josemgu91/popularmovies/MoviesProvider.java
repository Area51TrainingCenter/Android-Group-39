package com.josemgu91.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesProvider extends ContentProvider {

    private DatabaseManager databaseManager;

    private final static UriMatcher URI_MATCHER;

    private final static int URI_MOVIE = 100;
    private final static int URI_MOVIE_WITH_ID = 101;
    private final static int URI_REVIEW = 200;
    private final static int URI_REVIEW_WITH_MOVIE_ID = 201;
    private final static int URI_VIDEO = 300;
    private final static int URI_VIDEO_WITH_MOVIE_ID = 301;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(
                MovieContract.CONTENT_AUTHORITY,
                MovieContract.MovieEntry.CONTENT_URI.getPath(),
                URI_MOVIE
        );
        URI_MATCHER.addURI(
                MovieContract.CONTENT_AUTHORITY,
                MovieContract.MovieEntry.CONTENT_URI.getPath() + "/#",
                URI_MOVIE_WITH_ID
        );
        URI_MATCHER.addURI(
                MovieContract.CONTENT_AUTHORITY,
                MovieContract.ReviewEntry.CONTENT_URI.getPath(),
                URI_REVIEW
        );
        URI_MATCHER.addURI(
                MovieContract.CONTENT_AUTHORITY,
                MovieContract.ReviewEntry.CONTENT_URI.getPath() + "/#",
                URI_REVIEW_WITH_MOVIE_ID
        );
        URI_MATCHER.addURI(
                MovieContract.CONTENT_AUTHORITY,
                MovieContract.VideoEntry.CONTENT_URI.getPath(),
                URI_VIDEO
        );
        URI_MATCHER.addURI(
                MovieContract.CONTENT_AUTHORITY,
                MovieContract.VideoEntry.CONTENT_URI.getPath() + "/#",
                URI_VIDEO_WITH_MOVIE_ID
        );
    }

    @Override
    public boolean onCreate() {
        databaseManager = new DatabaseManager(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int matchCode = URI_MATCHER.match(uri);
        final String tableName;
        switch (matchCode) {
            case URI_MOVIE_WITH_ID:
                selection = MovieContract.MovieEntry._ID + "=" + uri.getLastPathSegment();
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                break;
            case URI_REVIEW_WITH_MOVIE_ID:
                selection = MovieContract.ReviewEntry.MOVIE_ID + "=" + uri.getLastPathSegment();
                tableName = MovieContract.ReviewEntry.TABLE_NAME;
                break;
            case URI_VIDEO_WITH_MOVIE_ID:
                selection = MovieContract.VideoEntry.MOVIE_ID + "=" + uri.getLastPathSegment();
                tableName = MovieContract.VideoEntry.TABLE_NAME;
                break;
            case URI_MOVIE:
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                break;
            case URI_REVIEW:
                tableName = MovieContract.ReviewEntry.TABLE_NAME;
                break;
            case URI_VIDEO:
                tableName = MovieContract.VideoEntry.TABLE_NAME;
                break;
            case UriMatcher.NO_MATCH:
                throw new IllegalArgumentException("Unknown URI: " + uri);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        final Cursor cursor = databaseManager.getReadableDatabase()
                .query(tableName,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("This operation isn't supported!");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int matchCode = URI_MATCHER.match(uri);
        long id;
        switch (matchCode) {
            case URI_MOVIE:
                id = databaseManager.getWritableDatabase()
                        .insert(MovieContract.MovieEntry.TABLE_NAME,
                                null,
                                values);
                break;
            case URI_REVIEW:
                id = databaseManager.getWritableDatabase()
                        .insert(MovieContract.ReviewEntry.TABLE_NAME,
                                null,
                                values);
                break;
            case URI_VIDEO:
                id = databaseManager.getWritableDatabase()
                        .insert(MovieContract.VideoEntry.TABLE_NAME,
                                null,
                                values);
                break;
            case UriMatcher.NO_MATCH:
                throw new IllegalArgumentException("Unknown URI: " + uri);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (id > 0) {
            final Uri returnUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        } else {
            throw new IllegalArgumentException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int matchCode = URI_MATCHER.match(uri);
        int rowsDeleted = 0;
        int reviewRowsDeleted = 0;
        int videoRowsDeleted = 0;
        int movieRowsDeleted = 0;
        switch (matchCode) {
            case URI_MOVIE_WITH_ID: {
                final String movieId = uri.getLastPathSegment();
                reviewRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.ReviewEntry.TABLE_NAME,
                                MovieContract.ReviewEntry.MOVIE_ID + "=?",
                                new String[]{movieId});
                videoRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.VideoEntry.TABLE_NAME,
                                MovieContract.VideoEntry.MOVIE_ID + "=?",
                                new String[]{movieId});
                movieRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.MovieEntry.TABLE_NAME,
                                MovieContract.MovieEntry._ID,
                                new String[]{movieId});
                break;
            }
            case URI_MOVIE:
                reviewRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.ReviewEntry.TABLE_NAME,
                                null,
                                null);
                videoRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.VideoEntry.TABLE_NAME,
                                null,
                                null);
                movieRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.MovieEntry.TABLE_NAME,
                                null,
                                null);
                break;
            case URI_VIDEO_WITH_MOVIE_ID: {
                final String movieId = uri.getLastPathSegment();
                videoRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.VideoEntry.TABLE_NAME,
                                MovieContract.VideoEntry.MOVIE_ID + "=?",
                                new String[]{movieId});
                break;
            }
            case URI_REVIEW_WITH_MOVIE_ID: {
                final String movieId = uri.getLastPathSegment();
                videoRowsDeleted = databaseManager.getWritableDatabase()
                        .delete(MovieContract.ReviewEntry.TABLE_NAME,
                                MovieContract.ReviewEntry.MOVIE_ID + "=?",
                                new String[]{movieId});
                break;
            }
            case UriMatcher.NO_MATCH:
                throw new IllegalArgumentException("Unknown URI: " + uri);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (reviewRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(MovieContract.ReviewEntry.CONTENT_URI, null);
            rowsDeleted += reviewRowsDeleted;
        }
        if (videoRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(MovieContract.VideoEntry.CONTENT_URI, null);
            rowsDeleted += videoRowsDeleted;
        }
        if (movieRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(MovieContract.MovieEntry.CONTENT_URI, null);
            rowsDeleted += movieRowsDeleted;
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int matchCode = URI_MATCHER.match(uri);
        final String tableName;
        switch (matchCode) {
            case URI_MOVIE_WITH_ID:
                selection = MovieContract.MovieEntry._ID + "=" + uri.getLastPathSegment();
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                break;
            case URI_REVIEW_WITH_MOVIE_ID:
                selection = MovieContract.ReviewEntry.MOVIE_ID + "=" + uri.getLastPathSegment();
                tableName = MovieContract.ReviewEntry.TABLE_NAME;
                break;
            case URI_VIDEO_WITH_MOVIE_ID:
                selection = MovieContract.VideoEntry.MOVIE_ID + "=" + uri.getLastPathSegment();
                tableName = MovieContract.VideoEntry.TABLE_NAME;
                break;
            case UriMatcher.NO_MATCH:
                throw new IllegalArgumentException("Unknown URI: " + uri);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        final int rowsUpdated = databaseManager.getWritableDatabase()
                .update(tableName,
                        values,
                        selection,
                        selectionArgs);
        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}
