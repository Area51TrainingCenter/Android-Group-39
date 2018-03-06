package pe.area51.listandcontent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListFragment.OnNoteSelectedListener {

    private static final String TAG_FRAGMENT_LIST = "tag_fragment_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ListFragment listFragment;
        if (savedInstanceState == null) {
            listFragment = new ListFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, listFragment, TAG_FRAGMENT_LIST)
                    .commit();
        } else {
            listFragment = (ListFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_LIST);
        }
        listFragment.setOnNoteSelectedListener(this);
    }

    @Override
    public void onNoteSelected(Note note) {
        Toast.makeText(this, note.toString(), Toast.LENGTH_SHORT).show();
    }
}
