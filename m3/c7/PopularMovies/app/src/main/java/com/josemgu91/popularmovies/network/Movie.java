package com.josemgu91.popularmovies.network;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Nonnull;

public class Movie implements Parcelable {

    private final String id;
    private final String posterPath; //Poster ID.
    private final String originalTitle;
    private final String plotSynopsis;
    private final String releaseDate;
    private final float userRating;
    private final float popularity;

    public Movie(@Nonnull String id,
                 @Nonnull String posterPath,
                 @Nonnull String originalTitle,
                 @Nonnull String plotSynopsis,
                 @Nonnull String releaseDate,
                 float userRating,
                 float popularity) {
        this.id = id;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.popularity = popularity;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        posterPath = in.readString();
        originalTitle = in.readString();
        plotSynopsis = in.readString();
        releaseDate = in.readString();
        userRating = in.readFloat();
        popularity = in.readFloat();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getUserRating() {
        return userRating;
    }

    public float getPopularity() {
        return popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (Float.compare(movie.userRating, userRating) != 0) return false;
        if (Float.compare(movie.popularity, popularity) != 0) return false;
        if (!id.equals(movie.id)) return false;
        if (!posterPath.equals(movie.posterPath)) return false;
        if (!originalTitle.equals(movie.originalTitle)) return false;
        if (!plotSynopsis.equals(movie.plotSynopsis)) return false;
        return releaseDate.equals(movie.releaseDate);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + posterPath.hashCode();
        result = 31 * result + originalTitle.hashCode();
        result = 31 * result + plotSynopsis.hashCode();
        result = 31 * result + releaseDate.hashCode();
        result = 31 * result + (userRating != +0.0f ? Float.floatToIntBits(userRating) : 0);
        result = 31 * result + (popularity != +0.0f ? Float.floatToIntBits(popularity) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", userRating=" + userRating +
                ", popularity=" + popularity +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(posterPath);
        parcel.writeString(originalTitle);
        parcel.writeString(plotSynopsis);
        parcel.writeString(releaseDate);
        parcel.writeFloat(userRating);
        parcel.writeFloat(popularity);
    }
}