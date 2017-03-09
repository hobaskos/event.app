package io.hobaskos.event.eventapp.ui.event.filter;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by andre on 2/20/2017.
 */

public interface FilterEventsView extends MvpView {

    void setDistance(int defaultValue);
    void setLocation(String name, double lat, double lon);
    void setCategory(); //TODO: Create enum of event categories
}
