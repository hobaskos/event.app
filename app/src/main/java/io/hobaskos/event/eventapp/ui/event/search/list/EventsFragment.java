package io.hobaskos.event.eventapp.ui.event.search.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.State;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.eventbus.FiltersUpdatedEvent;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.adapter.EventDateSectionPagedRecyclerAdapter;
import io.hobaskos.event.eventapp.ui.base.adapter.NpaLinearLayoutManager;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseLceViewStateFragment;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsActivity;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsDialog;
import io.hobaskos.event.eventapp.ui.event.search.map.SearchEventsMapActivity;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsFragment extends
        BaseLceViewStateFragment<SwipeRefreshLayout, List<Event>, EventsView, EventsPresenter>
        implements EventsView {

    public final static String TAG = EventsFragment.class.getName();

    // Views
    @BindView(R.id.recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.contentView) protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_events_fab) protected FloatingActionButton openMapFab;

    private Toolbar toolbar;

    private TextView emptyResultView;
    private NpaLinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    // Model
    private List<Event> eventsList = new ArrayList<>();
    private EventDateSectionPagedRecyclerAdapter adapter;

    // State
    @State boolean canLoadMore = true;
    @State boolean isLoadingMore = false;
    @State int page = 0;

    private DrawerLayout drawerLayout;

    private String searchQuery = "";

    @Inject
    public EventsPresenter eventsPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(FiltersUpdatedEvent event) {
        Log.d(TAG, "onEvent()");
        if (presenter != null) {
            page = 0;
            presenter.loadEvents(false, searchQuery);
        }
    }


    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "page: " + page );

        emptyResultView = (TextView) view.findViewById(R.id.emptyView);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        //progressBar = (ProgressBar) getView().findViewById(R.id.progress);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.contentView);

        // Configure toolbar:
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("Events");

        // Setup Navigation Drawer
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.home:
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                case R.id.action_search:
                    return true;
                case R.id.action_filter:
                    //Intent intent = new Intent(getActivity(), FilterEventsActivity.class);
                    //startActivity(intent);
                    FilterEventsDialog.newInstance().show(getFragmentManager(), "someTag");
                    return true;
            }
            return false;
        });

        // Configure Swipe refresh:
        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        // Configure recyclerview:
        linearLayoutManager = new NpaLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new EventDateSectionPagedRecyclerAdapter(getContext(),
                event -> {
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID, event.getId());
                    intent.putExtra(EventActivity.EVENT_THEME, event.getCategory().getTheme());
                    startActivity(intent);
                });

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (canLoadMore && !isLoadingMore && lastVisibleItemPosition == totalItemCount - 1) {
                    presenter.loadMoreEvents(++page, searchQuery);
                    Log.i(TAG, "page: " + page );
                }
            }
        });

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        openMapFab.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), SearchEventsMapActivity.class);
                startActivity(intent);
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(TAG, "onCreateOptionsMenu()");
        inflater.inflate(R.menu.events_toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((AppCompatActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.loadEvents(false, searchQuery);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                presenter.loadEvents(false, newText);
                return true;
            }
        });
        searchView.setOnClickListener(v ->  {});

        super.onCreateOptionsMenu(menu, inflater);
    }


    // Return layout resource used by this fragment
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search_events_list;
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
        presenter.loadEvents(false, searchQuery);
    }

    @Override
    public List<Event> getData() {
        Log.i(TAG, "getData()");
        return eventsList;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        Log.i(TAG, "getErrorMessage()");
        if (pullToRefresh) {
            return "An error has occurred!";
        } else {
            //return "An error has occurred. Click here to retry";
            return e.getMessage();
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
            eventsList.addAll(model);
            adapter.addItems(model);
        }
    }

    @Override
    public void setData(List<Event> data) {
        Log.i(TAG, "setData()");
        eventsList = new ArrayList<>();
        eventsList.addAll(data);
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
        presenter.loadEvents(pullToRefresh, searchQuery);
        canLoadMore = true;
    }

    @Override
    public void showContent() {
        super.showContent();
        if (adapter.isEmpty()) {
            contentView.setVisibility(View.GONE);
            emptyResultView.setVisibility(View.VISIBLE);
        } else {
            emptyResultView.setVisibility(View.GONE);
        }
    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        emptyResultView.setVisibility(View.GONE);
        super.showError(e, pullToRefresh);
    }

    @Override public void showLoading(boolean pullToRefresh) {
        emptyResultView.setVisibility(View.GONE);
        super.showLoading(pullToRefresh);
    }
}
