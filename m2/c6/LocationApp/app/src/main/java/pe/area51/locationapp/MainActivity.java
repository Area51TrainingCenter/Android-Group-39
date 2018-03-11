package pe.area51.locationapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private final static int REQUEST_CODE_LOCATION_PERMISSION = 100;

    private LocationManager locationManager;
    private TextView locationInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationInfoTextView = (TextView) findViewById(R.id.textview_location_info);
    }

    private void startLocationUpdates() {
        Log.d("MainActivity", "Starting location updates...");
        if (hasLocationPermission()) {
            final List<String> providers = locationManager.getAllProviders();
            for (final String provider : providers) {
                locationManager.requestLocationUpdates(provider, 0, 0, this);
            }
        } else {
            requestLocationPermission();
        }
    }

    private void stopLocationUpdates() {
        Log.d("MainActivity", "Stopping location updates...");
        locationManager.removeUpdates(this);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_LOCATION_PERMISSION == requestCode) {
            if (permissions.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @SuppressLint("StringFormatMatches")
    private void showLocation(final Location location) {
        final String provider = getString(R.string.provider, location.getProvider());
        final String latitude = getString(R.string.latitude, location.getLatitude());
        final String longitude = getString(R.string.longitude, location.getLongitude());
        final String altitude = getString(R.string.altitude, location.getAltitude());
        locationInfoTextView.setText(String.format("%s\n%s\n%s\n%s", provider, latitude, longitude, altitude));
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
