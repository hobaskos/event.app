package io.hobaskos.event.eventapp.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseLceViewStateFragment;
import io.hobaskos.event.eventapp.ui.event.EventActivity;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsFragment extends
        BaseLceViewStateFragment<SwipeRefreshLayout, List<Event>, EventsView, EventsPresenter>
        implements EventsView {

    public final static String TAG = EventsFragment.class.getName();

    // Views
    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.contentView) SwipeRefreshLayout swipeRefreshLayout;

    private NpaLinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    // Model
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter adapter;

    boolean canLoadMore = true;
    boolean isLoadingMore = false;
    int page = 0;

    @Inject
    public io.hobaskos.event.eventapp.ui.events.EventsPresenter eventsPresenter;


    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure toolbar:
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        //progressBar = (ProgressBar) getView().findViewById(R.id.progress);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.contentView);


        setHasOptionsMenu(true);
        toolbar.setTitle("Events");
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.action_search:
                    // TODO: Create search fragment/activity
                    return true;
                case R.id.action_filter:
                    // TODO: Create filter fragment/activity
                    return true;
                case R.id.action_create:
                    // TODO: Create "create event" fragment/activity
                    return true;
            }
            return false;
        });

        // Configure Swipe refresh:
        //swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        swipeRefreshLayout.setOnRefreshListener(() -> eventsPresenter.loadEvents(true));

        // Configure recyclerview:
        linearLayoutManager = new NpaLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //recyclerView.addOnScrollListener();
        adapter = new EventsAdapter(eventsList,
                event -> {
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID, event.getId());
                    startActivity(intent);
                },
                itemCount -> {
                        //presenter.loadMoreEvents(++page);
                });

        recyclerView.setAdapter(adapter);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (canLoadMore && !isLoadingMore && lastVisibleItemPosition == totalItemCount - 1) {
                    Log.i(TAG, "HEEEEEY");
                    presenter.loadMoreEvents(++page);
                }
            }
        });

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    // Return layout resource used by this fragment
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_events2;
    }

    @Override
    public EventsViewState createViewState() {
        return new EventsViewState();
    }

    @Override
    public EventsViewState getViewState() {
        return (EventsViewState) super.getViewState();
    }

    @Override
    public EventsPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return eventsPresenter;
    }

    @Override public void onNewViewStateInstance() {
        presenter.loadEvents(false);

    }

    @Override
    public List<Event> getData() {
        return adapter.getItems();
    }


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        if (pullToRefresh) {
            return "An error has occurred!";
        } else {
            return "An error has occurred. Click here to retry";
        }
    }


    @Override
    public void showLoadMore(boolean showLoadMore) {
        adapter.setLoadMore(showLoadMore);
        getViewState().setLoadingMore(showLoadMore);
        isLoadingMore = showLoadMore;
    }

    @Override
    public void showLoadMoreError(Throwable e) {
        Toast.makeText(getActivity(), "An error has occurred while loading older events", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addMoreData(List<Event> model) {
        adapter.addItems(model);

        if (model.isEmpty()) {
            canLoadMore = false;
            Toast.makeText(getActivity(), "No more events to show", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setData(List<Event> data) {
        adapter.setItems(data);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadEvents(pullToRefresh);
        canLoadMore = true;
    }

    @Override
    public void showContent() {
        super.showContent();
        //showLoadMore(true);
    }


}
