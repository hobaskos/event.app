package io.hobaskos.event.eventapp.ui.events;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.BaseMvpPresenter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsPresenter implements BaseMvpPresenter<List<Event>> {

    public final static String TAG = EventsPresenter.class.getName();

    private EventRepository eventRepository;
    private ReplaySubject<List<Event>> replaySubject = ReplaySubject.create();

    private int currentPage = 0;

    @Inject
    public EventsPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;

        fetchData(currentPage);
    }

    private void fetchData(int page) {
        eventRepository.getAll(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    list -> replaySubject.onNext(list),
                    throwable -> replaySubject.onError(throwable)
            );
    }

    public void subscribe(Observer<List<Event>> observer) {
        replaySubject.subscribe(observer);
    }

    public void getFreshData() {
        replaySubject.empty(); // TODO: Doesn't work. Have a workaround coming later.
        // "ReplaySubject doesn't offer a means to clear the buffer"
        // Solution, use simple MvpView interfaces. Store cache here as before as a List<Event>
        currentPage = 0;
        fetchData(currentPage);
    }

    public void requestNext() {
        fetchData(++currentPage);
    }

    public ReplaySubject<List<Event>> getObservable() {
        return replaySubject;
    }
}
