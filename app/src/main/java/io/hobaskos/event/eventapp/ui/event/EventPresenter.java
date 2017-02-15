package io.hobaskos.event.eventapp.ui.event;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */
public class EventPresenter extends BaseRxLcePresenter<EventView, Event> {

    private EventView view;

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

        eventObservable.subscribe(
                event -> view.setData(event),
                throwable -> view.showError(throwable, false)
        );
    }

    public Observable<Event> getObservable() {
        return eventObservable;
    }

}

