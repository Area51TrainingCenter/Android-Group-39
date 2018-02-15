package pe.area51.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    public final static String PARAM_NAME = "pe.area51.myfirstapp.WelcomeActivity.NAME";

    private String welcomeMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final Intent intentThatLaunchedThisActivity = getIntent();
        if (intentThatLaunchedThisActivity.hasExtra(PARAM_NAME)) {
            final String name = intentThatLaunchedThisActivity.getStringExtra(PARAM_NAME);
            final TextView textViewWelcome = (TextView) findViewById(R.id.textview_welcome);
            welcomeMessage = getString(R.string.welcome, name);
            textViewWelcome.setText(welcomeMessage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            if (welcomeMessage != null) {
                shareText(welcomeMessage);
            }
            return true;
        } else {
            return false;
        }
    }

    private void shareText(final String textToShare) {
        final String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setText(textToShare)
                .startChooser();
    }
}
