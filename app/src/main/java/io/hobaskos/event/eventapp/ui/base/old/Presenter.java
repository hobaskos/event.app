package io.hobaskos.event.eventapp.ui.base.old;

import android.support.annotation.UiThread;

/**
 * Created by andre on 1/27/2017.
 */

public interface Presenter<V extends View> {

    @UiThread
    void attachView(V view);

    @UiThread
    void detachView();
}
