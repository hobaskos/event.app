package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseLceViewStateFragment;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionFragment extends BaseLceViewStateFragment<SwipeRefreshLayout, List<CompetitionImage>, CompetitionView, CompetitionPresenter>
                implements CompetitionView {

    public static final String TAG = CompetitionFragment.class.getName();
    public static final String ARG_IS_LOGGED_IN = "isLoggedIn";
    public static final String ARG_COMPETITION_ID = "competitionId";
    public static final String ARG_EVENT_ID = "eventId";

    private ArrayList<CompetitionImage> competitionImages;
    private OnListFragmentInteractionListener listener;
    private DividerItemDecoration dividerItemDecoration;

    private CompetitionRecyclerViewAdapter competitionRecyclerViewAdapter;
    private Long eventId;
    private Long competitionId;
    private boolean isLoggedIn;

    @BindView(R.id.contentView)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_competition_list_fab)
    protected FloatingActionButton addCompetitionImage;
    @BindView(R.id.list)
    protected RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    protected TextView emptyResultView;

    @State
    protected boolean canLoadMore = true;
    @State
    protected boolean isLoadingMore = false;

    @Inject
    public CompetitionPresenter competitionPresenter;

    @NonNull
    @Override
    public CompetitionPresenter createPresenter() {
        Log.i(TAG, "createPresenter");
        App.getInst().getComponent().inject(this);
        return competitionPresenter;
    }

    public static CompetitionFragment newInstance(Event event, boolean isLoggedIn) {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, event.getId());
        args.putBoolean(ARG_IS_LOGGED_IN, isLoggedIn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        competitionImages = new ArrayList<>();

        if(getArguments() != null) {
            eventId = getArguments().getLong(ARG_EVENT_ID);
            isLoggedIn = getArguments().getBoolean(ARG_IS_LOGGED_IN);
        }

        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_competition_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_list, container, false);

        ButterKnife.bind(this, view);

        // set the adapter
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        competitionRecyclerViewAdapter = new CompetitionRecyclerViewAdapter(competitionImages, listener, context, isLoggedIn);
        recyclerView.setAdapter(competitionRecyclerViewAdapter);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        if(isLoggedIn) {
            addCompetitionImage.setOnClickListener(v -> showAddCompetitionImageDialog());
        } else {
            addCompetitionImage.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(eventId != null) {
            competitionPresenter.get(eventId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnListFragmentInteractionListener) {
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
    public LceViewState<List<CompetitionImage>, CompetitionView> createViewState() {
        return new CompetitionViewState();
    }

    @Override
    public CompetitionViewState getViewState() {
        return (CompetitionViewState) super.getViewState();
    }

    @Override
    public ArrayList<CompetitionImage> getData() {
        return competitionImages;
    }

    private void showAddCompetitionImageDialog() {
        Log.i(TAG, "Add Competition FAB clicked!");
    }

    @Override
    public void setData(List<CompetitionImage> data) {
        this.competitionImages.clear();
        this.competitionImages = (ArrayList<CompetitionImage>) data;
        competitionRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Log.i(TAG, "loadData pullToRefresh: " + pullToRefresh);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        Log.i(TAG, "getErrorMessage: " + e.getMessage());
        return null;
    }

    @Override
    public void showLoadMore(boolean showLoadMore) {

    }

    @Override
    public void showLoadMoreError(Throwable e) {

    }

    @Override
    public void addMoreData(List<CompetitionImage> model) {
        this.competitionImages.addAll(model);
        competitionRecyclerViewAdapter.notifyDataSetChanged();

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Long id);
    }

}

