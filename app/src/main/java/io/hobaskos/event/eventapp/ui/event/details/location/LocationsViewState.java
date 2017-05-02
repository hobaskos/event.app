package io.hobaskos.event.eventapp.ui.event.details.location;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Location;

public class LocationsViewState extends CastedArrayListLceViewState<List<Location>, LocationsView> {

    public final static String TAG = LocationsViewState.class.getName();

    public static final Creator<LocationsViewState> CREATOR = new Creator<LocationsViewState>() {
        @Override public LocationsViewState createFromParcel(Parcel source) {
            return new LocationsViewState(source);
        }

        @Override public LocationsViewState[] newArray(int size) {
            return new LocationsViewState[size];
        }
    };

    boolean loadingMore = false;

    // Constructors;
    public LocationsViewState() {};

    protected LocationsViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }


    @Override public void apply(LocationsView view, boolean retained) {
        super.apply(view, retained);

        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showLoadMore(loadingMore);
        }
    }
}
