package pe.area51.listandcontent;

import android.os.Parcel;
import android.os.Parcelable;


public class Note implements Parcelable {

    private long id;
    private String title;
    private String content;
    private long creationTimestamp;

    public Note(final Parcel parcel) {
        id = parcel.readLong();
        title = parcel.readString();
        content = parcel.readString();
        creationTimestamp = parcel.readLong();
    }

    public Note() {
        this.creationTimestamp = -1;
        this.id = -1;
        this.title = "";
        this.content = "";
    }

    public long getId() {
        return id;
    }

    public Note setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Note setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Note setContent(String content) {
        this.content = content;
        return this;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Note setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeLong(creationTimestamp);
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int i) {
            return new Note[i];
        }
    };
}
