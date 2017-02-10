package io.hobaskos.event.eventapp.ui.events;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.MvpPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsPresenter implements MvpPresenter<EventsView> {

    public final static String TAG = EventsPresenter.class.getName();

    private EventsView view;

    private EventRepository eventRepository;

    private List<Event> eventsCache;

    private int currentPage = 0;

    @Inject
    public EventsPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private void fetchPage(int page) {
        eventRepository.getAll(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    list -> view.appendData(list),
                    throwable -> view.showError(throwable)
            );
    }

    private void fetchPages(int pages) {
        eventRepository.getPages(pages)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> view.setData(list),
                        throwable -> view.showError(throwable)
                );
    }

    public void refreshData() {
        fetchPages(currentPage + 1);
    }

    public void requestNext() {
        fetchPage(++currentPage);
    }


    @Override
    public void onAttachView(EventsView view) {
        this.view = view;
        fetchPage(currentPage);
    }
}
