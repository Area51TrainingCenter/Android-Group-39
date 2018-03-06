package pe.area51.listandcontent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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

public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment";
    private static final int TEST_NOTES_SIZE = 100;

    private OnNoteSelectedListener onNoteSelectedListener;

    public ListFragment setOnNoteSelectedListener(OnNoteSelectedListener onNoteSelectedListener) {
        this.onNoteSelectedListener = onNoteSelectedListener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        final ListView listViewElements = (ListView) view.findViewById(R.id.listview_elements);
        final ArrayAdapter<Note> noteArrayAdapter = new NoteAdapter(getActivity(), createNotes(TEST_NOTES_SIZE));
        listViewElements.setAdapter(noteArrayAdapter);
        listViewElements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Note note = noteArrayAdapter.getItem(i);
                if (onNoteSelectedListener != null) {
                    onNoteSelectedListener.onNoteSelected(note);
                }
            }
        });
        return view;
    }

    private ArrayList<Note> createNotes(final int quantity) {
        final ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            notes.add(new Note()
                    .setId(i + 1)
                    .setContent("Content " + (i + 1))
                    .setTitle("Title " + (i + 1))
                    .setCreationTimestamp(System.currentTimeMillis()));
        }
        return notes;
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
            protected TextView titleTextView;
            protected TextView dateTextView;
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
