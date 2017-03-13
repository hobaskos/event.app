package io.hobaskos.event.eventapp.ui.event.search.map;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import de.greenrobot.event.EventBus;
import io.hobaskos.event.eventapp.data.eventbusevent.SetEventsEvent;
import io.hobaskos.event.eventapp.data.eventbusevent.UpdateEventsEvent;

/**
 * Created by test on 3/13/2017.
 */

public class SearchEventsMapPresenter extends MvpBasePresenter<SearchEventsMapView>{

    @Override public void attachView(SearchEventsMapView view) {
        super.attachView(view);
        EventBus.getDefault().register(this);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        EventBus.getDefault().unregister(this);

    }

    public void onEvent(SetEventsEvent event) {

    }

    public void onEvent(UpdateEventsEvent event) {

    }
}
