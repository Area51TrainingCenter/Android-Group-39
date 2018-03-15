package pe.area51.listandcontent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alumno on 12/23/16.
 */

public class ListFragment extends Fragment {

    private final static String TAG = "ListFragment";

    private ListView listView;

    private OnNoteSelectedListener onNoteSelectedListener;

    private NoteAdapter noteAdapter;

    private SQLiteManager sqLiteManager;

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
        sqLiteManager = SQLiteManager.getInstance(getActivity());
        noteAdapter = new NoteAdapter(getContext(), sqLiteManager.getNotes());
        listView.setAdapter(noteAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onNoteSelectedListener != null) {
                    final Note note = noteAdapter.getItem(position);
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
                final String title = "Title";
                final String content = "Content";
                final long creationTimestamp = System.currentTimeMillis();
                final long modificationTimestamp = System.currentTimeMillis();
                final long id = sqLiteManager.insertNote(new Note(-1, title, content, creationTimestamp, modificationTimestamp));
                final Note note = new Note(id, title, content, creationTimestamp, modificationTimestamp);
                noteAdapter.add(note);
                return true;
            default:
                return false;
        }
    }

    public void setOnNoteSelectedListener(OnNoteSelectedListener onNoteSelectedListener) {
        this.onNoteSelectedListener = onNoteSelectedListener;
    }

    public interface OnNoteSelectedListener {

        void onNoteSelected(final Note note);

    }

    private static class NoteAdapter extends ArrayAdapter<Note> {

        private final LayoutInflater layoutInflater;
        private DateFormat dateFormat;
        private Date date;

        public NoteAdapter(final Context context, final ArrayList<Note> notes) {
            super(context, 0, 0, notes);
            layoutInflater = LayoutInflater.from(getContext());
            dateFormat = SimpleDateFormat.getDateInstance();
            date = new Date();
        }

        private static class ViewHolder {
            private TextView titleTextView;
            private TextView dateTextView;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "Position: " + position + "; convertView " + (convertView == null ? "== null" : "!= null"));
            final Note note = getItem(position);
            final View view;
            final ViewHolder viewHolder;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.element_note, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.titleTextView = ((TextView) view.findViewById(R.id.textview_title));
                viewHolder.dateTextView = ((TextView) view.findViewById(R.id.textview_date));
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.titleTextView.setText(note.getTitle());
            date.setTime(note.getCreationTimestamp());
            viewHolder.dateTextView.setText(String.valueOf(dateFormat.format(date)));
            return view;
        }
    }

}
