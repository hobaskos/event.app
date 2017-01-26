package io.hobaskos.event.eventapp.events;

import io.hobaskos.event.eventapp.repository.EventRepository;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by andre on 1/26/2017.
 */
public class EventPresenter {
    private final EventRepository repository;
    private final EventView view;
    private CompositeSubscription subscriptions;

    public EventPresenter(EventRepository repository, EventView view) {
        this.repository = repository;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getEvents() {
        view.showWait();

        repository.getAll().subscribeOn(Schedulers.io())
                .doOnNext((list) -> {
                    view.setEvents(list);
                }).doOnError((throwable) -> {
                    view.onFailure(throwable.getMessage());
                });


        //subscriptions.add(subscription);
    }
    public void onStop() {
        subscriptions.unsubscribe();
    }
}
