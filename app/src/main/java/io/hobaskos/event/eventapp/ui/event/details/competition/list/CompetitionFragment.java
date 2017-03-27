package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.io.IOException;
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
import io.hobaskos.event.eventapp.util.ImageUtil;

import static android.app.Activity.RESULT_OK;
import static io.hobaskos.event.eventapp.util.ImageUtil.PICK_IMAGE_REQUEST;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionFragment extends MvpFragment<CompetitionView, CompetitionPresenter>
        implements CompetitionView {

    public static final String TAG = CompetitionFragment.class.getName();
    public static final String ARG_IS_LOGGED_IN = "isLoggedIn";
    public static final String ARG_COMPETITION_ID = "competitionId";
    public static final String ARG_EVENT_ID = "eventId";

    private ArrayList<CompetitionImage> competitionImages;
    private OnCompetitionListInteractionListener listener;
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
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        competitionImages = new ArrayList<>();

        if(getArguments() != null) {
            eventId = getArguments().getLong(ARG_EVENT_ID);
            isLoggedIn = getArguments().getBoolean(ARG_IS_LOGGED_IN);
            Log.i(TAG, "event id = " + eventId);
            Log.i(TAG, "is logged in = " + isLoggedIn);
        }

        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_competition_list, container, false);

        ButterKnife.bind(this, view);

        // set the adapter
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        competitionRecyclerViewAdapter = new CompetitionRecyclerViewAdapter(competitionImages, listener, context, isLoggedIn);
        recyclerView.setAdapter(competitionRecyclerViewAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        Log.i(TAG, "isLoggedIn=" + isLoggedIn);
        if(isLoggedIn) {
            addCompetitionImage.setOnClickListener(v -> showAddCompetitionImageDialog());
            addCompetitionImage.setVisibility(View.VISIBLE);
        } else {
            addCompetitionImage.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        if(eventId != null) {
            Log.i(TAG, "eventId != null");
            competitionPresenter.get(eventId);
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);
        if(context instanceof OnCompetitionListInteractionListener) {
            listener = (OnCompetitionListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
        listener = null;
    }

    private void showAddCompetitionImageDialog() {
        Log.i(TAG, "Add Competition FAB clicked!");
        openGallery();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(List<CompetitionImage> data) {
        Log.i(TAG, "setData with size = " + data.size());
        this.competitionImages.clear();
        this.competitionImages.addAll(data);
        competitionRecyclerViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Log.i(TAG, "loadData pullToRefresh: " + pullToRefresh);
        competitionPresenter.get(eventId);
        canLoadMore = false;
    }

    @Override
    public void showLoadMore(boolean showLoadMore) {
        Log.i(TAG, "showLoadMore");
    }

    @Override
    public void showLoadMoreError(Throwable e) {
        Log.i(TAG, "showLoadMoreError");
    }

    @Override
    public void addMoreData(List<CompetitionImage> model) {
        Log.i(TAG, "addMoreData");
        this.competitionImages.addAll(model);
        competitionRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume-method called");
        super.onResume();
        competitionPresenter.get(eventId);
    }

    public void onUpVoteButtonClicked(Long id) {
        presenter.upVote(id);
    }

    public void onDownVoteButtonClicked(Long id) {
        presenter.downVote(id);
    }

    public void onAddImageButtonClicked() {

    }

    private void openGallery(){
        Intent intent = new Intent();
        // Only show images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser
        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    String image = ImageUtil.getEncoded64ImageStringFromBitmap(bitmap);
                    Log.i(TAG, image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

