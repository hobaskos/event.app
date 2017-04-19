package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.enumeration.EventAttendingType;
import io.hobaskos.event.eventapp.util.ImageUtil;

import static android.app.Activity.RESULT_OK;
import static io.hobaskos.event.eventapp.util.ImageUtil.CAPTURE_IMAGE_REQUEST;
import static io.hobaskos.event.eventapp.util.ImageUtil.PICK_IMAGE_REQUEST;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionFragment extends MvpFragment<CompetitionView, CompetitionPresenter>
        implements CompetitionView {

    public static final String TAG = CompetitionFragment.class.getName();
    public static final String ARG_COMPETITION_ID = "competitionId";
    public static final String ARG_IS_ATTENDING_EVENT = "myAttendance";

    private static final String[] CAMERA_PERMS = { Manifest.permission.CAMERA };
    private static final int CAMERA_REQUEST = 4337;

    private ArrayList<CompetitionImage> competitionImages;
    private OnCompetitionListInteractionListener listener;

    private CompetitionRecyclerViewAdapter competitionRecyclerViewAdapter;
    private Long competitionId;
    private boolean isAttending;

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

    public static CompetitionFragment newInstance(Event event) {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();

        boolean going = false;

        if(event.getMyAttendance() != null) {
            Log.i(TAG, event.getMyAttendance().toString());
            going =  event.getMyAttendance().equals(EventAttendingType.GOING);
        }

        args.putBoolean(ARG_IS_ATTENDING_EVENT, going);
        args.putLong(ARG_COMPETITION_ID, event.getDefaultPollId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        competitionImages = new ArrayList<>();

        if (getArguments() != null) {
            competitionId = getArguments().getLong(ARG_COMPETITION_ID);
            isAttending = getArguments().getBoolean(ARG_IS_ATTENDING_EVENT);
        }

        setRetainInstance(true);
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
        competitionRecyclerViewAdapter = new CompetitionRecyclerViewAdapter(competitionImages, listener, context, isAttending);
        recyclerView.setAdapter(competitionRecyclerViewAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        addCompetitionImage.setOnClickListener(v -> pickNewCompetitionImage());

        if(isAttending) {
            addCompetitionImage.setVisibility(View.VISIBLE);
        } else {
            addCompetitionImage.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(competitionId != null) {
            competitionPresenter.setCompetitionId(competitionId);
            competitionPresenter.get();
        }
    }

    @Override
    public void onAttach(Context context) {
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
        super.onDetach();
        listener = null;
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

        competitionImages.clear();
        competitionImages.addAll(data);

        Collections.sort(competitionImages, (o1, o2) -> o2.getVoteScore().compareTo(o1.getVoteScore()));

        competitionRecyclerViewAdapter =
                new CompetitionRecyclerViewAdapter(competitionImages, listener, getContext(), isAttending);

        recyclerView.setAdapter(competitionRecyclerViewAdapter);

        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        competitionPresenter.get();
        canLoadMore = false;
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

    @Override
    public void onResume() {
        super.onResume();
        competitionPresenter.get();
    }

    public void onCompetitionImageVoteSubmitted(Long id, int vote) {
        if(!getCompetitionImageById(id).hasMyVote()) {
            competitionPresenter.vote(id, vote);

            updateCompetitionImageVoteScore(id, vote);
        }
    }

    private void updateCompetitionImageVoteScore(Long id, int vote) {
        Log.i(TAG, "Inside updateCompetitionImageVoteScore()");
        CompetitionImage competitionImage = getCompetitionImageById(id);
        int index = competitionImages.indexOf(competitionImage);
        Log.i(TAG, "Image has index = " + index);
        competitionImage.setHasMyVote(true);
        int numberOfVotes = competitionImage.getNumberOfVotes();
        Long voteScore = competitionImage.getVoteScore();
        Log.i(TAG, "Image has now " + numberOfVotes + " votes");
        Log.i(TAG, "Image has now " + voteScore + " voteScore");
        competitionImage.setNumberOfVotes(numberOfVotes + 1);
        competitionImage.setVoteScore(voteScore + vote);
        competitionImages.set(index, competitionImage);
        Collections.sort(competitionImages, (o1, o2) -> o2.getVoteScore().compareTo(o1.getVoteScore()));
        competitionRecyclerViewAdapter.notifyDataSetChanged();
        competitionRecyclerViewAdapter.notifyItemChanged(index);
        index = competitionImages.indexOf(competitionImage);
        recyclerView.scrollToPosition(index);
    }


    private CompetitionImage getCompetitionImageById(Long id) {
        for(int i = 0; i < competitionImages.size(); i++) {
            if(competitionImages.get(i).getId().equals(id)){
                Log.i(TAG, "getCompetitionImageById() => returning real image;");
                return competitionImages.get(i);
            }
        }

        Log.i(TAG, "getCompetitionImageById() => new CompetitionImage();");
        return new CompetitionImage();
    }

    @Override
    public void imageWasSuccessfullyNominated(CompetitionImage competitionImage) {
        competitionImages.add(competitionImage);
        competitionRecyclerViewAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(competitionImages.size() - 1);
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
                    presenter.nominateImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_IMAGE_REQUEST) {
                //Get the photo
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                String image = ImageUtil.getEncoded64ImageStringFromBitmap(bitmap);
                presenter.nominateImage(image);
            }
        }
    }

    public void setAttendingEvent(boolean isAttendingEvent) {
        isAttending = isAttendingEvent;

        Log.i(TAG, "setAttendingEvent = " + isAttendingEvent);

        if(isAttendingEvent) {
            addCompetitionImage.setVisibility(View.VISIBLE);
        } else {
            addCompetitionImage.setVisibility(View.GONE);
        }

        competitionPresenter.get();
    }

    public void pickNewCompetitionImage() {
        CharSequence options[] = new CharSequence[]{
                getString(R.string.capture_image),
                getString(R.string.select_image_from_lib)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.select_image_option))
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: launchCamera(); break;
                        case 1: launchLibrary(); break;
                    }
                })
                .setNegativeButton(R.string.close, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void launchCamera() {
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(getContext(), R.string.could_not_find_camera, Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), CAMERA_PERMS, CAMERA_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        }
    }

    private void launchLibrary(){
        Intent intent = new Intent();
        // Only show images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser
        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_REQUEST);
    }
}

