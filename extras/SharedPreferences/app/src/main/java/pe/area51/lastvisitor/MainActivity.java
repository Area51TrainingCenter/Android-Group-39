package pe.area51.lastvisitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_NAME = "last_visitor";
    private static final String KEY_LAST_VISITOR = "lastVisitor";

    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTextView = (TextView) findViewById(R.id.textview_status);
        showLastVisitor();
        final EditText visitorNameEditText = (EditText) findViewById(R.id.edittext_visitor_name);
        findViewById(R.id.button_save_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String visitorName = visitorNameEditText.getText().toString();
                saveVisitor(visitorName);
                showWelcome(visitorName);
            }
        });
    }

    private void showLastVisitor() {
        final String lastVisitorName = loadVisitor();
        if (lastVisitorName != null) {
            statusTextView.setText(getString(R.string.last_visitor, lastVisitorName));
        } else {
            statusTextView.setText(getString(R.string.first_visitor));
        }
    }

    private void showWelcome(final String visitorName) {
        statusTextView.setText(getString(R.string.visitor_saved, visitorName));
    }

    /**
    *Se pueden guardar múltiples valores en una operación, agregando múltiples "put".
    *Cabe resaltar que tenemos dos métodos para guardar los valores:
    *El método "commit" funciona de forma síncrona y el "apply" de forma asíncrona.
    */
    private boolean saveVisitor(final String visitorName) {
        return getSharedPreferences()
                .edit()
                .putString(KEY_LAST_VISITOR, visitorName)
                .commit();
    }

    /**
    *Este método puede bloquear el hilo por varios milisegundos (operación de entrada/salida ("I/O", "In/Out")),
    *por lo que se recomienda que la carga de datos (así como el almacenamiento, en general toda operación de I/O)
    *se haga de forma asíncrona (en otro hilo).
    */
    private String loadVisitor() {
        return getSharedPreferences().getString(KEY_LAST_VISITOR, null);
    }

    /**
    *Si el SharedPreferences no existe entonces se crea uno nuevo, de lo contrario se devuelve el existente.
    */
    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
    }
}
