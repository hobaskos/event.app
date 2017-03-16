package io.hobaskos.event.eventapp.ui.event.details.attending;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseLceViewStateFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A fragment representing a list of Users
 */
public class AttendeesFragment
        extends BaseLceViewStateFragment<SwipeRefreshLayout, List<User>, AttendeesView, AttendeesPresenter>
        implements AttendeesView {

    public static final String TAG = AttendeesFragment.class.getName();
    public static final String EVENT_ID = "eventId";

    private OnUserListFragmentInteractionListener listener;
    private DividerItemDecoration dividerItemDecoration;

    private List<User> users = new ArrayList<>();

    @BindView(R.id.contentView)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_attendees_attend)
    protected FloatingActionButton attendFab;
    @BindView(R.id.list)
    protected RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    protected TextView emptyResultView;

    @State
    protected Long eventId;
    @State
    protected boolean canLoadMore = true;
    @State
    protected boolean isLoadingMore = false;

    private UserRecyclerViewAdapter userRecyclerViewAdapter;

    @Inject
    public AttendeesPresenter attendeesPresenter;

    public AttendeesFragment() {}

    public static AttendeesFragment newInstance(Long eventId) {
        AttendeesFragment fragment = new AttendeesFragment();
        Bundle args = new Bundle();
        args.putLong(EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventId = getArguments().getLong(EVENT_ID);
        }

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        // Set the adapter
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        userRecyclerViewAdapter = new UserRecyclerViewAdapter(users, listener);
        recyclerView.setAdapter(userRecyclerViewAdapter);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (canLoadMore && !isLoadingMore && lastVisibleItemPosition == totalItemCount - 1) {
                    presenter.loadMoreAttendees();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserListFragmentInteractionListener) {
            listener = (OnUserListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnUserListFragmentInteractionListener {
        void onListFragmentInteraction(User item);
    }

    @Override
    public LceViewState<List<User>, AttendeesView> createViewState() {
        return new AttendeesViewState();
    }

    @Override
    public AttendeesViewState getViewState() {
        return (AttendeesViewState) super.getViewState();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadAttendees(pullToRefresh, eventId);
        canLoadMore = true;
    }

    @Override
    public List<User> getData() {
        return users;
    }

    @Override
    public void setData(List<User> data) {
        this.users.clear();
        this.users.addAll(data);
        userRecyclerViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
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
    public AttendeesPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return attendeesPresenter;
    }

    @Override
    public void showLoadMore(boolean showLoadMore) {

    }

    @Override
    public void addMoreData(List<User> data) {
        this.users.addAll(data);
        userRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadMoreError(Throwable e) {

    }
}
