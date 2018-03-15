package pe.area51.listandcontent;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private final long id;
    private final String title;
    private final String content;
    private final long creationTimestamp;
    private final long modificationTimestamp;

    public Note(long id, String title, String content, long creationTimestamp, long modificationTimestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    protected Note(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        creationTimestamp = in.readLong();
        modificationTimestamp = in.readLong();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public long getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(creationTimestamp);
        dest.writeLong(modificationTimestamp);
    }
}