package io.hobaskos.event.eventapp.ui.event.search.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategoryTheme;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.util.LocationUtil;

/**
 * Created by test on 3/13/2017.
 */

public class SearchEventsMapFragment extends Fragment implements SearchEventsMapView, OnMapReadyCallback {

    public final static String TAG = SearchEventsMapFragment.class.getName();

    private MapView mapView;
    private GoogleMap googleMap;

    private LatLng cameraLocation;

    private List<Event> events;

    private List<Marker> markers;

    private Marker prevMarker; // reference to last selected marker

    @Inject
    public SearchEventsMapPresenter presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInst().getComponent().inject(this);
        presenter.attachView(this);

        markers = new ArrayList<>();
    } // end of onCreate()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_events_map, parent, false);
        mapView = (MapView) view.findViewById(R.id.fragment_search_events_map_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    } // end of onCreateView()

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    } // end of onResume

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    } // end of onPause()

    @Override
    public void onDestroy() {
        if (mapView != null) {
            try {
                mapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    } // end of onDestroy()

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupMap();
    }

    @Override
    public void setEvents(List<Event> events) {
        this.events = events;

    }

    @Override
    public void setCameraLocation(LatLng latLng) {
        cameraLocation = latLng;
    }

    private void setupMap() {
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Clear map before redrawing:
            googleMap.clear();

            MarkerManager markerManager = new MarkerManager(googleMap);
            MarkerManager.Collection mainMarkerCollection = markerManager.newCollection("main_markers");

            MarkerManager.Collection subMarkerCollection = markerManager.newCollection("sub_markers");

            // Get locations and add them as markers on the map:
            for (Event event : events) {
                Location l = event.getLocations().get(0);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(LocationUtil.LocationToLatLng(l))
                        .title(event.getTitle())
                        .snippet(event.getDate(getContext()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                Marker marker = googleMap.addMarker(markerOptions);

                // Put markers in markers list:
                marker.setTag(event);
                mainMarkerCollection.addMarker(markerOptions);
                markers.add(marker);
            }

            googleMap.setOnMapClickListener(latLng -> {
                // Clear previous submarker collection
                subMarkerCollection.clear();
                googleMap.setOnMarkerClickListener(arg0 -> {
                    if (prevMarker != null) {
                        //Set prevMarker back to default color
                        prevMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }

                    arg0.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    final Event event1 = (Event) arg0.getTag();


                    for (Location location : event1.getLocations()) {
                        MarkerOptions markerOptions1 = new MarkerOptions()
                                .position(LocationUtil.LocationToLatLng(location))
                                .title(location.getName())
                                .snippet("")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        subMarkerCollection.addMarker(markerOptions1);
                        googleMap.addMarker(markerOptions1);
                    }
                    prevMarker = arg0; // update reference to previous marker
                    return true;
                });
            });


            googleMap.setOnInfoWindowClickListener(arg0 -> {
                final Event event12 = (Event) arg0.getTag();
                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra(EventActivity.EVENT_ID, event12.getId());
                intent.putExtra(EventActivity.EVENT_THEME, event12.getCategory().getTheme());
                startActivity(intent);
            });


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

            googleMap.moveCamera(cameraUpdate);

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLocation, 10));


        }
    }
}
