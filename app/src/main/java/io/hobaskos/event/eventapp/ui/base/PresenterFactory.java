package io.hobaskos.event.eventapp.ui.base;

import android.support.annotation.NonNull;

/**
 * Created by andre on 2/2/2017.
 */

public interface PresenterFactory<P extends MvpPresenter> {
    @NonNull
    P create();
}
