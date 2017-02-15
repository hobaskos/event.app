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
    public static final String PAGE_KEY = "PAGE";

    @Inject
    public EventsPresenter eventsPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        /*
        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(PAGE_KEY, 0);
        }
        */
        Log.i(TAG, "page: " + page );
        //Icepick.restoreInstanceState(this, savedInstanceState);

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
        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        // Configure recyclerview:
        linearLayoutManager = new NpaLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
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
                    presenter.loadMoreEvents(++page);
                    Log.i(TAG, "page: " + page );
                }
            }
        });

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(TAG, "onCreateOptionsMenu()");
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
        Log.i(TAG, "createViewState()");
        return new EventsViewState();
    }

    @Override
    public EventsViewState getViewState() {
        Log.i(TAG, "getViewState()");
        return (EventsViewState) super.getViewState();
    }

    @Override
    public EventsPresenter createPresenter() {
        Log.i(TAG, "createPresenter()");
        App.getInst().getComponent().inject(this);
        return eventsPresenter;
    }

    @Override public void onNewViewStateInstance() {
        Log.i(TAG, "onNewViewStateInstance()");
        presenter.loadEvents(false);

    }

    @Override
    public List<Event> getData() {
        Log.i(TAG, "getData()");
        return adapter.getItems();
    }


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        Log.i(TAG, "getErrorMessage()");
        if (pullToRefresh) {
            return "An error has occurred!";
        } else {
            return "An error has occurred. Click here to retry";
        }
    }


    @Override
    public void showLoadMore(boolean showLoadMore) {
        Log.i(TAG, "showLoadMore()");
        adapter.setLoadMore(showLoadMore);
        getViewState().setLoadingMore(showLoadMore);
        isLoadingMore = showLoadMore;
    }

    @Override
    public void showLoadMoreError(Throwable e) {
        Log.i(TAG, "showLoadMoreError()");
        Toast.makeText(getActivity(), "An error has occurred while loading older events", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addMoreData(List<Event> model) {
        Log.i(TAG, "addMoreData()");

        if (model.isEmpty()) {
            canLoadMore = false;
            Toast.makeText(getActivity(), "No more events to show", Toast.LENGTH_SHORT).show();
        } else {
            adapter.addItems(model);
        }
    }

    @Override
    public void setData(List<Event> data) {
        Log.i(TAG, "setData()");
        adapter.setItems(data);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Log.i(TAG, "loadData()");
        if (pullToRefresh) {
            page = 0;
        }
        presenter.loadEvents(pullToRefresh);
        canLoadMore = true;
    }

    @Override
    public void showContent() {
        Log.i(TAG, "showContent()");
        super.showContent();
    }
}
