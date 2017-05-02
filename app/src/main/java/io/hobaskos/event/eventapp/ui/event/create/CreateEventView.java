package io.hobaskos.event.eventapp.ui.event.create;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategory;

/**
 * Created by hansp on 11.03.2017.
 */

public interface CreateEventView extends MvpView {
    void onSuccess(Event event);
    void onFailure();
    void onCategoriesLoaded(List<EventCategory> categoryList);
    void onFailureLoadingCategories();


}
