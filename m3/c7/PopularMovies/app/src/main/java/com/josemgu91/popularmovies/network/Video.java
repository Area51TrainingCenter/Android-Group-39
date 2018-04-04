package com.josemgu91.popularmovies.network;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Nonnull;

public class Video implements Parcelable {

    private final String id;
    private final String name;
    private final Uri url;

    public Video(@Nonnull String id,
                 @Nonnull String name,
                 @Nonnull Uri url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    protected Video(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video trailer = (Video) o;

        if (!id.equals(trailer.id)) return false;
        if (!name.equals(trailer.name)) return false;
        return url.equals(trailer.url);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url=" + url +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeParcelable(url, i);
    }
}
