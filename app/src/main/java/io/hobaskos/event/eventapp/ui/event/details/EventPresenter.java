package io.hobaskos.event.eventapp.ui.event.details;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */
public class EventPresenter extends BaseRxLcePresenter<EventView, Event> {

    private EventView view;

    private final EventRepository eventRepository;
    private final AccountManager accountManager;
    private Observable<Event> eventObservable = Observable.empty();

    @Inject
    public EventPresenter(EventRepository eventRepository, AccountManager accountManager) {
        this.eventRepository = eventRepository;
        this.accountManager = accountManager;
    }

    public void getEvent(Long id) {

        eventObservable = eventRepository.get(id);
        subscribe(eventObservable, false);
    }

    public void getOwnerStatus(Event event) {
        getView().setOwner(event.getOwnerLogin().equals(accountManager.getLocalAccount().getLogin()));
    }
  
    public void getEvent(Long id, Observer<Event> eventObserver) {
        eventRepository.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventObserver);
    }
}

