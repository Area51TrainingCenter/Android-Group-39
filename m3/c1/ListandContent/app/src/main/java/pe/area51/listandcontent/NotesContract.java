package pe.area51.listandcontent;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alumno on 1/13/18.
 */

public class NotesContract {

    public static final Uri CONTENT_URI =
            Uri.parse("content://pe.area51.listandcontent.NotesProvider");


    public static class NoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                NotesContract.CONTENT_URI
                        .buildUpon()
                        .appendPath("note")
                        .build();

        public static final String TABLE_NAME = "notes";

        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String CREATION_TIMESTAMP = "creationTimestamp";
        public static final String MODIFICATION_TIMESTAMP = "modificationTimestamp";

    }


}
