package io.hobaskos.event.eventapp.ui.event;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */

public class EventPresenter {
    @Inject
    public EventRepository repository;

    private final EventView view;
    //private CompositeSubscription subscriptions;

    public EventPresenter(EventView view) {

        App.getInst().getDiComponent().inject(this);

        this.view = view;
        //this.subscriptions = new CompositeSubscription();
    }

    public void getEvent(int id) {
        view.showWait();

        repository.get(id).subscribeOn(Schedulers.io())
                .doOnNext((event) -> {
                    view.setEvent(event);
                }).doOnError((throwable) -> {
            view.onFailure(throwable.getMessage());
        });


        //subscriptions.add(subscription);
    }
    public void onStop() {
        //subscriptions.unsubscribe();
    }
}

