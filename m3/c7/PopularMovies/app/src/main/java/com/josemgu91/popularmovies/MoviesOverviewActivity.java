package com.josemgu91.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

public class MoviesOverviewActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private MoviesCursorAdapter moviesCursorAdapter;

    private final static int LOADER_ID_MOVIES = 1;

    private final static String LOADER_ARG_KEY_MOVIE_FILTER = "movie_filter_by";
    private final static int LOADER_MOVIE_FILTER_MOST_POPULAR = 1;
    private final static int LOADER_MOVIE_FILTER_TOP_RATED = 2;
    private final static int LOADER_MOVIE_FILTER_FAVORITE = 3;

    private GridView gridViewMovies;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private static final String SAVED_INSTANCE_STATE_KEY_SELECTED_MENU_ID = "selected_menu_id";
    @IdRes
    private int selectedMenuId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movies_overview);
        gridViewMovies = findViewById(R.id.gridview_movies);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        progressBar = findViewById(R.id.progressbar);

        swipeRefreshLayout.setOnRefreshListener(this);
        gridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Cursor cursor = moviesCursorAdapter.getCursor();
                if (cursor.moveToPosition(position)) {
                    final String remoteId = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.REMOTE_ID));
                    startActivity(
                            new Intent(MoviesOverviewActivity.this, MovieDetailActivity.class)
                                    .putExtra(MovieDetailActivity.PARAM_MOVIE_REMOTE_ID, remoteId)
                    );
                }
            }
        });
        moviesCursorAdapter = new MoviesCursorAdapter(this);
        gridViewMovies.setAdapter(moviesCursorAdapter);

        showLoading();

        final Bundle defaultLoaderArguments = new Bundle();
        defaultLoaderArguments.putInt(LOADER_ARG_KEY_MOVIE_FILTER, LOADER_MOVIE_FILTER_MOST_POPULAR);
        getSupportLoaderManager().initLoader(LOADER_ID_MOVIES, defaultLoaderArguments, this);

        if (savedInstanceState == null) {
            refresh();
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_KEY_SELECTED_MENU_ID, selectedMenuId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedMenuId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_KEY_SELECTED_MENU_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String selection;
        switch (id) {
            case LOADER_ID_MOVIES:
                final int selectionArg = args.getInt(
                        LOADER_ARG_KEY_MOVIE_FILTER,
                        LOADER_MOVIE_FILTER_MOST_POPULAR);
                switch (selectionArg) {
                    default:
                    case LOADER_MOVIE_FILTER_MOST_POPULAR:
                        selection = MovieContract.MovieEntry.IS_MOST_POPULAR;
                        break;
                    case LOADER_MOVIE_FILTER_TOP_RATED:
                        selection = MovieContract.MovieEntry.IS_TOP_RATED;
                        break;
                    case LOADER_MOVIE_FILTER_FAVORITE:
                        selection = MovieContract.MovieEntry.IS_FAVORITE;
                        break;
                }
                return new CursorLoader(
                        this,
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        selection + "=?",
                        new String[]{"1"},
                        null
                );
            default:
                throw new RuntimeException(String.format("Loader with ID %s not Implemented", id));
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesCursorAdapter.swapCursor(data);
        dismissLoading();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_overview, menu);
        if (selectedMenuId != 0) {
            menu.findItem(selectedMenuId).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_order_most_popular:
                item.setChecked(true);
                fetchMostPopularMovies();
                selectedMenuId = R.id.action_order_most_popular;
                return true;
            case R.id.action_order_top_rated:
                item.setChecked(true);
                fetchTopRatedMovies();
                selectedMenuId = R.id.action_order_top_rated;
                return true;
            case R.id.action_favorites:
                item.setChecked(true);
                fetchFavoriteMovies();
                selectedMenuId = R.id.action_favorites;
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            default:
                return false;
        }
    }

    private void refresh() {
        showLoading();
        dispatchSync();
    }

    private void fetchFavoriteMovies() {
        restartLoader(LOADER_MOVIE_FILTER_FAVORITE);
    }

    private void fetchMostPopularMovies() {
        restartLoader(LOADER_MOVIE_FILTER_MOST_POPULAR);
    }

    private void fetchTopRatedMovies() {
        restartLoader(LOADER_MOVIE_FILTER_TOP_RATED);
    }

    private void restartLoader(int filter) {
        final Bundle loaderArguments = new Bundle();
        loaderArguments.putInt(LOADER_ARG_KEY_MOVIE_FILTER, filter);
        getSupportLoaderManager().restartLoader(
                LOADER_ID_MOVIES,
                loaderArguments,
                this
        );
    }

    private void showLoading() {
        gridViewMovies.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissLoading() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        gridViewMovies.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void dispatchSync() {
        startService(new Intent(this, MoviesSyncIntentService.class));
    }
}