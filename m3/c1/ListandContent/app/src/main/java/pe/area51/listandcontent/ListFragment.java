package pe.area51.listandcontent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alumno on 12/23/16.
 */

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TAG = "ListFragment";

    private ListView listView;

    private OnNoteSelectedListener onNoteSelectedListener;

    private NoteAdapter noteAdapter;

    private final static int NOTE_LOADER_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteAdapter = new NoteAdapter(getContext());
        getLoaderManager()
                .initLoader(
                        NOTE_LOADER_ID,
                        null,
                        this
                );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.listview_elements);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        listView.setAdapter(noteAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onNoteSelectedListener != null) {
                    final Cursor cursor = noteAdapter.getCursor();
                    cursor.moveToPosition(position);
                    final String title = cursor.getString(cursor.getColumnIndex(NotesContract.NoteEntry.TITLE));
                    final String content = cursor.getString(cursor.getColumnIndex(NotesContract.NoteEntry.CONTENT));
                    final long creationTimestamp = cursor.getLong(cursor.getColumnIndex(NotesContract.NoteEntry.CREATION_TIMESTAMP));
                    final long modificationTimestamp = cursor.getLong(cursor.getColumnIndex(NotesContract.NoteEntry.MODIFICATION_TIMESTAMP));
                    final Note note = new Note(id, title, content, creationTimestamp, modificationTimestamp);
                    onNoteSelectedListener.onNoteSelected(note);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_note:
                insertTestNote();
                return true;
            default:
                return false;
        }
    }

    private void insertTestNote() {
        final String title = "Title";
        final String content = "Content";
        final long creationTimestamp = System.currentTimeMillis();
        final long modificationTimestamp = System.currentTimeMillis();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.NoteEntry.TITLE, title);
        contentValues.put(NotesContract.NoteEntry.CONTENT, content);
        contentValues.put(NotesContract.NoteEntry.CREATION_TIMESTAMP, creationTimestamp);
        contentValues.put(NotesContract.NoteEntry.MODIFICATION_TIMESTAMP, modificationTimestamp);
        getActivity().getContentResolver().insert(
                NotesContract.NoteEntry.CONTENT_URI,
                contentValues
        );
    }

    public void setOnNoteSelectedListener(OnNoteSelectedListener onNoteSelectedListener) {
        this.onNoteSelectedListener = onNoteSelectedListener;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                NotesContract.NoteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        noteAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        noteAdapter.changeCursor(null);
    }

    public interface OnNoteSelectedListener {

        void onNoteSelected(final Note note);

    }

    private static class NoteAdapter extends CursorAdapter {

        private final LayoutInflater layoutInflater;
        private DateFormat dateFormat;
        private Date date;

        public NoteAdapter(final Context context) {
            super(context, null, false);
            layoutInflater = LayoutInflater.from(context);
            dateFormat = SimpleDateFormat.getDateInstance();
            date = new Date();
        }

        private static class ViewHolder {
            private TextView titleTextView;
            private TextView dateTextView;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            final View view;
            final ViewHolder viewHolder;
            view = layoutInflater.inflate(R.layout.element_note, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = ((TextView) view.findViewById(R.id.textview_title));
            viewHolder.dateTextView = ((TextView) view.findViewById(R.id.textview_date));
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();
            final String title = cursor
                    .getString(cursor.getColumnIndex(
                            NotesContract.NoteEntry.TITLE
                            )
                    );
            final long creationTimestamp = cursor
                    .getLong(cursor.getColumnIndex(
                            NotesContract.NoteEntry.CREATION_TIMESTAMP
                            )
                    );
            viewHolder.titleTextView.setText(title);
            date.setTime(creationTimestamp);
            viewHolder.dateTextView.setText(String.valueOf(dateFormat.format(date)));
        }
    }

}
