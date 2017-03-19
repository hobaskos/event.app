package io.hobaskos.event.eventapp.ui.event.filter;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.EventCategory;

/**
 * Created by andre on 2/20/2017.
 */

public interface FilterEventsView extends MvpView {

    void setFilterValues(int distance, boolean currentLocStatus, String placeName, double lat, double lon, long categoryId);
    void setDistance(int defaultValue);
    void setCurrentLocationStatus(boolean status);
    void setLocation(String name, double lat, double lon);
    void setCategories(List<EventCategory> categories);
    void setCategory(long id);
    void showError(Throwable e);
}
