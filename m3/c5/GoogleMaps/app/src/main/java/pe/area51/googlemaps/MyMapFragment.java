package pe.area51.googlemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class MyMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private final static int REQUEST_PERMISSION_FINE_LOCATION = 100;

    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map_normal:
                setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.action_map_hybrid:
                setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.action_map_satellite:
                setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.action_map_terrain:
                setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return false;
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (hasFineLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
        } else {
            requestFineLocationPermission();
        }
        googleMap.setTrafficEnabled(true);
        final LatLng latLngArea51 = new LatLng(-12.102409, -77.025861);
        moveToLocation(latLngArea51, true);
        showMarker(latLngArea51);
        final LatLng[] latLngArea51Area = new LatLng[]{
                new LatLng(-12.102564, -77.025920),
                new LatLng(-12.102530, -77.025803),
                new LatLng(-12.102323, -77.025804),
                new LatLng(-12.102328, -77.025933)
        };
        showPolygon(latLngArea51Area);
    }

    private boolean hasFineLocationPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestFineLocationPermission() {
        requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION_FINE_LOCATION
        );
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void moveToLocation(final LatLng location, boolean animate) {
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(20)
                .bearing(70)
                .tilt(40)
                .build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        if (animate) {
            googleMap.animateCamera(cameraUpdate, 5000, null);
        } else {
            googleMap.moveCamera(cameraUpdate);
        }
    }

    private void showMarker(final LatLng location) {
        googleMap.addMarker(
                new MarkerOptions()
                        .position(location)
        );
    }

    private void showPolygon(final LatLng... locations) {
        googleMap.addPolygon(
                new PolygonOptions()
                        .add(locations)
                        .fillColor(
                                getTransparentColor(R.color.colorPrimary)
                        )
                        .strokeColor(
                                getTransparentColor(R.color.colorAccent)
                        )
        );
    }

    @ColorInt
    private int getTransparentColor(@ColorRes final int colorRes) {
        return ContextCompat.getColor(
                getActivity(),
                colorRes
        ) & 0x00FFFFFF | 0x77000000; //AARRGGBB color format.
    }

    private void setMapType(final int mapType) {
        googleMap.setMapType(mapType);
    }
}
