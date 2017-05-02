package io.hobaskos.event.eventapp.ui.event.details.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.eventbus.EventHasUpdatedLocations;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseLceViewStateFragment;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.details.location.create.CreateLocationActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LocationsFragment
        extends BaseLceViewStateFragment<SwipeRefreshLayout, List<Location>, LocationsView, LocationsPresenter>
        implements LocationsView {

    public static final String TAG = LocationsFragment.class.getName();

    private static final String ARG_LOCATIONS_LIST = "locations-list";
    private static final String ARG_EVENT_ID = "eventId";
    private static final String ARG_IS_EVENT_OWNER = "isOwner";

    private ArrayList<Location> locations = new ArrayList<>();
    private OnListFragmentInteractionListener listener;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;

    @BindView(R.id.contentView)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list)
    protected RecyclerView recyclerView;
    @BindView(R.id.fragment_location_list_fab)
    protected FloatingActionButton addLocation;

    private boolean isOwner;

    @State
    protected Long eventId;
    @State
    protected boolean canLoadMore = true;
    @State
    protected boolean isLoadingMore = false;
    @State
    protected int page = 0;

    @Inject
    protected LocationsPresenter locationsPresenter;

    public LocationsFragment() {}

    @SuppressWarnings("unused")
    public static LocationsFragment newInstance(Long eventId, boolean isOwner) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, eventId);
        args.putBoolean(ARG_IS_EVENT_OWNER, isOwner);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            eventId = getArguments().getLong(ARG_EVENT_ID);
            isOwner = getArguments().getBoolean(ARG_IS_EVENT_OWNER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);

        ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        // Set the adapter
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        locationRecyclerViewAdapter = new LocationRecyclerViewAdapter(locations, listener, context, isOwner);
        recyclerView.setAdapter(locationRecyclerViewAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (canLoadMore && !isLoadingMore && lastVisibleItemPosition == totalItemCount - 1) {
                    presenter.loadMoreLocations(++page);
                }
            }
        });

        if (isOwner) {
            addLocation.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CreateLocationActivity.class);
                intent.putExtra(CreateLocationActivity.EVENT_ID, eventId);
                startActivityForResult(intent, EventActivity.ADD_LOCATION_REQUEST);
            });
        } else {
            addLocation.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: resultCode: " + requestCode + ", requestCode: " + requestCode);
        loadData(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public LceViewState<List<Location>, LocationsView> createViewState() {
        return new LocationsViewState();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadLocations(pullToRefresh, eventId);
        canLoadMore = true;
    }

    @Override
    public List<Location> getData() {
        return locations;
    }

    @Override
    public void setData(List<Location> data) {
        locations = new ArrayList<>();
        locations.addAll(data);
        locationRecyclerViewAdapter.setItems(locations);
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        EventBus.getDefault().post(new EventHasUpdatedLocations(locations));
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_user_list;
    }

    @Override
    public LocationsPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return locationsPresenter;
    }

    @Override
    public void showLoadMore(boolean showLoadMore) {

    }

    @Override
    public void addMoreData(List<Location> data) {
        this.locations.addAll(data);
        locationRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadMoreError(Throwable e) {

    }

    public interface OnListFragmentInteractionListener {
        void onLocationMapInteraction(List<Location> locations, Location focus);
        void onLocationEditInteraction(Location item);
        void onLocationDeleteInteraction(Location item);
    }
}
