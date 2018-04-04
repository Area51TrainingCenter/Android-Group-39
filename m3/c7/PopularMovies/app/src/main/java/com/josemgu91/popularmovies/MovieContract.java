package com.josemgu91.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.josemgu91.popularmovies.ContentProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_VIDEO = "video";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movies";

        public static final String TITLE = "title";
        public static final String PLOT_SYNOPSIS = "plotSynopsis";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String POSTER_REMOTE_ID = "posterRemoteId";
        public static final String USER_RATING = "userRating";
        public static final String POPULARITY = "popularity";
        public static final String IS_FAVORITE = "isFavorite";
        public static final String IS_MOST_POPULAR = "isMostPopular";
        public static final String IS_TOP_RATED = "isTopRated";
        public static final String REMOTE_ID = "remoteId";

    }

    public static class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEW)
                .build();

        public static final String TABLE_NAME = "reviews";

        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String MOVIE_ID = "movieId";

    }

    public static class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEO)
                .build();

        public static final String TABLE_NAME = "videos";

        public static final String TITLE = "title";
        public static final String URL = "url";
        public static final String MOVIE_ID = "movieId";

    }

}
