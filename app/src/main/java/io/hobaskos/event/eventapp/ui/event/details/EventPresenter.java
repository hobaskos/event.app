package io.hobaskos.event.eventapp.ui.event.details;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Observable;

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
}

