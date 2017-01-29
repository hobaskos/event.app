package io.hobaskos.event.eventapp.ui.events;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.BasePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsPresenter extends BasePresenter<EventsView> {

    private EventRepository eventRepository;
    private Observable<List<Event>> eventsObservable;
    private CompositeSubscription subscriptions;

    @Inject
    public EventsPresenter(EventRepository eventRepository) {

        this.eventRepository = eventRepository;
        this.subscriptions = new CompositeSubscription();
    }

    public void getEvents() {
        getView().showLoading();

        eventsObservable = eventRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        eventsObservable.subscribe(
                    events -> getView().setData(events),
                    throwable -> getView().showError(throwable.getMessage())
                );

        //subscriptions.add(subscription);
    }

    public Observable<List<Event>> getObservable() {
        return eventsObservable;
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
