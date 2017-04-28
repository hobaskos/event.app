package io.hobaskos.event.eventapp.ui.event.details;

import android.util.Log;

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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */
public class EventPresenter extends BaseRxLcePresenter<EventView, Event> {

    public static final String TAG = EventPresenter.class.getName();

    private EventView view;

    private AccountManager accountManager;
    private EventRepository eventRepository;
    private LocationRepository locationRepository;

    @Inject
    public EventPresenter(EventRepository eventRepository, AccountManager accountManager, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.accountManager = accountManager;
        this.locationRepository = locationRepository;
    }

    public void getEvent(Long eventId) {
        Log.d(TAG, "Getting event with ID=" + eventId);

        getEvent(eventId, new Subscriber<Event>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error getting event: " + e.getMessage());
            }

            @Override
            public void onNext(Event event) {
                Log.d(TAG, "Found event with id=" + event.getId());
                if (isViewAttached() && getView() != null) {
                    Log.d(TAG, "Competition id=" + event.getDefaultPollId());
                    getView().setData(event);
                }
            }
        });
    }

    public void reloadEvent(Long eventId) {
        Log.d(TAG, "Reloading event with ID=" + eventId);

        getEvent(eventId, new Subscriber<Event>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error getting event: " + e.getMessage());
            }

            @Override
            public void onNext(Event event) {
                if (isViewAttached() && getView() != null) {
                    Log.d(TAG, "Competition id=" + event.getDefaultPollId());
                    getView().reloadData(event);
                }
            }
        });

    }

    public void getOwnerStatus(Event event) {
        if (isViewAttached() && getView() != null) {
            if (accountManager.isLoggedIn()) {
                getView().setIsOwner(
                        event.getOwnerLogin().equals(accountManager.getLocalAccount().getLogin())
                );
            }            
        }
    }
  
    public void getEvent(Long id, Observer<Event> eventObserver) {
        Log.d(TAG, "Getting event with ID=" + id);

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

    public boolean isLoggedIn() {
        return accountManager.isLoggedIn();
    }

    public void leaveEvent(Long eventAttendingId, Action1<Boolean> callback) {
        Log.d(TAG, "leaveEvent: " + eventAttendingId);
        eventRepository.leaveEvent(eventAttendingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        callback.call(false);
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        callback.call(true);
                    }
                });
    }
}

