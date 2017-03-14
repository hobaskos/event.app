package io.hobaskos.event.eventapp.ui.event.search.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsPresenter;
import io.hobaskos.event.eventapp.util.LocationUtil;

/**
 * Created by test on 3/13/2017.
 */

public class SearchEventsMapFragment extends Fragment implements SearchEventsMapView, OnMapReadyCallback {

    public final static String TAG = SearchEventsMapFragment.class.getName();

    private MapView mapView;
    private GoogleMap googleMap;

    private List<Event> events;

    @Inject
    public SearchEventsMapPresenter presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInst().getComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_events_map, parent, false);
        mapView = (MapView) view.findViewById(R.id.fragment_search_events_map_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

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
    }

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

    }

    @Override
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    private void populateMap() {
        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.9, 10.75), 10));

//            if (events != null) {
//                for (Event event : events) {
//                    Location l = event.getLocations().get(0);
//                    googleMap.addMarker(new MarkerOptions().position(new LatLng(43.1, -87.9)));
//                }
//            }
            //presenter
            for (Event event : events) {
                Location l = event.getLocations().get(0);
                googleMap.addMarker(new MarkerOptions()
                        .position(LocationUtil.LocationToLatLng(l))
                        .title(event.getTitle())
                        .snippet(event.getDate(getContext())));
            }
        }
    }
}
