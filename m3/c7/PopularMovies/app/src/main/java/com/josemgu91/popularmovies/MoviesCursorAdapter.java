package com.josemgu91.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.josemgu91.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;

public class MoviesCursorAdapter extends CursorAdapter {

    private final LayoutInflater layoutInflater;
    private final Picasso picasso;

    private static class MovieViewHolder {

        ImageView imageViewMoviePoster;

    }

    public MoviesCursorAdapter(final Context context) {
        super(context, null, 0);
        layoutInflater = LayoutInflater.from(context);
        picasso = Picasso.with(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.element_movie, parent, false);
        final MovieViewHolder movieViewHolder = new MovieViewHolder();
        movieViewHolder.imageViewMoviePoster = view.findViewById(R.id.imageview_movie_poster);
        view.setTag(movieViewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final MovieViewHolder movieViewHolder = (MovieViewHolder) view.getTag();
        final int posterRemoteIdColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.POSTER_REMOTE_ID);
        final String posterPath = cursor.getString(posterRemoteIdColumnIndex);
        try {
            picasso
                    .load(NetworkUtils.createImageUri(posterPath, NetworkUtils.IMAGE_SIZE_SMALL))
                    .into(movieViewHolder.imageViewMoviePoster);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
