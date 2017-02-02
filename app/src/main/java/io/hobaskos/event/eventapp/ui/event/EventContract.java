package io.hobaskos.event.eventapp.ui.event;

import io.hobaskos.event.eventapp.ui.base.MvpPresenter;
import io.hobaskos.event.eventapp.ui.base.MvpView;

/**
 * Created by andre on 2/2/2017.
 */

public interface EventContract {

    interface View extends MvpView {

    }

    interface Presenter extends MvpPresenter<EventContract.View> {

    }
}
