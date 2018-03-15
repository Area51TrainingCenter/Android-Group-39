package pe.area51.listandcontent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager INSTANCE;

    private static final int VERSION = 1;

    public static SQLiteManager getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SQLiteManager(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private SQLiteManager(final Context context) {
        super(context, "notes", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sql = "CREATE TABLE notes (_id INTEGER PRIMARY KEY, title TEXT, content TEXT, creationTimestamp INTEGER, modificationTimestamp INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("This shouldn't be executed, because we are in the first version.");
    }

    public long insertNote(final Note note) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("creationTimestamp", note.getCreationTimestamp());
        contentValues.put("modificationTimestamp", note.getModificationTimestamp());
        return getWritableDatabase().insert("notes", null, contentValues);
    }

    public ArrayList<Note> getNotes() {
        final Cursor queryCursor = getReadableDatabase().rawQuery("SELECT * FROM notes", null);
        final ArrayList<Note> notes = new ArrayList<>();
        while (queryCursor.moveToNext()) {
            final long id = queryCursor.getLong(queryCursor.getColumnIndex("_id"));
            final String title = queryCursor.getString(queryCursor.getColumnIndex("title"));
            final String content = queryCursor.getString(queryCursor.getColumnIndex("content"));
            final long creationTimestamp = queryCursor.getLong(queryCursor.getColumnIndex("creationTimestamp"));
            final long modificationTimestamp = queryCursor.getLong(queryCursor.getColumnIndex("modificationTimestamp"));
            final Note note = new Note(id, title, content, creationTimestamp, modificationTimestamp);
            notes.add(note);
        }
        queryCursor.close();
        return notes;
    }
}
