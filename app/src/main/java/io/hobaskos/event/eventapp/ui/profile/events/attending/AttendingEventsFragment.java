package io.hobaskos.event.eventapp.ui.profile.events.attending;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.State;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseLceViewStateFragment;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.details.attending.AttendeesPresenter;
import io.hobaskos.event.eventapp.ui.event.details.attending.AttendeesView;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsAdapter;
import io.hobaskos.event.eventapp.ui.event.search.list.NpaLinearLayoutManager;
import io.hobaskos.event.eventapp.ui.profile.events.mine.EventPagedRecyclerAdapter;
import io.hobaskos.event.eventapp.ui.profile.events.mine.MyEventsFragment;
import io.hobaskos.event.eventapp.ui.profile.events.mine.MyEventsPresenter;
import io.hobaskos.event.eventapp.ui.profile.events.mine.MyEventsViewState;

/**
 * Created by Magnus on 13.03.2017.
 */

public class AttendingEventsFragment extends BaseLceViewStateFragment<SwipeRefreshLayout, List<Event>, AttendingEventsView, AttendingEventsPresenter>
        implements AttendingEventsView {

    public final static String TAG = AttendingEventsFragment.class.getName();

    // Views
    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.contentView) SwipeRefreshLayout swipeRefreshLayout;

    TextView emptyResultView;

    private NpaLinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    // Model
    List<Event> eventsList = new ArrayList<>();
    private AttendingEventsAdapter adapter;

    // State
    @State
    boolean canLoadMore = true;
    @State boolean isLoadingMore = false;
    @State int page = 0;


    @Inject
    public AttendingEventsPresenter attendingEventsPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "page: " + page );

        emptyResultView = (TextView) view.findViewById(R.id.emptyView);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.contentView);



        // Configure Swipe refresh:
        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        // Configure recyclerview:
        linearLayoutManager = new NpaLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AttendingEventsAdapter(eventsList, getContext(),
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
                    presenter.loadMoreEvents(++page);
                    Log.i(TAG, "page: " + page );
                }
            }
        });

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    // Return layout resource used by this fragment
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_event_list;
    }

    @Override
    public AttendingEventsViewState createViewState() {
        Log.i(TAG, "createViewState()");
        return new AttendingEventsViewState();
    }

    @Override
    public AttendingEventsViewState getViewState() {
        Log.i(TAG, "getViewState()");
        return (AttendingEventsViewState) super.getViewState();
    }

    @Override
    public AttendingEventsPresenter createPresenter() {
        Log.i(TAG, "createPresenter()");
        App.getInst().getComponent().inject(this);
        return attendingEventsPresenter;
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
            adapter.addItems(model);
        }
    }

    @Override
    public void setData(List<Event> data) {
        Log.i(TAG, "setData(), size: " + data.size());
        Log.i(TAG, "setData(), toString: " + data.toString());
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
        Log.i(TAG, "showContent");
        super.showContent();
        if (adapter.getItems().isEmpty()) {
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
} // end of Class
