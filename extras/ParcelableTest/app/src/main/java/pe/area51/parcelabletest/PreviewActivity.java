package pe.area51.parcelabletest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PreviewActivity extends AppCompatActivity {

    public static final String ARG_VISIT = "visit";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);
        final Visit visit = getIntent().getParcelableExtra(ARG_VISIT);
        ((TextView) findViewById(R.id.textview_preview)).setText(visit.toString());
    }
}
