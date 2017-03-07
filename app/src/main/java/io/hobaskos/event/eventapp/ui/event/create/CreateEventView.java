package io.hobaskos.event.eventapp.ui.event.create;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.EventCategory;

/**
 * Created by hansp on 07.03.2017.
 */

public interface CreateEventView extends MvpView {
    void onCreateSuccess();
    void onCreateFailure();
    void onCategoriesLoaded(List<EventCategory> categories);
}
