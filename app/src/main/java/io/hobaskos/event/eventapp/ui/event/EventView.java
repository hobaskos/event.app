package io.hobaskos.event.eventapp.ui.event;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.MvpView;

/**
 * Created by andre on 2/10/2017.
 */

public interface EventView extends MvpView {
    void showLoading(boolean loading);

    void showError(Throwable e);

    void showContent();

    void setData(Event data);
}
