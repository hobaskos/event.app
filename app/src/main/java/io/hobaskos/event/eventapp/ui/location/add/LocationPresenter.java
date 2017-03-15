package io.hobaskos.event.eventapp.ui.location.add;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Location;

/**
 * Created by osvold.hans.petter on 13.03.2017.
 */

public class LocationPresenter implements MvpPresenter<LocationView> {

    private LocationView view;

    @Inject
    public LocationPresenter() {

    }

    @Override
    public void attachView(LocationView view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        if(!retainInstance) {
            this.view = null;
        }
    }

    public void addLocation(Location location) {
        Log.i("LocationPresenter", location.toString());
        Log.i("LocationPresenter", "Event-id:" + location.getEventId());
    }
}
