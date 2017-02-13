package io.hobaskos.event.eventapp.ui.events;

import android.support.v4.widget.SwipeRefreshLayout;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseLceViewStateFragment;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsFragment extends BaseLceViewStateFragment<SwipeRefreshLayout,
        List<Event>, EventsView, EventsPresenter>
        implements EventsView {


    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @Override
    public LceViewState createViewState() {
        return null;
    }

    @Override
    public List<Event> getData() {
        return null;
    }


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }


    @Override
    public void showLoadMore(boolean showLoadMore) {

    }

    @Override
    public void showLoadMoreError(Throwable e) {

    }

    @Override
    public void addMoreData(List<Event> model) {

    }

    @Override
    public void setData(List<Event> data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }

    @Override
    public EventsPresenter createPresenter() {
        return null;
    }
}
