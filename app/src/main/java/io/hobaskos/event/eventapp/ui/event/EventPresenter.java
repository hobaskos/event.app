package io.hobaskos.event.eventapp.ui.event;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.BaseMvpPresenter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */
public class EventPresenter implements BaseMvpPresenter<Event> {

    private EventRepository eventRepository;
    private Observable<Event> eventObservable = Observable.empty();

    @Inject
    public EventPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void getEvent(Long id) {

        eventObservable = eventRepository.get(id)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void subscribe(Observer<Event> observer) {
        eventObservable.subscribe(observer);
    }

    public Observable<Event> getObservable() {
        return eventObservable;
    }
}

