package pe.area51.accelerometertest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView accelerometerStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerStatusTextView = (TextView) findViewById(R.id.textview_accelerometer_status);
    }

    private void startAccelerometer() {
        final Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void stopAccelerometer() {
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAccelerometer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAccelerometer();
    }

    private void updateAccelerometerStatus(SensorEvent sensorEvent) {
        final float xAxis = sensorEvent.values[0];
        final float yAxis = sensorEvent.values[1];
        final float zAxis = sensorEvent.values[2];
        final String accelerometerStatus =
                getString(R.string.x_axis, xAxis) + "\n" +
                        getString(R.string.y_axis, yAxis) + "\n" +
                        getString(R.string.z_axis, zAxis);
        accelerometerStatusTextView.setText(accelerometerStatus);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            updateAccelerometerStatus(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

