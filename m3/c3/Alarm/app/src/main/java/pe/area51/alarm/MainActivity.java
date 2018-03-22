package pe.area51.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText timeInSecondsEditText = (EditText) findViewById(R.id.edittext_time_in_seconds);
        findViewById(R.id.button_program_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String timeInSecondsString = timeInSecondsEditText.getText().toString();
                    final int timeInSeconds = Integer.parseInt(timeInSecondsString);
                    programAlarm(timeInSeconds);
                    showMessage(R.string.alarm_programmed);
                } catch (NumberFormatException e) {
                    showMessage(R.string.only_number_error);
                }
            }
        });
    }

    private void programAlarm(final int timeInSeconds) {
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final PendingIntent operation = PendingIntent.getService(this,
                1,
                new Intent(this, MyAlarmService.class)
                        .putExtra(MyAlarmService.ARG_COMMAND, MyAlarmService.COMMAND_START_ALARM),
                PendingIntent.FLAG_CANCEL_CURRENT);
        setExactAlarm(alarmManager, timeInSeconds, operation);
    }

    private void setExactAlarm(final AlarmManager alarmManager,
                               final int timeInSeconds,
                               final PendingIntent operation) {
        final long triggerAtMillis = SystemClock.elapsedRealtime() + timeInSeconds * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerAtMillis,
                    operation
            );
        } else {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerAtMillis,
                    operation
            );
        }
    }

    private void showMessage(@StringRes final int stringRes) {
        showMessage(getString(stringRes));
    }

    private void showMessage(final String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
