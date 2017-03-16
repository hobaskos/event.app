package io.hobaskos.event.eventapp.data.repository;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.AccountService;
import io.hobaskos.event.eventapp.data.model.Event;
import rx.Observable;

/**
 * Created by Magnus on 15.03.2017.
 */

public class AccountRepository {

    private AccountService accountService;

    @Inject
    public AccountRepository(AccountService accountService) {
        this.accountService = accountService;
    }

    public Observable<List<Event>> getAttendingEvents() {
        return accountService.getAttendingEvents();
    }
}
