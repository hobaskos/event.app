package io.hobaskos.event.eventapp.ui.event.details;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.repository.LocationRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */
public class EventPresenter extends BaseRxLcePresenter<EventView, Event> {

    private EventView view;

    private AccountManager accountManager;
    private EventRepository eventRepository;
    private LocationRepository locationRepository;
    private Observable<Event> eventObservable = Observable.empty();

    @Inject
    public EventPresenter(EventRepository eventRepository, AccountManager accountManager, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.accountManager = accountManager;
        this.locationRepository = locationRepository;
    }

    public void getEvent(Long id) {

        Log.i("EventPresenter", "Getting event with ID=" + id);

        eventRepository.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Event>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("EventPresenter", "Error getting event: " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Event event) {
                        Log.i("EventPresenter", "Found event with id=" + event.getId());
                        if(isViewAttached() && getView() != null) {
                            Log.i("EventPresenter", "View is attached.");
                            getView().setData(event);
                        }
                    }
                });
    }

    public void getOwnerStatus(Event event) {
        if(isViewAttached() && getView() != null) {

            if(accountManager.isLoggedIn()) {
                getView().setOwner(
                        event.getOwnerLogin().equals(accountManager.getLocalAccount().getLogin())
                );
                return;
            }

            getView().setOwner(false);
        }
    }
  
    public void getEvent(Long id, Observer<Event> eventObserver) {
        Log.i("EventPresenter", "Getting event with ID=" + id);

        eventRepository.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventObserver);
    }

    public void remove(Location location, Observer<Void> observer) {
        locationRepository.remove(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

