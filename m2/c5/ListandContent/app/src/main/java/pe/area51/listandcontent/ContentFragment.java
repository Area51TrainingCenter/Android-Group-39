package pe.area51.listandcontent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alumno on 12/23/16.
 */

public class ContentFragment extends Fragment {

    private static final String KEY_NOTE = "note";

    private TextView textViewTitle;
    private TextView textViewContent;

    private Note note;

    public static ContentFragment newInstance(final Note note) {
        final ContentFragment contentFragment = new ContentFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(KEY_NOTE, note);
        contentFragment.setArguments(arguments);
        return contentFragment;
    }

    public boolean isShowingNote() {
        return note != null;
    }

    public void showNote(final Note note) {
        this.note = note;
        printNote(note);
    }

    private void printNote(final Note note) {
        textViewTitle.setText(note.getTitle());
        textViewContent.setText(note.getContent());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (note != null) {
            outState.putParcelable(KEY_NOTE, note);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(KEY_NOTE);
        } else {
            final Bundle arguments = getArguments();
            if (arguments != null) {
                note = arguments.getParcelable(KEY_NOTE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content, container, false);
        textViewTitle = (TextView) view.findViewById(R.id.textview_title);
        textViewContent = (TextView) view.findViewById(R.id.textview_content);
        if (note != null) {
            printNote(note);
        }
        return view;
    }
}
