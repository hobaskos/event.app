package io.hobaskos.event.eventapp.ui.base;

import android.support.annotation.NonNull;

/**
 * Created by andre on 2/2/2017.
 */

public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    public void onViewAttached(@NonNull V view) {
        // TODO
    }
    public void onViewDetached() {
        // TODO
    }
    public void onDestroyed() {
        // TODO
    }
}
