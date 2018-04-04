package com.josemgu91.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josemgu91.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private final static int LOADER_ID_MOVIE_DETAIL = 1;
    private final static int LOADER_ID_MOVIE_VIDEOS = 2;
    private final static int LOADER_ID_MOVIE_REVIEWS = 3;

    public final static String PARAM_MOVIE_REMOTE_ID = "com.josemgu91.popularmovies.MOVIE_REMOTE_ID";

    private final static String SAVED_INSTANCE_STATE_KEY_MOVIE_REMOTE_ID = "movie_remote_id";

    private String movieRemoteId;
    private String movieId;

    private ImageView imageViewMoviePoster;
    private TextView textViewMovieTitle;
    private TextView textViewMovieUserRating;
    private TextView textViewMovieReleaseDate;
    private TextView textViewMoviePlotSynopsis;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ViewGroup viewGroupVideos;
    private ViewGroup viewGroupReviews;

    private LayoutInflater layoutInflater;

    private Menu menu;

    private boolean isFavorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        imageViewMoviePoster = findViewById(R.id.imageview_movie_poster);
        textViewMovieTitle = findViewById(R.id.textView_original_title);
        textViewMovieUserRating = findViewById(R.id.textview_user_rating);
        textViewMovieReleaseDate = findViewById(R.id.textview_release_date);
        textViewMoviePlotSynopsis = findViewById(R.id.textview_plot_synopsis);

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        viewGroupVideos = findViewById(R.id.linearlayout_videos);
        viewGroupReviews = findViewById(R.id.linearlayout_reviews);

        swipeRefreshLayout.setOnRefreshListener(this);

        layoutInflater = LayoutInflater.from(this);

        if (savedInstanceState == null) {
            final Intent intentThatStartedThisActivity = getIntent();
            if (intentThatStartedThisActivity.hasExtra(PARAM_MOVIE_REMOTE_ID)) {
                movieRemoteId = intentThatStartedThisActivity.getStringExtra(PARAM_MOVIE_REMOTE_ID);
                getSupportLoaderManager().initLoader(LOADER_ID_MOVIE_DETAIL, null, this);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_INSTANCE_STATE_KEY_MOVIE_REMOTE_ID, movieRemoteId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieRemoteId = savedInstanceState.getString(SAVED_INSTANCE_STATE_KEY_MOVIE_REMOTE_ID);
        getSupportLoaderManager().restartLoader(LOADER_ID_MOVIE_DETAIL, null, this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final boolean result = super.onPrepareOptionsMenu(menu);
        setFavoriteIcon(isFavorite);
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_switch_favorite:
                switchFavorite();
                return true;
            default:
                return false;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String selection;
        final String[] selectionArguments;
        final Uri uri;
        switch (id) {
            case LOADER_ID_MOVIE_DETAIL:
                uri = MovieContract.MovieEntry.CONTENT_URI;
                selection = MovieContract.MovieEntry.REMOTE_ID + "=?";
                selectionArguments = new String[]{movieRemoteId};
                break;
            case LOADER_ID_MOVIE_REVIEWS:
                uri = MovieContract.ReviewEntry.CONTENT_URI;
                selection = MovieContract.ReviewEntry.MOVIE_ID + "=?";
                selectionArguments = new String[]{movieId};
                break;
            case LOADER_ID_MOVIE_VIDEOS:
                uri = MovieContract.VideoEntry.CONTENT_URI;
                selection = MovieContract.VideoEntry.MOVIE_ID + "=?";
                selectionArguments = new String[]{movieId};
                break;
            default:
                throw new RuntimeException(String.format("Loader with ID %s not Implemented", id));
        }
        return new CursorLoader(
                this,
                uri,
                null,
                selection,
                selectionArguments,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_MOVIE_DETAIL:
                if (data.moveToNext()) {
                    movieId = data.getString(data.getColumnIndex(MovieContract.MovieEntry._ID));

                    final MovieDetail movieDetail = new MovieDetail();
                    movieDetail.posterPath = data.getString(data.getColumnIndex(MovieContract.MovieEntry.POSTER_REMOTE_ID));
                    movieDetail.originalTitle = data.getString(data.getColumnIndex(MovieContract.MovieEntry.TITLE));
                    movieDetail.userRating = data.getString(data.getColumnIndex(MovieContract.MovieEntry.USER_RATING));
                    movieDetail.releaseDate = data.getString(data.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE));
                    movieDetail.plotSynopsis = data.getString(data.getColumnIndex(MovieContract.MovieEntry.PLOT_SYNOPSIS));
                    fillMovieDetails(movieDetail);

                    isFavorite = data.getInt(data.getColumnIndex(MovieContract.MovieEntry.IS_FAVORITE)) == 1;
                    invalidateOptionsMenu();

                    getSupportLoaderManager().initLoader(LOADER_ID_MOVIE_VIDEOS, null, this);
                    getSupportLoaderManager().initLoader(LOADER_ID_MOVIE_REVIEWS, null, this);

                    refresh();
                }
                break;
            case LOADER_ID_MOVIE_REVIEWS:
                final List<Review> reviews = new ArrayList<>();
                while (data.moveToNext()) {
                    final Review review = new Review();
                    review.author = data.getString(data.getColumnIndex(MovieContract.ReviewEntry.AUTHOR));
                    review.content = data.getString(data.getColumnIndex(MovieContract.ReviewEntry.CONTENT));
                    reviews.add(review);
                }
                viewGroupReviews.removeAllViews();
                fillReviews(reviews);
                dismissLoading();
                break;
            case LOADER_ID_MOVIE_VIDEOS:
                final List<Video> videos = new ArrayList<>();
                while (data.moveToNext()) {
                    final Video video = new Video();
                    video.title = data.getString(data.getColumnIndex(MovieContract.VideoEntry.TITLE));
                    video.url = Uri.parse(data.getString(data.getColumnIndex(MovieContract.VideoEntry.URL)));
                    videos.add(video);
                }
                viewGroupVideos.removeAllViews();
                fillVideos(videos);
                dismissLoading();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        dispatchSync(movieId);
    }

    private void fillReviews(List<Review> reviews) {
        for (final Review review : reviews) {
            final View view = layoutInflater.inflate(R.layout.element_review, viewGroupReviews, false);
            final TextView textViewTitle = view.findViewById(R.id.textview_author);
            final TextView textViewContent = view.findViewById(R.id.textview_content);
            textViewTitle.setText(review.author);
            textViewContent.setText(review.content);
            viewGroupReviews.addView(view);
        }
    }

    private void fillVideos(List<Video> videos) {
        for (final Video video : videos) {
            final View view = layoutInflater.inflate(R.layout.element_video, viewGroupVideos, false);
            final TextView textViewTitle = view.findViewById(R.id.textview_title);
            textViewTitle.setText(video.title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(createVideoIntent(video.url));
                }
            });
            viewGroupVideos.addView(view);
        }
    }

    private void dispatchSync(final String movieId) {
        startService(new Intent(this, MoviesSyncIntentService.class)
                .putExtra(MoviesSyncIntentService.PARAM_MOVIE_ID, movieId));
    }

    private Intent createVideoIntent(final Uri videoUri) {
        return new Intent(Intent.ACTION_VIEW, videoUri);
    }

    private void fillMovieDetails(final MovieDetail movieDetail) {
        try {
            Picasso.with(this)
                    .load(NetworkUtils.createImageUri(movieDetail.posterPath, NetworkUtils.IMAGE_SIZE_BIG))
                    .into(imageViewMoviePoster);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        textViewMovieTitle.setText(getString(R.string.movie_detail_original_title, movieDetail.originalTitle));
        textViewMovieUserRating.setText(getString(R.string.movie_detail_user_rating, movieDetail.userRating));
        textViewMovieReleaseDate.setText(getString(R.string.movie_detail_release_date, movieDetail.releaseDate));
        textViewMoviePlotSynopsis.setText(getString(R.string.movie_detail_plot_synopsis, movieDetail.plotSynopsis));
    }

    private void setFavoriteIcon(final boolean isFavorite) {
        final MenuItem menuItem = menu.findItem(R.id.action_switch_favorite);
        if (menuItem != null) {
            menuItem.setIcon(isFavorite ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void switchFavorite() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.IS_FAVORITE, isFavorite ? "0" : "1");
        getContentResolver().update(
                ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, Integer.valueOf(movieId)),
                contentValues,
                null,
                null
        );
        setFavoriteIcon(!isFavorite);
    }

    private void dismissLoading() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private static class Review {
        private String author;
        private String content;
    }

    private static class Video {
        private String title;
        private Uri url;
    }

    private static class MovieDetail {
        private String posterPath;
        private String originalTitle;
        private String userRating;
        private String releaseDate;
        private String plotSynopsis;
    }
}
