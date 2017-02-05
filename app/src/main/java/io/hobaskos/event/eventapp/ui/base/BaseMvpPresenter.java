package io.hobaskos.event.eventapp.ui.base;

import rx.Observer;

/**
 * Created by andre on 2/2/2017.
 */

public interface BaseMvpPresenter<T> {

    void subscribe(Observer<T> observer);
}
