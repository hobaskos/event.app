package io.hobaskos.event.eventapp.ui.profile;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.AccountRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Magnus on 16.03.2017.
 */

public class AttendingEventsPresenter extends MvpBasePresenter<AttendingEventsView> {
    private AccountRepository accountRepository;

    @Inject
    public AttendingEventsPresenter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void getEventUserAttending() {
        accountRepository.getAttendingEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Event>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Event> events) {
                        getView().setEventAttending(events);
                    }
                });
    }

}
