package pe.area51.listandcontent;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by alumno on 1/13/18.
 */

public class NotesProvider extends ContentProvider {

    private SQLiteManager sqLiteManager;

    private UriMatcher uriMatcher;

    private final static int MATCH_NOTE = 100;
    private final static int MATCH_NOTE_WITH_ID = 101;

    @Override
    public boolean onCreate() {
        sqLiteManager = new SQLiteManager(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(
                NotesContract.NoteEntry.CONTENT_URI.getAuthority(),
                NotesContract.NoteEntry.CONTENT_URI.getPath(),
                MATCH_NOTE
        );
        uriMatcher.addURI(
                NotesContract.NoteEntry.CONTENT_URI.getAuthority(),
                NotesContract.NoteEntry.CONTENT_URI.getPath() + "/#",
                MATCH_NOTE_WITH_ID
        );
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArguments,
                        @Nullable String sortOrder) {
        final int uriMatch = uriMatcher.match(uri);
        if (uriMatch != UriMatcher.NO_MATCH) {
            if (uriMatch == MATCH_NOTE_WITH_ID) {
                selection = NotesContract.NoteEntry._ID + "=?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
            }
            final Cursor cursor = sqLiteManager
                    .getReadableDatabase()
                    .query(NotesContract.NoteEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArguments,
                            null,
                            null,
                            sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int uriMatch = uriMatcher.match(uri);
        if (uriMatch == MATCH_NOTE) {
            final long id = sqLiteManager
                    .getWritableDatabase()
                    .insert(
                            NotesContract.NoteEntry.TABLE_NAME,
                            null,
                            contentValues
                    );
            getContext().getContentResolver().notifyChange(
                    uri,
                    null
            );
            return ContentUris.withAppendedId(uri, id);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
