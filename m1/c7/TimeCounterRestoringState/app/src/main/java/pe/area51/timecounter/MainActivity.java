package pe.area51.timecounter;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private final static long REPETITIVE_TASK_DELAY_IN_MILLIS = 10;

    private TextView textViewStatus;

    private RepetitiveTask timeCounterRepetitiveTask;

    private long startTime;

    private DecimalFormat decimalFormat;

    private static final String SAVED_INSTANCE_STATE_KEY_START_TIME = "startTime";
    private static final String SAVED_INSTANCE_STATE_KEY_IS_RUNNING = "isRunning";
    private static final String SAVED_INSTANCE_STATE_KEY_LAST_TIME_TEXT = "lastTimeText";

    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewStatus = (TextView) findViewById(R.id.textview_status);
        timeCounterRepetitiveTask = createTimeCounterRepetitiveTask();
        startTime = 0;
        decimalFormat = new DecimalFormat("0.000");
        isPaused = false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        startTime = savedInstanceState.getLong(SAVED_INSTANCE_STATE_KEY_START_TIME, 0);
        final boolean isRunning = savedInstanceState.getBoolean(SAVED_INSTANCE_STATE_KEY_IS_RUNNING, false);
        if (isRunning) {
            timeCounterRepetitiveTask.start(true);
        } else {
            final String lastTimeText = savedInstanceState.getString(SAVED_INSTANCE_STATE_KEY_LAST_TIME_TEXT, "");
            textViewStatus.setText(lastTimeText);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(SAVED_INSTANCE_STATE_KEY_START_TIME, startTime);
        outState.putBoolean(SAVED_INSTANCE_STATE_KEY_IS_RUNNING, isPaused || timeCounterRepetitiveTask.isRunning());
        outState.putString(SAVED_INSTANCE_STATE_KEY_LAST_TIME_TEXT, textViewStatus.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isPaused) {
            timeCounterRepetitiveTask.start(true);
            isPaused = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timeCounterRepetitiveTask.isRunning()) {
            timeCounterRepetitiveTask.stop();
            isPaused = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch_timer:
                toggleTimeCounter();
                return true;
            default:
                return false;
        }
    }

    private void toggleTimeCounter() {
        if (timeCounterRepetitiveTask.isRunning()) {
            timeCounterRepetitiveTask.stop();
        } else {
            startTime = SystemClock.elapsedRealtime();
            timeCounterRepetitiveTask.start(true);
        }
    }

    private RepetitiveTask createTimeCounterRepetitiveTask() {
        return new RepetitiveTask(new Runnable() {
            @Override
            public void run() {
                final long elapsedTimeInMillis = (SystemClock.elapsedRealtime() - startTime);
                updateTextViewStatus(elapsedTimeInMillis);
            }
        }, REPETITIVE_TASK_DELAY_IN_MILLIS);
    }

    private void updateTextViewStatus(final long elapsedTimeInMillis) {
        final String formattedElapsedTime = decimalFormat.format(elapsedTimeInMillis / 1000.0);
        textViewStatus.setText(formattedElapsedTime);
    }
}