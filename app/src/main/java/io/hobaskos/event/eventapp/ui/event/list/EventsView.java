package io.hobaskos.event.eventapp.ui.event.list;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.viewcontract.MvpLoadMoreView;

/**
 * Created by andre on 2/13/2017.
 */

public interface EventsView extends MvpLoadMoreView<List<Event>> {

    // Methods inherited from MvpLoadMore & MvpLceView:
    //void showLoadMore(boolean showLoadMore);
    //void showLoadMoreError(Throwable e);
    //void addMoreData(M model);
    //public void showLoading(boolean pullToRefresh);
    //public void showContent();
    //public void showError(Throwable e, boolean pullToRefresh);
    //public void setData(M data);
    //public void loadData(boolean pullToRefresh);
}
