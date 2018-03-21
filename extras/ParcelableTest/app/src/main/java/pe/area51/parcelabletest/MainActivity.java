package pe.area51.parcelabletest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText nameEditText = (EditText) findViewById(R.id.edittext_name);
        final EditText lastNameEditText = (EditText) findViewById(R.id.edittext_lastname);
        final EditText ageEditText = (EditText) findViewById(R.id.edittext_age);
        final EditText addressEditText = (EditText) findViewById(R.id.edittext_address);
        findViewById(R.id.button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                final float age = Float.parseFloat(ageEditText.getText().toString());
                final String address = addressEditText.getText().toString();
                final Visit visit = createVisit(name, lastName, age, address);
                startActivity(new Intent(MainActivity.this, PreviewActivity.class).putExtra(PreviewActivity.ARG_VISIT, visit));
            }
        });
    }

    private Visit createVisit(final String name, final String lastName, final float age, final String address) {
        final Person person = new Person(1, name, lastName, age, address);
        return new Visit(1, person, System.currentTimeMillis());
    }
}
