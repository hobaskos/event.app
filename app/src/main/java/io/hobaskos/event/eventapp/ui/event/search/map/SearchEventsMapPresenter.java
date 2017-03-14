package io.hobaskos.event.eventapp.ui.event.search.map;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;


import io.hobaskos.event.eventapp.data.eventbusevent.SetEventsEvent;
import io.hobaskos.event.eventapp.data.eventbusevent.UpdateEventsEvent;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.storage.FilterSettings;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;



/**
 * Created by test on 3/13/2017.
 */

public class SearchEventsMapPresenter extends MvpBasePresenter<SearchEventsMapView>{

    public final static String TAG = SearchEventsMapPresenter.class.getName();

    private FilterSettings filterSettings;

    @Inject
    public SearchEventsMapPresenter(FilterSettings filterSettings) {
        this.filterSettings = filterSettings;
    }

    @Override public void attachView(SearchEventsMapView view) {
        super.attachView(view);
        EventBus.getDefault().register(this);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        EventBus.getDefault().unregister(this);

    }

    public void loadEvents() {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void onEvent(SetEventsEvent event) {
        getView().setEvents(event.getData());
        Log.d(TAG, "    " + event.getData().size());
    }

    public void onEvent(UpdateEventsEvent event) {

    }
}
