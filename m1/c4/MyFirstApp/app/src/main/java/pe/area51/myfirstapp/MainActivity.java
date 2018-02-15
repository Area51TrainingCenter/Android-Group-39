package pe.area51.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button buttonShowWelcome = (Button) findViewById(R.id.button_show_welcome);
        final EditText editTextName = (EditText) findViewById(R.id.edittext_name);
        buttonShowWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                if (!name.trim().isEmpty()) {
                    final Intent intentLaunchWelcomeActivity = new Intent(MainActivity.this, WelcomeActivity.class);
                    intentLaunchWelcomeActivity.putExtra(WelcomeActivity.PARAM_NAME, name);
                    startActivity(intentLaunchWelcomeActivity);
                } else {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(MainActivity.this, R.string.error_no_name, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
