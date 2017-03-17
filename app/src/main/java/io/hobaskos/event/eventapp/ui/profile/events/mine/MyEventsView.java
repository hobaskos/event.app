package io.hobaskos.event.eventapp.ui.profile.events.mine;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.viewcontract.MvpLoadMoreView;

/**
 * Created by test on 3/16/2017.
 */

public interface MyEventsView extends MvpLoadMoreView<List<Event>> {

}
