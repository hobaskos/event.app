package io.hobaskos.event.eventapp.ui.events.old;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.old.BaseMvpFragment;
import io.hobaskos.event.eventapp.ui.base.old.PresenterFactory;
import io.hobaskos.event.eventapp.ui.event.EventActivity;


public class EventsFragment extends BaseMvpFragment<EventsPresenter> implements EventsView  {

    public final static String TAG = EventsActivity.class.getName();

    // Views
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    // Model
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;

    @Inject
    public EventsPresenter eventsPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout:
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        // Find views:
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.content_view);

        // Configure toolbar:
        configureToolbar();

        // Configure Swipe refresh:
        //swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        swipeRefreshLayout.setOnRefreshListener(() -> eventsPresenter.refreshData());

        // Configure recyclerview:
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        eventsAdapter = new EventsAdapter(eventsList,
                event -> {
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID, event.getId());
                    startActivity(intent);
                },
                itemCount -> {
                    //showLoading();
                    eventsPresenter.requestNext();
                });

        recyclerView.setAdapter(eventsAdapter);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @NonNull
    @Override
    protected String tag() {
        return TAG;
    }

    @NonNull
    @Override
    protected PresenterFactory<EventsPresenter> getPresenterFactory() {
        App.getInst().getComponent().inject(this);
        return () -> eventsPresenter;
    }

    @Override
    protected void onPresenterPrepared(@NonNull EventsPresenter presenter) {
        this.eventsPresenter = presenter;
        eventsPresenter.onAttachView(this);
        eventsPresenter.getCache();
    }


    @Override
    public void showLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(Throwable e) {
        stopLoading();
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showContent() {
        stopLoading();
    }

    @Override
    public void setData(List<Event> data) {
        eventsList.clear();
        eventsList.addAll(data);
        eventsAdapter.notifyDataSetChanged();
        stopLoading();
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void appendData(List<Event> data) {
        eventsList.addAll(data);
        eventsAdapter.notifyDataSetChanged();
        //stopLoading();
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void configureToolbar() {
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
