package io.hobaskos.event.eventapp.ui.event.create;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.data.model.Location;

/**
 * Created by hansp on 07.03.2017.
 */

public interface CreateEventView extends MvpView {
    void onCreateSuccess();
    void onCreateFailure();
    void showLoader();
    void hideLoader();
    void onCategoriesLoaded(List<EventCategory> categories);
    void onLocationListUpdated(Location location);
}
