package pe.area51.listandcontent;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ListFragment.OnNoteSelectedListener {

    private static final String TAG_FRAGMENT_LIST = "tag_fragment_list";
    private static final String TAG_FRAGMENT_CONTENT = "tag_fragment_content";

    private FragmentManager fragmentManager;
    private ListFragment listFragment;
    private ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            listFragment = new ListFragment();
            contentFragment = new ContentFragment();
        } else {
            listFragment = (ListFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_LIST);
            contentFragment = (ContentFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_CONTENT);
            fragmentManager.popBackStack();
            fragmentManager
                    .beginTransaction()
                    .remove(listFragment)
                    .remove(contentFragment)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, listFragment, TAG_FRAGMENT_LIST)
                    .add(R.id.fragment_container_2, contentFragment, TAG_FRAGMENT_CONTENT)
                    .commit();
        } else {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, listFragment, TAG_FRAGMENT_LIST)
                    .add(R.id.fragment_container, contentFragment, TAG_FRAGMENT_CONTENT)
                    .hide(contentFragment)
                    .commit();
        }
        listFragment.setOnNoteSelectedListener(this);
    }

    @Override
    public void onNoteSelected(Note note) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .hide(listFragment)
                    .show(contentFragment)
                    .commit();
        }
        contentFragment.showNote(note);
    }
}
