package io.hobaskos.event.eventapp.ui.event.details.map;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Location;

public class EventMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = EventMapActivity.class.getName();

    public static final String LOCATIONS = "locations";
    public static final String FOCUS_POINT = "focusPoint";
    public static final String FOCUS_LAT = "focusLat";
    public static final String FOCUS_LNG = "focusLng";

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private android.location.Location mLastLocation;
    private Marker mCurrLocationMarker;

    private ArrayList<LatLng> points = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private boolean focusPoint = false;
    private Double focusLat;
    private Double focusLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (getIntent() != null) {
            Bundle extra = getIntent().getExtras();
            locations = extra.getParcelableArrayList(LOCATIONS);
            focusLat = extra.getDouble(FOCUS_LAT);
            focusLng = extra.getDouble(FOCUS_LNG);
            focusPoint = extra.getBoolean(FOCUS_POINT);
        } else {
            Toast.makeText(this, R.string.could_not_get_locations, Toast.LENGTH_LONG).show();
            finish();
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("loc", locations);
        super.onSaveInstanceState(outState);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);

        //Initializing Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        if (locations == null || locations.size() == 0) {
            Toast.makeText(this, R.string.this_event_has_no_locations, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            addMarkers();
        }
    }

    private void addMarkers() {


        //Add first Marker
        Marker marker0 = map.addMarker(new MarkerOptions()
                .position(getLatLng(locations.get(0)))
                .title(locations.get(0).getName() + " START")
                .snippet(locations.get(0).getDateLine(this))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        markers.add(marker0);


        //Add all markers except first and last
        for (int i = 1; i < locations.size()-1; i++) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(getLatLng(locations.get(i)))
                    .title(locations.get(i).getName())
                    .snippet(locations.get(i).getDateLine(this))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            markers.add(marker);
        }

        //Add last Marker
        Marker markerLast = map.addMarker(new MarkerOptions()
                .position(getLatLng(locations.get(locations.size() - 1)))
                .title(locations.get(locations.size()-1).getName() + " SLUTT")
                .snippet(locations.get(locations.size() -1).getDateLine(this))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        markers.add(markerLast);

        // Create LatLng bounds from markers:
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            latLngBoundsBuilder.include(marker.getPosition());
        }
        LatLngBounds bounds = latLngBoundsBuilder.build();

        // Initialize padding for the map boundary:
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20); // offset from edges of the map 20% of screen
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        if (focusPoint && focusLat != null && focusLng != null) {
            Log.d(TAG, "focusPoint: " + focusLat + ", " + focusLng);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(focusLat, focusLng), 20));
        } else {
            map.moveCamera(cameraUpdate);
        }

        //Creat a pathline from first to last location
        for (int i = 0; i < locations.size(); i++) {
            points.add(new LatLng(locations.get(i).getGeoPoint().getLat(),locations.get(i).getGeoPoint().getLon()));
        }

        PolylineOptions polyLineOptions = new PolylineOptions();
        polyLineOptions.color(Color.RED);
        polyLineOptions.width(5);
        polyLineOptions.addAll(points);
        map.addPolyline(polyLineOptions);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if ((ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = getLatLng(location);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getString(R.string.your_location));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //Move map camera
        //map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //map.animateCamera(CameraUpdateFactory.zoomTo(11));

        //Stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private LatLng getLatLng(Location location) {
        return new LatLng(location.getGeoPoint().getLat(), location.getGeoPoint().getLon());
    }

    private LatLng getLatLng(android.location.Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
