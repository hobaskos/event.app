package io.hobaskos.event.eventapp.ui.event.details.location.create;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by osvold.hans.petter on 13.03.2017.
 */

public interface CreateLocationView extends MvpView {
    void onSuccess();
    void onFailure();
}
